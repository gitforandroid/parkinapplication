package com.Mahmood.parking1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainScreen extends Activity{
	Button searchParking, trackCar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		searchParking =(Button)findViewById(R.id.getParkingAddress);
		trackCar = (Button)findViewById(R.id.button2);
		//comment
		searchParking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent searchParkignIntent = new Intent(MainScreen.this, SearchParking.class);
				startActivity(searchParkignIntent);
			}
		});
		
		trackCar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
