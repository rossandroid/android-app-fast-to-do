package com.example.rossellamorgante.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rossellamorgante.todo.Model.AppDatabase;
import com.example.rossellamorgante.todo.Model.Todo;
import com.example.rossellamorgante.todo.alarm.AlarmReceiver;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    ArrayList<Todo> lista = new ArrayList<Todo>();
    AppDatabase db;
    AdapterToDoList adapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getInstance(this);
        lista = (ArrayList<Todo>) db.todoDao().getAll();
        adapter = new AdapterToDoList(this,lista);
        list=((ListView)findViewById(R.id.lista_todo_main));
        list.setAdapter(adapter);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                pickItemList(position);
            }
        });
        try{
            Todo t= (Todo)getIntent().getSerializableExtra("notifity_todo");
            showOptions(t);
        }catch (Exception e){}
    }

    private void showOptions(final Todo t){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(t.titolo.toUpperCase());
        String[] list_option = {"","Edit..."};
        list_option[0]=(t.stato)?"Not completed":"Completed";
        builder.setItems(list_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        t.stato=!t.stato;
                        db.todoDao().update(t);
                        updateLista();
                        if(t.stato) {
                            AlarmReceiver.removeAlarm(getApplicationContext(),(int)t.id,true);
                        }else {
                            AlarmReceiver.setAlarm(getApplicationContext(),(int)t.id,(t.reminder-t.data),true);
                        }
                        break;
                    case 1:
                        Intent myIntent = new Intent(getApplicationContext(), AddTodo.class);
                        myIntent.putExtra("todo", t);
                        startActivity(myIntent);
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void pickItemList (final int position){
        showOptions(lista.get(position));
    }

    private void updateLista(){
        lista.clear();
        lista.addAll (db.todoDao().getAll());
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateLista();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addtodo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                Intent myIntent = new Intent(this, AddTodo.class);
                startActivity(myIntent);
                break;
            case R.id.delete_timeout:
                db.todoDao().deleteTimeout(new Date().getTime());
                updateLista();
                break;
            case R.id.delete_completed:
                db.todoDao().deleteCompleted();
                updateLista();
                break;
            default:
                break;
        }
        return true;
    }
}
