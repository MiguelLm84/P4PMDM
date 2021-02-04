package com.miguel_lm.appjsondata.modelo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

public class DBManagerRoom {

    @Database(entities = {Posts.class, Users.class}, version = 1, exportSchema = false)
    public static abstract class AppDatabase extends RoomDatabase {
        public abstract JsonDAO getJsonDao();
    }
}
