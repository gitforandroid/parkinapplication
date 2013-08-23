package com.Mahmood.parking1;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationFinder extends Activity implements LocationListener{
	
	LocationManager locationManager;
	String latitude;
	String longitude;
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
