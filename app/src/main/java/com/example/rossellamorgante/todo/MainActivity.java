package com.example.rossellamorgante.todo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.rossellamorgante.todo.Model.Todo;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Todo todo1 = new Todo("Prova1", "descr", false);
        Todo todo2 = new Todo("Prova2", "descr", true);
        Todo todo3 = new Todo("Prova3", "descr", false);

        // bla bla bla
        //bla bla 2

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
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }
}
