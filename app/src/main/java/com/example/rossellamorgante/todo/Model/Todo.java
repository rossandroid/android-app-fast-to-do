package com.example.rossellamorgante.todo.Model;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity()
public class Todo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    @ColumnInfo(name = "titolo")
    public String titolo;

    @ColumnInfo(name = "descr")
    public String descr;

    @NonNull
    @ColumnInfo(name = "stato")
    public boolean stato;

    @NonNull
    @ColumnInfo(name = "categoria")
    public String categoria;

    @NonNull
    @ColumnInfo(name = "data")
    public long data;

    @NonNull
    @ColumnInfo(name = "reminder")
    public long reminder;

    public Todo(String t, String d, boolean s, String c, long data, long reminder) {
        this.titolo = t; //title
        this.descr = d; // subtitle
        this.stato = s; // completed or not
        this.categoria = c; // category
        this.data=data; // timestamp
        this.reminder=reminder; //millisec
    }
    public Todo(){}




}

