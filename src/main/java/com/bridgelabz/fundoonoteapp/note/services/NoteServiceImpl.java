package com.bridgelabz.fundoonoteapp.note.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNameAlreadyUsedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.ReminderDateNotValidException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonoteapp.note.models.Label;
import com.bridgelabz.fundoonoteapp.note.models.LabelDTO;
import com.bridgelabz.fundoonoteapp.note.models.Note;
import com.bridgelabz.fundoonoteapp.note.models.UpdateNoteDTO;
import com.bridgelabz.fundoonoteapp.note.models.NoteDTO;
import com.bridgelabz.fundoonoteapp.note.repositories.NoteElasticRepository;
import com.bridgelabz.fundoonoteapp.note.repositories.LabelElasticRepository;
import com.bridgelabz.fundoonoteapp.note.repositories.LabelRepository;
import com.bridgelabz.fundoonoteapp.note.repositories.NoteRespository;
import com.bridgelabz.fundoonoteapp.note.utility.Utility;
import com.bridgelabz.fundoonoteapp.user.repositories.UserRepository;

@Service
public class NoteServiceImpl implements NoteService {

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
	public NoteDTO createNote(CreateNoteDTO createNoteDTO, String userId) throws UnAuthorizedException,
			NoteNotFoundException, LabelNotFoundException, ReminderDateNotValidException, UserNotFoundException {

		Utility.validateCreateDTO(createNoteDTO);

		if (!userRepository.findById(userId).isPresent()) {
			throw new UserNotFoundException(env.getProperty("UserNotFound"));
		}
		List<LabelDTO> newlabelList = new ArrayList<>();
		if (createNoteDTO.getLabelNameList() != null) {

			List<String> labelList = createNoteDTO.getLabelNameList();
			for (int i = 0; i < labelList.size(); i++) {
				if (labelList.get(i).trim().length() != 0 || labelList.get(i) != null) {
					Optional<Label> labelFound = labelRepository.findByLabelNameAndUserId(labelList.get(i), userId);
					if (!labelFound.isPresent()) {
						throw new UnAuthorizedException(env.getProperty("UnAuthorization1"));
					}

					LabelDTO label = new LabelDTO();
					label.setLabelId(labelFound.get().getLabelId());
					label.setLabelName(labelList.get(i));
					newlabelList.add(label);

				}
			}

		}
		Note note = new Note();

		if (createNoteDTO.getColor() == null || createNoteDTO.getColor().trim().equals("")) {
			note.setColor("white");
		} else {
			note.setColor(createNoteDTO.getColor());
		}

		if (createNoteDTO.getReminder() != null) {
			if (Utility.validateReminder(createNoteDTO.getReminder())) {
				throw new ReminderDateNotValidException(env.getProperty("InvalidReminder"));
			}
			note.setReminder(createNoteDTO.getReminder());

		}

		if (createNoteDTO.isArchive()) {
			note.setArchive(true);
		}

		if (createNoteDTO.isPin()) {
			note.setPin(true);
		}
		Date date = new Date();
		note.setTitle(createNoteDTO.getTitle());
		note.setDescription(createNoteDTO.getDescription());
		note.setCreatedAt(date);
		note.setUpdatedAt(date);
		note.setUserId(userId);
		note.setLabels(newlabelList);
		noteRespository.save(note);
		Note optionalNote = elasticSearchRepository.save(note);
		
		return mapModels(optionalNote); // change to convertor

	}

	
	
	@Override
	public void trashNote(String userId, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {
		
		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);
		
		Note note = optionalNote.get();
		note.setTrashed(true);
		
		noteRespository.save(note);
		elasticSearchRepository.save(note);
	}

	@Override
	public void updateNote(UpdateNoteDTO updateNoteDTO, String userId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {
		
		Optional<Note> optionalNote = validateNoteAndUser(updateNoteDTO.getNoteId(), userId);
		Note note = optionalNote.get();
		
		if (updateNoteDTO.getTitle() != null) {
			note.setTitle(updateNoteDTO.getTitle());
		}
		
		if (updateNoteDTO.getDescription() != null) {
			note.setDescription(updateNoteDTO.getDescription());
		}
		note.setUpdatedAt(new Date());
		
		noteRespository.save(note);
		elasticSearchRepository.save(note);
	}

	@Override
	public void deleteOrRestoreTrashedNote(String noteId, String userId, boolean isdelete)
			throws UnAuthorizedException, NoteNotTrashedException, NoteNotFoundException, UserNotFoundException {
		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);

		if (!optionalNote.get().isTrashed()) {
			throw new NoteNotTrashedException(env.getProperty("NoteNotTrashed"));
		}
		if (isdelete) {
			noteRespository.deleteById(noteId);
			elasticSearchRepository.deleteById(noteId);
		} else {
			Note note = optionalNote.get();
			note.setTrashed(false);
			
			noteRespository.save(note);
			elasticSearchRepository.save(note);
		}

	}

