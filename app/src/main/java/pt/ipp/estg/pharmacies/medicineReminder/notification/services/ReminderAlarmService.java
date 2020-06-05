package pt.ipp.estg.pharmacies.medicineReminder.notification.services;

import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.model.History;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;
import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;
import pt.ipp.estg.pharmacies.medicineReminder.notification.alarm.AlarmScheduler;
import pt.ipp.estg.pharmacies.medicineReminder.notification.helpers.NotificationHelper;

import static pt.ipp.estg.pharmacies.medicineReminder.notification.receivers.AlarmReceiver.TAKE_ACTION;

public class ReminderAlarmService extends IntentService {

    private ViewModel viewModel = new ViewModel(getApplication());
    private static final String TAG = ReminderAlarmService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 5000;

    private Context mContext;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private Reminder reminder;
    private Medicine medicine;

    public static PendingIntent getReminderPendingIntent(Context context, String action, int reminderId) {
        Bundle bundle = new Bundle();
        bundle.putInt("reminderId", reminderId);
        Intent intent = new Intent(context, ReminderAlarmService.class);
        intent.setAction(action);
        intent.putExtras(bundle);
        return PendingIntent.getService(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.mContext);
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.mLocationCallback = new LocationCallback();

        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationManager manager = notificationHelper.getManager();
        String action = intent.getAction();
        int reminderId;
        if (intent.getExtras().get("reminderId") != null) {
            reminderId = (Integer) intent.getExtras().get("reminderId");
            this.reminder = viewModel.getReminder(reminderId);
            this.medicine = viewModel.getMedicine(reminder.getMedicineId());

            if (TAKE_ACTION.equals(action)) {

                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        medicine.removePill();
                        viewModel.updateMedicine(medicine);
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        int mHour = c.get(Calendar.HOUR);
                        int mMinute = c.get(Calendar.MINUTE);
                        double lat = 0;
                        double lon = 0;

                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }

                        viewModel.insertHistory(new History(medicine.id, lat, lon, mDay, mMonth, mYear, mHour, mMinute));

                        if (medicine.getAmount() == 0) {
                            viewModel.deleteReminder(reminder);
                        }
                    }
                });

                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                manager.cancel(reminderId);
            }
        }
    }
}

