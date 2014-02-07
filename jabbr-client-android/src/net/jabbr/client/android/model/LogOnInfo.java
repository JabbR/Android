package net.jabbr.client.android.model;

import java.util.ArrayList;

public class LogOnInfo {
	
	@com.google.gson.annotations.SerializedName("UserId")
    private String mUserId;
	@com.google.gson.annotations.SerializedName("Rooms")
    private ArrayList<Room> mRooms;
    
    public LogOnInfo()
    {
    	mRooms = new ArrayList<Room>();
    }

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String mUserId) {
		this.mUserId = mUserId;
	}

	public ArrayList<Room> getRooms() {
		return mRooms;
	}

	public void setRooms(ArrayList<Room> mRooms) {
		this.mRooms = mRooms;
	}
}
