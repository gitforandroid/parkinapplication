package com.Mahmood.parking1;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LocalAttractions extends Activity{
	Button amusement_park, casino, museum, campground,aquarium,artgallery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_attractions);
		
		amusement_park = (Button) findViewById(R.id.btAmusement_park);
		casino = (Button) findViewById(R.id.btCasino);
		museum = (Button) findViewById(R.id.btMuseum);
		campground = (Button) findViewById(R.id.btCampground);
		aquarium = (Button) findViewById(R.id.btAquarium);
		artgallery = (Button) findViewById(R.id.btArt_gallery);
	
		amusement_park.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle sendId = new Bundle();
				sendId.putString("buttonId", "btAmusement_park");
				Intent sendIdIntent = new Intent(LocalAttractions.this,FamousPlaces.class);
				sendIdIntent.putExtras(sendId);
				startActivity(sendIdIntent);
				
			}
		});
		
		casino.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle sendId = new Bundle();
				sendId.putString("buttonId", "btCasino");
				Intent sendIdIntent = new Intent(LocalAttractions.this,FamousPlaces.class);
				sendIdIntent.putExtras(sendId);
				startActivity(sendIdIntent);
				
			}
		});
		
		museum.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle sendId = new Bundle();
				sendId.putString("buttonId", "btMuseum");
				Intent sendIdIntent = new Intent(LocalAttractions.this,FamousPlaces.class);
				sendIdIntent.putExtras(sendId);
				startActivity(sendIdIntent);
				
			}
		});
		
		campground.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle sendId = new Bundle();
				sendId.putString("buttonId", "btCampground");
				Intent sendIdIntent = new Intent(LocalAttractions.this,FamousPlaces.class);
				sendIdIntent.putExtras(sendId);
				startActivity(sendIdIntent);
				
			}
		});
		
		artgallery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle sendId = new Bundle();
				sendId.putString("buttonId", "btArt_gallery");
				Intent sendIdIntent = new Intent(LocalAttractions.this,FamousPlaces.class);
				sendIdIntent.putExtras(sendId);
				startActivity(sendIdIntent);
				
			}
		});
		
		aquarium.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle sendId = new Bundle();
				sendId.putString("buttonId", "btAquarium");
				Intent sendIdIntent = new Intent(LocalAttractions.this,FamousPlaces.class);
				sendIdIntent.putExtras(sendId);
				startActivity(sendIdIntent);
				
			}
		});
		
	}
	
	

}
