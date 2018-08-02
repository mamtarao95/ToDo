package com.bridgelabz.fundoonoteapp.note.models;

public class LabelDTO {
	
	private String labelId;
	
	private String labelName;

	public LabelDTO() {
		
	}
	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public LabelDTO(String labelId, String labelName) {
		super();
		this.labelId = labelId;
		this.labelName = labelName;
	}
	
	

}
