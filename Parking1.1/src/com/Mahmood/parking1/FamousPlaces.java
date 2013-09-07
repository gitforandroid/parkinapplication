package com.Mahmood.parking1;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import com.androidhive.googleplacesandmaps.R;


public class FamousPlaces extends Activity implements LocationListener{

	
	
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();
	
	

	// Google Places
	GooglePlaces googlePlaces;

	// Places List
	PlacesList nearPlaces;

	// GPS Location
	

	// Progress dialog
	ProgressDialog pDialog;
	
	// Places Listview
	ListView lv;
	
	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	
	double latitude;
	double longitude;
	
	String gotLat;
	String gotLon;
	
	String gotId;
	
	String types;
	
	Context context;
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name

	LocationManager locationManager;
	Location location;
	
	String provider;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.famous_activity);

		cd = new ConnectionDetector(getApplicationContext());
		context=this;
		/*context=this;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
*/
		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(FamousPlaces.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// AMEY -- RECEIVING A BUNDLE FROM LocalAttractions.. FIND THE REASON FOR THIS IN THAT CLASS..
		
		Bundle gotIdBundle = getIntent().getExtras();
		gotId = gotIdBundle.getString("buttonId");
		//System.out.println("heyhaaaheyhaa-------"+gotId);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Criteria criteria = new Criteria();
		
	    provider = locationManager.getBestProvider(criteria, true);
		
	    if(provider==null)
		{
			Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(settingsIntent);
			Toast.makeText(getApplicationContext(), "You Need To Turn On Location Services", Toast.LENGTH_LONG).show();
			
		}
		else
		{
		
			location = locationManager.getLastKnownLocation(provider);
			
		}
		if(location!=null)
		{
			onLocationChanged(location);
			locationManager.requestLocationUpdates(provider, 2000, 0, this);
		}
		
		// creating GPS Class object
		
		
//******************************************* COMMENTING THIS****************************************//
		// Getting listview
		lv = (ListView) findViewById(R.id.list);
		
		// button show on map
		
		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();

		
		
		
		
		/**
		 * ListItem click event
		 * On selecting a listitem SinglePlaceActivity is launched
		 * */
		lv.setOnItemClickListener(new OnItemClickListener() {
 
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                System.out.println("Reference in Famous is --------------------"+reference);
                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                in.putExtra("key", reference);
                startActivity(in);
            }
        });
	}
	
	

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(FamousPlaces.this);
			pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();
			
			try {
				// Separeate your place types by PIPE symbol "|"
				// If you want all types places make it as null
				// Check list of types supported by google
				// 
				
				System.out.println("Button id issssssssssssssssssssssssssssssss:"+gotId);
				
				if(gotId.equalsIgnoreCase("btAmusement_park"))
				{
					System.out.println("I AM CONSIDERING ONLY AMUSEMENT PARKS");
					types = "amusement_park";
				
					// get nearest places
					nearPlaces = googlePlaces.search(latitude,longitude, types);

				}
				else if(gotId.equalsIgnoreCase("btCasino"))
				{
					types = "casino";
					
					nearPlaces = googlePlaces.search(latitude,longitude, types);

				}
				else if(gotId.equalsIgnoreCase("btMuseum"))
				{
					types = "museum";
					
					nearPlaces = googlePlaces.search(latitude,longitude, types);

				}
				else if(gotId.equalsIgnoreCase("btCampground"))
				{
					types = "campground";
					
					nearPlaces = googlePlaces.search(latitude,longitude, types);

				}
				else if(gotId.equalsIgnoreCase("btArt_gallery"))
				{
					types = "art_gallery";
				
					nearPlaces = googlePlaces.search(latitude,longitude, types);

				}
				else if(gotId.equalsIgnoreCase("btAquarium"))
				{
					types = "aquarium";
					nearPlaces = googlePlaces.search(latitude,longitude, types);

				}
				
		
				
/******************************************** modified everything before this ****************************************************************************************/
			//IN CASE OF ERRORS, IGNORE THE STATEMENTS WITH SLASHES.. I HAVE NOT USED THEM ANYWAY.. JUST CONCENTRATE ON COMMENTED STATEMENTS..	
				//		String types = "amusement_park|aquarium|art_gallery|campground|casino|museum"; // Listing places only cafes, restaurants
				
			//	String types = "parking";
				// Radius in meters - increase this value if you don't find any places
/*				double radius = 50000; // 1000 meters 
*/				
		//		System.out.println("Bundle lat is:"+gotLat+"Bundle lon is:"+gotLon);
				
/*				latitude = 37.7749295 ;
				longitude = -122.4194155 ;
				// get nearest places
				nearPlaces = googlePlaces.search(latitude,longitude, types);
*/				//location=32.7889541,-96.7968337&radius=3000&types=amusement_park|aquarium|art_gallery|campground|casino|museum&sensor=false

				
/**************************************************************************************************************************************************************************/

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = nearPlaces.status;
					
					// Check for all possible status
					if(status.equals("OK")){
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(KEY_REFERENCE, p.reference);
								
								// Place name
								map.put(KEY_NAME, p.name);
								
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							// list adapter
							ListAdapter adapter = new SimpleAdapter(FamousPlaces.this, placesListItems,
					                R.layout.list_item,new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {R.id.reference, R.id.name });
							
							// Adding data into listview
							lv.setAdapter(adapter);
						}
					}
					else if(status.equals("ZERO_RESULTS")){
						// Zero results found
						/*alert.showAlertDialog(FamousPlaces.this, "Near Places",
								"Sorry no such place found in your vicinity",
								false);
						
						FamousPlaces.this.finish();*/
						
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								context);

						alertDialogBuilder.setMessage("Sorry no such place found in your vicinity").setCancelable(false).setPositiveButton("OK",new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog,int id) 
							{
								// if this button is clicked, close
								// current activity
								FamousPlaces.this.finish();
							}
						  });
						AlertDialog alertDialog = alertDialogBuilder.create();
						 
						// show it
						alertDialog.show();
						
					}
					else if(status.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog(FamousPlaces.this, "Places Error",
								"Sorry unknown error occured.",
								false);
					}
					else if(status.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog(FamousPlaces.this, "Places Error",
								"Sorry query limit to google places is reached",
								false);
					}
					else if(status.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog(FamousPlaces.this, "Places Error",
								"Sorry error occured. Request is denied",
								false);
					}
					else if(status.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog(FamousPlaces.this, "Places Error",
								"Sorry error occured. Invalid Request",
								false);
					}
					else
					{
						alert.showAlertDialog(FamousPlaces.this, "Places Error",
								"Sorry error occured.",
								false);
					}
				}
			});

		}

	}



	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		
	}



	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	

	
	

}
