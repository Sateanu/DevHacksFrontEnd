package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.indyvision.metronome.pojo.Playlist;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.models.POJO.Food;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class FoodDataSource extends BaseDataSource {

    private String[] allColumns = {DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_FOOD_NAME,
            DatabaseHelper.COLUMN_FOOD_PRICE,
            DatabaseHelper.COLUMN_FOOD_DESCRIPTION,
            DatabaseHelper.COLUMN_FOOD_RESTAURANTID,
            DatabaseHelper.COLUMN_FOOD_CATEGORY
    };

    public FoodDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_FOOD_NAME;
    }

    public FoodDataSource(Context context, SQLiteDatabase database) {
        super(context);
        tableName = DatabaseHelper.TABLE_FOOD_NAME;
        this.setDatabase(database);
    }

    public long insertFood(Food food) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getFoodContentValues(food);

            long rowId = getDatabase().insert(tableName, null, contentValues);

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

//    public boolean updatePlaylist(Playlist playlist) {
//        if (!getDatabase().isOpen()) {
//            open();
//        }
//        try {
//            ContentValues contentValues = getFoodContentValues(playlist);
//
//            if (getDatabase().update(tableName, contentValues, DatabaseHelper.COLUMN_ID + " = ? ", new String[]{String.valueOf(playlist.getId())}) <= 0) {
//                Log.e("database", playlist.getName() + " not found: " + playlist.getId());
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

    public Food getFood(long foodId) {
        if (!getDatabase().isOpen()) {
            open();
        }

        Food food = null;

        try {
            Cursor cursor = getDatabase().query(tableName,
                    null,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{
                            String.valueOf(foodId)
                    },
                    null, null, null);

            if (cursor.moveToFirst()) {
                food = cursorToFood(cursor);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return food;
    }

//    public long playlistExists(String playlistName) {
//        if (!getDatabase().isOpen()) {
//            open();
//        }
//
//        long playlistID = -1;
//
//        try {
//            Cursor cursor = getDatabase().query(tableName,
//                    null,
//                    DatabaseHelper.COLUMN_FOOD_NAME + " = ?",
//                    new String[]{
//                            playlistName
//                    },
//                    null, null, null);
//
//            Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
//
//            if (cursor.moveToFirst()) {
//                playlistID = cursor.getLong(columnIdIndex);
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            closeDatabase();
//        }
//
//        return playlistID;
//    }

    public ArrayList<Food> getAllFoods() {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Food> foods = new ArrayList<>();
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null, null, null, null, null, DatabaseHelper.COLUMN_ID);

            if (cursor.moveToFirst()) {
                do {
                    foods.add(cursorToFood(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return foods;
    }

    private ContentValues getFoodContentValues(Food food) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_FOOD_NAME, food.getName());
        contentValues.put(DatabaseHelper.COLUMN_FOOD_PRICE, food.getPrice());
        contentValues.put(DatabaseHelper.COLUMN_FOOD_DESCRIPTION, food.getDescription());
        contentValues.put(DatabaseHelper.COLUMN_FOOD_RESTAURANTID, food.getRestaurantID());
        contentValues.put(DatabaseHelper.COLUMN_FOOD_CATEGORY, food.getCategory());

        return contentValues;
    }

//    public Integer deletePlaylist(long playlistId) {
//        if (!getDatabase().isOpen()) {
//            open();
//        }
//        try {
//            getDatabase().delete(tableName,
//                    DatabaseHelper.COLUMN_ID + " = ? ",
//                    new String[]{String.valueOf(playlistId)});
//
//            SubOrderDataSource subOrderDataSource = new SubOrderDataSource(getContext());
//            subOrderDataSource.open();
//            subOrderDataSource.deletePlaylist(playlistId);
//            subOrderDataSource.closeHelper();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            closeDatabase();
//        }
//        return -1;
//    }

    private Food cursorToFood(Cursor cursor) {
        Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        Integer columnFoodName = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_NAME);
        Integer columnFoodPrice = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_PRICE);
        Integer columnFoodDescription = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_DESCRIPTION);
        Integer columnFoodRestaurantId = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_RESTAURANTID);
        Integer columnFoodCategory = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOOD_CATEGORY);

        long id = cursor.getLong(columnIdIndex);
        String foodName = cursor.getString(columnFoodName);
        float foodPrice = cursor.getInt(columnFoodPrice);
        long foodRestaurantId = cursor.getInt(columnFoodRestaurantId);
        String foodCategory = cursor.getString(columnFoodCategory);
        String foodDescription = cursor.getString(columnFoodDescription);

        return new Food(id, foodName, foodPrice, foodRestaurantId, foodCategory,foodDescription);
    }
}
