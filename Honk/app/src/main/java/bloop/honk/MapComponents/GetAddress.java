package bloop.honk.MapComponents;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Map;

import bloop.honk.Fragments.MapFragment;
import bloop.honk.MainActivity;
import bloop.honk.R;

/**
 * Created by Chiang on 16/10/2017.
 */

public class GetAddress extends AsyncTask<String, String, LocationInfo> {
    private Activity context;
    private String url;
    private String googleAddressData;
    private GoogleMap mMap;
    private float zoomLevel;
    private boolean setAddress;
    private MapFragment mapfrag;

    public GetAddress(MapFragment mapfrag, GoogleMap mMap, float zoomLevel, boolean setAddress) {
        this.context = context;
        this.mMap = mMap;
        this.zoomLevel = zoomLevel;
        this.setAddress = setAddress;
        this.mapfrag = (MapFragment) mapfrag;
    }

    @Override
    protected LocationInfo doInBackground(String... params) {
        url = (String) params[0];

        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googleAddressData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocationInfo locationInfo = null;
        DataParser dataParser = new DataParser();
        locationInfo = dataParser.parseAddress(googleAddressData);
        return locationInfo;
    }

    @Override
    protected void onPostExecute(LocationInfo locationInfo) {

        if(locationInfo != null) {
            mapfrag.setAddress(locationInfo.getAddress());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(locationInfo.getLocation())
                    .title(locationInfo.getAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.favourite))
                    .draggable(true));
            mapfrag.setMarker(marker);
            Log.i("android","Lat: " + Double.toString(locationInfo.getLocation().latitude) + " Lng: " + Double.toString(locationInfo.getLocation().longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationInfo.getLocation(),zoomLevel));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());

            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 2000, null);
            if(setAddress) {
                mapfrag.setAddressET(locationInfo.getAddress());
            }
            marker.showInfoWindow();
        }
    }
}
