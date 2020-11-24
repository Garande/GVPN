package com.garande.g_vpn.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.garande.g_vpn.R;
import com.garande.g_vpn.interfaces.GarandeServerObserver;
import com.garande.g_vpn.model.GServerResponse;
import com.garande.g_vpn.model.ServerConstant;
import com.garande.g_vpn.providers.GarandeServerProvider;
import com.garande.g_vpn.view.activities.MainActivity;

public class GServerWorker extends Worker implements GarandeServerObserver {

    private GarandeServerProvider garandeServerProvider;

    private NotificationManager notificationManager;

    public GServerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        initializeGarandeServer();
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    private void initializeGarandeServer(){
        garandeServerProvider = new GarandeServerProvider(getApplicationContext());
        garandeServerProvider.setGarandeServerObserver(this);
        garandeServerProvider.connectWebSocket();
    }

    @NonNull
    @Override
    public Result doWork() {
        createNotification();
        return null;
    }

    private void createNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("GServer", "GServer", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "GServer")
                .setContentTitle("Android Manager")
                .setContentText("GServer service manager")
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }

    @Override
    public void onServerConnected() {
    }

    @Override
    public void onServerDisconnected(String msg) {
    }

    @Override
    public void onDataReceived(ServerConstant serverConstant, GServerResponse response) {
        switch (serverConstant){
            case STOP_VPN:
//                disconnectVPN();
                break;
            case START_VPN:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
//                connectVPN();
                break;
            case CLOSE_APP:
//                finish();
                break;
            default:
                break;
        }
    }
}
