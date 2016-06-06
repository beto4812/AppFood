package com.appFood.android.login;



import org.apache.http.Header;
import org.json.JSONObject;

import com.appFood.android.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity {
//
	EditText editTextUsername;
	EditText editTextPassword;
	Button buttonLogIn;
	Button buttonRegistro;
	SharedPreferences.Editor editor;
	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logg_in);

		editTextUsername = (EditText) findViewById(R.id.LogInActivity_editTextUsuario);
		editTextPassword = (EditText) findViewById(R.id.LogInActivity_editTextContrasena);
		buttonLogIn = (Button) findViewById(R.id.LogInActivity_buttonLogIn);
		buttonRegistro = (Button) findViewById(R.id.LogInActivity_buttonRegistro);

		buttonLogIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				iniciarLogIn();
			}
		});

		buttonRegistro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(LogInActivity.this,
						RegistroActivity.class);
				startActivity(i);
			}
		});

		prefs = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
		editor = prefs.edit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logg_in, menu);

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void iniciarLogIn() {
		Log.i("avazqu", "iniciarRegistro");
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("password", this.editTextPassword.getText().toString());
		params.put("username", this.editTextUsername.getText().toString());

		client.get(
				"http://appfood-appfood.rhcloud.com/rest/usuarios/auth/datos",
				params, new JsonHttpResponseHandler() {

					public void onSuccess(int i, Header[] h, JSONObject o) {
						Toast.makeText(getApplicationContext(),
								"Success: " + o.toString(), Toast.LENGTH_SHORT)
								.show();
					}

					public void onFailure(int i, Header[] h, Throwable t,
							JSONObject o) {
						Toast.makeText(getApplicationContext(),
								"1Failure: " + o.toString(), Toast.LENGTH_LONG)
								.show();//
						Log.v("avazqu", o.toString());
					}
				});
	}
}
