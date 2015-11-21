package exception.overdose.stack.devhacksapp.models;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.database.OrderDataSource;
import exception.overdose.stack.devhacksapp.interfaces.mvc.SimpleObservable;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;

/**
 * Created by Adriana on 21/11/2015.
 */
public class OrdersModel  extends SimpleObservable<OrdersModel> {
    private ArrayList<Orders> orders;
    private OrderDataSource eventDataSource;

    public ArrayList<Orders> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Orders> orders, boolean... notifyObservers) {
        this.orders = orders;
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
}
