package com.example.rossellamorgante.todo.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    static AppDatabase INSTANCE;
    private static String DATABASE_NAME ="database-todo";
    private static Context context;

    public abstract TodoDao todoDao();

    public static AppDatabase getInstance(Context _context) {
        if (INSTANCE == null) {
            context=_context;
            INSTANCE =  Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
