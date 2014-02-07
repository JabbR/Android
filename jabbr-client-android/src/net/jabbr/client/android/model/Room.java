package net.jabbr.client.android.model;

import java.util.ArrayList;

public class Room {
	
	@com.google.gson.annotations.SerializedName("Name")
    private String mName;
	@com.google.gson.annotations.SerializedName("Count")
    private int mCount;
	@com.google.gson.annotations.SerializedName("Private")
    private boolean mPrivate;
	@com.google.gson.annotations.SerializedName("Topic")
    private String mTopic;
	@com.google.gson.annotations.SerializedName("Closed")
    private boolean mClosed;
	@com.google.gson.annotations.SerializedName("Welcome")
    private String mWelcome;
	@com.google.gson.annotations.SerializedName("Users")
    private ArrayList<User> mUsers;
	@com.google.gson.annotations.SerializedName("Owners")
    private ArrayList<String> mOwners;
	@com.google.gson.annotations.SerializedName("RecentMessages")
    private ArrayList<Message> mRecentMessages;
    
    public Room() 
    {
    	mUsers = new ArrayList<User>();
    	mOwners = new ArrayList<String>();
    	mRecentMessages = new ArrayList<Message>();
    }
    
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public int getCount() {
		return mCount;
	}
	public void setCount(int mCount) {
		this.mCount = mCount;
	}
	public boolean isPrivate() {
		return mPrivate;
	}
	public void setPrivate(boolean mPrivate) {
		this.mPrivate = mPrivate;
	}
	public String getTopic() {
		return mTopic;
	}
	public void setTopic(String mTopic) {
		this.mTopic = mTopic;
	}
	public boolean isClosed() {
		return mClosed;
	}
	public void setClosed(boolean mClosed) {
		this.mClosed = mClosed;
	}
	public String getWelcome() {
		return mWelcome;
	}
	public void setWelcome(String mWelcome) {
		this.mWelcome = mWelcome;
	}
	public ArrayList<User> getUsers() {
		return mUsers;
	}
	public void setUsers(ArrayList<User> mUsers) {
		this.mUsers = mUsers;
	}
	public ArrayList<String> getOwners() {
		return mOwners;
	}
	public void setOwners(ArrayList<String> mOwners) {
		this.mOwners = mOwners;
	}
	public ArrayList<Message> getRecentMessages() {
		return mRecentMessages;
	}
	public void setRecentMessages(ArrayList<Message> mRecentMessages) {
		this.mRecentMessages = mRecentMessages;
	}
}
