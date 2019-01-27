package com.example.rossellamorgante.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

    private Toolbar mTopToolbar;
    private Toolbar mTopToolbar2;
    float upY=0,moveY=0;

    float oldY=0,newY=0;
    int maxH=0;
    int maxW=0;
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
                pickItemList(position-1);
            }
        });
        try{
            Todo t= (Todo)getIntent().getSerializableExtra("notifity_todo");
            showOptions(t);
        }catch (Exception e){}


        View header = getLayoutInflater().inflate(R.layout.header_list_todo, list, false);
        list.addHeaderView(header, null, false);

        mTopToolbar = (Toolbar) list.findViewById(R.id.my_toolbar);
        mTopToolbar.setTitle("");

        mTopToolbar2 = (Toolbar) findViewById(R.id.toolbar_two);


        setSupportActionBar(mTopToolbar);


        TextView mTxtView  = ((TextView)list.findViewById(R.id.titleheader));
        ViewGroup.LayoutParams layoutparams = mTxtView.getLayoutParams();
        maxH=layoutparams.height-45;
        maxW=layoutparams.width;

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View c = list.getChildAt(0);
                if(c!=null && firstVisibleItem==0){
                    TextView mTxtView  = ((TextView)list.findViewById(R.id.titleheader));
                    ViewGroup.LayoutParams layoutparams = mTxtView.getLayoutParams();
                    int top=c.getTop();
                    layoutparams.height= maxH+top;
                    double f=((double)maxW*(1.0-((double)Math.abs(top)/(double)maxH)));
                    layoutparams.width= (int)f;

                    //Log.i("layoutparams",""+layoutparams.width +" "+layoutparams.height);
                    mTxtView.setLayoutParams(layoutparams);
                    mTxtView.requestLayout();

                    setSupportActionBar(mTopToolbar);
                    mTopToolbar2.setVisibility(View.GONE);

                }else if(firstVisibleItem!=0)
                {
                    setSupportActionBar(mTopToolbar2);
                    mTopToolbar2.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private boolean userIsInteracting=false;
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
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
