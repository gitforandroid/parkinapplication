package com.Mahmood.parking1;

import java.io.IOException;
import java.util.List;

import com.google.android.gms.internal.bt;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddressInput extends Activity {

	Button getParking,btCurrentLocation;
	String concatenatedAddress;
	EditText streetAddress, cityAddress, stateAddress, zipAddress;
	AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_input);
		
		
		getParking = (Button)findViewById(R.id.getparkingaddress);
		btCurrentLocation =(Button)findViewById(R.id.btCurrentLocation);
		
		btCurrentLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			Intent currentLocationIntent = new Intent(AddressInput.this,LocationFinder.class);
			startActivity(currentLocationIntent);
			}
		});
		
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
					
					System.out.println("Gecode is***********************************"+foundGeocode);
					
					if(foundGeocode.size()==0)
					{
						alert.showAlertDialog(AddressInput.this, "Wrong Address",
								"Please Enter The Correct Address",
								false);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
				if(foundGeocode.size()!=0)
				{
				 String latitude = String.valueOf(foundGeocode.get(0).getLatitude()); //getting latitude
				 String longitude = String.valueOf(foundGeocode.get(0).getLongitude());//getting longitude
				System.out.println("this is epic"+latitude+","+longitude);
				
				
				
				
				Bundle b = new Bundle();
				b.putString("latitude",latitude );
				b.putString("longitude", longitude);
				Intent displayMapIntent = new Intent(AddressInput.this, MainActivity.class);
				displayMapIntent.putExtras(b);
				startActivity(displayMapIntent);
				}
				
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
