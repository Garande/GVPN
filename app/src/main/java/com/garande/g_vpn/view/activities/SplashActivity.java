package com.garande.g_vpn.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;

import com.garande.g_vpn.workers.GServerWorker;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    private PeriodicWorkRequest gServerPeriodicWorkRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeWorkRequest();
        goToMainActivity();
    }

    private void initializeWorkRequest(){
        gServerPeriodicWorkRequest = new PeriodicWorkRequest.Builder(GServerWorker.class, 20, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueue(gServerPeriodicWorkRequest);
    }

    private void goToMainActivity(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                timer.cancel();
            }
        };

        timer.schedule(timerTask, 5000);
    }
}