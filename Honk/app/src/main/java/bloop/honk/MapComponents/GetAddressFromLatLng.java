package bloop.honk.MapComponents;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import bloop.honk.Fragments.MapFragment;
import bloop.honk.R;

public class GetAddressFromLatLng extends AsyncTask<String, String, String> {
    private GoogleMap mMap;
    private float zoomLevel;
    private boolean setAddress;
    private MapFragment mapfrag;

    public GetAddressFromLatLng(MapFragment mapfrag, GoogleMap mMap, float zoomLevel, boolean setAddress) {
        this.mMap = mMap;
        this.zoomLevel = zoomLevel;
        this.setAddress = setAddress;
        this.mapfrag = (MapFragment) mapfrag;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = (String) params[0];
        String googleAddressData = "";
        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleAddressData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleAddressData;
    }

    @Override
    protected void onPostExecute(String data) {
        if(data != null) {
            String address = "", lat = "", lng = "";
            try {
                JSONObject jsonObject = new JSONObject(data);
                jsonObject = jsonObject.getJSONArray("results").getJSONObject(0);
                address = jsonObject.getString("formatted_address");
                jsonObject = jsonObject.getJSONObject("geometry");
                lat = jsonObject.getJSONObject("location").getString("lat");
                lng = jsonObject.getJSONObject("location").getString("lng");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mapfrag.setAddress(address);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)))
                    .title(address)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.favourite))
                    .draggable(true));
            mapfrag.setMarker(marker);
            Log.i("android","Lat: " + lat + " Lng: " + lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)),zoomLevel));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());

            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 2000, null);
            if(setAddress) {
                mapfrag.setAddressET(address);
            }
            marker.showInfoWindow();
        }
    }
}
