package net.jabbr.client.android;

import java.util.ArrayList;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import net.jabbr.client.android.helpers.HubConnectionFactory;
import net.jabbr.client.android.model.Room;

import net.jabbr.client.android.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class RoomLobbyActivity extends ActionBarActivity {
	
	@Override
	public void onBackPressed() {
		HubConnection conn = HubConnectionFactory.getInstance().getHubConnection();
		conn.stop();
		
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_room_lobby);

		final Activity context = this;
		
		this.setTitle(getString(R.string.roomslobby));
		
		HubConnection conn = HubConnectionFactory.getInstance().getHubConnection();
		conn.connectionSlow(new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(context, "Connection is slow and may fail...", Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
		conn.reconnected(new Runnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(context, "Connection is reconnected", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		
		final ListView lv = (ListView) findViewById(R.id.evUser);		
		final HubProxy chat = HubConnectionFactory.getInstance().getChatHub();
		
		chat.invoke(Room[].class, "GetRooms").done(new Action<Room[]>() {
			
			@Override
			public void run(final Room[] rooms) throws Exception {
				final ArrayList<String> roomsCaption = new ArrayList<String>();
				
				for(Room r : rooms) {
					roomsCaption.add(r.getName() + "(" + r.getCount() + ")");
				}
				
				runOnUiThread(new Runnable() {
					public void run() {
						
						lv.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, roomsCaption));

						// listening to single list item on click
				        lv.setOnItemClickListener(new OnItemClickListener() {
				        	public void onItemClick(AdapterView<?> parent, View view,
			        			int position, long id) {
				               
								Intent i = new Intent(getApplicationContext(), RoomActivity.class);
								i.putExtra("roomName", rooms[position].getName());
								startActivity(i);
				             
				        	}
				        });						
					}
				});
			}
		});	
		
		chat.subscribe(new Object() {
			@SuppressWarnings("unused")
			public void updateRoom(Room room) {					
				chat.invoke(Room[].class, "GetRooms").done(new Action<Room[]>() {
					
					@Override
					public void run(final Room[] rooms) throws Exception {
						final ArrayList<String> roomsCaption = new ArrayList<String>();
						
						for(Room r : rooms) {
							roomsCaption.add(r.getName() + "(" + r.getCount() + ")");
						}
						
						runOnUiThread(new Runnable() {
							public void run() {
								@SuppressWarnings("unchecked")
								ArrayAdapter<String> adap = (ArrayAdapter<String>) lv.getAdapter();
								adap.clear();
								
								for(String room : roomsCaption) {	
									adap.add(room);
								}
						
								adap.notifyDataSetChanged();
							}
						});
						
					}
				});	
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_lobby, menu);
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

}
