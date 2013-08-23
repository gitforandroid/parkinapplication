package com.Mahmood.parking1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
//hehaaaaaaaa
public class SearchParking extends Activity{
	Button btCurrentLocation, btFamous, btAddress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_parking);
		
		btCurrentLocation = (Button)findViewById(R.id.btCurrentLocation);
		btFamous = (Button)findViewById(R.id.btFamous);
		btAddress = (Button)findViewById(R.id.btAddress);
		
		btCurrentLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent displayMapIntent = new Intent(SearchParking.this, MainActivity.class);
				startActivity(displayMapIntent);
				
			}
		});
		
		btFamous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent famousIntent = new Intent(SearchParking.this,FamousPlaces.class);
				startActivity(famousIntent);
			}
		});
		
		btAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent displayMapIntent = new Intent(SearchParking.this, AddressInput.class);
				startActivity(displayMapIntent);
				
			}
		});
	}

}
