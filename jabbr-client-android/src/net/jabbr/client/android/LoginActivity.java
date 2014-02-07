package net.jabbr.client.android;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.CookieCredentials;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import net.jabbr.client.android.helpers.HubConnectionFactory;

import net.jabbr.client.android.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	
	private static final String SERVER_URL_CONFIG_KEY = "server_url";
	
	// Values for user and password at the time of the login attempt.
	private String mUser;
	private String mPassword;

	// UI references.
	private EditText mUserView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Platform.loadPlatformComponent(new AndroidPlatformComponent());
		this.setTitle("");
		
		setContentView(R.layout.activity_login);
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		
		// Set up the login form.
		mUserView = (EditText) findViewById(R.id.evUser);
		//mUserView.setText(mUser);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		SharedPreferences settings = this.getSharedPreferences(getResources().getText(R.string.JABBRSettings).toString(), MODE_PRIVATE);
		String cookie = settings.getString(getResources().getText(R.string.CookieKey).toString(), null);
		
		if(cookie == null) {
			showProgress(false);
		} else {
			
			CookieCredentials cc = new CookieCredentials(cookie);
			
			final HubConnectionFactory hcf = HubConnectionFactory.getInstance();
			
			SignalRFuture<Void> connect = hcf.connect(getJabbrServerFromPreferences(), cc);
			configConnectFuture(connect);
		}
	}

	private String getJabbrServerFromPreferences() {
		String defaultServer = getString(R.string.default_server_url);
		String server = PreferenceManager.getDefaultSharedPreferences(this).getString(SERVER_URL_CONFIG_KEY, defaultServer);
		server = ensureTrailingSlash(server);
		
		return server;
	}

	private String ensureTrailingSlash(String server) {
		server = server.trim();
		
		if (!server.endsWith("/")) {
			server += "/";
		}
		
		return server;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, SettingsActivity.class));
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		// Reset errors.
		mUserView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mUser = mUserView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} 

		// Check for a valid user.
		if (TextUtils.isEmpty(mUser)) {
			mUserView.setError(getString(R.string.error_field_required));
			focusView = mUserView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			
			showProgress(true);

			final HubConnectionFactory hcf = HubConnectionFactory.getInstance();
			
			SignalRFuture<Void> connect = hcf.connect(getJabbrServerFromPreferences(), mUser, mPassword);
			configConnectFuture(connect);
		}
	}

	
	private void configConnectFuture(SignalRFuture<Void> connect) {
		final HubConnectionFactory hcf = HubConnectionFactory.getInstance();
		final LoginActivity la = this;
		
		connect.onError(new ErrorCallback() {
			
			@Override
			public void onError(final Throwable error) {
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						showProgress(false);
						
						la.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								TextView tvError = (TextView) la.findViewById(R.id.tvError);
								tvError.setText(getString(R.string.login_error) + error.getMessage());
								tvError.setVisibility(View.VISIBLE);								
							}
						});
					}
				});
				
			}
		});
		

		
		connect.done(new Action<Void>() {
			@Override
			public void run(Void obj) throws Exception {
				
				HubConnection conn = hcf.getHubConnection();
				CookieCredentials cookieCredentials = (CookieCredentials) conn.getCredentials();
				
				
				SharedPreferences settings = la.getSharedPreferences(getResources().getText(R.string.JABBRSettings).toString(), MODE_PRIVATE);
				Editor editor = settings.edit();
				editor.putString(getResources().getText(R.string.CookieKey).toString(), cookieCredentials.toString());
				editor.commit();
				
				final HubProxy chat = hcf.getChatHub();
				
				chat.invoke("Join").done(new Action<Void>() {
					
					@Override
					public void run(Void obj) throws Exception {
						runOnUiThread(new Runnable() {
							
							@SuppressLint("InlinedApi")
							public void run() {
								Intent i = new Intent(getApplicationContext(), RoomLobbyActivity.class);
								
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
									i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
								}
					            startActivity(i);
							}
						});

					}
				});
			}
		});	
	}
	
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {

		this.setTitle(show ? "" : getString(R.string.action_sign_in_short));

		
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}
