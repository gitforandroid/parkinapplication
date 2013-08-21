package com.Mahmood.parking1;

import android.app.Activity;
import android.os.Bundle;

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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

	static String parkingapi = "http://api.sfpark.org/sfpark/rest/availabilityservice?lat=37.792275&long=-122.397089&radius=0.25&uom=mile&response=json&method=availability&jsoncallback=sfpcallback&pricing=yes";

	static String streetnum = "";
	static String intersect = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("hello");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new MyAsyncTask().execute();
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
		protected String doInBackground(String... arg0) {

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
							SecondActiv.class);
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

}
