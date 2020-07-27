package com.alesapps.islamik.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.alesapps.islamik.R;
import com.alesapps.islamik.listener.UserListListener;
import com.alesapps.islamik.model.ParseConstants;
import com.alesapps.islamik.model.UserModel;
import com.alesapps.islamik.utils.MyMath;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActionBarActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
	public static MapActivity instance;
	GoogleMap googleMap;
	List<ParseUser> mDataList = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		SetTitle(R.string.mosque_near_me, -1);
		ShowActionBarIcons(true, R.id.action_back);
		setContentView(R.layout.activity_map);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@SuppressLint("MissingPermission")
	@Override
	public void onMapReady(GoogleMap map) {
		googleMap = map;
		googleMap.setTrafficEnabled(false);
		googleMap.setBuildingsEnabled(true);
		googleMap.getUiSettings().setCompassEnabled(false);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.getUiSettings().setMapToolbarEnabled(false);
		googleMap.getUiSettings().setScrollGesturesEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.setMyLocationEnabled(true);
		googleMap.setOnMarkerClickListener(this);
		getServerData();
	}

	private void getServerData() {
		UserModel.GetAllUsers(new UserListListener() {
			@Override
			public void done(List<ParseUser> users, String error) {
				mDataList.clear();
				if (error == null && users.size() > 0)
					mDataList.addAll(users);
				showMap();
			}
		});
	}

	private void showMap() {
		for (int i = 0; i < mDataList.size(); i ++) {
			ParseUser mosque = mDataList.get(i);
			ParseGeoPoint point = mosque.getParseGeoPoint(ParseConstants.KEY_LON_LAT);
			googleMap.addMarker(new MarkerOptions()
					.position(new LatLng(point.getLatitude(), point.getLongitude()))
					.title(String.valueOf(i))
					.snippet(mosque.getString(ParseConstants.KEY_MOSQUE))
					.anchor(0.5f, 90/100f)
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_red)));
		}
		setZoomToMarkers();
	}

	private void setZoomToMarkers() {
		if (mDataList.size() > 0) {
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			for (int i = 0; i < mDataList.size(); i++) {
				ParseGeoPoint point = mDataList.get(i).getParseGeoPoint(ParseConstants.KEY_LON_LAT);
				if (point != null) {
					builder.include(new LatLng(point.getLatitude(), point.getLongitude()));
				}
			}
			LatLngBounds bounds = builder.build();
			Double range = Double.valueOf((Math.sqrt(2.0d) * 2000.0d) / 2.0d);
			LatLng center = bounds.getCenter();
			LatLng northEast = MyMath.move(center, range.doubleValue(), range.doubleValue());
			LatLng southWest = MyMath.move(center, -range.doubleValue(), -range.doubleValue());
			builder.include(northEast);
			builder.include(southWest);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20));
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		ParseUser mosque = mDataList.get(Integer.parseInt(marker.getTitle()));
		SermonListActivity.mUserObj = mosque;
		startActivity(new Intent(instance, SermonListActivity.class));
		return false;
	}
}