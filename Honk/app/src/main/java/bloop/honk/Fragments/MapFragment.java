package bloop.honk.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import bloop.honk.Config;
import bloop.honk.LoginActivity;
import bloop.honk.MapComponents.GetAddressFromLatLng;
import bloop.honk.MapComponents.MapWrapperLayout;
import bloop.honk.MapComponents.OnInfoWindowElemTouchListener;
import bloop.honk.MapComponents.PlacesAutoCompleteAdapter;
import bloop.honk.MapComponents.PlacesItemClickListener;
import bloop.honk.MapComponents.LocationInfo;
import bloop.honk.R;

import static com.google.android.gms.common.api.CommonStatusCodes.API_NOT_CONNECTED;

/**
 * Created by Jun Hao Ng on 6/9/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, ResultCallback<LocationSettingsResult>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final int ACCESS_FINE_LOCATION_REQUEST_CODE = 100;
    private final int REQUEST_CHECK_SETTINGS = 101;
    private boolean permissionGranted = false, settingText = false, login;
    private GoogleMap mMap = null;
    private GoogleApiClient mGoogleApiClient;
    private Marker marker = null;
    private LocationRequest locationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LatLng latLng = null;
    private EditText addressET;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private AppCompatImageView delete;
    private ViewGroup infoWindow;
    private TextView infoTitle;
    private Button infoLoginButton;
    private Button infoFavButton;
    private OnInfoWindowElemTouchListener infologinButtonListener, infofavButtonListener;
    private MapWrapperLayout mapWrapperLayout;
    private View v;
    private SharedPreferences sharedPreferences;
    private String address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapWrapperLayout = view.findViewById(R.id.map_relative_layout);
        getActivity().setTitle("Maps");//set the title on the toolbar
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        login = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);
        buildGoogleApiClient();
        setupLocationRequest();
        v = view;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
            return;
        } else {
            buildLocationSettingsRequest();
            checkLocationSettings();
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    private void setupLocationRequest() {
        // 18sec per request for 12hours usage
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if(permissionGranted) {
            if(mMap != null) {
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mapWrapperLayout.init(mMap, getPixelsFromDp(getActivity(), 39 + 20));
                this.infoWindow = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.marker_info_layout, null);
                this.infoTitle = (TextView)infoWindow.findViewById(R.id.addressTV);
                this.infoLoginButton = (Button)infoWindow.findViewById(R.id.loginBtn);
                this.infoFavButton = (Button)infoWindow.findViewById(R.id.favBtn);

                this.infologinButtonListener = new OnInfoWindowElemTouchListener(infoLoginButton,
                        ContextCompat.getDrawable(getActivity(), R.drawable.login_norm),
                        ContextCompat.getDrawable(getActivity(), R.drawable.login_pressed)) {

                    @Override
                    protected void onClickConfirmed(View v, Marker markerz) {
                        if(marker != null) {
                            marker.remove();
                        }
                        float zoom = mMap.getCameraPosition().zoom;
                        getAddressFromLatLng(markerz.getPosition().latitude, markerz.getPosition().longitude, mMap, zoom, false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                };

                this.infofavButtonListener = new OnInfoWindowElemTouchListener(infoFavButton,
                        ContextCompat.getDrawable(getActivity(), R.drawable.fav_norm),
                        ContextCompat.getDrawable(getActivity(), R.drawable.fav_pressed)) {

                    @Override
                    protected void onClickConfirmed(View v, Marker markerz) {
                        if(marker != null) {
                            marker.remove();
                        }
                        float zoom = mMap.getCameraPosition().zoom;
                        final LocationInfo locationInfo = getAddressFromLatLng(markerz.getPosition().latitude, markerz.getPosition().longitude, mMap, zoom, false);
                        address = locationInfo.getAddress();
                        final double lat = locationInfo.getLocation().latitude;
                        final double lng = locationInfo.getLocation().longitude;
                        final String ADD_BOOKMARK_POST = "http://172.21.148.166/example/dao/Hookdaoimpl.php?function=addbookmark";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_BOOKMARK_POST, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equalsIgnoreCase("unsucessful")){
                                    Toast.makeText(getContext(), "Adding of Bookmark Failed", Toast.LENGTH_LONG).show();
                                } else if(response.equalsIgnoreCase("duplicate")){
                                    Toast.makeText(getContext(), "This bookmark already exist in your list", Toast.LENGTH_LONG).show();
                                } else if(response.equalsIgnoreCase("success")){
                                    Toast.makeText(getContext(), "You have added " + address + " into your Favourite List", Toast.LENGTH_SHORT).show();
                                    Log.i("android", "address: " + address + " lat: " + lat + " lng: " + lng);
                                } else {
                                    Toast.makeText(getContext(),"Unknown Error Occurred!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "invalid");
                                Map<String,String> params = new HashMap<>();
                                //Adding parameters to request
                                params.put(Config.TAG_USERNAME, username);
                                params.put("bookmarkname", address);
                                params.put("latitude", Double.toString(lat));
                                params.put("longtitude", Double.toString(lng));

                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(stringRequest);
                    }
                };

                this.infoLoginButton.setOnTouchListener(infologinButtonListener);
                this.infoFavButton.setOnTouchListener(infofavButtonListener);

                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) { }

                    @Override
                    public void onMarkerDrag(Marker marker) { }

                    @Override
                    public void onMarkerDragEnd(Marker markerDragged) {
                        if(marker != null) {
                            marker.remove();
                        }
                        float zoom = mMap.getCameraPosition().zoom;
                        getAddressFromLatLng(markerDragged.getPosition().latitude, markerDragged.getPosition().longitude, mMap, zoom, false);
                    }
                });

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) { return null; }

                    @Override
                    public View getInfoContents(Marker marker) {
                        LatLng latLng = marker.getPosition();
                        infoTitle.setText(marker.getTitle());
                        if(login) {
                            // do stuffs for user not login
                            infofavButtonListener.setMarker(marker);
                            infoFavButton.setEnabled(true);
                            infoFavButton.setVisibility(View.VISIBLE);
                            infoLoginButton.setEnabled(false);
                            infoLoginButton.setVisibility(View.GONE);
                        } else {
                            // do stuffs for user logged in
                            infoFavButton.setEnabled(false);
                            infoFavButton.setVisibility(View.GONE);
                            infoLoginButton.setEnabled(true);
                            infoLoginButton.setVisibility(View.VISIBLE);
                            infologinButtonListener.setMarker(marker);
                        }
                        mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                        return infoWindow;
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    buildLocationSettingsRequest();
                    checkLocationSettings();
                } else {
                    permissionGranted = false;
                    Toast.makeText(getActivity(), "This application requires LocationInfo Permission to be Allowed.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                /* Load stuffs */
                permissionGranted = true;
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                if (mGoogleApiClient.isConnected()) {
                    requestLocationUpdates();
                }
                Log.i("android", Boolean.toString(mGoogleApiClient.isConnected()));
                // Set up autocompletePlaces here
                setupAutoCompletePlaces();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Toast.makeText(getActivity(), "Your device does not support LocationInfo Services.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) { }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(permissionGranted) {
            if(mGoogleApiClient.isConnected()) {
                if(sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false)) {
                    login = true;
                } else {
                    login = false;
                }

                if(mMap != null) {
                    if(marker != null) {
                        marker.remove();
                    }
                    getAddressFromLatLng(latLng.latitude, latLng.longitude, mMap, 15, false);
                    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
                    addressET.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(permissionGranted) {
            if(mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            // stop something
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(latLng != null) {
            if(mMap != null) {
                if(marker != null) {
                    marker.remove();
                }
                getAddressFromLatLng(latLng.latitude, latLng.longitude, mMap, 15, false);
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
                addressET.setEnabled(true);
            }
        }
    }

    public void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
            return;
        }
        // Request LocationInfo with Loop
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
    }

    public void setupAutoCompletePlaces() {
        addressET = (EditText) v.findViewById(R.id.addressET);
        delete=(AppCompatImageView) v.findViewById(R.id.crossImageView);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(getActivity(), R.layout.searchview_adapter,
                mGoogleApiClient, new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0)), new AutocompleteFilter.Builder().
                setTypeFilter(Place.TYPE_COUNTRY).setCountry("SG").build());

        mRecyclerView=(RecyclerView) getView().findViewById(R.id.locationResultRecyclerView);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressET.setText("");
            }
        });

        addressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(settingText) {
                    settingText = false;
                } else {
                    if (mGoogleApiClient.isConnected()) {
                        mAutoCompleteAdapter.getFilter().filter(charSequence.toString());
                    }else if(!mGoogleApiClient.isConnected()){
                        Toast.makeText(getActivity(), API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
        mRecyclerView.addOnItemTouchListener(
                new PlacesItemClickListener(getActivity(), new PlacesItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                            final String placeId = String.valueOf(item.placeId);
                            //Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);
                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if (places.getCount() == 1) {
                                        //RecyclerView Item Clicked
                                        LatLng latLng = new LatLng(places.get(0).getLatLng().latitude, places.get(0).getLatLng().longitude);
                                        if(marker != null) {
                                            marker.remove();
                                        }
                                        float zoom = mMap.getCameraPosition().zoom;
                                        getAddressFromLatLng(latLng.latitude, latLng.longitude, mMap, zoom, true);
                                        settingText = true;
                                        mAutoCompleteAdapter.clearList();
                                        hideKeyboard();
                                        Toast.makeText(getActivity(), String.valueOf(places.get(0).getAddress()), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "SOMETHING_WENT_WRONG", Toast.LENGTH_SHORT).show();
                                    }
                                    places.release();
                                }
                            });
                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getActivity(), "SOMETHING_WENT_WRONG", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    private static int getPixelsFromDp(FragmentActivity context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    public void setAddressET(String address) {
        addressET.setText(address);
    }

    public LocationInfo getAddressFromLatLng(double latitude, double longitude, GoogleMap mMap, float zoom, boolean setAddress)
    {
        String geocodeRequestUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&key=" + getResources().getString(R.string.google_maps_key);

        GetAddressFromLatLng getAddressFromLatLng = new GetAddressFromLatLng(this, mMap, zoom, setAddress);
        LocationInfo locationInfo = null;
        try {
            locationInfo = getAddressFromLatLng.execute(geocodeRequestUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return locationInfo;
    }

    public void setPermissionGranted(boolean permissionGranted) { this.permissionGranted = permissionGranted; }

    public void setAddress(String address) { this.address = address; }
}
