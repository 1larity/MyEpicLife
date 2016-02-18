package com.digitale.database;

public class AwardedDef {
	/** Unique ID number for this Award **/
	private int uid;
	/** The date this event was completed **/
	private Long awardedDate;
	/** The status of this completion cancelled/completed && **/
	private int awardId;

	/** Create blank completion **/
	public AwardedDef() {

	}

	/** Create populated completion **/
	public AwardedDef(int uid, Long completionDate,
			int awardId) {
		setUid(uid);
		setAwardDate(completionDate);
		setAwardId(awardId);
	}

	

	public int getAwardId() {
		return awardId;
	}

	public void setAwardId(int awardId) {
		this.awardId = awardId;
	}

	public Long getAwardDate() {
		return awardedDate;
	}

	public void setAwardDate(Long awardDate) {
		this.awardedDate = awardDate;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}
}
