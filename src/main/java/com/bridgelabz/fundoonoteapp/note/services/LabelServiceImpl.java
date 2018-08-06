package com.bridgelabz.fundoonoteapp.note.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNameAlreadyUsedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.Label;
import com.bridgelabz.fundoonoteapp.note.models.LabelDTO;
import com.bridgelabz.fundoonoteapp.note.models.Note;
import com.bridgelabz.fundoonoteapp.note.models.NoteDTO;
import com.bridgelabz.fundoonoteapp.note.repositories.LabelElasticRepository;
import com.bridgelabz.fundoonoteapp.note.repositories.LabelRepository;
import com.bridgelabz.fundoonoteapp.note.repositories.NoteElasticRepository;
import com.bridgelabz.fundoonoteapp.note.repositories.NoteRespository;
import com.bridgelabz.fundoonoteapp.user.repositories.UserRepository;

@Service
public class LabelServiceImpl implements LabelService {

	@Autowired
	private NoteRespository noteRespository;

	@Autowired
	private NoteElasticRepository elasticSearchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private LabelElasticRepository labelElasticRepository;

	@Autowired
	private Environment env;

	@Override
	public List<LabelDTO> getLabels(String userId) throws LabelNotFoundException {

		List<Label> labels = labelRepository.findAllByUserId(userId);

		if (labels.isEmpty()) {
			throw new LabelNotFoundException(env.getProperty("LabelNotFound"));
		}

		return labels.stream().map(streamLabels -> (modelMapper.map(streamLabels, LabelDTO.class)))
				.collect(Collectors.toList());

	}

	@Override
	public LabelDTO createLabel(String labelName, String userId)
			throws UnAuthorizedException, LabelNotFoundException, LabelNameAlreadyUsedException {

		if (!userRepository.findById(userId).isPresent()) {
			throw new UnAuthorizedException(env.getProperty("UserNotFound"));
		}

		Optional<Label> labelFound = labelRepository.findByLabelIdAndUserId(labelName, userId);

		if (labelFound.isPresent()) {
			throw new LabelNameAlreadyUsedException("Label already exists.You cannot have duplicate labels");
		}

		Label label = new Label();
		label.setLabelName(labelName);
		label.setUserId(userId);

		labelRepository.save(label);
		labelElasticRepository.save(label);
		return modelMapper.map(label, LabelDTO.class);

	}

	@Override
	public void deleteLabel(String labelId, String userId) throws LabelNotFoundException, UserNotFoundException {

		validateLabelAndUser(userId, labelId);

		labelRepository.deleteByLabelId(labelId);
		labelElasticRepository.deleteByLabelId(labelId);

		List<Note> noteList = noteRespository.findAllByQuery(userId, labelId);
		boolean flag = false;
		for (int i = 0; i < noteList.size(); i++) {

			for (int j = 0; j < noteList.get(i).getLabels().size(); j++) {

				if (noteList.get(i).getLabels().get(j).getLabelId().equals(labelId)) {
					noteList.get(i).getLabels().remove(j);
					Note note = noteList.get(i);

					noteRespository.save(note);
					elasticSearchRepository.save(note);
					flag = true;
				}
				if (flag)
					break;
			}
		}
	}

	@Override
	public void removeLabel(String userId, String labelId, String noteId)
			throws UserNotFoundException, LabelNotFoundException, NoteNotFoundException, UnAuthorizedException {
		NoteService noteService = new NoteServiceImpl();
		Optional<Note> optionalNote = noteService.validateNoteAndUser(noteId, userId);

		boolean found = false;

		for (int i = 0; i < optionalNote.get().getLabels().size(); i++) {
			if (optionalNote.get().getLabels().get(i).getLabelId().equals(labelId)) {
				optionalNote.get().getLabels().remove(i);
				found = true;
				break;
			}
		}
		if (!found) {
			throw new LabelNotFoundException(env.getProperty("LabelNotFound"));
		}

		noteRespository.save(optionalNote.get());
		elasticSearchRepository.save(optionalNote.get());

	}

	@Override
	public void renameLabel(String labelId, String userId, String newLabelName)
			throws LabelNotFoundException, UserNotFoundException {

		Optional<Label> labelFound = validateLabelAndUser(userId, labelId);
		labelFound.get().setLabelName(newLabelName);
		labelRepository.save(labelFound.get());
		labelElasticRepository.save(labelFound.get());
		boolean flag = false;
		List<Note> noteList = noteRespository.findAllByQuery(userId, labelId);

		for (int i = 0; i < noteList.size(); i++) {

			for (int j = 0; j < noteList.get(i).getLabels().size(); j++) {

				if (noteList.get(i).getLabels().get(j).getLabelId().equals(labelId)) {
					noteList.get(i).getLabels().get(j).setLabelName(newLabelName);
					Note note = noteList.get(i);

					noteRespository.save(note);
					elasticSearchRepository.save(note);
					flag = true;
				}
				if (flag)
					break;
			}

		}

	}

	@Override
	public Iterable<NoteDTO> getNotesOfLabel(String labelId, String userId)
			throws LabelNotFoundException, UserNotFoundException {

		validateLabelAndUser(userId, labelId);
		List<Note> noteList = noteRespository.findAllByQuery(userId, labelId);

		return noteList.stream().map(streamNote -> (modelMapper.map(streamNote, NoteDTO.class)))
				.collect(Collectors.toList());

	}

	@Override
	public void addLabel(String labelId, String userId, String noteId) throws LabelNameAlreadyUsedException,
			UnAuthorizedException, NoteNotFoundException, UserNotFoundException, LabelNotFoundException {
		NoteService noteService = new NoteServiceImpl();
		Optional<Note> note = noteService.validateNoteAndUser(noteId, userId);

		// Check if note has a list of labels or not, if not ,then create a new List
		if (note.get().getLabels() == null) {
			List<LabelDTO> newLabelList = new ArrayList<>();
			note.get().setLabels(newLabelList);
		}

		// check if label is present in labelRepository
		Optional<Label> labelFound = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (!labelFound.isPresent()) {
			throw new LabelNotFoundException(env.getProperty("LablenotFound"));
		}

		LabelDTO label = new LabelDTO();

		for (int i = 0; i < note.get().getLabels().size(); i++) {
			if (note.get().getLabels().get(i).getLabelId().equals(labelId)) {
				throw new LabelNameAlreadyUsedException(env.getProperty("DuplicateLabel"));
			}
		}
		label.setLabelId(labelFound.get().getLabelId());
		label.setLabelName(labelFound.get().getLabelName());
		note.get().getLabels().add(label);

		noteRespository.save(note.get());
		elasticSearchRepository.save(note.get());
	}

	private Optional<Label> validateLabelAndUser(String userId, String labelId)
			throws UserNotFoundException, LabelNotFoundException {
		if (!userRepository.findById(userId).isPresent()) {
			throw new UserNotFoundException(env.getProperty("UserNotFound"));
		}
		Optional<Label> labelFound = labelElasticRepository.findByLabelIdAndUserId(labelId, userId);
		if (!labelFound.isPresent()) {
			throw new LabelNotFoundException(env.getProperty("LabelNotFound"));
		}
		return labelFound;
	}

}
