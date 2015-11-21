package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class SubOrderDataSource extends BaseDataSource {

    public SubOrderDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_SUBORDER_NAME;
    }

    @Override
    public void closeHelper()
    {
        getDbHelper().close();
    }

    public long insertSubOrder(SubOrder subOrder) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            long rowId;
            ContentValues contentValues = getSubOrderContentValues(subOrder);

            rowId = getDatabase().insert(tableName, null, contentValues);

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public ArrayList<SubOrder> getSubOrders(long orderId) {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<SubOrder> subOrders = new ArrayList<>();
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null,
                    DatabaseHelper.COLUMN_SUBORDER_ORDER_ID + " = ?",
                    new String[]{
                            String.valueOf(orderId)
                    },
                    null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    subOrders.add(cursorToSubOrder(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return subOrders;
    }

    public boolean updateSubOrder(SubOrder subOrder) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getSubOrderContentValues(subOrder);

            if (getDatabase().update(tableName, contentValues,
                    DatabaseHelper.COLUMN_SUBORDER_ORDER_ID + " = ? and " +
                            DatabaseHelper.COLUMN_SUBORDER_FOOD_ID + " = ?",
                    new String[]{String.valueOf(subOrder.getOrderID()), String.valueOf(subOrder.getFoodID())}) <= 0) {
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return false;
    }

    private ContentValues getSubOrderContentValues(SubOrder subOrder) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_SUBORDER_FOOD_ID, subOrder.getFoodID());
        contentValues.put(DatabaseHelper.COLUMN_SUBORDER_ORDER_ID, subOrder.getOrderID());
        contentValues.put(DatabaseHelper.COLUMN_SUBORDER_QUANTITY, subOrder.getQuantity());

        return contentValues;
    }

    public Integer deleteSubOrderByOrderId(long orderId) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            int rows = getDatabase().delete(tableName,
                    DatabaseHelper.COLUMN_SUBORDER_ORDER_ID + " = ? ",
                    new String[]{String.valueOf(orderId)});

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public Integer deleteSubOrderById(long subOrderid) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            int rows = getDatabase().delete(tableName,
                    DatabaseHelper.COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(subOrderid)});

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    private SubOrder cursorToSubOrder(Cursor cursor)
    {
        Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        Integer columnFoodIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUBORDER_FOOD_ID);
        Integer columnOrderIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUBORDER_ORDER_ID);
        Integer columnQuantityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SUBORDER_QUANTITY);

        long id = cursor.getLong(columnIdIndex);
        long foodId = cursor.getLong(columnFoodIdIndex);
        long orderId = cursor.getLong(columnOrderIdIndex);
        int quantity = cursor.getInt(columnQuantityIndex);

        return new SubOrder(id, orderId, foodId, quantity);
    }
}
