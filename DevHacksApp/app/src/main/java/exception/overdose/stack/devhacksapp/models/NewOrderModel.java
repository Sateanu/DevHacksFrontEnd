package exception.overdose.stack.devhacksapp.models;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.database.OrderDataSource;
import exception.overdose.stack.devhacksapp.interfaces.mvc.SimpleObservable;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;

/**
 * Created by alexbuicescu on 22.11.2015.
 */
public class NewOrderModel extends SimpleObservable<MainModel> {
    private Orders newOrder;
    private ArrayList<SubOrder> subOrders;
    private OrderDataSource eventDataSource;
    private String restaurantName;

    public ArrayList<SubOrder> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(ArrayList<SubOrder> subOrders, boolean... notifyObservers) {
        this.subOrders = subOrders;
        update(notifyObservers);
    }
    public void update(boolean... notifyObservers) {
        if (notifyObservers.length > 0 && notifyObservers[0]) {
            notifyObservers();
        }
    }
    public OrderDataSource getOrderDataSource() {
        return eventDataSource;
    }

    public void setOrderDataSource(OrderDataSource eventDataSource) {
        this.eventDataSource = eventDataSource;
    }

    public Orders getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Orders newOrder, boolean... notifyObservers) {
        this.newOrder = newOrder;
        update(notifyObservers);
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
