package com.Mahmood.parking1;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActiv extends Activity {
	
	JSONObject valuedfrommain;
	String type;
	String oper;
	String occ;
	String rate;
	String beg;
	String end;
	JSONArray rs;
	JSONObject rates;
	String from;
	String to;
	JSONObject bat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {System.out.println("hello");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		Bundle b = getIntent().getExtras();
		String name = b.getString("name");
		try {
			valuedfrommain = new JSONObject(b.getString("jsonvalue"));
			rates = valuedfrommain.getJSONObject("RATES");
			rs = rates.getJSONArray("RS");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			type = valuedfrommain.getString("TYPE");
			occ = valuedfrommain.getString("OCC");
			oper = valuedfrommain.getString("OPER");
			
			for(int j=0;j<rs.length();j++)
			{
				bat = rs.getJSONObject(j);
				
				beg = bat.getString("BEG");
				rate = bat.getString("RATE");
				end = bat.getString("END");
				from = bat.getString("FROM");
				to = bat.getString("TO");
				
				
				//System.out.println("Raterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr is:"+ rate);
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		System.out.println(name);
		System.out.println(valuedfrommain);
		TextView txt1 = (TextView)findViewById(R.id.textView1);
		txt1.setText("PARKING NAME:"+name+"\n\nTYPE(ON/OFF):"+type+"\nOPERATIONAL SPOTS:"+oper+"\nOCCUPIED:"+occ+"\n"+ "Rate is:" + rate);
		

	}
}
