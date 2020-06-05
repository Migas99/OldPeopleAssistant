package pt.ipp.estg.pharmacies.FallsDetector;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;

import java.util.Map;

public class IntentHandler extends BroadcastReceiver {

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!"OK".equals(intent.getStringExtra("Answer"))) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CALL_PHONE)) {
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST);
                }
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String emergencyNumber = preferences.getString("emergencyNumber", "DEFAULT");

            Intent call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel: +351 " + emergencyNumber));
            call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(call);
        }

        this.mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mNotificationManager.cancel(2);
    }
}

