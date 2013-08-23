package com.Mahmood.parking1;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ParkingDetails extends Activity {
	
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
	ArrayList<HashMap<String, JSONObject>> forMap;
	TextView tv2;
	int count =0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {System.out.println("hello");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parking_details);
		Bundle b = getIntent().getExtras();
		String name = b.getString("name");
		forMap = new ArrayList<HashMap<String, JSONObject>>();
		tv2= (TextView)findViewById(R.id.tv2);
		try {
			valuedfrommain = new JSONObject(b.getString("jsonvalue"));
			rates = valuedfrommain.getJSONObject("RATES");
			rs = rates.getJSONArray("RS");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			System.out.println("Enering try blockkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
			type = valuedfrommain.getString("TYPE");
			occ = valuedfrommain.getString("OCC");
			oper = valuedfrommain.getString("OPER");
			forMap.add(null);
			for(int j=0;j<rs.length();j++)
			{
				bat = rs.getJSONObject(j);
				
				beg = bat.getString("BEG");
				rate = bat.getString("RATE");
				end = bat.getString("END");
				from = bat.getString("FROM");
				to = bat.getString("TO");
				
				
				System.out.println("Raterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr is:"+ rate);
				HashMap<String, JSONObject> h = new HashMap<String, JSONObject>();
				h.put(rate, bat);
				forMap.add(h);
				count = j;
				System.out.println("Count is:"+count);
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		System.out.println(name);
		System.out.println(valuedfrommain);
		TextView txt1 = (TextView)findViewById(R.id.textView1);
		txt1.setText("PARKING NAME:"+name+"\n\nTYPE(ON/OFF):"+type+"\nOPERATIONAL SPOTS:"+oper+"\nOCCUPIED:"+occ+"\n"+ "Rate is:" + rate);
		while(count!=0)
		{
			System.out.println("Rate is:"+rate);
		}

	}
}
