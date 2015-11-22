package exception.overdose.stack.devhacksapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Alexandru on 21-Mar-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "DatabaseHelper";

    protected static SQLiteDatabase mydb;
    public static final String DATABASE_NAME = "metronome.db";

    public static final String COLUMN_ID = "id";


    /**
     * food table
     */
    public static final String TABLE_FOOD_NAME = "food";
    public static final String COLUMN_FOOD_NAME = "COLUMN_FOOD_NAME";
    public static final String COLUMN_FOOD_PRICE = "COLUMN_FOOD_PRICE";
    public static final String COLUMN_FOOD_DESCRIPTION = "COLUMN_FOOD_DESCRIPTION";
    public static final String COLUMN_FOOD_RESTAURANTID = "COLUMN_FOOD_RESTAURANTID";
    public static final String COLUMN_FOOD_CATEGORY = "COLUMN_FOOD_CATEGORY";

    /**
     * restaurant table
     */
    public static final String TABLE_RESTAURANT_NAME = "restaurant";
    public static final String COLUMN_RESTAURANT_NAME = "COLUMN_RESTAURANT_NAME";
    public static final String COLUMN_RESTAURANT_SPECIFIC = "COLUMN_RESTAURANT_SPECIFIC";
    public static final String COLUMN_RESTAURANT_LONGITUDE = "COLUMN_RESTAURANT_LONGITUDE";
    public static final String COLUMN_RESTAURANT_LATITUDE = "COLUMN_RESTAURANT_LATITUDE";
    public static final String COLUMN_RESTAURANT_LOCATION = "COLUMN_RESTAURANT_LOCATION";
    public static final String COLUMN_RESTAURANT_URL="COLUMN_RESTAURANT_URL";

  
    /**
     * orders table
     */
    public static final String TABLE_ORDER_NAME="orders";
    public static final String COLUMN_ORDER_TIME="COLUMN_ORDER_TIME";
    public static final String COLUMN_ORDER_RESTAURANTID="COLUMN_ORDER_RESTAURANTID";
    public static final String COLUMN_ORDER_PRICE="COLUMN_ORDER_PRICE";
    public static final String COLUMN_ORDER_DISCOUNT="COLUMN_ORDER_DISCOUNT";
    public static final String COLUMN_ORDER_USER_ID="COLUMN_ORDER_USER_ID";

    
    /**
     * suborders table
     */
    public static final String TABLE_SUBORDER_NAME = "suborder";
    public static final String COLUMN_SUBORDER_ORDER_ID = "COLUMN_SUBORDER_ORDER_ID";
    public static final String COLUMN_SUBORDER_FOOD_ID = "COLUMN_SUBORDER_FOOD_ID";
    public static final String COLUMN_SUBORDER_QUANTITY = "COLUMN_SUBORDER_QUANTITY";


    private final Context context;



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
//        initDB();
    }

    private void initDB() {
        mydb = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + TABLE_FOOD_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_FOOD_NAME + " text, " +
                        COLUMN_FOOD_PRICE + " real," +
                        COLUMN_FOOD_RESTAURANTID + " integer," +
                        COLUMN_FOOD_CATEGORY+" text, "+
                        COLUMN_FOOD_DESCRIPTION + " text)"
        );

        db.execSQL(
                "create table " + TABLE_RESTAURANT_NAME + " (" +
                        COLUMN_ID + " integer, " +
                        COLUMN_RESTAURANT_NAME + " text, " +
                        COLUMN_RESTAURANT_SPECIFIC + " text, " +
                        COLUMN_RESTAURANT_LOCATION + " text, " +
                        COLUMN_RESTAURANT_URL + " text, " +
                        COLUMN_RESTAURANT_LONGITUDE + " real, " +
                        COLUMN_RESTAURANT_LATITUDE + " real)"
        );

        db.execSQL(
                "create table " + TABLE_SUBORDER_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_SUBORDER_FOOD_ID + " integer," +
                        COLUMN_SUBORDER_ORDER_ID + " integer," +
                        COLUMN_SUBORDER_QUANTITY + " integer)"
        );

        db.execSQL(
                "create table " + TABLE_ORDER_NAME + " (" +
                        COLUMN_ID + " integer, " +
                        COLUMN_ORDER_PRICE + " real," +
                        COLUMN_ORDER_TIME + " integer," +
                        COLUMN_ORDER_RESTAURANTID + " integer," +
                        COLUMN_ORDER_DISCOUNT + " integer," +
                        COLUMN_ORDER_USER_ID + " integer,"+
                        COLUMN_RESTAURANT_LOCATION + " text, " +
                        COLUMN_RESTAURANT_LONGITUDE + " real, " +
                        COLUMN_RESTAURANT_LATITUDE + " real)"
        );

        mydb = db;
