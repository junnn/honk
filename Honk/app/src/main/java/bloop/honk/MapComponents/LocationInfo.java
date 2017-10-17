package bloop.honk.MapComponents;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by Chiang on 16/10/2017.
 */

public class LocationInfo {
    private String address;
    private LatLng location;
    private String locationType;
    private LatLngBounds viewport;
    private String placeId;

    public LocationInfo(String address, LatLng location, String locationType, LatLngBounds viewport, String placeId) {

        this.address = address;
        this.location = location;
        this.locationType = locationType;
        this.viewport = viewport;
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public LatLngBounds getViewport() {
        return viewport;
    }

    public void setViewport(LatLngBounds viewport) {
        this.viewport = viewport;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
