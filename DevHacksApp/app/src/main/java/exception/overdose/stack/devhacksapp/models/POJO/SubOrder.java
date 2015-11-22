package exception.overdose.stack.devhacksapp.models.POJO;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class SubOrder {

    private long id;
    private long orderID;
    private long foodID;
    private int quantity;

    public SubOrder()
    {

    }

    public SubOrder(long OrderID, long FoodID, int Quantity)
    {
        this.orderID = OrderID;
        this.foodID = FoodID;
        this.quantity = Quantity;
    }

    public SubOrder(long Id, long OrderID, long FoodID, int Quantity)
    {
        this(OrderID, FoodID, Quantity);
        this.id = Id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getFoodID() {
        return foodID;
    }

    public void setFoodID(long foodID) {
        this.foodID = foodID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