//        addAllSongsPlaylist();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_NAME);

        
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBORDER_NAME);

        onCreate(db);
    }

    public void dropDatabase() {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            // TODO Auto-generated method stub
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANT_NAME);
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_NAME);


            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_NAME);

            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBORDER_NAME);


            onCreate(mydb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    public long insertFood(Playlist playlist) {
//
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            ContentValues contentValues = getPlaylistContentValues(playlist);
//
//            long rowId = mydb.insert(TABLE_FOOD_NAME, null, contentValues);
//
//            return rowId;
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return -1;
//    }
//
//    public boolean updatePlaylist(Playlist playlist) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            ContentValues contentValues = getPlaylistContentValues(playlist);
//
//            if (mydb.update(TABLE_FOOD_NAME, contentValues, COLUMN_ID + " = ? ", new String[]{String.valueOf(playlist.getId())}) <= 0) {
//                Log.e("database", playlist.getName() + " not found: " + playlist.getId());
//                return false;
//            }
//
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return false;
//    }
//
//    public Playlist getFood(long playlistId) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        try {
//            Cursor cursor = mydb.query(TABLE_FOOD_NAME,
//                    null,
//                    COLUMN_ID + " = ?",
//                    new String[]{
//                            String.valueOf(playlistId)
//                    },
//                    null, null, null);
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//            Integer columnPlaylistNameIndex = cursor.getColumnIndex(COLUMN_FOOD_NAME);
//            Integer columnPlaylistOrderIndex = cursor.getColumnIndex(COLUMN_FOOD_PRICE);
//            Integer columnPlaylistSongsCounterIndex = cursor.getColumnIndex(COLUMN_FOOD_DESCRIPTION);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    long id = cursor.getLong(columnIdIndex);
//                    String playlistName = cursor.getString(columnPlaylistNameIndex);
//                    int playlistOrder = cursor.getInt(columnPlaylistOrderIndex);
//                    int songsCounter = cursor.getInt(columnPlaylistSongsCounterIndex);
//                    cursor.close();
//
//                    return new Playlist(id, playlistName, playlistOrder, songsCounter);
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//
//        return null;
//    }
//
//    public long playlistExists(String playlistName) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        try {
//            Cursor cursor = mydb.query(TABLE_FOOD_NAME,
//                    null,
//                    COLUMN_FOOD_NAME + " = ?",
//                    new String[]{
//                            playlistName
//                    },
//                    null, null, null);
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    long id = cursor.getLong(columnIdIndex);
//                    cursor.close();
//
//                    return id;
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//
//        return -1;
//    }
//
//    public ArrayList<Playlist> getAllFoods() {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        ArrayList<Playlist> playlists = new ArrayList<>();
//        try {
//            Cursor cursor = mydb.query(TABLE_FOOD_NAME,
//                    null, null, null, null, null, COLUMN_FOOD_PRICE);
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//            Integer columnPlaylistNameIndex = cursor.getColumnIndex(COLUMN_FOOD_NAME);
//            Integer columnPlaylistOrderIndex = cursor.getColumnIndex(COLUMN_FOOD_PRICE);
//            Integer columnPlaylistSongsCounterIndex = cursor.getColumnIndex(COLUMN_FOOD_DESCRIPTION);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    long id = cursor.getLong(columnIdIndex);
//                    String playlistName = cursor.getString(columnPlaylistNameIndex);
//                    int playlistOrder = cursor.getInt(columnPlaylistOrderIndex);
//                    int songsCounter = cursor.getInt(columnPlaylistSongsCounterIndex);
//
//                    playlists.add(new Playlist(id, playlistName, playlistOrder, songsCounter));
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//
//        return playlists;
//    }
//
//    private ContentValues getPlaylistContentValues(Playlist playlist) {
//        ContentValues contentValues = new ContentValues();
//
////        contentValues.put(COLUMN_ID, playlist.getId());
//        contentValues.put(COLUMN_FOOD_NAME, playlist.getName());
//        contentValues.put(COLUMN_FOOD_PRICE, playlist.getOrder());
//        contentValues.put(COLUMN_FOOD_DESCRIPTION, playlist.getSongsCounter());
//
//        return contentValues;
//    }
//
//    public Integer deletePlaylist(Playlist playlist) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            mydb.delete(TABLE_FOOD_NAME,
//                    COLUMN_ID + " = ? ",
//                    new String[]{String.valueOf(playlist.getId())});
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return -1;
//    }
//
//    public long insertSongToPlaylist(Playlist playlist, Song song, int orderInPlaylist) {
//
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            long rowId;
//            ContentValues contentValues = getConnectionContentValues(playlist.getId(), song.getId(), orderInPlaylist);
//
//            rowId = mydb.insert(TABLE_CONNECTIONS_NAME, null, contentValues);
//
//            playlist.setSongsCounter(playlist.getSongsCounter() + 1);
//            updatePlaylist(playlist);
//
//            return rowId;
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return -1;
//    }
//
//    public boolean updateSongInPlaylist(long playlistId, long songId, int newOrderInPlaylist) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            ContentValues contentValues = getConnectionContentValues(playlistId, songId, newOrderInPlaylist);
//
//            if (mydb.update(TABLE_CONNECTIONS_NAME, contentValues,
//                    COLUMN_CONNECTIONS_PLAYLISTS_ID + " = ? and " +
//                            COLUMN_CONNECTIONS_SONG_ID + " = ?",
//                    new String[]{String.valueOf(playlistId), String.valueOf(songId)}) <= 0) {
//                Log.e("database", songId + " not found in: " + playlistId);
//                return false;
//            }
//
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return false;
//    }
//
//    private ContentValues getConnectionContentValues(long playlistId, long songId, int orderInPlaylist) {
//        ContentValues contentValues = new ContentValues();
//
//        contentValues.put(COLUMN_CONNECTIONS_PLAYLISTS_ID, playlistId);
//        contentValues.put(COLUMN_CONNECTIONS_SONG_ID, songId);
//        contentValues.put(COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST, orderInPlaylist);
//
//        return contentValues;
//    }
//
//    public Integer deleteSongFromPlaylist(Playlist playlist, Song song) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            int rows = mydb.delete(TABLE_CONNECTIONS_NAME,
//                    COLUMN_CONNECTIONS_PLAYLISTS_ID + " = ? and" +
//                            COLUMN_CONNECTIONS_SONG_ID + " = ? and",
//                    new String[]{String.valueOf(playlist.getId()), String.valueOf(song.getId())});
//
//            if(rows > 0) {
//                playlist.setSongsCounter(playlist.getSongsCounter() - 1);
//                updatePlaylist(playlist);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return -1;
//    }
//
//    public long insertSong(Song song) {
//
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            ContentValues contentValues = getSongContentValues(song);
//
//            long rowId = mydb.insert(TABLE_SONGS_NAME, null, contentValues);
//
//            song.setId(rowId);
//            //insert song to the "all songs" playlist
//            insertSongToPlaylist(getFood(PrefUtils.getLongFromPrefs(context, Constants.PREFS_ALL_SONGS_PLAYLIST_ID, 0)), song, 0);
//
//            return rowId;
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return -1;
//    }
//
//    public Song getSong(long songId) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        try {
//            Cursor cursor = mydb.query(TABLE_SONGS_NAME,
//                    null,
//                    COLUMN_ID + " = ?",
//                    new String[]{
//                            String.valueOf(songId)
//                    },
//                    null, null, null);
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//            Integer columnSongNameIndex = cursor.getColumnIndex(COLUMN_SONGS_SONG_NAME);
//            Integer columnBeatsPerMinuteIndex = cursor.getColumnIndex(COLUMN_SONGS_BEATS_PER_MINUTE);
//            Integer columnLengthIndex = cursor.getColumnIndex(COLUMN_SONGS_LENGTH);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    long id = cursor.getLong(columnIdIndex);
//                    String songName = cursor.getString(columnSongNameIndex);
//                    int beatsPerMinute = cursor.getInt(columnBeatsPerMinuteIndex);
//                    int length = cursor.getInt(columnLengthIndex);
//                    cursor.close();
//
//                    return new Song(id, beatsPerMinute, length, songName);
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//
//        return null;
//    }
//
//    public ArrayList<Song> getAllSongs() {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        ArrayList<Song> songs = new ArrayList<>();
//        try {
//            Cursor cursor = mydb.query(TABLE_SONGS_NAME,
//                    null, null, null, null, null, null);
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//            Integer columnSongNameIndex = cursor.getColumnIndex(COLUMN_SONGS_SONG_NAME);
//            Integer columnTimeBetweenBeatsIndex = cursor.getColumnIndex(COLUMN_SONGS_BEATS_PER_MINUTE);
//            Integer columnLengthIndex = cursor.getColumnIndex(COLUMN_SONGS_LENGTH);
//
//            if (cursor.moveToFirst()) {
//                do {
//                    long id = cursor.getLong(columnIdIndex);
//                    String songName = cursor.getString(columnSongNameIndex);
//                    int timeBetweenBeats = cursor.getInt(columnTimeBetweenBeatsIndex);
//                    int length = cursor.getInt(columnLengthIndex);
//
//                    songs.add(new Song(id, timeBetweenBeats, length, songName));
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//
//        return songs;
//    }
//
//    public ArrayList<Song> getAllSongsFromPlaylist(long playlistId) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        ArrayList<Song> songs = new ArrayList<>();
//        try {
////            final String MY_QUERY = "SELECT * FROM " + TABLE_SONGS_NAME + " songs " +
////                    "JOIN " + TABLE_CONNECTIONS_NAME + " connections " +
////                    "ON songs." + COLUMN_ID + "=connections." + COLUMN_CONNECTIONS_SONG_ID + " " +
////                    "WHERE connections." + COLUMN_CONNECTIONS_PLAYLISTS_ID + "=? " +
////                    "ORDER BY connections." + COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST;
//            final String MY_QUERY = "SELECT * FROM " + TABLE_SONGS_NAME + " songs " +
//                    "JOIN " + TABLE_CONNECTIONS_NAME + " connections " +
//                    "ON songs." + COLUMN_ID + "=connections." + COLUMN_CONNECTIONS_SONG_ID + " " +
//                    "WHERE connections." + COLUMN_CONNECTIONS_PLAYLISTS_ID + "=? " +
//                    "ORDER BY connections." + COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST;
//            Cursor cursor = mydb.rawQuery(MY_QUERY, new String[]{String.valueOf(playlistId)});
//
////            Cursor cursor = mydb.rawQuery(MY_QUERY, new String[]{String.valueOf(propertyId)});
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//            Integer columnSongNameIndex = cursor.getColumnIndex(COLUMN_SONGS_SONG_NAME);
//            Integer columnTimeBetweenBeatsIndex = cursor.getColumnIndex(COLUMN_SONGS_BEATS_PER_MINUTE);
//            Integer columnLengthIndex = cursor.getColumnIndex(COLUMN_SONGS_LENGTH);
//
//            if (cursor.moveToFirst()) {
//                int i = 0;
//                do {
//                    long id = cursor.getLong(columnIdIndex);
//                    String songName = cursor.getString(columnSongNameIndex);
//                    int timeBetweenBeats = cursor.getInt(columnTimeBetweenBeatsIndex);
//                    int length = cursor.getInt(columnLengthIndex);
//
//                    songs.add(new Song(id, timeBetweenBeats, length, songName, i));
//                    i++;
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//
//        return songs;
//    }
//
//    public boolean updateSong(long songId, Song song) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            ContentValues contentValues = getSongContentValues(song);
//
//            if (mydb.update(TABLE_SONGS_NAME, contentValues, COLUMN_ID + " = ? ", new String[]{String.valueOf(songId)}) <= 0) {
//                Log.e("database", song.getName() + " not found: " + songId);
//                return false;
//            }
//
//            return true;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return false;
//    }
//
//    private ContentValues getSongContentValues(Song song) {
//        ContentValues contentValues = new ContentValues();
//
////        contentValues.put(COLUMN_ID, song.getId());
//        contentValues.put(COLUMN_SONGS_SONG_NAME, song.getName());
//        contentValues.put(COLUMN_SONGS_BEATS_PER_MINUTE, song.getBeatsPerMinute());
//        contentValues.put(COLUMN_SONGS_LENGTH, song.getLength());
//        contentValues.put(COLUMN_SONGS_NO_OF_FIRST_BEATS, song.getNoOfFirstBeats());
//        contentValues.put(COLUMN_SONGS_NO_OF_SECOND_BEATS, song.getNoOfSecondBeats());
//
//        return contentValues;
//    }
//
//    public Integer deleteSong(Song song) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            mydb.delete(TABLE_SONGS_NAME,
//                    COLUMN_ID + " = ? ",
//                    new String[]{String.valueOf(song.getId())});
//
//            //delete song from playlists
//            ArrayList<Playlist> playlists = getAllFoods();
//            for(Playlist playlist : playlists)
//            {
//                deleteSongFromPlaylist(playlist, song);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            mydb.close();
//        }
//        return -1;
//    }

//    private void addAllSongsPlaylist()
//    {
//        FoodDataSource foodDataSource = new FoodDataSource(context, mydb);
//        long allSongsId = foodDataSource.insertPlaylist(new Playlist(0, "All Songs", 0, 0, context.getResources().getColor(R.color.colorAccentDark)));
//        PrefUtils.setLongToPrefs(context, Constants.PREFS_ALL_SONGS_PLAYLIST_ID, allSongsId);
//        foodDataSource.closeHelper();
//    }
}
