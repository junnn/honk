package bloop.honk.Model;

public class MapPlace {
    public CharSequence placeId;
    public CharSequence description;

    public MapPlace(CharSequence placeId, CharSequence description) {
        this.placeId = placeId;
        this.description = description;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}
