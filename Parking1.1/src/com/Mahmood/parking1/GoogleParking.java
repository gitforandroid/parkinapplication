package com.Mahmood.parking1;


import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.androidhive.googleplacesandmaps.R;


public class GoogleParking extends Activity {

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
	
	PlaceDetails placeDetails;

	// Progress dialog
	ProgressDialog pDialog;
	
	// Places Listview
	ListView lv;
	
	GoogleMap googleMap;
	
	// ListItems data
	ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
	
	double latitude;
	double longitude;
	
	String gotLat;
	String gotLon;
	
	String valued;
	
	LatLng CURRENT;
	//String rankBy;
	
	
	//String latFromSinglePlace;
	
	
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	public static String KEY_NAME = "name"; // name of the place
	public static String KEY_VICINITY = "vicinity"; // Place area name
//	public static double rankby = "rankby";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_parking);

		cd = new ConnectionDetector(getApplicationContext());
		
		
		 

		// Check if Internet present
		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			// Internet Connection is not present
			alert.showAlertDialog(GoogleParking.this, "Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// AMEY -- RECEIVING A BUNDLE FROM LOCATIONFINDER.. FIND THE REASON FOR THIS IN THAT CLASS.. i think here we need to get bundle from main activity as well..nahi nlahiem..me i  have used same 
		// name for keys.. and current and location finder will always have current location anyway.. so it does not matter..
		
		Bundle gotLocationBundle = getIntent().getExtras();
		gotLat = gotLocationBundle.getString("latitude1");
		gotLon = gotLocationBundle.getString("longitude1");
		
		Log.d("Lat in Google Parking is:", gotLat);
		
		// creating GPS Class object
		

		// Getting listview
		//lv = (ListView) findViewById(R.id.mapFamous);
		
		// button show on map
		
		// calling background Async task to load Google Places
		// After getting places from Google all the data is shown in listview
		new LoadPlaces().execute();

		
		
		
		
		/**
		 * ListItem click event
		 * On selecting a listitem SinglePlaceActivity is launched
		 * */
		/*lv.setOnItemClickListener(new OnItemClickListener() {
 
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
                
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                
                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
        });
*/	}

	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		
		
		ArrayList<Double> parkingLat = new ArrayList<Double>();
		ArrayList<Double> parkingLon = new ArrayList<Double>();
		ArrayList<String> parkingName = new ArrayList<String>();
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GoogleParking.this);
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
				String types = "parking"; // Listing places only cafes, restaurants
				
			//	String types = "parking";
				// Radius in meters - increase this value if you don't find any places
				double radius = 4000; // 1000 meters 
				
		//		System.out.println("Bundle lat is:"+gotLat+"Bundle lon is:"+gotLon);
				
				latitude = Double.parseDouble(gotLat) ;
				longitude = Double.parseDouble(gotLon) ;
				
				
				// get nearest places
				nearPlaces = googlePlaces.search(latitude,longitude, types);
				//location=32.7889541,-96.7968337&radius=3000&types=amusement_park|aquarium|art_gallery|campground|casino|museum&sensor=false


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
			final String latcurrent;
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = nearPlaces.status;
			//	System.out.println("******************"+nearPlaces.results);
					// Check for all possible status
					if(status.equals("OK")){
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (  Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								//COMMENTING THIS ---------------------------------------------------AMEY 
								System.out.println("------shitty--------"+p.geometry.location.lat);
								parkingLat.add(p.geometry.location.lat);
								parkingLon.add(p.geometry.location.lng);
								parkingName.add(p.name);
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(p.name, p.reference);
							
								/*// Place name
								map.put(KEY_NAME, p.name);*/
								
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
								
								
							}
							for (Double s : parkingLat) {

								System.out.println(s);
							}
							for (Double s : parkingLon) {

								System.out.println(s);
							}
							
							
							if (googleMap == null) {
								googleMap = ((MapFragment) getFragmentManager().findFragmentById(
										R.id.mapFamous)).getMap();
							}
							
							CURRENT = new LatLng(latitude,longitude);
							googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT, 18));
							// Zoom in, animating the camera.
							googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
							
							googleMap.setMyLocationEnabled(true);
							
							for(int i=0;i<parkingLat.size();i++)
							{
								googleMap.addMarker(new MarkerOptions()
								.title("parking")
								.snippet(parkingName.get(i))
								.position(
										new LatLng(parkingLat.get(i), parkingLon.get(i))));
							}
							
							
							googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
								@Override
								public void onInfoWindowClick(Marker marker) {
									// TODO Auto-generated method stub
									String name = marker.getSnippet();
									

									
									for (int i = 0; i < placesListItems.size(); i++) {
										if ((placesListItems.get(i).get(name) != null)) {
											System.out.println("Place Itemi s****************************"+placesListItems.get(i).get(name));
											valued = placesListItems.get(i).get(name);
										}
									}
									/*
									 * for(Map<String, JSONObject> map : forMap) {
									 * if(map.containsKey("Pine St (2-98)"))
									 * System.out.println("works*********"); }
									 */
									// System.out.println(valued);
									Intent intent = new Intent(GoogleParking.this,SinglePlaceActivity.class);
									Bundle b = new Bundle();
									b.putString("key", valued);
									intent.putExtras(b);
									startActivity(intent);
									// finish();
								}
							});
							//System.out.println("****************************** P NAME IS-------------------------"+p.name);
							
							// ENd of FOR LOOP
							
							
							
							//SinglePlaceActivity singlePlaceActivity = new SinglePlaceActivity();
							//latFromSinglePlace=singlePlaceActivity.latitude;
							
							
					//		System.out.println("latFromSinglePlace------------------------"+latFromSinglePlace);
							
							// list adapter
						/*	ListAdapter adapter = new SimpleAdapter(GoogleParking.this, placesListItems,
					                R.layout.list_item,new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {R.id.reference, R.id.name });
							
							// Adding data into listview
							lv.setAdapter(adapter);*/
						}
					}
					else if(status.equals("ZERO_RESULTS")){
						// Zero results found
						alert.showAlertDialog(GoogleParking.this, "Near Places",
								"Sorry no places found. Try to change the types of places",
								false);
					}
					else if(status.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog(GoogleParking.this, "Places Error",
								"Sorry unknown error occured.",
								false);
					}
					else if(status.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog(GoogleParking.this, "Places Error",
								"Sorry query limit to google places is reached",
								false);
					}
					else if(status.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog(GoogleParking.this, "Places Error",
								"Sorry error occured. Request is denied",
								false);
					}
					else if(status.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog(GoogleParking.this, "Places Error",
								"Sorry error occured. Invalid Request",
								false);
					}
					else
					{
						alert.showAlertDialog(GoogleParking.this, "Places Error",
								"Sorry error occured.",
								false);
					}
				}
			});

		}

	}

	
	

}
