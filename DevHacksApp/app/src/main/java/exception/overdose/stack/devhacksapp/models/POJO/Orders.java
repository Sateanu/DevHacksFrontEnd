package exception.overdose.stack.devhacksapp.models.POJO;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class Orders {

    private long Id;
    private long Time;
    private long RestaurantID;
    private float Price;
    private int Discount;

    public Orders(long id, long time, long restaurantID, float price, int discount) {
        Id = id;
        Time = time;
        RestaurantID = restaurantID;
        Price = price;
        Discount = discount;
    }

    public Orders() {
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public long getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(long restaurantID) {
        RestaurantID = restaurantID;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }
}
