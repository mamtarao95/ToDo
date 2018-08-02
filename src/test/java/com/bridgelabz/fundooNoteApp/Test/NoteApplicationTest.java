package com.bridgelabz.fundooNoteApp.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import com.bridgelabz.fundoonoteapp.FundooNoteApplication;
import com.bridgelabz.fundoonoteapp.note.controllers.NoteController;
import com.bridgelabz.fundoonoteapp.note.exceptions.LabelNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.exceptions.ReminderDateNotValidException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UnAuthorizedException;
import com.bridgelabz.fundoonoteapp.note.exceptions.UserNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonoteapp.note.models.LabelDTO;
import com.bridgelabz.fundoonoteapp.note.models.Note;
import com.bridgelabz.fundoonoteapp.note.models.NoteDTO;
import com.bridgelabz.fundoonoteapp.note.repositories.NoteRespository;
import com.bridgelabz.fundoonoteapp.note.services.NoteServiceImpl;
import com.bridgelabz.fundoonoteapp.user.repositories.UserRepository;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = FundooNoteApplication.class)
@SpringBootTest
public class NoteApplicationTest {

	private MockMvc mockMvc;
	
	@Autowired
	ModelMapper  modelMappper;
	@Autowired
	private NoteController noteController;

	@InjectMocks
	private NoteServiceImpl noteService;
	
	@Mock
	private NoteRespository noteRespository;
	
	@Mock
	private UserRepository userRepository;
	
	@Mock
	Environment env;
	
	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
	}
    @Autowired
    private JacksonTester < CreateNoteDTO > json;
	
	@Test
		public void createNoteTest() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.post("/note/create").contentType(MediaType.APPLICATION_JSON)
					
					.content("{\"description\" : \"notedescription\", \"title\" : \"title\",\"color\" : \"white\",\"reminder\" : \"2018-07-21T10:02:43.210Z\",\"pin\" : \"false\",\"archive\" : \"false\"}")
					.requestAttr("userId", "5b5014124b47f81fbe60ce52")
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.*", hasSize(7)))
			.andDo(print());
		}
	
	//@Test
		public void deleteNoteTest() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.put("/fundoo/deletenote/{noteId}","5b558dec4b47f8117152b246")
					.requestAttr("userId","5b5014124b47f81fbe60ce52")
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.message").value("Note trashed Successfully!!"))
					.andExpect(jsonPath("$.status").value(2))
					.andDo(print());
		}
	
	
	//@Test
	public void updateNoteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/fundoo/updatenote/{noteId}","5b558dec4b47f8117152b246").contentType(MediaType.APPLICATION_JSON)
				.content("{\"description\" : \"notedescription\", \"title\" : \"title\",\"noteId\" : \"5b558dec4b47f8117152b246\"}")
				.requestAttr("userId","5b5014124b47f81fbe60ce52")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Updated Note Successfull!!"))
				.andExpect(jsonPath("$.status").value(3))
				.andDo(print());
	}
	
	
	//@Test
	public void getNoteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/fundoo/getnote/{noteId}","5b5594014b47f81b502c09b4").contentType(MediaType.APPLICATION_JSON)
				.requestAttr("userId","5b5014124b47f81fbe60ce52")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.*", hasSize(7)))
			.andDo(print());
	}
	

	//@Test
	public void deleteOrRestoreTrashedNoteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/fundoo/deleteForeverOrRestoreNote/{noteId}","5b5594014b47f81b502c09b4").contentType(MediaType.APPLICATION_JSON)
				.requestAttr("userId","5b5014124b47f81fbe60ce52")
				.content("true")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.message").value("Deleted trashed Note Successfully!!"))
		.andExpect(jsonPath("$.status").value(4))
		.andDo(print());
	}
	/*
	//@Test
	public void testGetNote() throws UnAuthorizedException, NoteNotFoundException, LabelNotFoundException, ReminderDateNotValidException, UserNotFoundException{
		
		LabelDTO label=new LabelDTO("5b5851e94b47f81d79cfa459","label2");
		List<LabelDTO> labelList=new ArrayList<>();
		labelList.add(label);
		 NoteDTO note=new NoteDTO("5b5b00424b47f82fb1547807", "title", "notedescription", new java.util.Date(System.currentTimeMillis()),  new java.util.Date(System.currentTimeMillis()),  new java.util.Date(System.currentTimeMillis()),
					"white",true,true,labelList);
		String userId="5b5014124b47f81fbe60ce52";
		CreateNoteDTO createNoteDTO=new CreateNoteDTO("title","description","white" ,new java.util.Date(System.currentTimeMillis()),false,false);
	Mockito.when(noteService.createNote(createNoteDTO,userId)).thenReturn(note);

	}*/
	
	
	//@Test
	public void testDeleteNote() throws UnAuthorizedException, NoteNotFoundException, LabelNotFoundException, ReminderDateNotValidException, UserNotFoundException{
		String noteId="5b5b00c24b47f82fb1547809";
		String userId="5b5014124b47f81fbe60ce52";
		Optional<Note> note=noteRespository.findById(noteId);
		boolean isTrashed=note.get().isTrashed();
		//doNothing().when(noteService.trashNote(userId, noteId));
		//assertEquals(noteService.trashNote(userId, noteId),isTrashed);
		// verify(noteService.trashNote(userId, noteId)).

	}
	
}

