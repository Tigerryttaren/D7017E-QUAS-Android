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
    JSONObject question1 = new JSONObject();
    //private ProgressBar bar;
    

  @SuppressLint("NewApi")
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
    //Setting the progressbar
    //setContentView(R.layout.activity_my_list);
    //bar = (ProgressBar) this.findViewById(R.id.progressBar);
    
   /* String[] values = new String[] { "Question 1", "Question 2", "Question 3",
        "Question 4", "Question 5", "Question 6", "Question 7", "Question 8",
        "Question 9", "Question 10" }; */
    
    /*
    ArrayList<String> values = new ArrayList<String>();
    values.add("Question 1");
    values.add("Question 2");
    values.add("Question 3");
    */
    
    ActionBar actionBar = getActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setTitle("Questions");
    
    
    /*try {
    	JSONObject j = new JSONObject().put("title", "Katt");
    	question1 = j;
    } catch (Exception e) {
    	e.printStackTrace();
    }*/
    
	String question_id = "1";
	//String url = "http://130.240.5.168:5000/questions/" + question_id + "/";
	String url = "http://130.240.5.168:5000/questions/";
    
    // Task to get JSON from end point in background
    AsyncHTTPGETToJSONTask task = new AsyncHTTPGETToJSONTask(this);
    task.execute(new String[] { url });
   
    
    
    
    /*
    ArrayList<JSONObject> values = new ArrayList<JSONObject>();
    values.add(question1);
    //values.add(question2);
    //values.add(question3);
    
    QUASAdapter adapter = new QUASAdapter(this, values);
    setListAdapter(adapter);
    */
 
    
    /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        R.layout.question_list_item, R.id.firstLine, values);
    setListAdapter(adapter);*/
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    //String item = (String) getListAdapter().getItem(position);
    Toast.makeText(this, position + " selected", Toast.LENGTH_LONG).show();
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
	    protected void onPreExecute() {
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
	    		
	    		for (int i = 0; i < 5; i++) {
	    			values.add(json.getJSONArray("QuestionList").getJSONObject(i));
	    		}
	    		//question1 = json;
	    		
	    	    
	    	    //values.add(question1);
	    	    //values.add(question2);
	    	    //values.add(question3);
	    	    
	    	    QUASAdapter adapter = new QUASAdapter(context, values);
	    	    setListAdapter(adapter); 
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    	    
	    		//String result = json.getString("Question");
	    		
	    		//Making it a human-readable string with indentations
	    		/*String question_title = json.getJSONObject("Question").getString("title");
	    		textview_question_title.setText(question_title);
	    		
	    		String question_body = json.getJSONObject("Question").getString("body");
	    		textview_question_body.setText(question_body);
	    		
	    		String author_name = json.getJSONObject("Question").getJSONObject("author").getString("username");
	    		textview_author.setText(author_name);
	    		
	    		String timestamp_time = json.getJSONObject("Question").getString("timestamp");
	    		textview_timestamp.setText(timestamp_time);
	    				
	    		String tags_list = json.getJSONObject("Question").getJSONArray("tags").toString();
	    		textview_tags.setText(tags_list);*/
	    		
	    		//String result = json.toString(5);
	    		//textview_raw_json.setText(result);
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	  }
} 




/*QUAS Special Custom Made Super Adapter*/
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
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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



/*
 * ONLY FOR TESTING STRING
QUAS Special Custom Made Super Adapter
class QUASAdapter extends ArrayAdapter<String> {
	private final Context context;
	public ArrayList<String> values;
 
	public QUASAdapter(Context context, ArrayList<String> values) {	
		super(context, R.layout.question_list_item, values);		
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View item = inflater.inflate(R.layout.question_list_item, parent, false);
		
		// Setting The Title
		TextView titleview = (TextView) item.findViewById(R.id.firstLine);
		titleview.setText(values.get(position));
		
		// Setting The Tags
		TextView tagsview = (TextView) item.findViewById(R.id.secondLine);
		tagsview.setText(values.get(position));
		
		//Return the list item
		return item;
	}
}*/










/*
public class MyListActivity extends ListActivity {

  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    // create an array of Strings, that will be put to our ListActivity
    ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(this,
        getModel());
    setListAdapter(adapter);
  }

  private List<Model> getModel() {
    List<Model> list = new ArrayList<Model>();
    list.add(get("Linux"));
    list.add(get("Windows7"));
    list.add(get("Suse"));
    list.add(get("Eclipse"));
    list.add(get("Ubuntu"));
    list.add(get("Solaris"));
    list.add(get("Android"));
    list.add(get("iPhone"));
    // Initially select one of the items
    list.get(1).setSelected(true);
    return list;
  }

  private Model get(String s) {
    return new Model(s);
  }

} */