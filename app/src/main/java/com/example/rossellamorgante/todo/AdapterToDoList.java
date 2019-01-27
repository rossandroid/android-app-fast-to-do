package com.example.rossellamorgante.todo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rossellamorgante.todo.Model.Todo;
import java.util.ArrayList;
import java.util.Date;

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
        View listItem = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);

        Todo t = todoList.get(position);
        TextView titolo = ((TextView)listItem.findViewById(R.id.titolo));
        TextView descr  =  ((TextView)listItem.findViewById(R.id.sottotitolo));
        TextView categoria  =  ((TextView)listItem.findViewById(R.id.t_category));
        TextView h_m  =  ((TextView)listItem.findViewById(R.id.t_m_h));
        ImageView i =  ((ImageView)listItem.findViewById(R.id.i_clock));
        TextView data  =  ((TextView)listItem.findViewById(R.id.t_data));
        long v= (t.reminder-new Date().getTime())/1000/60;

        //aggiornamento tempo/timeout
        if(v>0){
            h_m.setText( ""+((Math.abs((new Date().getTime()- t.reminder)/1000/60))+1)+" min/s" );
        }else {
            h_m.setText("Timeout");
        }

        // aggiornamento icona
        if(t.stato){
            i.setImageResource(R.drawable.ic_timer_off_black_24dp);
        }else {
            i.setImageResource(R.drawable.ic_timer_black_24dp);
        }

        //aggiornamento barra
        if(t.stato){
            ((ImageView)listItem.findViewById(R.id.comp1)).setVisibility(View.VISIBLE);
        }else {
           if(v<=0){
               ((ImageView)listItem.findViewById(R.id.comp1)).setBackgroundResource(R.drawable.circle_timeout);//setColorFilter(getContext().getResources().getColor(R.color.colorDarkOrange));
               ((ImageView)listItem.findViewById(R.id.comp1)).setVisibility(View.VISIBLE);
           }
        }


        String [] categorie = mContext.getResources().getStringArray(R.array.array_categorie);
        String [] colors = mContext.getResources().getStringArray(R.array.array_color_categorie);
        int x = java.util.Arrays.asList(categorie).indexOf(t.categoria);
        categoria.setTextColor(ColorStateList.valueOf(Color.parseColor(colors[x])));



        titolo.setText(t.titolo.toUpperCase());
        descr.setText(t.descr.toLowerCase());
        categoria.setText(t.categoria);

        String d= DateUtils.getRelativeTimeSpanString(t.data, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NO_NOON).toString();
        data.setText(d);


        return listItem;
    }
}
