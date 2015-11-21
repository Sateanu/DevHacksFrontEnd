package exception.overdose.stack.devhacksapp.models.POJO;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class Restaurant {

    private long id;
    private String name;
    private String specific;
    private float longitude;
    private float latitude;
    private String location;

    public Restaurant()
    {

    }

    public Restaurant(String Name, String Specific, float Longitude, float Latitude, String Location)
    {
        this.name = Name;
        this.specific = Specific;
        this.longitude = Longitude;
        this.latitude = Latitude;
        this.location = Location;
    }

    public Restaurant(long Id, String Name, String Specific, float Longitude, float Latitude, String Location)
    {
        this(Name, Specific, Longitude, Latitude, Location);
        this.id = Id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
