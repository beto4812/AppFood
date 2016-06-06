package com.appFood.android;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appFood.android.login.LogInActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RestaurantesFragment extends Fragment {

	String pageTitle = "Bienvenido";//
	ProgressDialog dialog;
	View rootView;
	ArrayList<String> listTitles;
	ListView listview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		rootView = inflater.inflate(R.layout.fragment_section_restaurantes,
				container, false);
		listTitles = new ArrayList<>();
		listview = (ListView) rootView.findViewById(R.id.listViewRestaurantes);
		getListValues();
		return rootView;
	}

	public void getListValues() {
		dialog = new ProgressDialog(rootView.getContext());
		dialog.setMessage("getting data...");
		dialog.show();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://appfood-appfood.rhcloud.com/rest/restaurantes/list",
				new JsonHttpResponseHandler() {
					public void onSuccess(int i, Header[] h, JSONObject o) {
						Log.v("avazqu", "succ");
					}

					public void onSuccess(int a, Header[] h, JSONArray array) {
						for (int i = 0; i < array.length(); i++) {
							try {
								JSONObject obj = (JSONObject) array.get(i);
								// obj.getString("name");
								Log.v("avazqu", obj.getString("name"));
								listTitles.add(obj.getString("name"));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						populateList();
					}

					public void onFailure(int i, Header[] h, Throwable t,
							JSONObject o) {
						Log.v("avazqu", "fail");
						dialog.dismiss();
					}
				});
	}

	public void populateList() {
		getActivity();

		final StableArrayAdapter adapter = new StableArrayAdapter(
				getActivity(), android.R.layout.simple_list_item_1, listTitles);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(rootView.getContext(),
						"Click ListItem Number " + position, Toast.LENGTH_LONG)
						.show();
				
				Intent intent = new Intent(getActivity(), RestauranteSeleccionActivity.class);
				intent.putExtra("id",""+position);
				startActivity(intent);
			}
		});

		dialog.dismiss();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			Toast toast = Toast.makeText(getActivity().getApplicationContext(),
					"SEARCH", Toast.LENGTH_SHORT);
			toast.show();
			return true;
		case R.id.action_log_in:
			Intent intent = new Intent(getActivity(), LogInActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		List<String> objects;

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			this.objects = objects;
		}

		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				view = inflater.inflate(R.layout.list_single, parent, false);
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.text = (TextView) view.findViewById(R.id.label);
				viewHolder.text.setTextColor(Color.BLACK);
				viewHolder.image = (ImageView) view.findViewById(R.id.image);
				viewHolder.image.setVisibility(View.GONE);
				viewHolder.pb = (ProgressBar) view
						.findViewById(R.id.progressBar1);
				view.setTag(viewHolder);
			} else {
				// viewHolder = (ViewHolder) convertView.getTag();
			}
			ViewHolder holder = (ViewHolder) view.getTag();
			/*
			 * view.setOnClickListener(new OnClickListener(){
			 * 
			 * 
			 * @Override public void onClick(View v) { // TODO Auto-generated
			 * method stub
			 * 
			 * } });
			 */
			// mIdMap.get(position);
			holder.text.setText(objects.get(position));
			holder.image.setId(position);
			holder.image.setTag("http://appfood-appfood.rhcloud.com/resources/images/"
					//.setTag("http://appfood-appfood.rhcloud.com/resources/images/"
							+ position + ".jpg");
			PbAndImage pb_and_image = new PbAndImage();
			pb_and_image.setImg(holder.image);
			pb_and_image.setPb(holder.pb);
			new DownloadImageTask().execute(pb_and_image);
			return view;
		}

		public class PbAndImage {
			private ImageView img;
			private ProgressBar pb;

			public ImageView getImg() {
				return img;
			}

			public void setImg(ImageView img) {
				this.img = img;
			}

			public ProgressBar getPb() {
				return pb;
			}

			public void setPb(ProgressBar pb) {
				this.pb = pb;
			}
		}

		private class ViewHolder {
			protected TextView text;
			protected ImageView image;
			protected ProgressBar pb;
		}

		private class DownloadImageTask extends
				AsyncTask<PbAndImage, Void, Bitmap> {

			ImageView imageView = null;
			ProgressBar pb = null;

			protected Bitmap doInBackground(PbAndImage... pb_and_images) {
				this.imageView = (ImageView) pb_and_images[0].getImg();
				this.pb = (ProgressBar) pb_and_images[0].getPb();
				return getBitmapDownloaded((String) imageView.getTag());
			}

			protected void onPostExecute(Bitmap result) {
				System.out.println("Downloaded " + imageView.getId());
				imageView.setVisibility(View.VISIBLE);
				pb.setVisibility(View.GONE); // hide the progressbar after
												// downloading the image.
				imageView.setImageBitmap(result); // set the bitmap to the
													// imageview.
			}

			/** This function downloads the image and returns the Bitmap **/
			private Bitmap getBitmapDownloaded(String url) {
				System.out.println("String URL " + url);
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeStream((InputStream) new URL(
							url).getContent());
					bitmap = getResizedBitmap(bitmap, 150, 150);
					return bitmap;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return bitmap;
			}

			/** decodes image and scales it to reduce memory consumption **/
			public Bitmap getResizedBitmap(Bitmap bm, int newHeight,
					int newWidth) {
				int width = bm.getWidth();
				int height = bm.getHeight();
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				// CREATE A MATRIX FOR THE MANIPULATION
				Matrix matrix = new Matrix();
				// RESIZE THE BIT MAP
				matrix.postScale(scaleWidth, scaleHeight);
				// RECREATE THE NEW BITMAP
				Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width,
						height, matrix, false);
				return resizedBitmap;
			}
		}
	}
}
