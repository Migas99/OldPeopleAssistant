package pt.ipp.estg.pharmacies.medicineReminder.notification.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Calendar;

import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;
import pt.ipp.estg.pharmacies.medicineReminder.notification.receivers.AlarmReceiver;

public class AlarmScheduler {

    public void setAlarm(Context context, long alarmTime, Reminder reminderTask) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putInt("reminderId", reminderTask.getReminderId());
        intent.putExtras(b);
        PendingIntent operation = PendingIntent.getBroadcast(context, reminderTask.getReminderId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime > alarmTime) {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime + 86400000, operation);
        } else {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Reminder reminderTask, long RepeatTime) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putInt("reminderId", reminderTask.getReminderId());
        intent.putExtras(b);
        PendingIntent operation = PendingIntent.getBroadcast(context, reminderTask.getReminderId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);
    }

    public void cancelAlarm(Context context, Reminder reminderTask) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent operation = PendingIntent.getBroadcast(context, reminderTask.getReminderId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(operation);
    }

}