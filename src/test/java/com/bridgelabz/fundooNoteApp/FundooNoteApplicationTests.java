//package com.bridgelabz.fundooNoteApp;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import com.bridgelabz.fundoonoteapp.FundooNoteApplication;
//import com.bridgelabz.fundoonoteapp.note.controllers.NoteController;
//import com.bridgelabz.fundoonoteapp.user.controllers.UserController;
//
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = FundooNoteApplication.class)
//@SpringBootTest
//public class FundooNoteApplicationTests {
//
//	private MockMvc mockMvc;
//	
//	@Autowired
//	private NoteController noteController;
//
//	@Before
//	public void init() {
//		this.mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
//	}
//
//	// @Test
//	public void loginTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
//				.content("{\"email\" : \"mamtarao9395@gmail.com\", \"password\" : \"Mamta@222\" }")
//				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
//				.andExpect(jsonPath("$.status").exists()).andExpect(jsonPath("$.message").value("Login Successfull!!"))
//				.andExpect(jsonPath("$.status").value(2));
//	}
//
//	// @Test
//	public void activationTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/activateaccount")
//				.param("token",
//						"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUwMTE5NTRiNDdmODFhMzVhYzYyNzkiLCJpYXQiOjE1MzE5NzQzNjcsImV4cCI6MTUzMjA2MDc2Nywic3ViIjoidXNlcnRva2VuIn0.hFn7Zs0EgUQBnCvbEaKDEHwbqC5ovPtRHM7ATP8QJzM")
//				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.message").exists())
//				.andExpect(jsonPath("$.status").exists())
//				.andExpect(jsonPath("$.message").value("account activated Successfull!!"))
//				.andExpect(jsonPath("$.status").value(3));
//
//	}
//
//	//@Test
//	public void registerTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register").contentType(MediaType.APPLICATION_JSON).content(
//				"{\"email\" : \"mamtarao9395@gmail.com\", \"password\" : \"Mamta@222\",\"confirmPassword\":\"Mamta@222\",\"firstName\":\"mamta\",\"lastName\":\"rao\",\"mobileNumber\":\"9854667188\" }")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.message").value("Registeration Successfull!!"))
//				.andExpect(jsonPath("$.status").value(1));
//	}
//
//	//@Test
//	public void forgotPasswordTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/forgotpassword").contentType(MediaType.TEXT_PLAIN_VALUE)
//				.content("mamtarao9395@gmail.com")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.message").value("An email has been sent successfully to reset your password!!"))
//				.andExpect(jsonPath("$.status").value(4));
//	}
//	
//	//@Test
//	public void setPasswordTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/setpassword").contentType(MediaType.APPLICATION_JSON)
//				.param("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUwMTQxMjRiNDdmODFmYmU2MGNlNTIiLCJpYXQiOjE1MzE5NzcyOTMsImV4cCI6MTUzMjA2MzY5Mywic3ViIjoidXNlcnRva2VuIn0.UXGv--yqUmryz1XO0MVGzWIXEGHoRSKE1D-oh2ZbtS8")
//				.content("{\"newPassword\" : \"Mamta@123\", \"confirmPassword\" : \"Mamta@123\" }")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.message").value("New Password has been set Successfully!!"))
//				.andExpect(jsonPath("$.status").value(5));
//	}
//	
//	//***********************************NOTE TEST*********************//
//	//@Test
//	public void createNoteTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/note/createnote").contentType(MediaType.APPLICATION_JSON)
//				.content("{\"description\" : \"notedescription\", \"title\" : \"title\" }")
//				.param("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUwMTQxMjRiNDdmODFmYmU2MGNlNTIiLCJpYXQiOjE1MzIwMDE5NjEsImV4cCI6MTUzMjA4ODM2MSwic3ViIjoidXNlcnRva2VuIn0.2NJA0ZS1d8OtRSe-9udNqBIHfKOmOS3mXPJBrNjo5bU")
//				
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.message").value("Note created Successfully!!"))
//				.andExpect(jsonPath("$.status").value(1));
//	}
//	
//
//	//@Test
//	public void deleteNoteTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.delete("/api/note/deletenote")
//				.param("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUwMTQxMjRiNDdmODFmYmU2MGNlNTIiLCJpYXQiOjE1MzIwMDE5NjEsImV4cCI6MTUzMjA4ODM2MSwic3ViIjoidXNlcnRva2VuIn0.2NJA0ZS1d8OtRSe-9udNqBIHfKOmOS3mXPJBrNjo5bU")
//				.param("noteId", "5b5094b24b47f855622e8b84")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.message").value("Note trashed Successfully!!"))
//				.andExpect(jsonPath("$.status").value(2));
//	}
//	
//	//@Test
//	public void updateNoteTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/note/updatenote").contentType(MediaType.APPLICATION_JSON)
//				.content("{\"description\" : \"notedescription1\", \"title\" : \"title1\" }")
//				.param("token","eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1YjUwMTQxMjRiNDdmODFmYmU2MGNlNTIiLCJpYXQiOjE1MzIwMDE5NjEsImV4cCI6MTUzMjA4ODM2MSwic3ViIjoidXNlcnRva2VuIn0.2NJA0ZS1d8OtRSe-9udNqBIHfKOmOS3mXPJBrNjo5bU")
//				.param("noteId", "5b5077204b47f82463b06efd")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.message").value("Updated Note Successfull!!"))
//				.andExpect(jsonPath("$.status").value(3));
//	}
//	
////	@Test
//	public void deleteTrashedNoteTest() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.delete("/api/note/deleteTrashedNote")
//				.param("noteId","5b5094b24b47f855622e8b84")
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.message").value("Deleted trashed Note Successfully!!"))
//				.andExpect(jsonPath("$.status").value(4));
//	}
//	
//}
