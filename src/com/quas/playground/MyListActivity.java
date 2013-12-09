package com.quas.playground;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.quas.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyListActivity extends ListActivity {
    //JSONObject question1 = new JSONObject();
    

  @SuppressLint("NewApi")
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
    ActionBar action_bar = getActionBar();
	action_bar.setDisplayHomeAsUpEnabled(true);
	action_bar.setTitle("Questions");
   
	//String question_id = "1";
	//String url = "http://130.240.5.168:5000/questions/" + question_id + "/";
	//String url = "http://130.240.5.168:5000/questions/";
	String url = "http://130.240.5.168:5000/questions/?paginate&page=1&page_size=10&order_by=date";
    
    // Task to get JSON from end point in background
    AsyncHTTPGETToJSONTask task = new AsyncHTTPGETToJSONTask(this);
    task.execute(new String[] { url });
   
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    //String item = (String) getListAdapter().getItem(position);
    Toast.makeText(this, position + " selected", Toast.LENGTH_LONG).show();
    
    //set ID to intent to i can get it later yao
    
    
  }
  
//PRIVATE INNER JSON PARSER CLASS
	private class AsyncHTTPGETToJSONTask extends AsyncTask<String, Integer, JSONObject> {
		protected Context context;
		private ProgressDialog dialog = new ProgressDialog(MyListActivity.this);
		
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
	    		//TODO: Dynamic size of for loop please!
	    		for (int i = 0; i < 10; i++) {
	    			values.add(json.getJSONArray("QuestionList").getJSONObject(i));
	    		}
	    	    
	    	    QUASAdapter adapter = new QUASAdapter(context, values);
	    	    setListAdapter(adapter); 
	    	    
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	  }
} 

//QUAS Special Custom Made Super Adapter
class QUASAdapter extends ArrayAdapter<JSONObject> {
	private final Context context;
	public ArrayList<JSONObject> values;
 
	public QUASAdapter(Context context, ArrayList<JSONObject> values) {	
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
		//.getJSONArray("QuestionList").getJSONObject(position)
		
		// setting author
		TextView authorview = (TextView) item.findViewById(R.id.authorline);
		authorview.setText(values.get(position).getJSONObject("author").getString("username"));
		//.getJSONArray("QuestionList").getJSONObject(position)
		
		// setting vote
		TextView votesview = (TextView) item.findViewById(R.id.votesline);
		votesview.setText(values.get(position).getString("score"));
		//.getJSONArray("QuestionList").getJSONObject(position)
				
		// setting tags
		TextView tagsview = (TextView) item.findViewById(R.id.tagsline);
		//TODO: Trim away head brackers and bunnyears
		tagsview.setText(values.get(position).getString("tags"));
		
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return item;
	}
}