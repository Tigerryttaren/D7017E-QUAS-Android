/*package com.quas.playground;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.quas.MyListActivity;
import com.quas.R;
import com.quas.R.id;
import com.quas.R.layout;
import com.quas.R.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainBackup extends Activity {
	
	// Messages
	public final static String EXTRA_QID = "com.quas.MESSAGE";
	
	//Server URL to first question
	String question_id = "1";
	String url = "http://130.240.5.168:5000/questions/" + question_id + "/";
	
	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textview_raw_json = (TextView) findViewById(R.id.test_json_output);
        //textview_question_title = (TextView) findViewById(R.id.test_title_question);
        //textview_question_body = (TextView) findViewById(R.id.test_body_question);
        //textview_author = (TextView) findViewById(R.id.test_author);
        //textview_timestamp = (TextView) findViewById(R.id.test_timestamp);
        //textview_tags = (TextView) findViewById(R.id.test_tags);
        
        		
        // Task to get JSON from endpoint in background
        //AsyncHTTPGETToJSONTask task = new AsyncHTTPGETToJSONTask();
        //task.execute(new String[] { url });

    }  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_new:
			openNew();
			return true;
		case R.id.action_filter:
			openFilter();
			return true;
		case R.id.action_settings:
			openSettings();
			return true;
		default: 
			return super.onOptionsItemSelected(item);
		
		}
	}
	
	private void openNew() {
		Toast.makeText(this, "To: New Question", Toast.LENGTH_SHORT).show();
	}
	
	private void openFilter() {
		Toast.makeText(this, "To: Filter Questions", Toast.LENGTH_SHORT).show();
	}
	
	private void openSettings() {
		Toast.makeText(this, "Don't Touch Me.", Toast.LENGTH_SHORT).show();
	}
	
	 public void sendMessageQuestionID(View view) {
	    	Intent intent = new Intent(this, MyListActivity.class);
	    	//Intent intent = new Intent(this, ViewQuestionActivity.class);
	    	EditText editText = (EditText) findViewById(R.id.test_enter_question_id);
	    	String message = editText.getText().toString();
	    	intent.putExtra(EXTRA_QID, message);
	    	startActivity(intent);
	    	
	    	
	    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    // PRIVATE INNER JSON PARSER CLASS
    private class AsyncHTTPGETToJSONTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
          String response = "";
          for (String url : urls) {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
              HttpResponse execute = client.execute(httpGet);
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
        protected void onPostExecute(JSONObject json) {
        	try {
        		//String result = json.getString("Question");
        		
        		//Making it a human-readable string with indentations
        		//String question_title = json.getJSONObject("Question").getString("title");
        		//textview_question_title.setText(question_title);
        		
        		//String question_body = json.getJSONObject("Question").getString("body");
        		//textview_question_body.setText(question_body);
        		
        		//String author_name = json.getJSONObject("Question").getJSONObject("author").getString("username");
        		//textview_author.setText(author_name);
        		
        		//String timestamp_time = json.getJSONObject("Question").getString("timestamp");
        		//textview_timestamp.setText(timestamp_time);
        				
        		//String tags_list = json.getJSONObject("Question").getJSONArray("tags").toString();
        		//textview_tags.setText(tags_list);
        		
        		//String result = json.toString(5);
        		//textview_raw_json.setText(result);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
      }
}













































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
import android.app.Activity;
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

public class ViewQuestionActivity extends Activity {
	
	private ArrayList<JSONObject> global_json_replies = new ArrayList<JSONObject>();
	
	
	//TextView To Fill With Question Data
		private TextView textview_question_title;
		private TextView textview_question_body;
		private TextView textview_author;
		private TextView textview_timestamp;
		private TextView textview_tags;
		
		//Server URL to first question
		//String question_id = "1";
		//String url = "http://130.240.5.168:5000/questions/" + question_id + "/";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_question);
		
		// Getting the Question ID from the intent
		Intent intent = getIntent();
		String qid = intent.getStringExtra(MainActivity.EXTRA_QID);
		String question_id = qid;
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
		Toast.makeText(this, "To: Reply View", Toast.LENGTH_SHORT).show();
	}
	
	private void openVote() {
		Toast.makeText(this, "To: Vote View", Toast.LENGTH_SHORT).show();
	}
	
	private void openSettings() {
		Toast.makeText(this, "Don't touch me.", Toast.LENGTH_SHORT).show();
	}

	// PRIVATE INNER JSON PARSER CLASS
    private class AsyncHTTPGETToJSONTask extends AsyncTask<String, Void, JSONObject> {
    	//private ProgressDialog dialog = new ProgressDialog(ViewQuestionActivity.this);
    	
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
	    	//this.dialog.setMessage("Fetching this question...");
	        //this.dialog.show();
	    }

        @Override
        protected void onPostExecute(JSONObject json) {
        	// Dismisses the loading dialog
	    	//if (dialog.isShowing()) {
	        //    dialog.dismiss();
	        //}
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
        		
        		ArrayList<JSONObject> values = new ArrayList<JSONObject>();
	    		for (int i = 0; i < json.getJSONArray("QuestionList").length(); i++) {
	    			values.add(json.getJSONArray("QuestionList").getJSONObject(i));
	    		}
	    	    
	    		// To get question id to send to intent
	    		//global_json = values;
	    		
	    	    //QUASAdapter adapter = new QUASAdapter(context, values);
	    	    //setListAdapter(adapter); 
       
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
      }
}


QUAS Special Custom Made Special-For-You Super Replies Adapter
class QUASRepliesAdapter extends ArrayAdapter<JSONObject> {
	private final Context context;
	public ArrayList<JSONObject> values;
 
	public QUASRepliesAdapter(Context context, ArrayList<JSONObject> values) {	
		super(context, R.layout.question_list_item, values);		
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View item = inflater.inflate(R.layout.question_list_item, parent, false);
		
		try {
		// setting title
		TextView titleview = (TextView) item.findViewById(R.id.titleline);
		titleview.setText(values.get(position).getString("title"));
		//.getJSONArray("QuestionList").getJSONObject(position)
		
		// setting time
		TextView timeview = (TextView) item.findViewById(R.id.timeline);
		timeview.setText(values.get(position).getString("timestamp"));
		
		// setting author
		TextView authorview = (TextView) item.findViewById(R.id.authorline);
		authorview.setText(values.get(position).getJSONObject("author").getString("username"));
		
		// setting vote
		TextView votesview = (TextView) item.findViewById(R.id.votesline);
		votesview.setText(values.get(position).getString("score"));
				
		// setting tags
		TextView tagsview = (TextView) item.findViewById(R.id.tagsline);
		//TODO: Trim away head brackets and bunnyears
		tagsview.setText(values.get(position).getString("tags"));
		
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return item;
	}
}*/
