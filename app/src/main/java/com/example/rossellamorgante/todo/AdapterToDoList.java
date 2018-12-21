package com.example.rossellamorgante.todo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rossellamorgante.todo.Model.Todo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdapterToDoList extends ArrayAdapter<Todo> {
    private Context mContext;
    private ArrayList<Todo> todoList = new ArrayList<Todo>();

    public AdapterToDoList(@NonNull Context context, ArrayList<Todo> list) {
        super(context, 0 , list);
        mContext = context;
        todoList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);

        Todo t = todoList.get(position);
        TextView titolo = ((TextView)listItem.findViewById(R.id.titolo));
        TextView descr  =  ((TextView)listItem.findViewById(R.id.sottotitolo));
        if(t.stato){
            titolo.setPaintFlags(titolo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        titolo.setText(t.titolo);
        descr.setText(t.descr);



        return listItem;
    }
}
