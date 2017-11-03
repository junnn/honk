package bloop.honk.Model;

public class MapPlace {
    private CharSequence placeId;
    private CharSequence description;

    public MapPlace(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    public CharSequence getPlaceId() {
        return placeId;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}
