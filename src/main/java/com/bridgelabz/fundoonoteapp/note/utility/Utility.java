package com.bridgelabz.fundoonoteapp.note.utility;

import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import com.bridgelabz.fundoonoteapp.note.exceptions.NoteNotFoundException;
import com.bridgelabz.fundoonoteapp.note.models.CreateNoteDTO;
import com.bridgelabz.fundoonoteapp.note.models.UpdateNoteDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Utility {

	final static String SECRET = "mamta";

	public static Claims parseJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET)).parseClaimsJws(token)
				.getBody();

		System.out.println("The details of claims: ");
		System.out.println("ID: " + claims.getId());
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Exp time: " + claims.getExpiration());
		System.out.println("issue at: " + claims.getIssuedAt());
		return claims;
	}

	public static boolean validateReminder(Date reminder) {
		
		return reminder.before(new Date());
		
	}

	public static void validateCreateDTO(CreateNoteDTO createNoteDTO) throws NoteNotFoundException {
		if (createNoteDTO.getDescription() == null && createNoteDTO.getTitle() == null || createNoteDTO.getDescription().equals("") && createNoteDTO.getTitle().equals("")
				) {
			throw new NoteNotFoundException("Fields cannot be empty");
		}
		if (createNoteDTO.getDescription().equals("") && createNoteDTO.getTitle() == null
				|| createNoteDTO.getDescription() == null && createNoteDTO.getTitle().equals("")) {
			throw new NoteNotFoundException("Fields cannot be empty");
		}

	}

	public static void validateUpdateDTO(UpdateNoteDTO updateNoteDTO) throws NoteNotFoundException {
		if (updateNoteDTO.getDescription() == "" || updateNoteDTO.getTitle() == "") {
			throw new NoteNotFoundException("Fields cannot be empty");
		}
	}

}
