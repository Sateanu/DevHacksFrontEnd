package exception.overdose.stack.devhacksapp.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by alexbuicescu on 17.09.2015.
 */
public class BaseDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    protected String tableName;
    private Context context;

    public BaseDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void closeHelper() {
        dbHelper.close();
    }

    protected void closeDatabase() {
        database.close();
    }

    protected DatabaseHelper getDbHelper() {
        return dbHelper;
    }

    protected SQLiteDatabase getDatabase() {
        return database;
    }

    protected void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    protected Context getContext() {
        return context;
    }
}
