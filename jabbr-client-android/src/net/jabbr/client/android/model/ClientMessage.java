package net.jabbr.client.android.model;

public class ClientMessage {
	
	@com.google.gson.annotations.SerializedName("Id")
	private String mId; 
	@com.google.gson.annotations.SerializedName("Content")
    private String mContent; 
	@com.google.gson.annotations.SerializedName("Room")
    private String mRoom;
    
	public String getContent() {
		return mContent;
	}
	public void setContent(String mContent) {
		this.mContent = mContent;
	}
	public String getId() {
		return mId;
	}
	public void setId(String mId) {
		this.mId = mId;
	}
	public String getRoom() {
		return mRoom;
	}
	public void setRoom(String mRoom) {
		this.mRoom = mRoom;
	} 

}
