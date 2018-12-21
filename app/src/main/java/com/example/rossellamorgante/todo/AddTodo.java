package com.example.rossellamorgante.todo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.rossellamorgante.todo.Model.AppDatabase;
import com.example.rossellamorgante.todo.Model.Todo;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class AddTodo extends AppCompatActivity {

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        ArrayList<Todo> lista = new ArrayList<Todo>();
        db = AppDatabase.getInstance(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.savetodo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                saveTodo();
                break;
            default:
                break;
        }
        return true;
    }


    private void saveTodo(){
        String titolo = ((EditText)findViewById(R.id.titolo)).getText().toString();
        String sottotitolo = ((EditText)findViewById(R.id.descrizione)).getText().toString();
        Todo t  = new Todo(titolo,sottotitolo,false);
        db.todoDao().insert(t);
        onBackPressed();

    }
}
