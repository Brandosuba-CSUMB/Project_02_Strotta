package com.example.project_02_exercise_app.tracking;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.project_02_exercise_app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardioTrackingService extends LifecycleService {

    public static final String ACTION_START   = "CardioTrackingService.ACTION_START";
    public static final String ACTION_STOP    = "CardioTrackingService.ACTION_STOP";
    public static final String ACTION_UPDATE  = "CardioTrackingService.ACTION_UPDATE";
    public static final String EXTRA_LOCATION = "extra_location";
    public static final String EXTRA_DISTANCE = "extra_distance_m";
    public static final String EXTRA_ELAPSED  = "extra_elapsed_ms";
    private static final String CHANNEL_ID = "cardio_tracking";
    private static final int NOTIF_ID = 42;
    private FusedLocationProviderClient fused;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private Location lastLoc;
    private float totalMeters   = 0f;
    private float elevationGain = 0f;
    private long startEpochMs  = 0L;
    private final String sessionId = UUID.randomUUID().toString();

    @Override public void onCreate() {
        super.onCreate();
        fused = LocationServices.getFusedLocationProviderClient(this);
        ensureChannel();
    }
    @Override public int onStartCommand(Intent intent,int flags,int startId) {
        super.onStartCommand(intent,flags,startId);
        if (intent == null) return START_NOT_STICKY;

        switch (intent.getAction()) {
            case ACTION_START -> beginTracking();
            case ACTION_STOP  -> stopAndPersist();
        }
        return START_STICKY;
    }

    private void beginTracking() {
        startForeground(NOTIF_ID, buildNotification("Recording…"));

        LocationRequest req = new LocationRequest.Builder(
                LocationRequest.PRIORITY_HIGH_ACCURACY, 1_000 /*ms*/)
                .setMinUpdateDistanceMeters(2f)
                .build();

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;   // permissions were checked in Activity

        fused.requestLocationUpdates(req, locationCallback, Looper.getMainLooper());
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override public void onLocationResult(LocationResult result) {
            Location loc = result.getLastLocation();
            if (loc == null) return;

            if (startEpochMs == 0L) startEpochMs = loc.getTime();
            if (lastLoc != null) {
                totalMeters   += lastLoc.distanceTo(loc);
                elevationGain += Math.max(0f,(float)(loc.getAltitude() - lastLoc.getAltitude()));
            }
            lastLoc = loc;

            long elapsed = loc.getTime() - startEpochMs;

            Intent up = new Intent(ACTION_UPDATE);
            up.putExtra(EXTRA_LOCATION, loc);
            up.putExtra(EXTRA_DISTANCE, totalMeters);
            up.putExtra(EXTRA_ELAPSED,  elapsed);
            LocalBroadcastManager.getInstance(CardioTrackingService.this).sendBroadcast(up);
        }
    };

    private void stopAndPersist() {
        fused.removeLocationUpdates(locationCallback);
        io.execute(this::writeToHealthConnect);
        stopForeground(STOP_FOREGROUND_DETACH);
        stopSelf();
    }
    private void writeToHealthConnect() {
        //TODO implement this function.
    }

    private Notification buildNotification(String text) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_run)
                .setContentTitle("Exercise Tracker")
                .setContentText(text)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build();
    }

    private void ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "Tracking", NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(ch);
        }
    }
    @Nullable @Override
    public IBinder onBind(Intent intent){ return super.onBind(intent); }
}