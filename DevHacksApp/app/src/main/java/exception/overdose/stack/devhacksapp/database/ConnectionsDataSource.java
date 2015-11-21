package exception.overdose.stack.devhacksapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.indyvision.metronome.pojo.Connection;
import net.indyvision.metronome.pojo.Playlist;
import net.indyvision.metronome.pojo.Song;

import java.util.ArrayList;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class ConnectionsDataSource extends BaseDataSource {
    private String[] allColumns = {DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_PLAYLISTS_NAME,
            DatabaseHelper.COLUMN_PLAYLISTS_ORDER,
            DatabaseHelper.COLUMN_PLAYLISTS_SONGS_COUNTER,
    };

    private PlaylistDataSource playlistDataSource;

    public ConnectionsDataSource(Context context) {
        super(context);
        tableName = DatabaseHelper.TABLE_CONNECTIONS_NAME;
        playlistDataSource = new PlaylistDataSource(context);
        playlistDataSource.open();
    }

    @Override
    public void closeHelper()
    {
        getDbHelper().close();
        playlistDataSource.closeHelper();
    }

    public long insertSongToPlaylist(Playlist playlist, Song song, int orderInPlaylist) {

        if (!getDatabase().isOpen()) {
            open();
        }
        try {
            long rowId;
            ContentValues contentValues = getConnectionContentValues(playlist.getId(), song.getId(), orderInPlaylist);

            rowId = getDatabase().insert(tableName, null, contentValues);

            playlist.setSongsCounter(playlist.getSongsCounter() + 1);
            playlistDataSource.updatePlaylist(playlist);

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
            ContentValues contentValues = getConnectionContentValues(playlistId, songId, newOrderInPlaylist);

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

    private ContentValues getConnectionContentValues(long playlistId, long songId, int orderInPlaylist) {
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
                playlistDataSource.updatePlaylist(playlist);
            }

        } catch (Exception e) {
            e.printStackTrace();
            closeDatabase();
        }
        return -1;
    }

    public Integer deletePlaylist(long playlistId) {
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

    private Connection cursorToConnection(Cursor cursor)
    {
        Integer columnIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        Integer columnPlaylistIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONNECTIONS_PLAYLISTS_ID);
        Integer columnSongIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CONNECTIONS_SONG_ID);

        long id = cursor.getLong(columnIdIndex);
        long playlistID = cursor.getLong(columnPlaylistIdIndex);
        long songID = cursor.getLong(columnSongIdIndex);

        return new Connection(id, playlistID, songID);
    }
}
