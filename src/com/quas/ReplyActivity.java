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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ReplyActivity extends Activity {
	private JSONObject json_post;

	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        
        ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(" Reply"); 
		
		//TODO: implement functionality
		// Fix login
    }
	
	@Override
	public void onBackPressed(){
		finish();
	}
	
	public void submitQuestion (View button) {
		
		EditText edit_body = (EditText) findViewById(R.id.reply_input_body);
		String content_body = edit_body.getText().toString();
		
		//TODO: Change this to post to correct question endpoint
		Intent intent = getIntent();
		String qid = intent.getStringExtra("REPLY_QID");
		
		String url_new_reply = "http://130.240.5.168:5000/questions/" + qid + "/replies/";
		
		//TODO: Login related 
		// TOOD: Fix this uniqueness. Haxxxing to bypass le Lôgin du Sýstémé
		String email = "admin@admin";//"antwen-9@student.ltu.se";
		String token = "123456";//"05953d1e22e3e5474ff250cb4f336ee7f85555655cb4a0a52d";
		
		JSONObject json = new JSONObject();		
		try {
			json.put("token", token);
			json.put("email", email);
			json.put("body", content_body);
			
			json_post = json;
			
			if (json_post.getString("body") == "" || json_post.getString("body") == null || json_post.getString("title") == "" || json_post.getString("title") == null) {
		        Toast.makeText(this, "Please, have some content.", Toast.LENGTH_SHORT).show();   
		    } else {
		    	AsyncHTTPPOSTToJSONTask task = new AsyncHTTPPOSTToJSONTask();
				task.execute(new String[] { url_new_reply });
		    }
		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reply, menu);
        return true;
    }
    
 // PRIVATE INNER JSON PARSER CLASS
    private class AsyncHTTPPOSTToJSONTask extends AsyncTask<String, Void, JSONObject> {
    	private ProgressDialog dialog = new ProgressDialog(ReplyActivity.this);
    	
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
	                //TODO: Remove debug code
	                int code = execute.getStatusLine().getStatusCode();
	                String test = "" +  code;
	                Log.d("QUAS", "Status Code: " + test);
	                Log.d("QUAS", json_post.toString());
	                
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
        	this.dialog.setMessage("Posting reply...");
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
