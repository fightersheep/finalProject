package com.example.finalProjectV1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ScheduleManager {
    private final Context context;
    private final Calendar calendar;
    private PendingIntent pendingIntent;
    private final AlarmManager alarmManager;

    public ScheduleManager(Context context) {
        this.context = context;
        this.calendar = Calendar.getInstance();
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setScheduleTime(int year, int month, int day, int hour, int minute) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
    }

    public void createPendingIntent(String message) {
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        intent.putExtra("message", message);

        pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    public void scheduleMessage() {
        if (pendingIntent != null && calendar != null) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }

    public void cancelSchedule() {
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
