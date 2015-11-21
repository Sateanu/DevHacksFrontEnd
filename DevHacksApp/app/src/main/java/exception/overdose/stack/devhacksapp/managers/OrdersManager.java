package exception.overdose.stack.devhacksapp.managers;

import android.content.Context;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.database.OrderDataSource;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.database.SubOrderDataSource;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;
import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class OrdersManager {

    private static OrdersManager instance;
    private Context context;

    private ArrayList<Orders> orderses;
    OrderDataSource orderDataSource;

    private OrdersManager(Context context)
    {
//        orderses = new ArrayList<>();

        orderDataSource = new OrderDataSource(context);
        orderDataSource.open();

        orderses = orderDataSource.getAllOrders();

//        orderDataSource.closeHelper();
    }

    public static OrdersManager getOrdersManager(Context context)
    {
        if(instance == null)
        {
            instance = new OrdersManager(context);
        }
        instance.context = context;
        return instance;
    }

    public static OrdersManager getOrdersManager()
    {
        return instance;
    }

    public ArrayList<Orders> getOrderses()
    {
        return orderses;
    }

    public void addOrder(Orders orders)
    {
        orderses.add(orders);

        orderDataSource.insertOrder(orders);
    }

    public void setOrderses(ArrayList<Orders> orderses)
    {
        this.orderses = orderses;
    }

    public void insertSubOrders(long orderId, ArrayList<SubOrder> subOrders)
    {
        SubOrderDataSource subOrderDataSource = new SubOrderDataSource(context);
        subOrderDataSource.open();

        for(SubOrder subOrder : subOrders)
        {
            subOrder.setOrderID(orderId);
            subOrderDataSource.insertSubOrder(subOrder);
        }

        subOrderDataSource.closeHelper();

    }

}
