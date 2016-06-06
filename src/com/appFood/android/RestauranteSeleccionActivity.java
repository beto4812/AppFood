package com.appFood.android;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RestauranteSeleccionActivity extends Activity {

	ProgressDialog dialog;
	View rootView;
	int idRest;
	TextView textViewNombre;
	TextView textViewPuntuacion;
	TextView textViewDescripcion;
	TextView textViewCategoria;
	TextView textComentarios;
	EditText editTextDejaComentario;
	RatingBar ratingBarComentario;
	Button enviarButton;
	Image image;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurante_seleccion);
		this.idRest = Integer.parseInt(getIntent().getStringExtra("id"));
		Log.v("avazqu", "" + idRest);
		this.textViewNombre = (TextView) findViewById(R.id.textViewNombre);
		this.textViewPuntuacion = (TextView) findViewById(R.id.textViewPuntuacion);
		this.textViewDescripcion = (TextView) findViewById(R.id.textViewDescripcion);
		this.textViewCategoria = (TextView) findViewById(R.id.textViewCategoria);
		this.textComentarios = (TextView) findViewById(R.id.textViewComentarios);
		
		this.editTextDejaComentario = (EditText) findViewById(R.id.editTextDejaComentario);
		this.ratingBarComentario = (RatingBar) findViewById(R.id.ratingBarComentario);
		this.enviarButton = (Button)findViewById(R.id.buttonEnviar);
		
		enviarButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				enviarComentario();
			}
		});
		getValues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.registro, menu);
		return true;
	}
	
	public void enviarComentario(){
		//http://localhost:8080/appFood/rest/comments/alta/data?idRes=1&comment=si&stars=1
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("idRes", idRest+1);//this.idRest
		params.put("comment", this.editTextDejaComentario.getText().toString());// this.editTextDejaComentario.getText().toString()
		params.put("stars",(int)this.ratingBarComentario.getRating());
		params.put("autor", "pedro");
		
		client.get("http://appfood-appfood.rhcloud.com/rest/comments/alta/data",  params,
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
					public void onFailure(int i, Header[] h,String s, Throwable t) {
						Toast.makeText(getApplicationContext(), "2Failure: "+ t.toString()+": "+s,
								Toast.LENGTH_LONG).show();//
						Log.v("avazqu", t.toString());
					}
				});
	}		
		
	

	public void getValues() {
		dialog = new ProgressDialog(this);
		dialog.setMessage("getting data...");
		dialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(
				"http://appfood-appfood.rhcloud.com/rest/restaurantes/getRestaurantById/datos?id="
						+ (idRest + 1), new JsonHttpResponseHandler() {
					public void onSuccess(int i, Header[] h, JSONObject o) {
						Log.v("avazqu", "succ");
						Log.v("", o.toString());
						try {
							textViewNombre.setText(o.getString("name"));
							textViewPuntuacion.setText("Stars: "
									+ o.getString("stars"));
							textViewDescripcion.setText("Descripcion: "
									+ o.getString("descripcion"));
							textViewCategoria.setText("Categoria: "
									+ o.getString("categoria"));
							dialog.dismiss();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					public void onSuccess(int a, Header[] h, JSONArray array) {
						for (int i = 0; i < array.length(); i++) {
							try {
								JSONObject obj = (JSONObject) array.get(i);
								// obj.getString("name");
								Log.v("avazqu", obj.getString("name"));
								// listTitles.add(obj.getString("name"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// populateList();
					}

					public void onFailure(int i, Header[] h, Throwable t,
							JSONObject o) {
						Log.v("avazqu", "fail");
						dialog.dismiss();
					}
				});
		//AsyncHttpClient client2 = new AsyncHttpClient();
		client.get(
				"http://appfood-appfood.rhcloud.com/rest/comments?id="
						+ (idRest + 1), new JsonHttpResponseHandler() {
					public void onSuccess(int i, Header[] h, JSONObject o) {
						Log.v("avazqu", "succ");
						Log.v("", o.toString());
					}

					public void onSuccess(int a, Header[] h, JSONArray array) {
						String textoComentarios = "";
						for (int i = 0; i < array.length(); i++) {
							try {
								JSONObject obj = (JSONObject) array.get(i);
								//obj.getString("name");
								Log.v("avazqu", obj.getString("comentario"));
								textoComentarios+=obj.getString("comentario")+" rate: "+obj.getString("stars")+"\n";
								// listTitles.add(obj.getString("name"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						textComentarios.setText(textoComentarios);
						// populateList();
					}

					public void onFailure(int i, Header[] h, Throwable t,
							JSONObject o) {
						Log.v("avazqu", "fail");
						dialog.dismiss();
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
