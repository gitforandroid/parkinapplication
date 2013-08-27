package com.Mahmood.parking1;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationFinder extends Activity implements LocationListener{
	
	LocationManager locationManager;
	String latitude;
	String longitude;
	String sendLatitude;
	String sendLongitude;
	Location location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE) ;
		Criteria criteria = new Criteria();

		
		String provider = locationManager.getBestProvider(criteria, true);
		
		location = locationManager.getLastKnownLocation(provider);
		
		if(location!=null)
		{
			onLocationChanged(location);
		}
		locationManager.requestLocationUpdates(provider, 2000, 0, this);
		
		
		//AMEY ---- BUNDLE TO SEND CURRENT COORDINATES FROM THIS CLASS TO FAMOUS PLACES..
		//WHEN USER SEES A FAMOUS PLACE AND HE WISHES TO NAVIGATE FROM HIS CURRENT LOCATION TO THE
		//FAMOUS PLACE, HE CAN USE THIS INTENT.
		sendLatitude = latitude;
		sendLongitude = longitude;
		
		System.out.println("Sending------------"+sendLongitude);
		
		
		Bundle locationBundle = new Bundle();
		locationBundle.putString("latitude1", sendLatitude);
		locationBundle.putString("longitude1", sendLongitude);
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
		intent.putExtras(locationBundle);
		startActivity(intent);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latitude = String.valueOf(location.getLatitude());
		longitude = String.valueOf(location.getLongitude());
		
		System.out.println("Amey's Lat is:"+latitude+"Lon is:"+longitude);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
