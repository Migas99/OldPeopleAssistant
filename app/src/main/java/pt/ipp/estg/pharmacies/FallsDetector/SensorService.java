package pt.ipp.estg.pharmacies.FallsDetector;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsManager;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pharmacies.MainActivity;
import pt.ipp.estg.pharmacies.R;


public class SensorService extends Service implements SensorEventListener {

    private final String CHANNEL_ID = "ForegroundService_Sensor";
    private final IBinder mBinder;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private Notification mNotificationDanger;
    private PendingIntent negativeActionIntent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mNotificationManager = this.createNotificationChannel();
        this.mNotification = this.createNotification();
        this.mNotificationDanger = this.createNotificationDanger();
        startForeground(1, mNotification);
        this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (this.mSensorManager != null) {
            this.mSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    public SensorService() {
        this.mBinder = new SensorServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double sum_square = (Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
        double normalVector = Math.sqrt(sum_square);

        if (normalVector >= 30) {

            boolean isActive = false;
            StatusBarNotification[] list = this.mNotificationManager.getActiveNotifications();
            for (int i = 0; i < list.length; i++) {
                if (list[i].getId() == 2) {
                    isActive = true;
                }
            }

            if (!isActive) {
                this.mNotificationManager.notify(2, this.mNotificationDanger);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void start() {
        this.mSensorManager.registerListener(this, this.mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop(){
        this.mSensorManager.unregisterListener(this);
    }

    private NotificationManager createNotificationChannel() {
        NotificationManager manager = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel alarmChannel = new NotificationChannel(
                    this.CHANNEL_ID, "Foreground Sensor Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(alarmChannel);
            }
        }

        return manager;
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0);

        return new NotificationCompat.Builder(this, this.CHANNEL_ID)
                .setContentTitle("Sensor de Quedas")
                .setContentText("Estamos cuidando de si!")
                .setSmallIcon(R.drawable.ic_local_pharmacy_black)
                .setContentIntent(pendingIntent)
                .build();
    }

    private Notification createNotificationDanger() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notificationIntent, 0);

        Intent positiveIntent = new Intent(this, IntentHandler.class);
        positiveIntent.putExtra("Answer", "OK");

        Intent negativeIntent = new Intent(this, IntentHandler.class);
        negativeIntent.putExtra("Answer", "NOT OK");

        PendingIntent positiveActionIntent = PendingIntent.getBroadcast(getApplicationContext(), 3, positiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        this.negativeActionIntent = PendingIntent.getBroadcast(getApplicationContext(), 4, negativeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, this.CHANNEL_ID)
                .setContentTitle("Está tudo bem?")
                .setContentText("Detetamos que foi efetuado um movimento brusco. Está tudo bem?")
                .setSmallIcon(R.drawable.ic_local_pharmacy_black)
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher_round, "Sim!", positiveActionIntent)
                .addAction(R.mipmap.ic_launcher, "Não, ajuda!", negativeActionIntent)
                .setAutoCancel(true)
                .setTimeoutAfter(15*1000)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
    }

    public class SensorServiceBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }

}