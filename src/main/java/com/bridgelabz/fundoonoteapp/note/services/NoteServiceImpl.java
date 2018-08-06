package com.bridgelabz.fundoonoteapp.note.services;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.MalformedUrlException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotTrashedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.ReminderDateNotValidException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonoteapp.note.models.Label;
import com.bridgelabz.fundoonoteapp.note.models.LabelDTO;
import com.bridgelabz.fundoonoteapp.note.models.URLMetaData;
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
	private Environment env;

	@Override
	public NoteDTO createNote(CreateNoteDTO createNoteDTO, String userId)
			throws UnAuthorizedException, NoteNotFoundException, LabelNotFoundException, ReminderDateNotValidException,
			UserNotFoundException, IOException, MalformedUrlException {

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

		List<URLMetaData> listOfLinks = analyseDescription(createNoteDTO.getDescription());

		Date date = new Date();
		note.setTitle(createNoteDTO.getTitle());
		note.setDescription(createNoteDTO.getDescription());
		note.setLinks(listOfLinks);
		note.setCreatedAt(date);
		note.setUpdatedAt(date);
		note.setUserId(userId);
		note.setLabels(newlabelList);
		noteRespository.save(note);
		Note optionalNote = elasticSearchRepository.save(note);

		return mapModels(optionalNote); // change to convertor

	}

	private List<URLMetaData> analyseDescription(String description) throws MalformedUrlException {

		String regex = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
		String[] desciptionArray = description.split("\\s+");

		List<URLMetaData> listOfLinks = new ArrayList<>();
		for (int i = 0; i < desciptionArray.length; i++) {
			if (desciptionArray[i].matches(regex)) {
				URLMetaData link = new URLMetaData();
				Document doc;
				try {
					doc = Jsoup.connect(desciptionArray[i]).get();
				} catch (IOException e1) {
					throw new MalformedUrlException("malformed url encountered");
					}
				link.setTitle(doc.title());
				List<String> imageLists = new ArrayList<>();
				
				for (Element e : doc.getElementsByTag("img")) {
					imageLists.add(e.absUrl("src"));
				}
				Optional<String> image = imageLists.stream()
						.filter(streamlink -> streamlink.endsWith(".jpg") || streamlink.endsWith(".png")).findFirst();

				if (image.isPresent()) {
					link.setImage(image.get());
				}
				link.setLinkDesc(doc.baseUri().replace("https://",""));
				link.setLink(doc.baseUri());
				listOfLinks.add(link);
			}
		}
		return listOfLinks;

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
		if (note.isEmpty()) {
			throw new NoteNotFoundException(env.getProperty("NoNotesPresent"));
		}

		List<NoteDTO> noteList = note.stream().map(streamNote -> (modelMapper.map(streamNote, NoteDTO.class)))
				.collect(Collectors.toList());

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
		return note.stream().map(streamNote -> (modelMapper.map(streamNote, NoteDTO.class)))
				.collect(Collectors.toList());

	}

	@Override
	public Iterable<NoteDTO> getArchiveNotes(String userId) throws NoteNotFoundException {

		List<Note> note = elasticSearchRepository.findAllByUserIdAndArchive(userId, true);

		if (note.isEmpty()) {
			throw new NoteNotFoundException(env.getProperty("NoteNotFound"));
		}

		return note.stream().map(streamNote -> (modelMapper.map(streamNote, NoteDTO.class)))
				.collect(Collectors.toList());

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
			if (optionalNote.get().isArchive()) {
				note.setArchive(false);
				note.setPin(true);
			}

		} else {
			note.setPin(false);
		}
		noteRespository.save(note);
		elasticSearchRepository.save(note);
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

	@Override
	public Optional<Note> validateNoteAndUser(String noteId, String userId)
			throws UnAuthorizedException, NoteNotFoundException, UserNotFoundException {

		if (!userRepository.findById(userId).isPresent()) {
			throw new UserNotFoundException(env.getProperty("UserNotFound"));
		}
		Optional<Note> optionalNote = noteRespository.findById(noteId);

		if (!optionalNote.isPresent()) {
			throw new NoteNotFoundException(env.getProperty("NoteNotFound"));
		}
		if (!optionalNote.get().getUserId().equals(userId)) {
			throw new UnAuthorizedException(env.getProperty("UnAuthorization2"));
		}
		return optionalNote;
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

}
