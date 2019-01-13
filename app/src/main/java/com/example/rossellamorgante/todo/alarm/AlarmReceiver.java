package com.example.rossellamorgante.todo.alarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import com.example.rossellamorgante.todo.MainActivity;
import com.example.rossellamorgante.todo.Model.AppDatabase;
import com.example.rossellamorgante.todo.Model.Todo;
import com.example.rossellamorgante.todo.R;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id", -1);
        Todo t = AppDatabase.getInstance(context).todoDao().getTodo(id);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //TODO: Colori e Icona personalizzata

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity( context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "rossella.minutesTodo")
                .setSmallIcon(R.drawable.ic_timer_black_24dp)
                .setContentTitle(t.titolo)
                .setContentText("["+t.categoria+"] " + t.descr)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .setSound(alarmSound)
                .setContentIntent(notifyPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(t.id, mBuilder.build());


    }


    public static void setAlarm(Context context, int id, long time,boolean toast){

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)id, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, time , pendingIntent);

        if(toast)
            Toast.makeText(context, "Ok, you have set the reminder", Toast.LENGTH_SHORT).show();
    }

    public static void removeAlarm(Context context,int id,boolean toast){
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)id, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        if(toast)
            Toast.makeText(context, "Ok, you have disabled the reminder", Toast.LENGTH_SHORT).show();
    }



}
