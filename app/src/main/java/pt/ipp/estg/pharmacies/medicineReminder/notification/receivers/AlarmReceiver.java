package pt.ipp.estg.pharmacies.medicineReminder.notification.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsManager;

import androidx.core.app.NotificationCompat;

import pt.ipp.estg.pharmacies.MainActivity;
import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.asycntasks.HandleNoResponseTask;
import pt.ipp.estg.pharmacies.medicineReminder.notification.helpers.NotificationHelper;
import pt.ipp.estg.pharmacies.medicineReminder.notification.services.ReminderAlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String TAKE_ACTION = "TAKE_ACTION";
    private int reminder_Id;
    private NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = "Medicine Reminder";
        String content = "Please take your medicine.";
        Bitmap android_Image = BitmapFactory.decodeResource(context.getResources(), R.drawable.medicine_row_image);
        Bundle answerBundle = intent.getExtras();
        reminder_Id = answerBundle.getInt("reminderId");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        manager = notificationHelper.getManager();

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        nb.setLargeIcon(android_Image).setContentTitle(title).setContentText(content).setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        nb.addAction(R.drawable.ic_pill_notification, "Medicine Taken", getTakenAction(context, reminder_Id));
        manager.notify(reminder_Id, nb.build());

        HandleNoResponseTask handleNoResponse = new HandleNoResponseTask(context, manager, reminder_Id);
        handleNoResponse.execute();
    }

    private PendingIntent getTakenAction(Context context, int reminderId) {
        return ReminderAlarmService.getReminderPendingIntent(context, TAKE_ACTION, reminderId);
    }
}

