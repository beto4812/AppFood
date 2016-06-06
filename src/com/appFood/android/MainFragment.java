package com.appFood.android;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.appFood.android.login.LogInActivity;

/**
 * A fragment that launches other parts of the demo application.
 */
public class MainFragment extends Fragment {

	String pageTitle = "Bienvenido";//
	ProgressDialog dialog;
	View rootView;
	ArrayList<String> listTitles;
	ListView listview;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		
		rootView = inflater.inflate(R.layout.fragment_section_main, container,
				false);
		//listTitles = new ArrayList<>();
		//listview = (ListView) rootView.findViewById(R.id.listViewMain);
		//getListValues();
		return rootView;
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
	

}