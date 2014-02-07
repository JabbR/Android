package net.jabbr.client.android.helpers;

import java.util.Hashtable;
import java.util.Map;

import android.graphics.Bitmap;

public class GravatarCache {
	private static GravatarCache mInstance= null;
	private Map<String, Bitmap> mGravatarStorage;
	
	protected GravatarCache() {
		mGravatarStorage = new Hashtable<String, Bitmap>();
	}
	
    public static synchronized GravatarCache getInstance(){
    	if(null == mInstance){
    		mInstance = new GravatarCache();
    	}
    	return mInstance;
    }

	public Map<String, Bitmap> getGravatarStorage() {
		return mGravatarStorage;
	}
}
