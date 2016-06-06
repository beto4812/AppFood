package com.appFood.android.login;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appFood.android.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RegistroActivity extends Activity {

	Button buttonRegistro;
	EditText editTextName;
	EditText editTextUsername;
	EditText editTextEmail;
	EditText editTextPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);

		buttonRegistro = (Button) findViewById(R.id.RegistroActivity_buttonRegistro);
		editTextName = (EditText) findViewById(R.id.RegistroActivity_editTextNombre);
		editTextUsername = (EditText) findViewById(R.id.RegistroActivity_editTextUsuario);
		editTextEmail = (EditText) findViewById(R.id.RegistroActivity_editTextEmail);
		editTextPassword = (EditText) findViewById(R.id.RegistroActivity_editTextContrasena);

		buttonRegistro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				iniciarRegistro();
			}
		});
	}

	public void iniciarRegistro() {
		Log.i("avazqu", "iniciarRegistro");
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("email", this.editTextEmail.getText().toString());
		params.put("name", this.editTextName.getText().toString());
		params.put("password", this.editTextPassword.getText().toString());
		params.put("username", this.editTextUsername.getText().toString());
		params.put("tipo", 1);
		
		client.get("http://appfood-appfood.rhcloud.com/rest/usuarios/alta/datos",  params,
				new JsonHttpResponseHandler(){
					
					public void onSuccess(int i, Header[] h, JSONObject o) {
						Toast.makeText(getApplicationContext(), "Success: "+o.toString(),
								Toast.LENGTH_SHORT).show();
					}
					
					public void onFailure(int i, Header[] h, Throwable t, JSONObject o) {
						Toast.makeText(getApplicationContext(), "1Failure: "+o.toString(),
								Toast.LENGTH_LONG).show();//
						Log.v("avazqu", o.toString());
					}					
					/*public void onFailure(int i, Header[] h,String s, Throwable t) {
						Toast.makeText(getApplicationContext(), "2Failure: "+ t.toString()+": "+s,
								Toast.LENGTH_LONG).show();//
						Log.v("avazqu", s);
					}*/
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registro, menu);
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

}
