package exception.overdose.stack.devhacksapp.models.POJO;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class Orders {

    private long id;
    private long time;
    private long restaurantID;
    private float price;
    private int discount;
    private long userId;
    private long clusterId;
    private int done;

    public Orders(long id, long time, long restaurantID, float price, int discount, long userId) {
        this(time, restaurantID, price, discount, userId);
        this.id = id;
    }

    public Orders(long time, long restaurantID, float price, int discount, long userId) {
        this.time = time;
        this.restaurantID = restaurantID;
        this.price = price;
        this.discount = discount;
        this.userId = userId;
    }

    public Orders() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(long restaurantID) {
        this.restaurantID = restaurantID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getClusterId() {
        return clusterId;
    }

    public void setClusterId(long clusterId) {
        this.clusterId = clusterId;
    }
}
