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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewQuestionActivity extends Activity {
	private String url_new_question = "http://130.240.5.168:5000/questions/";
	private String new_question = "";

	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        
        ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(" Ask Question"); 
		
		//TODO: implement functionality meow
		/*
		 * Build question in json
		 * POST question to server
		 * redirect to home view?
		 * */
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
			
			/*if (i == (parts.length)-1) {
				content_tags = content_tags + "\"" + parts[i]; 
			} else {
				content_tags = content_tags + "\"" + parts[i] + "\", "; 
			}*/
		}
		//content_tags = content_tags + "\"";	
	
		//String post = "{ \"Question\": { \"author\": { \"id\": 9001, \"posts\": 213, \"username\": \"AnnoyingFangirlboy\", \"votesum\": 0 }, \"body\": \"" + content_body + "\", \"tags\": [ " + content_tags + " ], \"title\": \"" + content_title + "\"}}";
		//String email = "admin@admin";
		//String token =  "123456";
		//String post = " { \"token\" : \"" + token + "\", \"email\" : \"" + email + "\", \"title\" : \"" + content_title + "\", \"body\" : \"" + content_body + "\",  \"tags\": [ " + content_tags + " ] } ";
		
		// Haxxxing to bypass le Lôgin du Sýstémé
		String email = "admin@admin";
		String token = "123456";
		
		JSONObject json = new JSONObject();
		String post = "post failure";
		
		try {
			json.put("token", token);
			json.put("email", email);
			json.put("title", content_title);
			json.put("body", content_body);
			json.put("tags", array_tags);
			
			post = json.toString();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		// visual inspection is visual
		//Toast.makeText(this, post, Toast.LENGTH_LONG).show();
		
		Log.d("Kitteh", "This should be visile");
		
		new_question = post;
		
		if (post == "" || post == null) {
	        Toast.makeText(this, "Please.", Toast.LENGTH_SHORT).show();   
	    }
	    else {
	    	AsyncHTTPPOSTToJSONTask task = new AsyncHTTPPOSTToJSONTask();
			task.execute(new String[] { url_new_question });
			
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
    	//private ProgressDialog dialog = new ProgressDialog(NewQuestionActivity.this);
    	
        @Override
        protected JSONObject doInBackground(String... urls) {
        	String response = "";
        	try {
	        	for (String url : urls) {
	        		DefaultHttpClient client = new DefaultHttpClient();
	        		HttpPost http_post= new HttpPost(url);
	                //http_post.addHeader("token", token);
	                //http_post.addHeader("Content-type", "application/json");
	                http_post.setEntity(new StringEntity(new_question));
	                response = response + new_question;
	                HttpResponse execute = client.execute(http_post);
	                //HttpEntity content = execute.getEntity();
	                
	                
	                // DEBUG
	                int code = execute.getStatusLine().getStatusCode();
	                String test = "" +  code;
	                Log.d("Kitteh", "Status Code: " + test);
	                
	                client.getConnectionManager().shutdown();
	        	}
        	} catch (Exception e) {
        		e.printStackTrace();
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
	    	// this.dialog.setMessage("Asking question...");
	        // this.dialog.show();
	    }

        @Override
        protected void onPostExecute(JSONObject json) {
        	// Dismisses the loading dialog
	    	//if (dialog.isShowing()) {
	        //    dialog.dismiss();
	        //}
        	try {
        		// just to check that string is in json style jao
        		Toast.makeText(NewQuestionActivity.this, json.toString(), Toast.LENGTH_LONG).show();
       
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
      }
}