	@Override
	public void emptyTrash(String userId) throws NoteNotFoundException, UnAuthorizedException {
		
		if (!userRepository.findById(userId).isPresent()) {
			throw new UnAuthorizedException(env.getProperty("UserNotFound"));
		}
		
		noteRespository.deleteByTrashedAndUserId(true, userId);
		elasticSearchRepository.deleteByTrashedAndUserId(true, userId);
	}

	@Override
	public void addReminder(String noteId, String userId, Date reminder)
			throws ReminderDateNotValidException, UnAuthorizedException, NoteNotFoundException, UserNotFoundException {
		
		if (Utility.validateReminder(reminder)) {
			throw new ReminderDateNotValidException(env.getProperty("InvalidReminder"));
		}
		
		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);
		Note note = optionalNote.get();
		note.setReminder(reminder);
		
		noteRespository.save(note);
		elasticSearchRepository.save(note);
	}

	@Override
	public void removeReminder(String noteId, String userId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {

		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);

		Note note = optionalNote.get();
		note.setReminder(null);
		
		noteRespository.save(note);
		elasticSearchRepository.save(note);

	}

	@Override
	public NoteDTO viewNote(String noteId, String userId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {
		
		Optional<Note> note = validateNoteAndUser(noteId, userId);
		
		if (note.get().isTrashed()) {
			throw new NoteNotFoundException(env.getProperty("NoteNotFound"));
		}
		
		return mapModels(note.get());
	}

	@Override
	public List<NoteDTO> viewAllNotes(String userId) throws NoteNotFoundException {
		
		List<Note> note = elasticSearchRepository.findAllByUserIdAndTrashed(userId, false);
		//note.stream().filter(note->note.i)
		if (note.isEmpty()) {
			throw new NoteNotFoundException(env.getProperty("NoNotesPresent"));
		}
		
		List<NoteDTO> noteList = new ArrayList<>();
		
		/*for (int i = 0; i < note.size(); i++) {
			noteList.add(modelMapper.map(note.get(), NoteDTO.class))
		}*/
		
		note.stream().map(streamNote -> (modelMapper.map(streamNote, NoteDTO.class))).forEach(streamMap -> noteList.add(streamMap));
		
		List<NoteDTO> pinnedNoteList = new ArrayList<>();
		List<NoteDTO> othersNoteList = new ArrayList<>();
		
		for (int i = 0; i < noteList.size(); i++) {
			if (noteList.get(i).isPin()) {
				pinnedNoteList.add(noteList.get(i));
			} else {
				othersNoteList.add(noteList.get(i));
			}
		}
		
		List<NoteDTO> merged = new ArrayList<>(pinnedNoteList);
		merged.addAll(othersNoteList);
		
		return merged;
	}

	@Override
	public Iterable<NoteDTO> viewAllTrashedNotes(String userId) throws NoteNotTrashedException {
		
		List<Note> note = elasticSearchRepository.findAllByUserIdAndTrashed(userId, true);
		
		if (note.isEmpty()) {
			throw new NoteNotTrashedException(env.getProperty("NoteNotTrashed"));
		}
		List<NoteDTO> noteList = new ArrayList<>();
		
		for (int i = 0; i < note.size(); i++) {
			noteList.add(modelMapper.map(note.get(i), NoteDTO.class));
		}
		
		return noteList;
	}

	@Override
	public Iterable<NoteDTO> getArchiveNotes(String userId) throws NoteNotFoundException {
		
		List<Note> note = elasticSearchRepository.findAllByUserIdAndArchive(userId, true);
		
		if (note.isEmpty()) {
			throw new NoteNotFoundException(env.getProperty("NoteNotFound"));
		}
		List<NoteDTO> noteList = new ArrayList<>();
		
		for (int i = 0; i < note.size(); i++) {
			noteList.add(modelMapper.map(note.get(i), NoteDTO.class));
		}
		
		return noteList;

	}

	@Override
	public void setArchive(String userId, String noteId)
			throws NoteNotFoundException, UnAuthorizedException, UserNotFoundException {
		
		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);
		
		Note note = optionalNote.get();
		note.setPin(false);
		note.setArchive(true);
		
		noteRespository.save(note);
		elasticSearchRepository.save(note);

	}

	@Override
	public void unArchive(String userId, String noteId)
			throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException {
		
		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);
		
		Note note = optionalNote.get();
		note.setArchive(false);
		
		noteRespository.save(note);
		elasticSearchRepository.save(note);
	}

	@Override
	public void pinOrUnpinNote(String userId, String noteId, boolean isPin)
			throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException {

		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);

		Note note = optionalNote.get();
		if (isPin) {
			note.setPin(true);
		} else {
			note.setPin(false);
		}
		noteRespository.save(note);
		elasticSearchRepository.save(note);
	}

	@Override
	public List<LabelDTO> getLabels(String userId) throws LabelNotFoundException {
		
		List<Label> labelList = labelRepository.findAllByUserId(userId);
		
		if (labelList.isEmpty()) {
			throw new LabelNotFoundException(env.getProperty("LabelNotFound"));
		}
		
		List<LabelDTO> labelListView = new ArrayList<>();
		
		for (int i = 0; i < labelList.size(); i++) {
			labelListView.add(modelMapper.map(labelList.get(i), LabelDTO.class));
		}
		
		return labelListView;
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
		
		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);
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
		List<Note> noteList =noteRespository.findAllByQuery(userId, labelId);
		List<NoteDTO> listOfNote = new ArrayList<>();
		
		for (int i = 0; i < noteList.size(); i++) {
			listOfNote.add(modelMapper.map(noteList.get(i), NoteDTO.class));
		}
		
		return listOfNote;

	}

	@Override
	public void changeColour(String noteId, String userId, String color)
			throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException {

		Optional<Note> optionalNote = validateNoteAndUser(noteId, userId);

		if (color != null && color.trim().length() != 0) {
			optionalNote.get().setColor(color);
			
			noteRespository.save(optionalNote.get());
			elasticSearchRepository.save(optionalNote.get());

		}
	}

	@Override
	public void addLabel(String labelId, String userId, String noteId)
			throws LabelNameAlreadyUsedException, UnAuthorizedException, NoteNotFoundException, UserNotFoundException {
		Optional<Note> note = validateNoteAndUser(noteId, userId);

		// Check if note has a list of labels or not, if not ,then create a new List
		if (note.get().getLabels() == null) {
			List<LabelDTO> newLabelList = new ArrayList<>();
			note.get().setLabels(newLabelList);
		}

		// check if label is present in labelRepository
		Optional<Label> labelFound = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (labelFound.isPresent()) {
			LabelDTO label = new LabelDTO();

			// if label present in the note matches with the given label,throw exception as
			// a note cannot have multiple labels with same name
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
	}

	private NoteDTO mapModels(Note note) {
		NoteDTO noteDTO = new NoteDTO();
		noteDTO.setTitle(note.getTitle());
		noteDTO.setDescription(note.getDescription());
		noteDTO.setColor(note.getColor());
		noteDTO.setCreatedAt(note.getCreatedAt());
		noteDTO.setArchive(note.isArchive());
		noteDTO.setReminder(note.getReminder());
		noteDTO.setNoteId(note.getNoteId());
		noteDTO.setPin(note.isPin());
		noteDTO.setUpdatedAt(note.getUpdatedAt());
		noteDTO.setLabels(note.getLabels());
		return noteDTO;
	}

	private Optional<Note> validateNoteAndUser(String noteId, String userId)
			throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException {

		if (!userRepository.findById(userId).isPresent()) {
			throw new UserNotFoundException(env.getProperty("UserNotFound"));
		}
		Optional<Note> optionalNote = elasticSearchRepository.findById(noteId);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException(env.getProperty("NoteNotFound"));
		}
		if (!optionalNote.get().getUserId().equals(userId)) {
			throw new UnAuthorizedException(env.getProperty("UnAuthorization2"));
		}
		return optionalNote;
	}

	private Optional<Label> validateLabelAndUser(String userId, String labelId)
			throws UserNotFoundException, LabelNotFoundException {
		if (!userRepository.findById(userId).isPresent()) {
			throw new UserNotFoundException(env.getProperty("UserNotFound"));
		}
		Optional<Label> labelFound = labelRepository.findByLabelIdAndUserId(labelId, userId);
		if (!labelFound.isPresent()) {
			throw new LabelNotFoundException(env.getProperty("LabelNotFound"));
		}
		return labelFound;
	}

}
