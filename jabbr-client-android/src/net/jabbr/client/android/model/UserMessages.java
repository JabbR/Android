package net.jabbr.client.android.model;

import java.util.ArrayList;

public class UserMessages {

	private User mUser;
	private ArrayList<Message> mMessages;
	
	
	public User getUser() {
		return mUser;
	}
	public void setUser(User mUser) {
		this.mUser = mUser;
	}
	public ArrayList<Message> getMessages() {
		return mMessages;
	}
	public void setMessages(ArrayList<Message> mMessages) {
		this.mMessages = mMessages;
	} 
}
