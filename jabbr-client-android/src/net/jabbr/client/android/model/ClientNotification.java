package net.jabbr.client.android.model;

import android.graphics.Bitmap;

public class ClientNotification {
	
	@com.google.gson.annotations.SerializedName("Room")
	private String mRoom;
	@com.google.gson.annotations.SerializedName("ImageUrl")
    private String mImageUrl;
	@com.google.gson.annotations.SerializedName("ImageBitmap")
    private Bitmap mImageBitmap;
	@com.google.gson.annotations.SerializedName("Source")
    private String mSource;
	@com.google.gson.annotations.SerializedName("Content")
    private String mContent;
    
    
	public String getImageUrl() {
		return mImageUrl;
	}
	public void setImageUrl(String mImageUrl) {
		this.mImageUrl = mImageUrl;
	}
	public String getRoom() {
		return mRoom;
	}
	public void setRoom(String mRoom) {
		this.mRoom = mRoom;
	}
	public String getSource() {
		return mSource;
	}
	public void setSource(String mSource) {
		this.mSource = mSource;
	}
	public String getContent() {
		return mContent;
	}
	public void setContent(String mContent) {
		this.mContent = mContent;
	}
	public Bitmap getImageBitmap() {
		return mImageBitmap;
	}
	public void setImageBitmap(Bitmap mImageBitmap) {
		this.mImageBitmap = mImageBitmap;
	}
}
