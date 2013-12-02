package com.quas;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ListExperimentActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_experiment);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		/*ll.setOrientation(VERTICAL);
	
		for (int i = 0; i < LIST_OF_OBJECTS; i++) {
			LinearLayout ll2 = new LinearLayout(this);
			ll2.setOrientation(HORIZONTAL);
			Button b = new Button(this);
			ll2.addView(b);
			ll.addView(ll2);
		}*/
		setContentView(ll);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_experiment, menu);
		return true;
	}

}
class Adapter extends ArrayAdapter<String> {
	private final Context context;
	public ArrayList<String> values;
 
	public Adapter(Context context, ArrayList<String> values) {	
		super(context, R.layout.question_list_item, values);		
		this.values = values;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.question_list_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
		textView.setText(values.get(position));
		return rowView;
	}
}
