package com.example.project_02_exercise_app;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.databinding.ActivityCardioBinding;
import com.example.project_02_exercise_app.tracking.CardioTrackingService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import com.example.project_02_exercise_app.CardioLogActivity;

public class CardioActivity extends FragmentActivity implements OnMapReadyCallback {

    private ActivityCardioBinding binding;
    private GoogleMap mMap;
    private Polyline  trail;
    private boolean firstFix = true;
    private long elapsedMs = 0;
    private long startRealtime = 0L;
    private float distM = 0f;
    private TextView timeTv, distTv, paceTv;
    private Location lastKnown = null;
    private Strotta id;


    private final ActivityResultLauncher<String[]> permissions =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    res -> { if (!res.containsValue(false)) enableRecording(); });

    private final BroadcastReceiver locReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context c,@NonNull Intent i) {
            Location l = i.getParcelableExtra(CardioTrackingService.EXTRA_LOCATION);
            distM      = i.getFloatExtra(CardioTrackingService.EXTRA_DISTANCE, distM);
            elapsedMs  = i.getLongExtra (CardioTrackingService.EXTRA_ELAPSED,  elapsedMs);
            updateStatsUI();

            if (l == null || mMap == null) return;

            lastKnown = l;

            LatLng p = new LatLng(l.getLatitude(), l.getLongitude());

            if (trail == null)
                trail = mMap.addPolyline(new PolylineOptions().width(10).color(Color.RED));

            List<LatLng> pts = new ArrayList<>(trail.getPoints());
            pts.add(p); trail.setPoints(pts);

            if (firstFix) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(p,18f));
                firstFix = false;
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(p));
            }
        }
    };

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        binding = ActivityCardioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        timeTv = binding.timeTv;
        distTv = binding.distTv;
        paceTv = binding.paceTv;
        FloatingActionButton zoomIn  = binding.zoomInBtn;
        FloatingActionButton zoomOut = binding.zoomOutBtn;
        FloatingActionButton center  = binding.centerBtn;

        zoomIn.setOnClickListener(v -> {
            if (mMap != null) mMap.animateCamera(CameraUpdateFactory.zoomIn());
        });

        zoomOut.setOnClickListener(v -> {
            if (mMap != null) mMap.animateCamera(CameraUpdateFactory.zoomOut());
        });

        center.setOnClickListener(v ->{
            if(mMap!=null&& lastKnown !=null){
                LatLng p = new LatLng(lastKnown.getLatitude(),lastKnown.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLng(p));
                    //permissions.launch(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACTIVITY_RECOGNITION
            };
        });

        SupportMapFragment mf = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);

        binding.recordBtn.setOnClickListener(v -> {
                    permissions.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACTIVITY_RECOGNITION
                });
        });

        binding.stopBtn.setOnClickListener(v -> stopRecording());
    }

    @Override public void onMapReady(@NonNull GoogleMap g){
        mMap = g;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override protected void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(
                locReceiver,new IntentFilter(CardioTrackingService.ACTION_UPDATE));
    }

    @Override protected void onStop(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(locReceiver);
        super.onStop();
    }

    private void enableRecording(){
        if (mMap == null) return;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        mMap.setMyLocationEnabled(true);

        ContextCompat.startForegroundService(this,
                new Intent(this,CardioTrackingService.class)
                        .setAction(CardioTrackingService.ACTION_START));

        binding.recordBtn.setEnabled(false);
        binding.stopBtn.setEnabled(true);
        startRealtime = SystemClock.elapsedRealtime();
        binding.statsBar.post(tick);
    }

    private void stopRecording(){
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);
        int userId = prefs.getInt(getString(R.string.preference_userId_key), -1);

        Intent log = new Intent(this, CardioLogActivity.class)
                .putExtra("uid",          userId)
                .putExtra("distance_m",   distM)
                .putExtra("duration_ms",  elapsedMs);
        startActivity(log);


        float pace = 0f;
        if(distM>0){
            pace = (elapsedMs/1000f)/(distM/1000f);
            pace /= 60f;
        }
        double km = distM / 1000.0;

        int seconds = (int) (elapsedMs /1000);
        StrottaRepository repository = StrottaRepository.getRepository(getApplication());
        repository.insertStrottaRepository(new Strotta(userId, km, seconds));

        /* reset UI */
        binding.recordBtn.setEnabled(true);
        binding.stopBtn.setEnabled(false);
        firstFix  = true;
        elapsedMs = 0;  distM = 0;
        timeTv.setText("00:00");
        distTv.setText("0.00 km");
        paceTv.setText("– min/km");
        if (trail != null) trail.remove();
        trail = null;
        binding.statsBar.removeCallbacks(tick);
        startRealtime = 0L;



    }

    private void updateStatsUI() {
        long s  = elapsedMs / 1000;
        long mm = (s % 3600) / 60;
        long hh =  s / 3600;
        long ss =  s % 60;
        timeTv.setText(hh > 0
                ? String.format("%d:%02d:%02d", hh, mm, ss)
                : String.format("%02d:%02d", mm, ss));

        distTv.setText(String.format("%.2f km", distM / 1000f));

        if (distM > 20) {
            float paceSec = (float) elapsedMs / 1000f / (distM / 1000f);
            int   pMin    = (int) (paceSec / 60);
            int   pSec    = Math.round(paceSec) % 60;
            paceTv.setText(String.format("%d:%02d min/km", pMin, pSec));
        } else {
            paceTv.setText("– min/km");
        }
    }
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (startRealtime == 0L) return;  // not recording

            elapsedMs = SystemClock.elapsedRealtime() - startRealtime;
            updateStatsUI();// refresh timer & pace even if no GPS
            binding.statsBar.postDelayed(this, 1);   // tick every 1 s
        }
    };

    public static Intent cardioIntentFactory(Context c){
        return new Intent(c, CardioActivity.class);
    }
}