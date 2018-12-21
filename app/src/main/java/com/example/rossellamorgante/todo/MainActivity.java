package com.example.rossellamorgante.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.rossellamorgante.todo.Model.AppDatabase;
import com.example.rossellamorgante.todo.Model.Todo;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    ArrayList<Todo> lista = new ArrayList<Todo>();
    AppDatabase db;
    AdapterToDoList adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = AppDatabase.getInstance(this);

        lista = (ArrayList<Todo>) db.todoDao().dammiTutto();

        adapter = new AdapterToDoList(this,lista);
        ((ListView)findViewById(R.id.lista_todo_main)).setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        lista.clear();
        lista.addAll (db.todoDao().dammiTutto());
        adapter.notifyDataSetChanged();


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
            default:
                break;
        }
        return true;
    }
}
