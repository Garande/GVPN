package com.yellowbird.g_vpn.view.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.yellowbird.g_vpn.R;
import com.yellowbird.g_vpn.databinding.ActivityMainBinding;
import com.yellowbird.g_vpn.listeners.VPNConnectionListener;
import com.yellowbird.g_vpn.model.Server;
import com.yellowbird.g_vpn.model.VPNConnection;
import com.yellowbird.g_vpn.utils.NetworkUtils;
import com.yellowbird.g_vpn.viewModel.ServersViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, VPNConnectionListener.OnVPNConnectionChange {

    private Server server;

    private NetworkUtils networkUtils = new NetworkUtils();

    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    boolean isVpnActive = false;

    ActivityMainBinding binding;

    ServersViewModel serversViewModel;

    VPNConnectionListener vpnConnectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();

        initConnectionListener();

        initViewModel();

    }

    private void initConnectionListener() {
        vpnConnectionListener = new VPNConnectionListener();
        vpnConnectionListener.setOnVpnConnectionChange(this::onConnectionChanged);
        isVpnRunning();
    }

    private void isVpnRunning(){
//        vpnConnectionListener.changeVPNConnection(new VPNConnection(vpnService.getSt));
    }

    private void initView() {
        binding.connectionBtn.setOnClickListener(this);
        VpnStatus.initLogCache(this.getCacheDir());
    }

    private void initViewModel(){
        serversViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ServersViewModel.class);

        serversViewModel.fetchActiveServer().observe(this, server1 -> {
            server = server1;
        });

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connectionBtn:
                if(isVpnActive){
                    disconnectVPN();
                }else {
                    connectVPN();
                }
        }
    }

    private void connectVPN() {
        if(!isVpnActive && isNetworkAvailable()){
            Intent intent = VpnService.prepare(getApplicationContext());
            if(intent != null)
                startActivityForResult(intent, 1);
            else
                startVpn();

//            vpnConnectionListener.changeVPNConnection(new VPNConnection(VPNConnection.CONNECTING));
        }else if(!isNetworkAvailable()){
            showToast("You have no internet connection");
        }else if(stopVpn()){
            showToast("Disconnected successfully");
        }
    }

    private void showToast(String message) {
        runOnUiThread(()-> {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                vpnConnectionListener.changeVPNConnection(new VPNConnection(intent.getStringExtra("state")));
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                String duration = intent.getStringExtra("duration");
                String lastPacketReceived = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if(duration == null) duration = "00:00:00";
                if(lastPacketReceived == null) lastPacketReceived = "0";
                if (byteIn == null) byteIn = " ";
                if(byteOut == null) byteOut = " ";
                updateConnectionStatus(duration, lastPacketReceived, byteIn, byteOut);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void updateConnectionStatus(String duration, String lastPacketReceived, String byteIn, String byteOut) {
        binding.bytesIn.setText("Bytes In : " + byteIn);
        binding.bytesOut.setText("Bytes Out : " + byteOut);
        binding.packets.setText("Received : " + lastPacketReceived);
        binding.uptime.setText("Uptime : " + duration);
    }

    private boolean stopVpn() {
        try {
            vpnThread.stopProcess();

            vpnConnectionListener.changeVPNConnection(new VPNConnection(VPNConnection.DISCONNECTED));

            isVpnActive = false;
            return true;
        }catch (Exception e){
            Log.e(MainActivity.class.getSimpleName(), null, e);
        }

        return false;
    }

    private void startVpn() {
        vpnConnectionListener.changeVPNConnection(new VPNConnection(VPNConnection.CONNECTING));
        try {
            //openning .ovpn file
            InputStream inputStream = getAssets().open(server.ovpn);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String configurations = "";
            String line;
            while (true){
                line = bufferedReader.readLine();
                if (line == null) break;
                configurations += line + "\n";
            }
            bufferedReader.readLine();
            OpenVpnApi.startVpn(getApplicationContext(), configurations, server.country, server.userName, server.password);
            vpnConnectionListener.changeVPNConnection(new VPNConnection(VPNConnection.CONNECTED));
//            binding.connectionBtn.setText("Connecting...");
            isVpnActive = true;

        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private void disconnectVPN() {
        //dialog
        stopVpn();
    }


    private boolean isNetworkAvailable(){
        return networkUtils.checkNetworkConnection(getApplicationContext());
    }




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnectionChanged(VPNConnection connection) {
        try {
            assert connection != null;
//            if(connection != null && connection.state != null) {
                if (connection.state.equals(VPNConnection.READY)) {
                    binding.connectionBtn.setText("Connect");
                } else if (connection.state.equals(VPNConnection.CONNECTING)) {
                    binding.connectionBtn.setText("Connecting");
                } else if (connection.state.equals(VPNConnection.CONNECTED)) {
                    binding.connectionBtn.setText("Connected");
                    binding.connectionBtn.setBackgroundColor(getColor(R.color.material_green600));
                } else if (connection.state.equals(VPNConnection.DISCONNECTED)) {
                    binding.connectionBtn.setText("Disconnected");
                    binding.connectionBtn.setBackgroundColor(getColor(R.color.material_red600));
                    isVpnActive = false;
                }
//            }
        }catch (Exception e){
            Log.e(MainActivity.class.getSimpleName(), null, e);
        }

    }
}