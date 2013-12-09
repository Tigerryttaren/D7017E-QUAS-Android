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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListRepliesActivity extends ListActivity {
	
	private ArrayList<JSONObject> global_json_replies = new ArrayList<JSONObject>();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_view_question);
		
		// Getting the Question ID from the intent
		Intent intent = getIntent();
		String qid = intent.getStringExtra(MainActivity.EXTRA_QID);
		String question_id = qid;
		String url = "http://130.240.5.168:5000/questions/" + question_id + "/";
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(" Replies"); 
        
        // Task to get JSON from end point in background
        AsyncHTTPGETToJSONTask task = new AsyncHTTPGETToJSONTask(this);
        task.execute(new String[] { url });
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    //TODO: set not_clickable somehow? for prettiness' sake...
	 }
	@Override
	public void onBackPressed(){
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_reply_actions, menu);
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
		//TODO: add link to Reply view
		Toast.makeText(this, "To: Reply View", Toast.LENGTH_SHORT).show();
	}
	
	private void openVote() {
		Toast.makeText(this, "To: Vote View", Toast.LENGTH_SHORT).show();
	}
	
	private void openSettings() {
		Toast.makeText(this, "The are no settings.", Toast.LENGTH_SHORT).show();
	}

	// PRIVATE INNER JSON PARSER CLASS
    private class AsyncHTTPGETToJSONTask extends AsyncTask<String, Void, JSONObject> {
    	private ProgressDialog dialog = new ProgressDialog(ListRepliesActivity.this);
		protected Context context;
		
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
	    	this.dialog.setMessage("Fetching question and replies...");
	        this.dialog.show();
	    }

        @Override
        protected void onPostExecute(JSONObject json) {
        	// dismisses the loading dialog
	    	if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
        	try {
        		
        		ArrayList<JSONObject> values = new ArrayList<JSONObject>();
	    		for (int i = 0; i < json.getJSONArray("ReplyList").length(); i++) {
	    			values.add(json.getJSONArray("ReplyList").getJSONObject(i));
	    		}
	    	    
	    		// To get question id to send to intent
	    		global_json_replies = values;
	    		
	    	    QUASRepliesAdapter adapter = new QUASRepliesAdapter(context, values);
	    	    setListAdapter(adapter); 
       
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
      }
}

/*QUAS Special Custom Made Special-For-You Super Replies Adapter*/
class QUASRepliesAdapter extends ArrayAdapter<JSONObject> {
	private final Context context;
	public ArrayList<JSONObject> values;
 
	public QUASRepliesAdapter(Context context, ArrayList<JSONObject> values) {	
		super(context, R.layout.question_list_reply_item, values);		
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View item = inflater.inflate(R.layout.question_list_reply_item, parent, false);
		
		try {
		// setting body
		TextView bodyview = (TextView) item.findViewById(R.id.reply_bodyline);
		bodyview.setText(values.get(position).getString("body"));
		
		// setting time
		TextView timeview = (TextView) item.findViewById(R.id.reply_timeline);
		timeview.setText(values.get(position).getString("timestamp"));
		
		// setting author
		TextView authorview = (TextView) item.findViewById(R.id.reply_authorline);
		authorview.setText(values.get(position).getJSONObject("author").getString("username"));
		
		// setting vote
		TextView votesview = (TextView) item.findViewById(R.id.reply_votesline);
		votesview.setText(values.get(position).getString("score"));
		
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return item;
	}
}
