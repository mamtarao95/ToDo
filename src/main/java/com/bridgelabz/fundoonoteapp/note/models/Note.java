package com.bridgelabz.fundoonoteapp.note.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.util.Date;
import java.util.List;

@Document(indexName = "fundoonote", type = "note")
public class Note {

	@Id
	private String id;
	private String userId;
	private String title;
	private String description;
	private Date createdAt;
	private Date updatedAt;
	private Date reminder;
	private boolean trashed;
	private String color;
	private boolean pin;
	private boolean archive;
	private List<LabelDTO> labels;
	public String getNoteId() {
		return id;
	}
	public void setNoteId(String noteId) {
		this.id = noteId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public boolean isTrashed() {
		return trashed;
	}
	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isPin() {
		return pin;
	}
	public void setPin(boolean pin) {
		this.pin = pin;
	}
	public boolean isArchive() {
		return archive;
	}
	public void setArchive(boolean archive) {
		this.archive = archive;
	}
	public List<LabelDTO> getLabels() {
		return labels;
	}
	public void setLabels(List<LabelDTO> labels) {
		this.labels = labels;
	}
	
	public Note() {
		
	}
	public Note(String noteId, String userId, String title, String description, Date createdAt, Date updatedAt,
			Date reminder, boolean trashed, String color, boolean pin, boolean archive, List<LabelDTO> labels) {
		super();
		this.id = noteId;
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.reminder = reminder;
		this.trashed = trashed;
		this.color = color;
		this.pin = pin;
		this.archive = archive;
		this.labels = labels;
	}
	
	
	

}
