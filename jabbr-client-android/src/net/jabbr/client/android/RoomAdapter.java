package net.jabbr.client.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import microsoft.aspnet.signalr.client.NullLogger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.HttpConnection;
import microsoft.aspnet.signalr.client.http.HttpConnectionFuture.ResponseCallback;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.Response;
import net.jabbr.client.android.helpers.GravatarCache;
import net.jabbr.client.android.model.Message;
import net.jabbr.client.android.model.UserMessages;

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
import android.widget.LinearLayout;
import android.widget.TextView;

public class RoomAdapter extends BaseAdapter  {

    private ArrayList<UserMessages> mListData;
    private LayoutInflater mLayoutInflater;
    private Activity mActivity;
 
    public RoomAdapter(Context context, ArrayList<UserMessages> listData) {
        mListData = listData;
        mLayoutInflater = LayoutInflater.from(context);
        mActivity = (Activity)context;
    }
 
    @Override
    public int getCount() {
        return mListData.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public ArrayList<UserMessages> getMessageList() {
    	return mListData;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.room_user_messages_row, null);
        }
        
        final View currentView = convertView;
        
        final UUID currentUUID = UUID.randomUUID();
        currentView.setTag(currentUUID);
        
        final String userHash = mListData.get(position).getUser().getHash();
        final ImageView iv = (ImageView) currentView.findViewById(R.id.ivUserImage);
        LinearLayout ll = (LinearLayout) currentView.findViewById(R.id.llMessages);
        
        if(userHash == null)  {
        	mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					iv.setImageBitmap(null);
				}
			});
        } else {
	        Map<String, Bitmap> gravatarCache = GravatarCache.getInstance().getGravatarStorage();	
        	
        	if(gravatarCache.containsKey(userHash)) {
        		iv.setImageBitmap(gravatarCache.get(userHash));
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
										iv.setImageBitmap(bitmap);
										GravatarCache.getInstance().getGravatarStorage().put(userHash, bitmap);
									}
									
								}
							});
						}
					}
				});
        	}
        }
        
        TextView tvUser = (TextView) currentView.findViewById(R.id.tvUserName);
        String userName = mListData.get(position).getUser().getName();		
        		
        tvUser.setText(userName);
        

        int statusImage = Color.GREEN;
        switch(mListData.get(position).getUser().getStatus()) {
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
        
        iv.setBackgroundColor(statusImage);
        ll.removeAllViews();
        
        Date lastdate = null;
        if(position > 0) {
        	ArrayList<Message> lastMessages = mListData.get(position - 1).getMessages();
        	
        	if(lastMessages.size() > 0) {
        		lastdate = lastMessages.get(lastMessages.size() - 1).getWhen();
        	}
        }
        
        for(Message msg : mListData.get(position).getMessages()) {
        	
        	if(lastdate != null && !isSameDay(lastdate, msg.getWhen())) {
        		View msgView = mLayoutInflater.inflate(R.layout.room_messages_row, null);
            	TextView tvMessage = (TextView) msgView.findViewById(R.id.tvMessage);
            	TextView tvWhen = (TextView) msgView.findViewById(R.id.tvWhen);

                tvMessage.setText("");
                tvWhen.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(msg.getWhen()));
                
                ll.addView(msgView);
        	}
        	
        	View msgView = mLayoutInflater.inflate(R.layout.room_messages_row, null);
        	TextView tvMessage = (TextView) msgView.findViewById(R.id.tvMessage);
        	TextView tvWhen = (TextView) msgView.findViewById(R.id.tvWhen);
        	
            tvMessage.setText(msg.getContent());
            tvWhen.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(msg.getWhen()));
            
            if(msg.getId().equals("private")) {
            	tvMessage.setTextColor(Color.RED);
            }
        	
        	ll.addView(msgView);
        	
        	lastdate = msg.getWhen();
        }
		
        return currentView;
    }
    
    private boolean isSameDay(Date date1, Date date2) {
    	Calendar cal1 = Calendar.getInstance();
    	Calendar cal2 = Calendar.getInstance();
    	
    	cal1.setTime(date1);
    	cal2.setTime(date2);
    	
    	return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
