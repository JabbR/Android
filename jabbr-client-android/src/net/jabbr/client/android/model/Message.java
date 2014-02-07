package net.jabbr.client.android.model;

import java.util.Date;

public class Message {
	@com.google.gson.annotations.SerializedName("HtmlEncoded")
	private boolean mHtmlEncoded;
	@com.google.gson.annotations.SerializedName("Id")
    private String mId;
	@com.google.gson.annotations.SerializedName("Content")
    private String mContent;
	@com.google.gson.annotations.SerializedName("When")
	private Date mWhen;
	@com.google.gson.annotations.SerializedName("User")
	private User mUser;
    
    
	public boolean isHtmlEncoded() {
		return mHtmlEncoded;
	}
	public void setHtmlEncoded(boolean mHtmlEncoded) {
		this.mHtmlEncoded = mHtmlEncoded;
	}
	public String getId() {
		return mId;
	}
	public void setId(String mId) {
		this.mId = mId;
	}
	public String getContent() {
		return mContent;
	}
	public void setContent(String mContent) {
		this.mContent = mContent;
	}
	public Date getWhen() {
		return mWhen;
	}
	public void setWhen(Date mWhen) {
		this.mWhen = mWhen;
	}
	public User getUser() {
		return mUser;
	}
	public void setUser(User mUser) {
		this.mUser = mUser;
	}
}
