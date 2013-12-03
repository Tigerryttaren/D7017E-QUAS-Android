package com.quas;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class FilterMenuActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_menu);	
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle(" Filter"); 
		
		/*EditText edit_number = (EditText) findViewById(R.id.filter_questions_filter_number);
		String content_number = edit_number.getText().toString();
		
		EditText edit_body = (EditText) findViewById(R.id.filter_questions_filter_number);
		String content_body = edit_body.getText().toString();
		
		EditText edit_tags = (EditText) findViewById(R.id.filter_questions_input_tags);
		String content_tags_pre = edit_tags.getText().toString();
		*/
	}
	
	public void filterQuestion (View button) {
		// bygg intent extra
		// put extra
		// in filterQuestion, get intent, get question
		
		EditText number_of_questions_view = (EditText) findViewById(R.id.filter_questions_filter_number);
		String number_of_questions = number_of_questions_view.getText().toString();
		
		// checking the radio buttons
		RadioButton rb_no = (RadioButton) findViewById(R.id.radio_order_0);
		RadioButton rb_date = (RadioButton) findViewById(R.id.radio_order_1);
		RadioButton rb_votes = (RadioButton) findViewById(R.id.radio_order_2);
		RadioButton rb_name = (RadioButton) findViewById(R.id.radio_order_3);
		
		Intent intent = new Intent(this, FilterQuestionsActivity.class);
		intent.putExtra("number_of_questions", number_of_questions);
		
		if (rb_no.isChecked()) {
			intent.putExtra("order_by", "no");
		} else if (rb_date.isChecked()) {
			intent.putExtra("order_by", "date");
		} else if (rb_votes.isChecked()) {
			intent.putExtra("order_by", "votes");
		} else if (rb_name.isChecked()) {
			intent.putExtra("order_by", "name");
		}
		
    	startActivity(intent);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.filter_menu, menu);
		return true;
	}

}
