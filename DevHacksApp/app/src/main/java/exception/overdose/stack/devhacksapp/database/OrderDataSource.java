package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.indyvision.metronome.pojo.Connection;
import net.indyvision.metronome.pojo.Playlist;
import net.indyvision.metronome.pojo.Song;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.models.POJO.Food;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class OrderDataSource extends BaseDataSource {
    private FoodDataSource foodDataSource;

    public OrderDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_ORDER_NAME;
        foodDataSource = new FoodDataSource(context);
        foodDataSource.open();
    }

    @Override
    public void closeHelper()
    {
        getDbHelper().close();
        foodDataSource.closeHelper();
    }

    public long insertSongToPlaylist(Playlist playlist, Song song, int orderInPlaylist) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            long rowId;
            ContentValues contentValues = getOrderContentValues(playlist.getId(), song.getId(), orderInPlaylist);

            rowId = getDatabase().insert(tableName, null, contentValues);

            playlist.setSongsCounter(playlist.getSongsCounter() + 1);
            foodDataSource.updatePlaylist(playlist);

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public ArrayList<Connection> getAllConnections() {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Connection> connections = new ArrayList<>();
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    connections.add(cursorToConnection(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return connections;
    }

    public boolean updateSongInPlaylist(long playlistId, long songId, int newOrderInPlaylist) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getOrderContentValues(playlistId, songId, newOrderInPlaylist);

            if (getDatabase().update(tableName, contentValues,
                    DatabaseHelper.COLUMN_CONNECTIONS_PLAYLISTS_ID + " = ? and " +
                            DatabaseHelper.COLUMN_CONNECTIONS_SONG_ID + " = ?",
                    new String[]{String.valueOf(playlistId), String.valueOf(songId)}) <= 0) {
                Log.e("database", songId + " not found in: " + playlistId);
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return false;
    }

    private ContentValues getOrderContentValues(long playlistId, long songId, int orderInPlaylist) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_CONNECTIONS_PLAYLISTS_ID, playlistId);
        contentValues.put(DatabaseHelper.COLUMN_CONNECTIONS_SONG_ID, songId);
        contentValues.put(DatabaseHelper.COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST, orderInPlaylist);

        return contentValues;
    }


    public Integer deleteSongFromPlaylist(Playlist playlist, Song song) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            int rows = getDatabase().delete(tableName,
                    DatabaseHelper.COLUMN_CONNECTIONS_PLAYLISTS_ID + " = ? and" +
                            DatabaseHelper.COLUMN_CONNECTIONS_SONG_ID + " = ?",
                    new String[]{String.valueOf(playlist.getId()), String.valueOf(song.getId())});

            if(rows > 0) {
                playlist.setSongsCounter(playlist.getSongsCounter() - 1);
                foodDataSource.updatePlaylist(playlist);
            }

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public Integer deleteOrder(long orderId) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            int rows = getDatabase().delete(tableName,
                    DatabaseHelper.COLUMN_CONNECTIONS_PLAYLISTS_ID + " = ?",
                    new String[]{String.valueOf(playlistId)});

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

        long id = cursor.getLong(columnIdIndex);
        float orderPrice = cursor.getFloat(columnOrderPrice);
        int orderDiscount = cursor.getInt(columnOrderDiscount);
        long orderRestaurantId = cursor.getLong(columnOrderRestaurantId);
        long orderTime = cursor.getLong(columnOrderTime);

        return new Orders(id, orderTime,orderRestaurantId,orderPrice,orderDiscount);
    }
    private ContentValues getOrderContentValues(Orders orders) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_ORDER_DISCOUNT, orders.getDiscount());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_PRICE, orders.getPrice());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_RESTAURANTID, orders.getRestaurantID());
        contentValues.put(DatabaseHelper.COLUMN_ORDER_TIME, orders.getTime());

        return contentValues;
    }

}
