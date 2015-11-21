package exception.overdose.stack.devhacksapp.models.POJO;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class Restaurant {

    private long Id;
    private String Name;
    private String Specific;
    private float Longitude;
    private float Latitude;
    private String Location;


    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpecific() {
        return Specific;
    }

    public void setSpecific(String specific) {
        Specific = specific;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
