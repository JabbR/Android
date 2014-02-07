package net.jabbr.client.android;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import microsoft.aspnet.signalr.client.NullLogger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.HttpConnection;
import microsoft.aspnet.signalr.client.http.HttpConnectionFuture.ResponseCallback;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.Response;
import net.jabbr.client.android.helpers.GravatarCache;
import net.jabbr.client.android.model.User;

import net.jabbr.client.android.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomUsersAdapter extends BaseAdapter {
	private ArrayList<User> mRoomUsers;
	private LayoutInflater mLayoutInflater;
	private Activity mActivity;
	
	public RoomUsersAdapter(Context context, ArrayList<User> roomUsers) {
		mRoomUsers = roomUsers;
		mLayoutInflater = LayoutInflater.from(context);
		mActivity = (Activity) context;
	}

	public ArrayList<User> getRoomUsers(){
		return mRoomUsers;
	}
	
	@Override
	public int getCount() {
		return mRoomUsers.size();
	}

	@Override
	public Object getItem(int position) {
		return mRoomUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
				
        if (convertView == null) {
        	convertView = mLayoutInflater.inflate(R.layout.room_user_row, null);
        }
		
        final View currentView = convertView;
        
        final ImageView ivUser = (ImageView) currentView.findViewById(R.id.ivRoomUserGravatar);
        TextView tvUserName  = (TextView) currentView.findViewById(R.id.tvRoomUserName);
        
        User currentUser = mRoomUsers.get(position);

        int statusImage = Color.GREEN;
        switch(currentUser.getStatus()) {
			case Active:
				statusImage = Color.GREEN;
				break;
			case Inactive:
				statusImage = Color.YELLOW;
				break;
			case Offline:
				statusImage = Color.RED;
				break;
			default:
				break;
        }
        
        ivUser.setBackgroundColor(statusImage);
        tvUserName.setText(currentUser.getName());

        final UUID currentUUID = UUID.randomUUID();
        currentView.setTag(currentUUID);
        
        final String userHash = currentUser.getHash();
        if(userHash == null)  {
        	mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					ivUser.setImageBitmap(null);
				}
			});
        } else {
        	
	        Map<String, Bitmap> gravatarCache = GravatarCache.getInstance().getGravatarStorage();	
        	
        	if(gravatarCache.containsKey(userHash)) {
        		ivUser.setImageBitmap(gravatarCache.get(userHash));
        	} else { 
	        	final String url = "https://secure.gravatar.com/avatar/" + userHash + "?s=16&d=mm";
	        	
	        	HttpConnection connection = Platform.createHttpConnection(new NullLogger());
	        	Request request = new Request("GET");
	        	request.setUrl(url);
	        	
	        	connection.execute(request, new ResponseCallback() {
					
					@Override
					public void onResponse(Response response) throws Exception {
						if (response.getStatus() == 200) {
							final byte[] buff = response.readAllBytes();
							mActivity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
	
									Bitmap bitmap = BitmapFactory.decodeByteArray(buff,0,buff.length);
									
									if (currentUUID.equals((UUID)currentView.getTag())) {
										ivUser.setImageBitmap(bitmap);
										GravatarCache.getInstance().getGravatarStorage().put(userHash, bitmap);
									}
									
								}
							});
						}
					}
				});
        	}
        }
        
		return currentView;
	}
	


}
