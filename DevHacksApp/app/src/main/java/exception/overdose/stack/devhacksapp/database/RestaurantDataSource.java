package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class RestaurantDataSource extends BaseDataSource {

    public RestaurantDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_RESTAURANT_NAME;
    }

    @Override
    public void closeHelper()
    {
        getDbHelper().close();
    }

    public long insertRestaurant(Restaurant restaurant) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getRestaurantContentValues(restaurant);

            long rowId = getDatabase().insert(tableName, null, contentValues);

            restaurant.setId(rowId);
            //insert song to the "all songs" playlist

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public Restaurant getRestaurant(long restaurantId) {
        if (!getDatabase().isOpen()) {
            open();
        }

        Restaurant restaurant = null;
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{
                            String.valueOf(restaurantId)
                    },
                    null, null, null);

            if (cursor.moveToFirst()) {
                restaurant = cursorToRestaurant(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return restaurant;
    }

    public ArrayList<Restaurant> getAllRestaurants() {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Restaurant> songs = new ArrayList<>();
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    songs.add(cursorToRestaurant(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return songs;
    }

//    public ArrayList<Song> getAllSongsFromPlaylist(long playlistId) {
//        if (!getDatabase().isOpen()) {
//            open();
//        }
//
//        ArrayList<Song> songs = new ArrayList<>();
//        try {
//            final String MY_QUERY = "SELECT songs.* FROM " + tableName + " songs " +
//                    "JOIN " + DatabaseHelper.TABLE_CONNECTIONS_NAME + " connections " +
//                    "ON songs." + DatabaseHelper.COLUMN_ID + "=connections." + DatabaseHelper.COLUMN_CONNECTIONS_SONG_ID + " " +
//                    "WHERE connections." + DatabaseHelper.COLUMN_CONNECTIONS_PLAYLISTS_ID + "=? " +
//                    "ORDER BY connections." + DatabaseHelper.COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST;
//            Cursor cursor = getDatabase().rawQuery(MY_QUERY, new String[]{String.valueOf(playlistId)});
//
//            if (cursor.moveToFirst()) {
//                int i = 0;
//                do {
//                    songs.add(cursorToSong(cursor, i));
//                    i++;
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            closeDatabase();
//        }
//
//        return songs;
//    }

//    public boolean updateSong(Song song) {
//        if (!getDatabase().isOpen()) {
//            open();
//        }
//        try {
//            ContentValues contentValues = getRestaurantContentValues(song);
//
//            if (getDatabase().update(tableName, contentValues, DatabaseHelper.COLUMN_ID + " = ? ", new String[]{String.valueOf(song.getId())}) <= 0) {
//                Log.e("database", song.getName() + " not found: " + song.getId());
//                return false;
//            }
//
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            closeDatabase();
//        }
//        return false;
//    }

    private ContentValues getRestaurantContentValues(Restaurant restaurant) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_ID, restaurant.getId());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_NAME, restaurant.getName());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_SPECIFIC, restaurant.getSpecific());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_LOCATION, restaurant.getLocation());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_LONGITUDE, restaurant.getLongitude());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_LATITUDE, restaurant.getLatitude());
        contentValues.put(DatabaseHelper.COLUMN_RESTAURANT_URL,restaurant.getUrl());

        return contentValues;
    }

//    public Integer deleteSong(Song song) {
//        if (!getDatabase().isOpen()) {
//            open();
//        }
//        try {
//            getDatabase().delete(tableName,
//                    DatabaseHelper.COLUMN_ID + " = ? ",
//                    new String[]{String.valueOf(song.getId())});
//
//            //delete song from playlists
//            ArrayList<Playlist> playlists = foodDataSource.getAllPlaylists();
//            for(Playlist playlist : playlists)
//            {
//                subOrderDataSource.deleteSongFromPlaylist(playlist, song);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            closeDatabase();
//        }
//        return -1;
//    }

    public ArrayList<Restaurant> searchRestaurant(String specific)
    {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Restaurant> songs = new ArrayList<>();
        try {
            final String MY_QUERY = "SELECT * FROM " + tableName + " songs " +
                    "WHERE songs." + DatabaseHelper.COLUMN_RESTAURANT_SPECIFIC + " LIKE ?" +
                    "ORDER BY songs." + DatabaseHelper.COLUMN_RESTAURANT_NAME;
            Cursor cursor = getDatabase().rawQuery(MY_QUERY, new String[]{"%" + specific + "%"});

            if (cursor.moveToFirst()) {
                int i = 0;
                do {
                    songs.add(cursorToRestaurant(cursor, i));
                    i++;
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return songs;
    }

    private Restaurant cursorToRestaurant(Cursor cursor)
    {
        return cursorToRestaurant(cursor, 0);
    }

    private Restaurant cursorToRestaurant(Cursor cursor, int orderInPlaylist)
    {
        Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        Integer columnRestaurantNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_NAME);
        Integer columnRestaurantSpecificIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_SPECIFIC);
        Integer columnRestaurantLocationIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_LOCATION);
        Integer columnRestaurantLongitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_LONGITUDE);
        Integer columnRestaurantLatitudeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_LATITUDE);
        Integer columnUrlIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_RESTAURANT_URL);

        long id = cursor.getLong(columnIdIndex);
        String name = cursor.getString(columnRestaurantNameIndex);
        String specific = cursor.getString(columnRestaurantSpecificIndex);
        String location = cursor.getString(columnRestaurantLocationIndex);
        String url = cursor.getString(columnUrlIndex);
        float longitude = cursor.getFloat(columnRestaurantLongitudeIndex);
        float latitude = cursor.getFloat(columnRestaurantLatitudeIndex);

        Restaurant restaurant= new Restaurant(id, name, specific, longitude, latitude, location);
        restaurant.setUrl(url);
        return restaurant;
    }
}
