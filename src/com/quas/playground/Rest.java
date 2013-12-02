package com.quas.playground;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Rest {
	
	public String getQuestion() {
		
		try {
			URL url = new URL ("http://130.240.5.168:5000/questions/1/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(connection.getInputStream());
			String out = readStream(in);
			//String out = "Meow!";
			return out;
		} catch (Exception e) {
				e.printStackTrace();
			}
		return "Error: failed to get the question";
	}
	
	private String readStream(InputStream in) {
		
		BufferedReader reader = null;
		String result = "not good";
		
		/*try {
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();
			result = sb.toString();
		
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		
		return "Katt";
	}
	
	
	
	// CHECK IF NETWORK IS AVAILABLE 
	/*
	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false; 
		}
	}
	*/
}
