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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class NewQuestionActivity extends Activity {

	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        
        ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("Ask Question"); 
		
		//Intent intent = new Intent(this, MyListActivity.class);
    	//Intent intent = new Intent(this, ViewQuestionActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.test_enter_question_id);
    	//String message = editText.getText().toString();
    	//intent.putExtra(EXTRA_QID, message);
    	//startActivity(intent);
		
		//TODO: implement functionality meow
		/*
		 * Build question in json
		 * POST question to server
		 * redirect to home view?
		 * */
		
    }
	
	public void submitQuestion (View button) {
		Toast.makeText(this, "Question (Fake)Posted", Toast.LENGTH_SHORT).show();
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_question, menu);
        return true;
    }
    
 // PRIVATE INNER JSON PARSER CLASS
    private class AsyncHTTPGETToJSONTask extends AsyncTask<String, Void, JSONObject> {
    	private ProgressDialog dialog = new ProgressDialog(NewQuestionActivity.this);
    	
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
        		/*
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
        		*/
       
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        }
      }

    
}
