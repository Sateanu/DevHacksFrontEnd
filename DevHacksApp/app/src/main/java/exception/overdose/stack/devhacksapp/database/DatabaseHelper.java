package exception.overdose.stack.devhacksapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.indyvision.metronome.R;
import net.indyvision.metronome.pojo.Playlist;
import net.indyvision.metronome.utils.Constants;
import net.indyvision.metronome.utils.PrefUtils;


/**
 * Created by Alexandru on 21-Mar-15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "DatabaseHelper";

    protected static SQLiteDatabase mydb;
    public static final String DATABASE_NAME = "metronome.db";

    public static final String COLUMN_ID = "id";

    /**
     * playlists table
     */
    public static final String TABLE_PLAYLISTS_NAME = "playlists";
    public static final String COLUMN_PLAYLISTS_NAME = "COLUMN_PLAYLISTS_NAME";
    public static final String COLUMN_PLAYLISTS_ORDER = "COLUMN_PLAYLISTS_ORDER";
    public static final String COLUMN_PLAYLISTS_SONGS_COUNTER = "COLUMN_PLAYLISTS_SONGS_COUNTER";
    public static final String COLUMN_PLAYLISTS_BACKGROUND_COLOR = "COLUMN_PLAYLISTS_BACKGROUND_COLOR";

    /**
     * connections table
     */
    public static final String TABLE_CONNECTIONS_NAME = "connections";
    public static final String COLUMN_CONNECTIONS_PLAYLISTS_ID = "COLUMN_PLAYLISTS_ID";
    public static final String COLUMN_CONNECTIONS_SONG_ID = "COLUMN_SONG_ID";
    public static final String COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST = "COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST";

    /**
     * songs table
     */
    public static final String TABLE_SONGS_NAME = "songs";
    public static final String COLUMN_SONGS_SONG_NAME = "COLUMN_SONGS_SONG_NAME";
    public static final String COLUMN_SONGS_LENGTH = "COLUMN_SONGS_LENGTH";
    public static final String COLUMN_SONGS_BEATS_PER_MINUTE = "COLUMN_SONGS_BEATS_PER_MINUTE";
    public static final String COLUMN_SONGS_NO_OF_FIRST_BEATS = "COLUMN_SONGS_NO_OF_FIRST_BEATS";
    public static final String COLUMN_SONGS_NO_OF_SECOND_BEATS = "COLUMN_SONGS_NO_OF_SECOND_BEATS";
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
                "create table " + TABLE_PLAYLISTS_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_PLAYLISTS_NAME + " text, " +
                        COLUMN_PLAYLISTS_ORDER + " integer," +
                        COLUMN_PLAYLISTS_BACKGROUND_COLOR + " integer," +
                        COLUMN_PLAYLISTS_SONGS_COUNTER + " integer)"
        );

        db.execSQL(
                "create table " + TABLE_CONNECTIONS_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_CONNECTIONS_PLAYLISTS_ID + " integer," +
                        COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST + " integer," +
                        COLUMN_CONNECTIONS_SONG_ID + " integer)"
        );

        db.execSQL(
                "create table " + TABLE_SONGS_NAME + " (" +
                        COLUMN_ID + " integer primary key, " +
                        COLUMN_SONGS_BEATS_PER_MINUTE + " integer," +
                        COLUMN_SONGS_LENGTH + " integer," +
                        COLUMN_SONGS_NO_OF_FIRST_BEATS + " integer," +
                        COLUMN_SONGS_NO_OF_SECOND_BEATS + " integer," +
                        COLUMN_SONGS_SONG_NAME + " text)"
        );

        mydb = db;
        addAllSongsPlaylist();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS_NAME);
        onCreate(db);
    }

    public void dropDatabase() {
        if (!mydb.isOpen()) {
            initDB();
        }
        try {
            // TODO Auto-generated method stub
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS_NAME);
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_CONNECTIONS_NAME);
            mydb.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS_NAME);

            onCreate(mydb);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

//    public long insertPlaylist(Playlist playlist) {
//
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            ContentValues contentValues = getPlaylistContentValues(playlist);
//
//            long rowId = mydb.insert(TABLE_PLAYLISTS_NAME, null, contentValues);
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
//            if (mydb.update(TABLE_PLAYLISTS_NAME, contentValues, COLUMN_ID + " = ? ", new String[]{String.valueOf(playlist.getId())}) <= 0) {
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
//    public Playlist getPlaylist(long playlistId) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        try {
//            Cursor cursor = mydb.query(TABLE_PLAYLISTS_NAME,
//                    null,
//                    COLUMN_ID + " = ?",
//                    new String[]{
//                            String.valueOf(playlistId)
//                    },
//                    null, null, null);
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//            Integer columnPlaylistNameIndex = cursor.getColumnIndex(COLUMN_PLAYLISTS_NAME);
//            Integer columnPlaylistOrderIndex = cursor.getColumnIndex(COLUMN_PLAYLISTS_ORDER);
//            Integer columnPlaylistSongsCounterIndex = cursor.getColumnIndex(COLUMN_PLAYLISTS_SONGS_COUNTER);
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
//            Cursor cursor = mydb.query(TABLE_PLAYLISTS_NAME,
//                    null,
//                    COLUMN_PLAYLISTS_NAME + " = ?",
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
//    public ArrayList<Playlist> getAllPlaylists() {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//
//        ArrayList<Playlist> playlists = new ArrayList<>();
//        try {
//            Cursor cursor = mydb.query(TABLE_PLAYLISTS_NAME,
//                    null, null, null, null, null, COLUMN_PLAYLISTS_ORDER);
//
//            Integer columnIdIndex = cursor.getColumnIndex(COLUMN_ID);
//            Integer columnPlaylistNameIndex = cursor.getColumnIndex(COLUMN_PLAYLISTS_NAME);
//            Integer columnPlaylistOrderIndex = cursor.getColumnIndex(COLUMN_PLAYLISTS_ORDER);
//            Integer columnPlaylistSongsCounterIndex = cursor.getColumnIndex(COLUMN_PLAYLISTS_SONGS_COUNTER);
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
//        contentValues.put(COLUMN_PLAYLISTS_NAME, playlist.getName());
//        contentValues.put(COLUMN_PLAYLISTS_ORDER, playlist.getOrder());
//        contentValues.put(COLUMN_PLAYLISTS_SONGS_COUNTER, playlist.getSongsCounter());
//
//        return contentValues;
//    }
//
//    public Integer deletePlaylist(Playlist playlist) {
//        if (!mydb.isOpen()) {
//            initDB();
//        }
//        try {
//            mydb.delete(TABLE_PLAYLISTS_NAME,
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
//            insertSongToPlaylist(getPlaylist(PrefUtils.getLongFromPrefs(context, Constants.PREFS_ALL_SONGS_PLAYLIST_ID, 0)), song, 0);
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
//            ArrayList<Playlist> playlists = getAllPlaylists();
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

    private void addAllSongsPlaylist()
    {
        PlaylistDataSource playlistDataSource = new PlaylistDataSource(context, mydb);
        long allSongsId = playlistDataSource.insertPlaylist(new Playlist(0, "All Songs", 0, 0, context.getResources().getColor(R.color.colorAccentDark)));
        PrefUtils.setLongToPrefs(context, Constants.PREFS_ALL_SONGS_PLAYLIST_ID, allSongsId);
        playlistDataSource.closeHelper();
    }
}
