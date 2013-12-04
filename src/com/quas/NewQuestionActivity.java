package com.quas;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewQuestionActivity extends Activity {
	private String url_new_question = "http://130.240.5.168:5000/questions/";
	//private String new_question = "";
	private JSONObject json_post;

	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        
        ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(" Ask Question"); 
		
		//TODO: implement functionality meow
		// Fix login
    }
	
	public void submitQuestion (View button) {
		
		EditText edit_title = (EditText) findViewById(R.id.new_question_input_title);
		String content_title = edit_title.getText().toString();
		
		EditText edit_body = (EditText) findViewById(R.id.new_question_input_body);
		String content_body = edit_body.getText().toString();
		
		EditText edit_tags = (EditText) findViewById(R.id.new_question_input_tags);
		String content_tags_pre = edit_tags.getText().toString();
		
		// json-prepping the tags
		//String content_tags = "";
		String temp = content_tags_pre.replaceAll(" ", "");
		String[] parts = temp.split(",");
		JSONArray array_tags = new JSONArray();
		
		for (int i = 0; i < parts.length; i++) {
			array_tags.put(parts[i]);
		}
		
		// Haxxxing to bypass le Lôgin du Sýstémé
		String email = "admin@admin";//"antwen-9@student.ltu.se";
		String token = "123456";//"05953d1e22e3e5474ff250cb4f336ee7f85555655cb4a0a52d";
		
		JSONObject json = new JSONObject();		
		try {
			json.put("token", token);
			json.put("email", email);
			json.put("title", content_title);
			json.put("body", content_body);
			json.put("tags", array_tags);
			
			json_post = json;
			
			if (json_post.getString("body") == "" || json_post.getString("body") == null || json_post.getString("title") == "" || json_post.getString("title") == null) {
		        Toast.makeText(this, "Please, have some content.", Toast.LENGTH_SHORT).show();   
		    } else {
		    	AsyncHTTPPOSTToJSONTask task = new AsyncHTTPPOSTToJSONTask();
				task.execute(new String[] { url_new_question });
		    }
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_question, menu);
        return true;
    }
    
 // PRIVATE INNER JSON PARSER CLASS
    private class AsyncHTTPPOSTToJSONTask extends AsyncTask<String, Void, JSONObject> {
    	private ProgressDialog dialog = new ProgressDialog(NewQuestionActivity.this);
    	
        @Override
        protected JSONObject doInBackground(String... urls) {
        	try {
	        	for (String url : urls) {
	        		DefaultHttpClient client = new DefaultHttpClient();
	        		HttpPost http_post= new HttpPost(url);
	        		http_post.addHeader("Content-type", "application/json");
	        		http_post.setEntity(new StringEntity(json_post.toString()));
	                HttpResponse execute = client.execute(http_post);
	                
	                // DEBUG
	                int code = execute.getStatusLine().getStatusCode();
	                String test = "" +  code;
	                Log.d("Kitteh", "Status Code: " + test);
	                Log.d("Kitteh", json_post.toString());
	                
	                client.getConnectionManager().shutdown();
	        	}
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	JSONObject jObj = json_post;         
        	return jObj;
        }
        
        @Override
        protected void onPreExecute() {
	    	// shows the loading dialog
        	this.dialog.setMessage("Posting question...");
	        this.dialog.show();
	    }

        @Override
        protected void onPostExecute(JSONObject json) {
        	// Dismisses the loading dialog
	    	if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
	    	finish();
        }
      }
}
