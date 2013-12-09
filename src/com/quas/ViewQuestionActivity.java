package com.quas;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewQuestionActivity extends Activity {
	
	private String global_qid;
	
	// question_id to send to ViewQuestion view
	public final static String EXTRA_QID = "com.quas.message_uninitialized_qid";
	
	//TextView To Fill With Question Data
		private TextView textview_question_title;
		private TextView textview_question_body;
		private TextView textview_author;
		private TextView textview_timestamp;
		private TextView textview_tags;
		private TextView textview_number_of_replies;
		
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_question);
		
		// Getting the Question ID from the intent
		Intent intent = getIntent();
		String qid = intent.getStringExtra(MainActivity.EXTRA_QID);
		String question_id = qid;
		global_qid = qid;
		String url = "http://130.240.5.168:5000/questions/" + question_id + "/";
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(" View Question"); 
		
		//textview_raw_json = (TextView) findViewById(R.id.test_json_output);
        textview_question_title = (TextView) findViewById(R.id.view_question_title_question);
        textview_question_body = (TextView) findViewById(R.id.view_question_body_question);
        textview_author = (TextView) findViewById(R.id.view_question_author);
        textview_timestamp = (TextView) findViewById(R.id.view_question_timestamp);
        textview_tags = (TextView) findViewById(R.id.view_question_tags);
        textview_number_of_replies = (TextView) findViewById(R.id.view_question_number_of_replies);
        
		final Button replies_button = (Button) findViewById(R.id.view_question_to_replies_button);
        replies_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(ViewQuestionActivity.this, ListRepliesActivity.class);
    			intent.putExtra(EXTRA_QID, global_qid);
    	    	startActivity(intent);
            }
        });
        
        // Task to get JSON from end point in background
        AsyncHTTPGETToJSONTask task = new AsyncHTTPGETToJSONTask();
        task.execute(new String[] { url });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_question_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reply:
			openReply();
			return true;
		case R.id.action_vote:
			openVote();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}

	private void openReply() {
		Intent intent = new Intent(this, ReplyActivity.class);
		intent.putExtra("REPLY_QID", global_qid);
    	startActivity(intent);
	}
	
	private void openVote() {
		Toast.makeText(this, "To: Vote View", Toast.LENGTH_SHORT).show();
	}
	
	private void openSettings() {
		Toast.makeText(this, "The are no settings.", Toast.LENGTH_SHORT).show();
	}

	// PRIVATE INNER JSON PARSER CLASS
    private class AsyncHTTPGETToJSONTask extends AsyncTask<String, Void, JSONObject> {
    	private ProgressDialog dialog = new ProgressDialog(ViewQuestionActivity.this);

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
	    	this.dialog.setMessage("Fetching question...");
	        this.dialog.show();
	    }

        @Override
        protected void onPostExecute(JSONObject json) {
        	// dismisses the loading dialog
	    	if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
        	try {
        		
        		String question_title = json.getJSONObject("Question").getString("title");
        		textview_question_title.setText(question_title);
        		
        		String question_body = json.getJSONObject("Question").getString("body");
        		textview_question_body.setText(question_body);
        		
        		String author_name = json.getJSONObject("Question").getJSONObject("author").getString("username");
        		textview_author.setText(author_name);
        		
        		String timestamp_time = json.getJSONObject("Question").getString("timestamp");
        		textview_timestamp.setText(timestamp_time);
        				
        		String tags_list = json.getJSONObject("Question").getJSONArray("tags").toString();
        		textview_tags.setText(tags_list);
        		
        		int number_of_replies = json.getJSONArray("ReplyList").length();
        		textview_number_of_replies.setText("" + number_of_replies);
       
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
      }
}
