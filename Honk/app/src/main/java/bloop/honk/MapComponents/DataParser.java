package bloop.honk.MapComponents;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chiang on 16/10/2017.
 */

public class DataParser {
    public LocationInfo parseAddress(String jsonData) {
        String address = "";
        LatLng location = null;
        String locationType = "";
        LatLngBounds viewport = null;
        String placeId = "";
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonObject = jsonObject.getJSONArray("results").getJSONObject(0);
            address = jsonObject.getString("formatted_address");
            placeId = jsonObject.getString("place_id");
            jsonObject = jsonObject.getJSONObject("geometry");
            location = new LatLng(Double.parseDouble(jsonObject.getJSONObject("location").getString("lat")), Double.parseDouble(jsonObject.getJSONObject("location").getString("lng")));
            locationType = jsonObject.getString("location_type");
            double swLat = Double.parseDouble(jsonObject.getJSONObject("viewport").getJSONObject("southwest").getString("lat"));
            double swLng = Double.parseDouble(jsonObject.getJSONObject("viewport").getJSONObject("southwest").getString("lng"));
            double neLat = Double.parseDouble(jsonObject.getJSONObject("viewport").getJSONObject("northeast").getString("lat"));
            double neLng = Double.parseDouble(jsonObject.getJSONObject("viewport").getJSONObject("northeast").getString("lng"));
            viewport = new LatLngBounds(new LatLng(swLat,swLng), new LatLng(neLat,neLng));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Android", address);
        return new LocationInfo(address, location, locationType, viewport, placeId);
    }
}
