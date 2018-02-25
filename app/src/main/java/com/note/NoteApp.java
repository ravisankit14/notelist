package com.note;

import android.app.Application;

import com.note.database.db.DaoMaster2;
import com.note.database.db.DaoSession2;

import org.greenrobot.greendao.database.Database;

/**
 * Created by ankit on 24/02/18.
 */

public class NoteApp extends Application {

    public static final boolean ENCRYPTED = true;
    private DaoSession2 daoSession2;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster2.DevOpenHelper helper = new DaoMaster2.DevOpenHelper(this,"note-db"); //The users-db here is the name of our database.
        Database db = helper.getWritableDb();
        daoSession2 = new DaoMaster2(db).newSession();
    }

    public DaoSession2 getDaoSession2() {
        return daoSession2;
    }

}
