package com.example.rossellamorgante.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rossellamorgante.todo.Model.AppDatabase;
import com.example.rossellamorgante.todo.Model.Todo;
import com.example.rossellamorgante.todo.alarm.AlarmReceiver;
import com.google.android.material.textfield.TextInputEditText;


import java.util.Arrays;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static android.graphics.Color.parseColor;

public class AddTodo extends AppCompatActivity {

    AppDatabase db;
    String[] categorie ;
    String[] colors;
    Todo t=null;
    boolean isNew=true;
    SeekBar seekBar ;
    int index_seek=0;
    final int [] timeing={2,5,10,15,30,45,60};
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        db = AppDatabase.getInstance(this);
        categorie = getResources().getStringArray(R.array.array_categorie);
        colors = getResources().getStringArray(R.array.array_color_categorie);

        ((ImageView) findViewById(R.id.color_label)).setColorFilter(parseColor(colors[0]));
        ((TextView)findViewById(R.id.lcat)).setText(categorie[0]);

        seekBar = (SeekBar)findViewById(R.id.timeBar);
        ((TextView)findViewById(R.id.labelCategoria)).setText(getResources().getString(R.string.category));

        try{
            t= (Todo)getIntent().getSerializableExtra("todo");
            ((TextInputEditText)findViewById(R.id.t_titolo)).setText(t.titolo);
            ((TextInputEditText)findViewById(R.id.t_descrizione)).setText(t.descr);
            ((ImageView) findViewById(R.id.color_label)).setColorFilter(parseColor(colors[Arrays.asList(categorie).indexOf(t.categoria)]));
            ((Switch) findViewById(R.id.switch_c)).setChecked(t.stato);
            isNew=false;
        }catch (Exception e){
             t  = new Todo();
            ((Switch) findViewById(R.id.switch_c)).setVisibility(View.GONE);
        }



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                index_seek=progress;
                ((TextView)findViewById(R.id.labeltime)).setText(getResources().getString(R.string.reminder_after)+" "+timeing[progress]+" "+getResources().getString(R.string.minutes));
            }
        });

        ((ImageView) findViewById(R.id.color_label)).setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                pickCategoria();
            }
        });

        mTopToolbar = (Toolbar) findViewById(R.id.toolbaradd);
        mTopToolbar.setTitle("");
        setSupportActionBar(mTopToolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(isNew)
            inflater.inflate(R.menu.savetodo, menu);
        else
            inflater.inflate(R.menu.savedeletetodo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                saveTodo();
                break;
            case R.id.savedelete_item:
                db.todoDao().remove(t);
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }


    private void saveTodo(){

        if(((TextInputEditText)findViewById(R.id.t_titolo)).getText().toString().length()==0) {
            ((TextInputEditText) findViewById(R.id.t_titolo)).setError(getString(R.string.error_mandatory));
            return;
        }
        if(((TextInputEditText)findViewById(R.id.t_descrizione)).getText().toString().length()==0) {
            ((TextInputEditText) findViewById(R.id.t_descrizione)).setError(getString(R.string.error_mandatory));
            return;
        }

        t.titolo = ((TextInputEditText)findViewById(R.id.t_titolo)).getText().toString();
        t.descr = ((TextInputEditText)findViewById(R.id.t_descrizione)).getText().toString();
        t.categoria =  ((TextView)findViewById(R.id.lcat)).getText().toString();
        t.stato = ((Switch) findViewById(R.id.switch_c)).isChecked();
        t.data = new Date().getTime();

        long millsec_more=timeing[index_seek]*1000*60;
        t.reminder = t.data + millsec_more;

        if (isNew == true) {
            long id = db.todoDao().insert(t);
            AlarmReceiver.setAlarm(this,(int)id,t.reminder,true);
        } else {
            db.todoDao().update(t);
            AlarmReceiver.removeAlarm(this,(int)t.id,false);
            AlarmReceiver.setAlarm(this,(int)t.id,t.reminder,true);
        }

        onBackPressed();

    }

    public void pickCategoria (){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.t_pick_categoria));
        categorie = getResources().getStringArray(R.array.array_categorie);
        colors = getResources().getStringArray(R.array.array_color_categorie);
        builder.setItems(categorie, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((ImageView) findViewById(R.id.color_label)).setColorFilter(parseColor(colors[which]));
                ((TextView)findViewById(R.id.lcat)).setText(categorie[which]);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
