package net.jabbr.client.android.model;

import java.util.Date;

public class User {
	
	@com.google.gson.annotations.SerializedName("Name")
    private String mName;
	@com.google.gson.annotations.SerializedName("Hash")
    private String mHash;
	@com.google.gson.annotations.SerializedName("Active")
    private boolean mActive;
	@com.google.gson.annotations.SerializedName("Status")
    private UserStatus mStatus;
	@com.google.gson.annotations.SerializedName("Note")
    private String mNote;
	@com.google.gson.annotations.SerializedName("AfkNote")
    private String mAfkNote;
	@com.google.gson.annotations.SerializedName("IsAfk")
    private boolean mIsAfk;
	@com.google.gson.annotations.SerializedName("Flag")
    private String mFlag;
	@com.google.gson.annotations.SerializedName("Country")
    private String mCountry;
	@com.google.gson.annotations.SerializedName("LastActivity")
    private Date mLastActivity;
	@com.google.gson.annotations.SerializedName("IsAdmin")
    private boolean mIsAdmin;
	

	
    public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public String getHash() {
		return mHash;
	}
	public void setHash(String mHash) {
		this.mHash = mHash;
	}
	public boolean isActive() {
		return mActive;
	}
	public void setActive(boolean mActive) {
		this.mActive = mActive;
	}
	public UserStatus getStatus() {
		return mStatus;
	}
	public void setStatus(UserStatus mStatus) {
		this.mStatus = mStatus;
	}
	public String getNote() {
		return mNote;
	}
	public void setNote(String mNote) {
		this.mNote = mNote;
	}
	public String getAfkNote() {
		return mAfkNote;
	}
	public void setAfkNote(String mAfkNote) {
		this.mAfkNote = mAfkNote;
	}
	public boolean isIsAfk() {
		return mIsAfk;
	}
	public void setIsAfk(boolean mIsAfk) {
		this.mIsAfk = mIsAfk;
	}
	public String getFlag() {
		return mFlag;
	}
	public void setFlag(String mFlag) {
		this.mFlag = mFlag;
	}
	public String getCountry() {
		return mCountry;
	}
	public void setCountry(String mCountry) {
		this.mCountry = mCountry;
	}
	public Date getLastActivity() {
		return mLastActivity;
	}
	public void setLastActivity(Date mLastActivity) {
		this.mLastActivity = mLastActivity;
	}
	public boolean isIsAdmin() {
		return mIsAdmin;
	}
	public void setIsAdmin(boolean mIsAdmin) {
		this.mIsAdmin = mIsAdmin;
	}
}
