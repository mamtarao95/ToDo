package com.bridgelabz.fundoonoteapp.note.models;

import java.util.Date;
import java.util.List;

public class NoteDTO {
	private String id; // make it id
	private String title;
	private String description;
	private Date createdAt;
	private Date updatedAt;
	private Date reminder;
	private String color;
	private boolean archive;
	private boolean pin;
	private boolean trashed;
	private List<LabelDTO> labels;
	
	
	public NoteDTO() {
		
	}

	
	
	public boolean isArchive() {
		return archive;
	}



	public void setArchive(boolean archive) {
		this.archive = archive;
	}



	public boolean isPin() {
		return pin;
	}



	public void setPin(boolean pin) {
		this.pin = pin;
	}



	public boolean isTrashed() {
		return trashed;
	}



	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}



	public String getNoteId() {
		return id;
	}

	public void setNoteId(String noteId) {
		this.id = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}



	public List<LabelDTO> getLabels() {
		return labels;
	}

	public void setLabels(List<LabelDTO> labels) {
		this.labels = labels;
	}



	public NoteDTO(String noteId, String title, String description, Date createdAt, Date updatedAt, Date reminder,
			String color, boolean archive, boolean pin, boolean trashed, List<LabelDTO> labels) {
		super();
		this.id = noteId;
		this.title = title;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.reminder = reminder;
		this.color = color;
		this.archive = archive;
		this.pin = pin;
		this.trashed = trashed;
		this.labels = labels;
	}





}
