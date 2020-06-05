package pt.ipp.estg.pharmacies.medicineReminder.asycntasks;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsManager;

import androidx.preference.PreferenceManager;

public class HandleNoResponseTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private int reminder_Id;
    private NotificationManager manager;

    public HandleNoResponseTask(Context context, NotificationManager manager, int reminder_Id) {
        this.context = context;
        this.manager = manager;
        this.reminder_Id = reminder_Id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(60 * 1000);

            StatusBarNotification[] status = manager.getActiveNotifications();
            for (int i = 0; i < status.length; i++) {
                if (status[i].getId() == reminder_Id) {
                    manager.cancel(reminder_Id);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    String emergencyNumber = preferences.getString("emergencyNumber", "DEFAULT");

                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage("+351 " + emergencyNumber, null, "Não foi confirmada a toma de um medicamento! \nPor favor, verifique.", null, null);
                }
            }

        } catch (InterruptedException e) {
        }

        return null;
    }


}
