package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class OrderDataSource extends BaseDataSource {
    private SubOrderDataSource subOrderDataSource;

    public OrderDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_ORDER_NAME;
        subOrderDataSource = new SubOrderDataSource(context);
        subOrderDataSource.open();
    }

    @Override
    public void closeHelper()
    {
        getDbHelper().close();
        subOrderDataSource.closeHelper();
    }

    public long insertOrder(Orders orders) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            long rowId;
            ContentValues contentValues = getOrderContentValues(orders);

            rowId = getDatabase().insert(tableName, null, contentValues);

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public boolean updateOrder(Orders orders) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getOrderContentValues(orders);

            if (getDatabase().update(tableName, contentValues,
                    DatabaseHelper.COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(orders.getId())}) <= 0) {
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return false;
    }
    public Integer deleteOrder(long orderId) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            subOrderDataSource.deleteSubOrderByOrderId(orderId);
            int rows = getDatabase().delete(tableName,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(orderId)});

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }
    public ArrayList<Orders> getAllOrders() {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Orders> orders = new ArrayList<>();
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null, null, null, null, null, DatabaseHelper.COLUMN_ORDER_TIME+" DESC ");

            if (cursor.moveToFirst()) {
                do {
                    orders.add(cursorToOrder(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return orders;
    }
    public Orders getOrder(long orderId) {
        if (!getDatabase().isOpen()) {
            open();
        }

        Orders order = null;

        try {
            Cursor cursor = getDatabase().query(tableName,
                    null,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{
                            String.valueOf(orderId)
                    },
                    null, null, null);

            if (cursor.moveToFirst()) {
                order = cursorToOrder(cursor);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return order;
    }
    private Orders cursorToOrder(Cursor cursor) {
        Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        Integer columnOrderDiscount = cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_DISCOUNT);
        Integer columnOrderPrice = cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_PRICE);
        Integer columnOrderRestaurantId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_RESTAURANTID);
        Integer columnOrderTime = cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_TIME);
        Integer columnOrderUserId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_USER_ID);

        Integer columnRestaurantLocationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_LOCATION);
        Integer columnRestaurantLongitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_LONGITUDE);
        Integer columnRestaurantLatitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_LATITUDE);

        long id = cursor.getLong(columnIdIndex);
        float orderPrice = cursor.getFloat(columnOrderPrice);
        int orderDiscount = cursor.getInt(columnOrderDiscount);
        long orderRestaurantId = cursor.getLong(columnOrderRestaurantId);
        long orderTime = cursor.getLong(columnOrderTime);
        long userId = cursor.getLong(columnOrderUserId);
        String location = cursor.getString(columnRestaurantLocationIndex);
        float longitude = cursor.getFloat(columnRestaurantLongitudeIndex);
        float latitude = cursor.getFloat(columnRestaurantLatitudeIndex);

        Orders order= new Orders(id, orderTime,orderRestaurantId,orderPrice,orderDiscount, userId);
        order.setLocation(location);
        order.setLatitude(latitude);
        order.setLongitude(longitude);
        return order;
    }
    private ContentValues getOrderContentValues(Orders orders) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_ID, orders.getId());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_DISCOUNT, orders.getDiscount());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_PRICE, orders.getPrice());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_RESTAURANTID, orders.getRestaurantID());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_TIME, orders.getTime());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_USER_ID, orders.getUserId());

        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_LOCATION, orders.getLocation());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_LONGITUDE, orders.getLongitude());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_LATITUDE, orders.getLatitude());

        return contentValues;
    }

}
