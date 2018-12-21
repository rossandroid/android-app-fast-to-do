package com.example.rossellamorgante.todo.Model;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TodoDao {

    @Query("SELECT * FROM Todo")
    List<Todo> dammiTutto();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Todo doto);
}

