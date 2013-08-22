package com.Mahmood.parking1;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddressInput extends Activity {

	Button getParking;
	String concatenatedAddress;
	EditText streetAddress, cityAddress, stateAddress, zipAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_input);
		
		
		getParking = (Button)findViewById(R.id.getparkingaddress);
		
		getParking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				streetAddress = (EditText)findViewById(R.id.streettxt);
				cityAddress = (EditText)findViewById(R.id.citytxt);
				stateAddress = (EditText)findViewById(R.id.statetxt);
				zipAddress = (EditText)findViewById(R.id.ziptxt);
				
				concatenatedAddress = streetAddress.getText().toString()+","+cityAddress.getText().toString()+","+stateAddress.getText().toString()+" "+zipAddress.getText().toString();
				
				System.out.println(concatenatedAddress);
				
				List<Address> foundGeocode = null;
				/* find the addresses  by using getFromLocationName() method with the given address*/
				Geocoder geoCoder = new Geocoder(AddressInput.this);
				try {
					foundGeocode = geoCoder.getFromLocationName(concatenatedAddress, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				 double latitude = foundGeocode.get(0).getLatitude(); //getting latitude
				 double longitude = foundGeocode.get(0).getLongitude();//getting longitude
				System.out.println("this is epic"+latitude+","+longitude);
				
				
				Intent displayMapIntent = new Intent(AddressInput.this, MainActivity.class);
				Bundle b = new Bundle();
				b.putDouble("lat",latitude );
				b.putDouble("lon", longitude);
				displayMapIntent.putExtras(b);
				startActivity(displayMapIntent);
				
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.address_input, menu);
		return true;
	}

}
