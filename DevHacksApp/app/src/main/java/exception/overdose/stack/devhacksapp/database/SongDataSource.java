package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.indyvision.metronome.pojo.Playlist;
import net.indyvision.metronome.pojo.Song;
import net.indyvision.metronome.utils.Constants;
import net.indyvision.metronome.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class SongDataSource extends BaseDataSource {

    private PlaylistDataSource playlistDataSource;
    private ConnectionsDataSource connectionsDataSource;

    public SongDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_SONGS_NAME;

        playlistDataSource = new PlaylistDataSource(context);
        playlistDataSource.open();
        connectionsDataSource = new ConnectionsDataSource(context);
        connectionsDataSource.open();
    }

    @Override
    public void closeHelper()
    {
        getDbHelper().close();
        playlistDataSource.closeHelper();
        connectionsDataSource.closeHelper();
    }

    public long insertSong(Song song) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getSongContentValues(song);

            long rowId = getDatabase().insert(tableName, null, contentValues);

            song.setId(rowId);
            //insert song to the "all songs" playlist
            connectionsDataSource.insertSongToPlaylist
                    (
                            playlistDataSource.getPlaylist
                                    (
                                            PrefUtils.getLongFromPrefs
                                                    (
                                                            getContext(),
                                                            Constants.PREFS_ALL_SONGS_PLAYLIST_ID,
                                                            0
                                                    )
                                    ),
                            song,
                            0
                    );

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public Song getSong(long songId) {
        if (!getDatabase().isOpen()) {
            open();
        }

        Song song = null;
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{
                            String.valueOf(songId)
                    },
                    null, null, null);

            if (cursor.moveToFirst()) {
                song = cursorToSong(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return song;
    }

    public ArrayList<Song> getAllSongs() {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Song> songs = new ArrayList<>();
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    songs.add(cursorToSong(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return songs;
    }

    public ArrayList<Song> getAllSongsFromPlaylist(long playlistId) {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Song> songs = new ArrayList<>();
        try {
            final String MY_QUERY = "SELECT songs.* FROM " + tableName + " songs " +
                    "JOIN " + DatabaseHelper.TABLE_CONNECTIONS_NAME + " connections " +
                    "ON songs." + DatabaseHelper.COLUMN_ID + "=connections." + DatabaseHelper.COLUMN_CONNECTIONS_SONG_ID + " " +
                    "WHERE connections." + DatabaseHelper.COLUMN_CONNECTIONS_PLAYLISTS_ID + "=? " +
                    "ORDER BY connections." + DatabaseHelper.COLUMN_CONNECTIONS_ORDER_IN_PLAYLIST;
            Cursor cursor = getDatabase().rawQuery(MY_QUERY, new String[]{String.valueOf(playlistId)});

            if (cursor.moveToFirst()) {
                int i = 0;
                do {
                    songs.add(cursorToSong(cursor, i));
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

    public boolean updateSong(Song song) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getSongContentValues(song);

            if (getDatabase().update(tableName, contentValues, DatabaseHelper.COLUMN_ID + " = ? ", new String[]{String.valueOf(song.getId())}) <= 0) {
                Log.e("database", song.getName() + " not found: " + song.getId());
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return false;
    }

    private ContentValues getSongContentValues(Song song) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_SONGS_SONG_NAME, song.getName());
        contentValues.put(DatabaseHelper.COLUMN_SONGS_BEATS_PER_MINUTE, song.getBeatsPerMinute());
        contentValues.put(DatabaseHelper.COLUMN_SONGS_LENGTH, song.getLength());
        contentValues.put(DatabaseHelper.COLUMN_SONGS_NO_OF_FIRST_BEATS, song.getNoOfFirstBeats());
        contentValues.put(DatabaseHelper.COLUMN_SONGS_NO_OF_SECOND_BEATS, song.getNoOfSecondBeats());

        return contentValues;
    }

    public Integer deleteSong(Song song) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            getDatabase().delete(tableName,
                    DatabaseHelper.COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(song.getId())});

            //delete song from playlists
            ArrayList<Playlist> playlists = playlistDataSource.getAllPlaylists();
            for(Playlist playlist : playlists)
            {
                connectionsDataSource.deleteSongFromPlaylist(playlist, song);
            }

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public ArrayList<Song> searchSong(String query)
    {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Song> songs = new ArrayList<>();
        try {
            final String MY_QUERY = "SELECT * FROM " + tableName + " songs " +
                    "WHERE songs." + DatabaseHelper.COLUMN_SONGS_SONG_NAME + " LIKE ?" +
                    "ORDER BY songs." + DatabaseHelper.COLUMN_SONGS_SONG_NAME;
            Cursor cursor = getDatabase().rawQuery(MY_QUERY, new String[]{"%" + query + "%"});

            if (cursor.moveToFirst()) {
                int i = 0;
                do {
                    songs.add(cursorToSong(cursor, i));
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

    private Song cursorToSong(Cursor cursor)
    {
        return cursorToSong(cursor, 0);
    }

    private Song cursorToSong(Cursor cursor, int orderInPlaylist)
    {
        Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        Integer columnSongNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SONGS_SONG_NAME);
        Integer columnBeatsPerMinuteIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SONGS_BEATS_PER_MINUTE);
        Integer columnLengthIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_SONGS_LENGTH);
        Integer columnNoFirstBeats = cursor.getColumnIndex(DatabaseHelper.COLUMN_SONGS_NO_OF_FIRST_BEATS);
        Integer columnNoSecondBeats = cursor.getColumnIndex(DatabaseHelper.COLUMN_SONGS_NO_OF_SECOND_BEATS);

        long id = cursor.getLong(columnIdIndex);
        String songName = cursor.getString(columnSongNameIndex);
        int beatsPerMinute = cursor.getInt(columnBeatsPerMinuteIndex);
        int length = cursor.getInt(columnLengthIndex);
        int noFirstBeats = cursor.getInt(columnNoFirstBeats);
        int noSecondBeats = cursor.getInt(columnNoSecondBeats);

        return new Song(id, beatsPerMinute, length, songName, orderInPlaylist, noFirstBeats, noSecondBeats);
    }
}
