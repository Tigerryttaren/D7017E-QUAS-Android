package com.quas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class FilterQuestionsActivity extends ListActivity {
	
	// "global"
	private ArrayList<JSONObject> global_json = new ArrayList<JSONObject>();
	
	// Messages
	
	// question_id to send to ViewQuestion view
	public final static String EXTRA_QID = "com.quas.message_uninitialized_qid";
		

	
	
	//Server URL to first question
	//String question_id = "1";
	//String url = "http://130.240.5.168:5000/questions/" + question_id + "/";
	
	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ActionBar action_bar = getActionBar();
        //action_bar.setDisplayShowTitleEnabled(false);
    	action_bar.setDisplayHomeAsUpEnabled(true);
    	action_bar.setTitle(" Filter");
    	
    	// String building area
    	Intent intent = getIntent();
    	String number_of_questions = intent.getStringExtra("number_of_questions");
    	String order_by = intent.getStringExtra("order_by");
    			
    	// check if order or no order
    	String url = "";
    	if (order_by == "no") {
    		url = "http://130.240.5.168:5000/questions/?paginate&page=1&page_size=" + number_of_questions;
    	} else {
    		url = "http://130.240.5.168:5000/questions/?paginate&page=1&page_size=" + number_of_questions + "&order_by=" + order_by;
    	}
        
        // Task to get JSON from end point in background
        AsyncHTTPGETToJSONTask task = new AsyncHTTPGETToJSONTask(this);
        task.execute(new String[] { url });
    }  
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	   
	    try {
			String qid = global_json.get(position).getString("id");
			Intent intent = new Intent(this, ViewQuestionActivity.class);
			intent.putExtra(EXTRA_QID, qid);
	    	startActivity(intent);
		} catch (JSONException je) {
			je.printStackTrace(); 
		}
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.filter_questions, menu);
		return super.onCreateOptionsMenu(menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			openNew();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void openNew() {
		Intent intent = new Intent(this, NewQuestionActivity.class);
    	startActivity(intent);		
		
	}
	
	private void openSettings() {
		Toast.makeText(this, "Don't touch me.", Toast.LENGTH_SHORT).show();
	}
	
	//PRIVATE INNER JSON PARSER CLASS
		private class AsyncHTTPGETToJSONTask extends AsyncTask<String, Integer, JSONObject> {
			protected Context context;
			private ProgressDialog dialog = new ProgressDialog(FilterQuestionsActivity.this);
			
			protected AsyncHTTPGETToJSONTask (Context c) {
				this.context = c;
			}
			
		    @Override
		    protected JSONObject doInBackground(String... urls) {
		      String response = "";
		      for (String url : urls) {
		        DefaultHttpClient client = new DefaultHttpClient();
		        HttpGet http_get = new HttpGet(url);
		        try {
		          HttpResponse execute = client.execute(http_get);
		          InputStream content = execute.getEntity().getContent();

		          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		          String string = "";
		          while ((string = buffer.readLine()) != null) {
		            response = response + string;
		          }
		        } catch (Exception e) {
		          e.printStackTrace();
		        }
		      }
		      JSONObject jObj = jsonify(response);
		      return jObj;
		    }
		    protected JSONObject jsonify (String input) {
		    	JSONObject jObj = null;
		    	try {
		    		jObj = new JSONObject(input); 
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    	return jObj;
		    }
		    
		    @Override
		    protected void onPreExecute() {
		    	// shows the loading dialog
		    	this.dialog.setMessage("Fetching questions...");
		        this.dialog.show();
		    }

		    @Override
		    protected void onPostExecute(JSONObject json) {
		    	// Dismisses the loading dialog
		    	if (dialog.isShowing()) {
		            dialog.dismiss();
		        }
		    	
		    	try {	    		
		    		ArrayList<JSONObject> values = new ArrayList<JSONObject>();
		    		for (int i = 0; i < json.getJSONArray("QuestionList").length(); i++) {
		    			values.add(json.getJSONArray("QuestionList").getJSONObject(i));
		    		}
		    	    
		    		// To get question id to send to intent
		    		global_json = values;
		    		
		    	    QUASAdapter adapter = new QUASAdapter(context, values);
		    	    setListAdapter(adapter); 
		    	    
		    	} catch (Exception e) {
		    		e.printStackTrace();
		    	}
		    }
		  }
}