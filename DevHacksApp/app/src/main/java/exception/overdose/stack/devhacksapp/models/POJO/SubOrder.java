package exception.overdose.stack.devhacksapp.models.POJO;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class SubOrder {

    private long Id;
    private long OrderID;
    private long FoodID;
    private int Quantity;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getOrderID() {
        return OrderID;
    }

    public void setOrderID(long orderID) {
        OrderID = orderID;
    }

    public long getFoodID() {
        return FoodID;
    }

    public void setFoodID(long foodID) {
        FoodID = foodID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
