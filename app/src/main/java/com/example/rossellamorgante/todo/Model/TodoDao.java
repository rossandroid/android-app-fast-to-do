package com.example.rossellamorgante.todo.Model;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM Todo ORDER BY data DESC")
    List<Todo> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Todo doto);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Todo t);

    @Delete
    void remove(Todo t);

    @Query("SELECT * FROM Todo WHERE id=:id LIMIT 1")
    Todo getTodo(int id);

    @Query ("DELETE FROM Todo WHERE stato=1")
    void deleteCompleted();

    @Query ("DELETE FROM Todo WHERE (reminder-:timer)<=0 AND stato=0")
    void deleteTimeout(long timer);
}

