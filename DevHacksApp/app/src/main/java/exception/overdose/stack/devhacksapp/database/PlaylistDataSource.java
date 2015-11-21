package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.indyvision.metronome.pojo.Playlist;

import java.util.ArrayList;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class PlaylistDataSource extends BaseDataSource {

    private String[] allColumns = {DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_PLAYLISTS_NAME,
            DatabaseHelper.COLUMN_PLAYLISTS_ORDER,
            DatabaseHelper.COLUMN_PLAYLISTS_SONGS_COUNTER,
            DatabaseHelper.COLUMN_PLAYLISTS_BACKGROUND_COLOR,
    };

    public PlaylistDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_PLAYLISTS_NAME;
    }

    public PlaylistDataSource(Context context, SQLiteDatabase database) {
        super(context);
        tableName = DatabaseHelper.TABLE_PLAYLISTS_NAME;
        this.setDatabase(database);
    }

    public long insertPlaylist(Playlist playlist) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getPlaylistContentValues(playlist);

            long rowId = getDatabase().insert(tableName, null, contentValues);

            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public boolean updatePlaylist(Playlist playlist) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            ContentValues contentValues = getPlaylistContentValues(playlist);

            if (getDatabase().update(tableName, contentValues, DatabaseHelper.COLUMN_ID + " = ? ", new String[]{String.valueOf(playlist.getId())}) <= 0) {
                Log.e("database", playlist.getName() + " not found: " + playlist.getId());
                return false;
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return false;
    }

    public Playlist getPlaylist(long playlistId) {
        if (!getDatabase().isOpen()) {
            open();
        }

        Playlist playlist = null;

        try {
            Cursor cursor = getDatabase().query(tableName,
                    null,
                    DatabaseHelper.COLUMN_ID + " = ?",
                    new String[]{
                            String.valueOf(playlistId)
                    },
                    null, null, null);

            if (cursor.moveToFirst()) {
                playlist = cursorToPlaylist(cursor);
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return playlist;
    }

    public long playlistExists(String playlistName) {
        if (!getDatabase().isOpen()) {
            open();
        }

        long playlistID = -1;

        try {
            Cursor cursor = getDatabase().query(tableName,
                    null,
                    DatabaseHelper.COLUMN_PLAYLISTS_NAME + " = ?",
                    new String[]{
                            playlistName
                    },
                    null, null, null);

            Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);

            if (cursor.moveToFirst()) {
                playlistID = cursor.getLong(columnIdIndex);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return playlistID;
    }

    public ArrayList<Playlist> getAllPlaylists() {
        if (!getDatabase().isOpen()) {
            open();
        }

        ArrayList<Playlist> playlists = new ArrayList<>();
        try {
            Cursor cursor = getDatabase().query(tableName,
                    null, null, null, null, null, DatabaseHelper.COLUMN_PLAYLISTS_ORDER);

            if (cursor.moveToFirst()) {
                do {
                    playlists.add(cursorToPlaylist(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }

        return playlists;
    }

    private ContentValues getPlaylistContentValues(Playlist playlist) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COLUMN_PLAYLISTS_NAME, playlist.getName());
        contentValues.put(DatabaseHelper.COLUMN_PLAYLISTS_ORDER, playlist.getOrder());
        contentValues.put(DatabaseHelper.COLUMN_PLAYLISTS_SONGS_COUNTER, playlist.getSongsCounter());
        contentValues.put(DatabaseHelper.COLUMN_PLAYLISTS_BACKGROUND_COLOR, playlist.getBackgroundColor());

        return contentValues;
    }

    public Integer deletePlaylist(long playlistId) {
        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            getDatabase().delete(tableName,
                    DatabaseHelper.COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(playlistId)});

            ConnectionsDataSource connectionsDataSource = new ConnectionsDataSource(getContext());
            connectionsDataSource.open();
            connectionsDataSource.deletePlaylist(playlistId);
            connectionsDataSource.closeHelper();

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    private Playlist cursorToPlaylist(Cursor cursor) {
        Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        Integer columnPlaylistNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYLISTS_NAME);
        Integer columnPlaylistOrderIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYLISTS_ORDER);
        Integer columnPlaylistSongsCounterIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYLISTS_SONGS_COUNTER);
        Integer columnPlaylistBackgroundColorIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PLAYLISTS_BACKGROUND_COLOR);

        long id = cursor.getLong(columnIdIndex);
        String playlistName = cursor.getString(columnPlaylistNameIndex);
        int playlistOrder = cursor.getInt(columnPlaylistOrderIndex);
        int songsCounter = cursor.getInt(columnPlaylistSongsCounterIndex);
        int backgroundColor = cursor.getInt(columnPlaylistBackgroundColorIndex);

        return new Playlist(id, playlistName, playlistOrder, songsCounter, backgroundColor);
    }
}
