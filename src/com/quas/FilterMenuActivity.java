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
	}
	
	public void filterQuestion (View button) {
			
		EditText number_of_questions_view = (EditText) findViewById(R.id.filter_questions_filter_number);
		String number_of_questions = number_of_questions_view.getText().toString();
		
		EditText author_or_tag_view = (EditText) findViewById(R.id.filter_questions_filter_author_or_tag);
		String author_or_tag = author_or_tag_view.getText().toString();
		
		// setting extras to the intent
		Intent intent = new Intent(this, FilterQuestionsActivity.class);
		intent.putExtra("number_of_questions", number_of_questions);
		intent.putExtra("author_or_tag", author_or_tag);
		
		// checking the order radio buttons
		RadioButton rb_order_no = (RadioButton) findViewById(R.id.radio_order_0);
		RadioButton rb_order_date = (RadioButton) findViewById(R.id.radio_order_1);
		RadioButton rb_order_votes = (RadioButton) findViewById(R.id.radio_order_2);
		RadioButton rb_order_name = (RadioButton) findViewById(R.id.radio_order_3);
	
		if (rb_order_no.isChecked()) {
			intent.putExtra("order_by", "");
		} else if (rb_order_date.isChecked()) {
			intent.putExtra("order_by", "date");
		} else if (rb_order_votes.isChecked()) {
			intent.putExtra("order_by", "vote");
		} else if (rb_order_name.isChecked()) {
			intent.putExtra("order_by", "name");
		}
		
		// checking the filter radio buttons
		RadioButton rb_filter_no = (RadioButton) findViewById(R.id.radio_filter_0);
		RadioButton rb_filter_author = (RadioButton) findViewById(R.id.radio_filter_1);
		RadioButton rb_filter_tag = (RadioButton) findViewById(R.id.radio_filter_2);
		
		if (rb_filter_no.isChecked()) {
			intent.putExtra("filter_by", "");
		} else if (rb_filter_author.isChecked()) {
			intent.putExtra("filter_by", "author");
		} else if (rb_filter_tag.isChecked()) {
			intent.putExtra("filter_by", "tags");
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
