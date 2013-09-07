package com.Mahmood.parking1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SinglePlaceActivity extends Activity implements LocationListener 
{
	//AMEY - ADDING LOCATION TRACKING FEATURE IN THIS CLASS
	
	
		LocationManager locationManager;
		String currentLatitude;
		String currentLongitude;
		Location location;
		Button navigate;
		Uri uri;
	
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;
	
	// Place Details
	PlaceDetails placeDetails;
	
	// Progress dialog
	ProgressDialog pDialog;
	
	Button findParking;
	
	String latitude;
	
	String longitude;
	
	// KEY Strings
	//public static String KEY_REFERENCE = "reference"; // id of the place

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);
		findParking = (Button)findViewById(R.id.findParking);
		navigate = (Button) findViewById(R.id.btNavigate);
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE) ;
		Criteria criteria = new Criteria();

		
		String provider = locationManager.getBestProvider(criteria, true);
		
		location = locationManager.getLastKnownLocation(provider);
		
		if(location!=null)
		{
			onLocationChanged(location);
		}
		locationManager.requestLocationUpdates(provider, 2000, 0, this);
		

		
		// Calling a Async Background thread
		Bundle b = getIntent().getExtras();
		String reference = b.getString("key");
		//Intent i = getIntent();
		
		//String reference = i.getStringExtra("key");
		
		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
	}// END OF ONCREATE
	
	
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String>
	{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) 
		{
			String reference = args[0];
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) 
		{
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() 
			{
				public void run() 
				{
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if(placeDetails != null)
					{
						String status = placeDetails.status;
						System.out.println("******************"+placeDetails.result);
						// check place deatils status
						// Check for all possible status
						if(status.equals("OK"))
						{
							if (placeDetails.result != null) 
							{
								
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								latitude = Double.toString(placeDetails.result.geometry.location.lat);
								longitude = Double.toString(placeDetails.result.geometry.location.lng);
								
								Log.d("Place ", name + address + phone + latitude + longitude);
								
								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);
								
								// Check for null data from google
								// Sometimes place details might be missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								
								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
								lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
							}
						}
						else if(status.equals("ZERO_RESULTS"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
									"Sorry no place found.",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Invalid Request",
									false);
						}
						else
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured.",
									false);
						}
					}
					else
					{
						alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
								"Sorry error occured.",
								false);
					}// end of placeDetails ! - null
					
					
				}
			});
			
			findParking.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					Bundle sendBundle = new Bundle();
					sendBundle.putString("latitude", latitude);
					sendBundle.putString("longitude", longitude);
					Intent intent = new Intent(getBaseContext(), MainActivity.class);
					intent.putExtras(sendBundle);
					startActivity(intent);
					
					
				}
			});
			
			navigate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
				//	Toast.makeText(getApplicationContext(), "latitude"+currentLatitude, Toast.LENGTH_LONG).show();
				//	uri = Uri.parse("http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"+"&daddr="+latitude+","+longitude+"+"&z= 18""+"zoomLevel");
					uri = Uri.parse("http://maps.google.com/maps?saddr="+currentLatitude+","+currentLongitude+"&daddr="+latitude+","+longitude+"&z="+18);
					Intent navIntent = new Intent(Intent.ACTION_VIEW,uri);
					startActivity(navIntent);
					System.out.println("URL is:"+uri);
				}
			});
		}
		
		

	}
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		currentLatitude = String.valueOf(location.getLatitude());
		currentLongitude = String.valueOf(location.getLongitude());
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
