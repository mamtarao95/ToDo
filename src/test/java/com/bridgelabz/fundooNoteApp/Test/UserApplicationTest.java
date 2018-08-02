//package com.bridgelabz.fundooNoteApp.Test;
//
//import static org.hamcrest.CoreMatchers.any;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//import java.util.Optional;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import com.bridgelabz.fundoonoteapp.FundooNoteApplication;
//import com.bridgelabz.fundoonoteapp.note.controllers.NoteController;
//import com.bridgelabz.fundoonoteapp.note.models.Note;
//import com.bridgelabz.fundoonoteapp.note.models.ViewNoteDTO;
//import com.bridgelabz.fundoonoteapp.note.repositories.NoteRespository;
//import com.bridgelabz.fundoonoteapp.note.services.NoteServiceImpl;
//import com.bridgelabz.fundoonoteapp.user.controllers.UserController;
//import com.bridgelabz.fundoonoteapp.user.models.User;
//import com.bridgelabz.fundoonoteapp.user.repositories.UserRepository;
//import com.bridgelabz.fundoonoteapp.user.services.UserServiceImpl;
//
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = FundooNoteApplication.class)
//@SpringBootTest
//public class UserApplicationTest {
//	private MockMvc mockMvc;
//	
//	@Autowired
//	ModelMapper  modelMappper;
//	@Autowired
//	private UserController userController;
//
//	@InjectMocks
//	private UserServiceImpl userService;
//	
//	@Mock
//	private UserRepository noteRespository;
//	
//	/*@Before
//	public void init() {
//		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//	}*/
//	
//	@Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//    }
//	
//	// @Test
//		public void loginTest() throws Exception {
//			mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
//					.content("{\"email\" : \"mamtarao9395@gmail.com\", \"password\" : \"Mamta@222\" }")
//					.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
//					.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.message").value("Login Successfull!!"))
//					.andExpect(jsonPath("$.status").value(2));
//		}
//	
//		 
//		   
//}
