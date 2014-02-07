package net.jabbr.client.android;


import java.util.ArrayList;
import java.util.Date;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import net.jabbr.client.android.helpers.HubConnectionFactory;
import net.jabbr.client.android.model.ClientMessage;
import net.jabbr.client.android.model.Message;
import net.jabbr.client.android.model.Room;
import net.jabbr.client.android.model.User;
import net.jabbr.client.android.model.UserMessages;
import net.jabbr.client.android.model.UserStatus;

import net.jabbr.client.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class RoomActivity extends ActionBarActivity {
	
	private String mCurrentRoomName;
	
	@Override
	public void onBackPressed() {
		HubProxy chat = HubConnectionFactory.getInstance().getChatHub();
		chat.invoke("Send","/leave " + mCurrentRoomName, "");
		
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Bundle b = getIntent().getExtras();
		
		final Activity activity = this;
		final String roomName = b.getString("roomName");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
		
		final SlidingPaneLayout splMain = (SlidingPaneLayout) findViewById(R.id.splMain);
		final EditText etPost = (EditText) findViewById(R.id.etPost);
		final ListView lv = (ListView) findViewById(R.id.lvMessages);
		
		Button sendButton = (Button) findViewById(R.id.send_button);
		
		splMain.openPane();
		this.setTitle(roomName);
		lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		etPost.setEnabled(false);
		etPost.setText("Joining Room...");
		
		final HubProxy chat = HubConnectionFactory.getInstance().getChatHub();
		
		chat.invoke(Room.class, "GetRoomInfo", roomName).done(new Action<Room>() {
			
			@Override
			public void run(final Room room) throws Exception {
				mCurrentRoomName = room.getName();
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						ListView lvUsers = (ListView) findViewById(R.id.lvRoomUsers);
						ListView lvOwners = (ListView) findViewById(R.id.lvRoomOwners);

						ArrayList<User> listUsers = new ArrayList<User>();
						ArrayList<User> listOwners = new ArrayList<User>();
						
						for(User u : room.getUsers()) {
							if(room.getOwners().contains(u.getName())) {
								listOwners.add(u);
							} else {
								listUsers.add(u);
							}
						}
						
						lvUsers.setAdapter(new RoomUsersAdapter(activity, listUsers));
						lvOwners.setAdapter(new RoomUsersAdapter(activity, listOwners));
						
						ArrayList<UserMessages> msgList = new ArrayList<UserMessages>();
						for(Message m : room.getRecentMessages()) {
							
							addMessageToUserList(msgList, m);
						}
						
				        lv.setAdapter(new RoomAdapter(activity, msgList));	
					}
				});
				
				chat.subscribe(new Object() {
					@SuppressWarnings("unused")
					public void addMessage(final Message message,final String room) {
						
							runOnUiThread(new Runnable() {
								public void run() {
									if(room.equals(roomName)) {
										RoomAdapter ra = (RoomAdapter) lv.getAdapter();
										
										addMessageToUserList(ra.getMessageList(), message); 
										
										ra.notifyDataSetChanged();	
									}
								}
							});	
					}
					
					@SuppressWarnings("unused")
					public void sendPrivateMessage(final String callingUser, final String toUser, final String privateMsg) {
						runOnUiThread(new Runnable() {
							public void run() {
								
								RoomAdapter ra = (RoomAdapter) lv.getAdapter();
								
								Message msg = new Message();
								msg.setContent("private: " + privateMsg);
								msg.setWhen(new Date());
								msg.setId("private");
								
								User u = new User();
								u.setName(callingUser);
								u.setStatus(UserStatus.Active);
								msg.setUser(u);
								
								addMessageToUserList(ra.getMessageList(), msg);
								
								ra.notifyDataSetChanged();	
							}
						});
					}
					
					@SuppressWarnings("unused")
					public void updateRoom(Room room) {					
						updateRoomUsers(room);
					}
				});
				
				chat.invoke("Send", "/join " + roomName, "").done(new Action<Void>() {
					
					@Override
					public void run(Void obj) throws Exception {
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {

						        etPost.setEnabled(true);
						        etPost.setText("");
							}
						});						
					}
				});
			}
		});
		
		sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendMessage(roomName, chat);
			}
		});
		
		etPost.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
			
				sendMessage(roomName, chat);
				return true;
			}
		});
	}

	private void updateRoomUsers(Room room) {
		final ListView lvUsers = (ListView) findViewById(R.id.lvRoomUsers);
		final ListView lvOwners = (ListView) findViewById(R.id.lvRoomOwners);

		HubProxy chat = HubConnectionFactory.getInstance().getChatHub();
		
		chat.invoke(Room.class, "GetRoomInfo", room.getName()).done(new Action<Room>() {
			
			@Override
			public void run(final Room room) throws Exception {
				runOnUiThread(new Runnable() {
					public void run() {
						ArrayList<User> listUsers = new ArrayList<User>();
						ArrayList<User> listOwners = new ArrayList<User>();
						
						for(User u : room.getUsers()) {
							if(room.getOwners().contains(u.getName())) {
								listOwners.add(u);
							} else {
								listUsers.add(u);
							}
						}
				
						RoomUsersAdapter adapUsers = (RoomUsersAdapter) lvUsers.getAdapter();
						adapUsers.getRoomUsers().clear();
						adapUsers.getRoomUsers().addAll(listUsers);
						adapUsers.notifyDataSetChanged();
				
						RoomUsersAdapter adapOwners = (RoomUsersAdapter) lvOwners.getAdapter();
						adapOwners.getRoomUsers().clear();
						adapOwners.getRoomUsers().addAll(listOwners);
						adapOwners.notifyDataSetChanged();
					}
				});
			}
		});
	}
	
	private void sendMessage(String roomName, HubProxy chat) {
		EditText etPost = (EditText) findViewById(R.id.etPost);
		
		ClientMessage cm = new ClientMessage();
		cm.setContent(etPost.getText().toString());
		cm.setRoom(roomName);

		chat.invoke("Send",  cm);
			
		etPost.setText("");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room, menu);
		return true;
	}

	@SuppressLint("InlinedApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	        	HubConnectionFactory.getInstance().disconnect();
	        	
				SharedPreferences settings = this.getSharedPreferences(getResources().getText(R.string.JABBRSettings).toString(), MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putString(getResources().getText(R.string.CookieKey).toString(), null);
				editor.commit();
	        	
	        	Intent al = new Intent(getApplicationContext(), LoginActivity.class);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					al.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				}
	            startActivity(al);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void addMessageToUserList(ArrayList<UserMessages> msgList, Message message){

		UserMessages last = (msgList.size() == 0) ? null : msgList.get(msgList.size() - 1);
		UserMessages current = null;
		
		if(last == null || !last.getUser().getName().equals(message.getUser().getName())) {	
			current = new UserMessages();
			current.setUser(message.getUser());
			current.setMessages(new ArrayList<Message>());
			msgList.add(current);
		} else {
			current = last;
		}
		
		current.getMessages().add(message);
	}
}
 