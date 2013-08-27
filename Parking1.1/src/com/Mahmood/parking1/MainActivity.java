package com.Mahmood.parking1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements LocationListener {

	static String parkingapi;

	static String streetnum = "";
	static String intersect = "";
	String gotLat;
	String gotLon;
	String num_records;
	private MyAsyncTask mAsyncTask;
	Intent googleIntent;
	private ProgressDialog pd;
	private Context context;
	LocationFinder locationFinder;
	Location location;
	LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("hello");
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
		
		/*This bundle is received from SinglePlaceActivity*/ 
		/*locationFinder.onLocationChanged(location);
		Bundle gotBundle = getIntent().getExtras();
		gotLat = gotBundle.getString("latitude1");
		gotLon = gotBundle.getString("longitude1");*/
		
		
		// MAHMOOD'S STUFF.. AMEY IS COMMENTING THIS BUNDLE..
		
		/*this intent is one got from AddressInput.java fetching latitude
		 *  and longitude from user entered address.*/
		
		/*Bundle b = getIntent().getExtras();
		Double latFromAddress = b.getDouble("lat"); //to be used later
		Double lonFromAddress = b.getDouble("lon"); //to be used later
*/		
		mAsyncTask = (MyAsyncTask) new MyAsyncTask().execute();
		
		/*setContentView(R.layout.activity_main);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class MyAsyncTask extends AsyncTask<String, String, String> {

		protected GoogleMap map;
		JSONArray Avl;

		List<String> items = null;

		ArrayList<HashMap<String, JSONObject>> forMap = new ArrayList<HashMap<String, JSONObject>>();

		// WHY MAKING AN ARRAYLIST FOR LAT LON
		// VALUES???????????????????????????????????????????????????????????????????

		ArrayList<String> lat = new ArrayList<String>();
		ArrayList<String> lon = new ArrayList<String>();
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(MainActivity.this);
			pd.setTitle("Processing...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			
			System.out.println("Ameys Bundle lat is:"+gotLat+"Lon is:"+gotLon);
			
			parkingapi = "http://api.sfpark.org/sfpark/rest/availabilityservice?lat="+gotLat+"&long="+gotLon+"&radius=2.0&uom=mile&response=json&method=availability&jsoncallback=sfpcallback&pricing=yes";
			
		//	parkingapi = "http://api.sfpark.org/sfpark/rest/availabilityservice?&radius=0.25&uom=mile&response=json&method=availability&jsoncallback=sfpcallback&pricing=yes&lat=37.792275&long=-122.397089";

			// FOR DALLAS
			
			
		//	parkingapi = "http://api.sfpark.org/sfpark/rest/availabilityservice?&radius=0.25&uom=mile&response=json&method=availability&jsoncallback=sfpcallback&pricing=yes&lat=32.7879407&long=-96.7966204";
			
			System.out.println("Url is:"+parkingapi);
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(parkingapi);
			// httppost.setHeader("Content-type", "application/json");
			InputStream inputstream = null;
			String result = null;

			try {
				HttpResponse response = httpClient.execute(httpget);
				int code = response.getStatusLine().getStatusCode();
				System.out.println(code);

				/*
				 * if (response.getStatusLine().getStatusCode() == 200) {
				 * System.out.println("hwudhwudhw"); HttpEntity entity =
				 * response.getEntity();// WHY ENTITY IS COMMENTED?????? String
				 * json = EntityUtils.toString(entity);
				 * System.out.println(json); }
				 */

				HttpEntity entity = response.getEntity();
				inputstream = entity.getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputstream, "UTF-8"), 8);
				StringBuilder thestringbuilder = new StringBuilder();

				String line = null;

				while ((line = reader.readLine()) != null) {
					System.out.println(line.toString());
					thestringbuilder.append(line + "\n");
				}

				result = thestringbuilder.toString();
			} catch (Exception e) {
				e.getStackTrace();
			}

			finally {

				if (inputstream != null)
					try {
						inputstream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			}

			System.out.println("hello1");
			
			Log.d("json", result.toString());
			JSONObject jsonObject;
			try {
				result = result.substring(12);
				// result = result.substring(0, result.length()-2);
				System.out.println(result);
				Log.d("JSON Tag", result);

				jsonObject = new JSONObject(result);
				num_records = jsonObject.getString("NUM_RECORDS");
				
				System.out.println("the number of records is :" +num_records);
				
				if(num_records.equalsIgnoreCase("0"))
				{
					
					mAsyncTask.cancel(true);
					onPostExecute(result);
					 
					
				}
				
					
				Avl = jsonObject.getJSONArray("AVL");
				lat.add("");
				lon.add("");
				forMap.add(null);
				System.out.println(Avl.length());

				for (int i = 1; i < Avl.length(); i++) {
					System.out.println("numbernine:" + i);
					
					
					JSONObject a = Avl.getJSONObject(i);

					String type = a.getString("TYPE");
					// String bfid = a.getString("BFID");
					String name = a.getString("NAME");
					String occ = a.getString("OCC");
					String oper = a.getString("OPER");
					String pts = a.getString("PTS");
					String loc = a.getString("LOC");
					
					//AMEY MAKES CHANGES FROM HERE.....
					
				/*	String rate = a.getString("RATE");*/

					HashMap<String, JSONObject> h = new HashMap<String, JSONObject>();
					h.put(name, a);
					// System.out.println("the value of h" + h);
					forMap.add(h);
					// System.out.println("the value of list" + forMap.get(i));
					items = Arrays.asList(loc.split("\\s*,\\s*"));

					lat.add(items.get(0));
					lon.add(items.get(1));

					System.out.println("TYPE(ON/OFF): " + type + "\n");
					System.out.println("NAME: " + name + "\n");
					System.out.println("OCCUPIED: " + occ + "\n");
					System.out.println("TOTAL OPERATIONAL: " + oper + "\n");
					System.out.println("PTS: " + pts + "\n");
					System.out.println("LOC: " + loc + "\n");
					/*System.out.println("RATE: " + rate + "\n");*/
					// System.out.println("BFID: "+bfid+ "\n");

					JSONObject rates = a.getJSONObject("RATES");
					JSONArray rs = rates.getJSONArray("RS");

					System.out.println("*********rate array start*********");

					 /*for(int j=0;j<rs.length();j++)
					{*/
					/*
					 * System.out.println("2"); System.out.println(rs.length());
					 * System.out.println(j); System.out.println(rs.get(j));
					 */
					/*
					 * JSONObject b = rs.getJSONObject(j);
					 * 
					 * String beg = b.getString("BEG"); String end =
					 * b.getString("END"); String rate = b.getString("RATE");
					 * String rq = b.getString("RQ");
					 * System.out.println("_______"+j+"slot______");
					 * System.out.println("BEG: "+beg +"\n");
					 * System.out.println("END: "+end +"\n");
					 * System.out.println("RATE: "+rate +"\n");
					 * System.out.println("RQ: "+rq +"\n");
					 * 
					 * }
					 */
					System.out.println("*********Rate array end*********");
					System.out.println("---Entire" + i + "record printed--");
					System.out.println(Avl.get(i));
					System.out.println("--------------------------\n");

				}
				for (String s : lat) {

					System.out.println(s);
				}
				for (String s : lon) {

					System.out.println(s);
				}

			} catch (Exception e) {
				e.getStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			if(num_records.equalsIgnoreCase("0"))
			{
				Bundle locationBundle = new Bundle();
				locationBundle.putString("latitude1", gotLat);
				locationBundle.putString("longitude1", gotLon);
				
				googleIntent = new Intent(MainActivity.this, GoogleParking.class);
				googleIntent.putExtras(locationBundle);
				startActivity(googleIntent);
				finish();
			}
			if (pd!=null) {
				pd.dismiss();
				
			}
			setContentView(R.layout.activity_main);
			// TODO Auto-generated method stub
			for (int i = 1; i < Avl.length() - 1; i++) {
				// Create a marker for each city in the JSON data.
				if (map == null) {
					map = ((MapFragment) getFragmentManager().findFragmentById(
							R.id.map)).getMap();
				}
				map.setMyLocationEnabled(true);
				try {
					map.addMarker(new MarkerOptions()
							.title("parking")
							.snippet(Avl.getJSONObject(i).getString("NAME"))
							.position(
									new LatLng(Double.parseDouble(lon.get(i)),
											Double.parseDouble(lat.get(i)))));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker marker) {
					// TODO Auto-generated method stub
					String name = marker.getSnippet();
					JSONObject valued = null;

					/*
					 * for (HashMap<String, JSONObject> map : forMap) for
					 * (Entry<String, JSONObject> mapEntry : map.entrySet()) {
					 * String key = mapEntry.getKey(); JSONObject value =
					 * mapEntry.getValue(); System.out.println(key +""+ value);
					 * }
					 */
					// System.out.println(forMap.get(1).get("Pine St (2-98)"));

					for (int i = 1; i < forMap.size(); i++) {
						if ((forMap.get(i).get(name) != null)) {
							System.out.println("got it");
							valued = forMap.get(i).get(name);
						}
					}
					/*
					 * for(Map<String, JSONObject> map : forMap) {
					 * if(map.containsKey("Pine St (2-98)"))
					 * System.out.println("works*********"); }
					 */
					// System.out.println(valued);
					Intent intent = new Intent(MainActivity.this,
							ParkingDetails.class);
					Bundle b = new Bundle();
					b.putString("name", name);
					b.putString("jsonvalue", valued.toString());
					intent.putExtras(b);
					startActivity(intent);
					// finish();
				}
			});

		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		gotLat = String.valueOf(location.getLatitude());
		gotLon = String.valueOf(location.getLongitude());
		
		System.out.println("Amey's Lat in MainActivity is:"+gotLat+"Lon is:"+gotLon);
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
