package com.example.rossellamorgante.todo.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "to_do")
public class Todo {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "titolo")
    public String titolo;

    @ColumnInfo(name = "descr")
    public String descr;

    @NonNull
    @ColumnInfo(name = "stato")
    public boolean stato;

    public Todo(String t, String d, boolean s) {
        this.titolo = t;
        this.descr = d;
        this.stato = s;
    }




}

