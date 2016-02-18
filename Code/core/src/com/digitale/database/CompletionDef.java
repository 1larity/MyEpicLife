package com.digitale.database;

public class CompletionDef {
	/** Unique ID number for this Event completion **/
	private int uid;
	/** The date this event was completed **/
	private Long completionDate;
	/** The status of this completion cancelled/completed && **/
	private String completionStatus;

	/** Create blank completion **/
	public CompletionDef() {

	}

	/** Create populated completion **/
	public CompletionDef(Integer uid, Long completionDate,
			String completionStatus) {
		setUid(uid);
		setCompletionDate(completionDate);
		setCompletionStatus(completionStatus);
	}

	public String getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}
	//** return the date of this completion as long
	public Long getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Long completionDate) {
		this.completionDate = completionDate;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
}
