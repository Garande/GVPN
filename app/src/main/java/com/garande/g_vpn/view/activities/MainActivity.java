package com.garande.g_vpn.view.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.garande.g_vpn.R;
import com.garande.g_vpn.adapters.ServersListAdapter;
import com.garande.g_vpn.databinding.ActivityMainBinding;
//import com.yellowbird.g_vpn.databinding.ServersLayoutBinding;
import com.garande.g_vpn.interfaces.GarandeServerObserver;
import com.garande.g_vpn.listeners.VPNConnectionListener;
import com.garande.g_vpn.model.GServerResponse;
import com.garande.g_vpn.model.Server;
import com.garande.g_vpn.model.ServerConstant;
import com.garande.g_vpn.model.VPNConnection;
import com.garande.g_vpn.providers.GarandeServerProvider;
import com.garande.g_vpn.utils.NetworkUtils;
import com.garande.g_vpn.utils.RightSheetBehavior;
import com.garande.g_vpn.viewModel.ServersViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, VPNConnectionListener.OnVPNConnectionChange, GarandeServerObserver {

    private Server server;

    private NetworkUtils networkUtils = new NetworkUtils();

    private OpenVPNThread vpnThread = new OpenVPNThread();
    private OpenVPNService vpnService = new OpenVPNService();
    boolean isVpnActive = false;

    ActivityMainBinding binding;
//    ServersLayoutBinding serversLayoutBinding;

    ServersViewModel serversViewModel;

    VPNConnectionListener vpnConnectionListener;

    private ActionBarDrawerToggle drawerToggle;

    private ServersListAdapter serversListAdapter;

    RightSheetBehavior rightSheetBehavior;
    ArrayList<Server> servers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initViewModel();

        initView();

        initRightSheet();

        initializeToolbar();

        initConnectionListener();

        observerViewModel();

    }


    private void initializeToolbar() {
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.Open, R.string.Close);

        binding.drawerLayout.addDrawerListener(drawerToggle);

        drawerToggle.syncState();


        binding.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(binding.navigationView);
            }
        });

        binding.serversBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightSheetBehavior.setState(RightSheetBehavior.STATE_EXPANDED);
            }
        });


//        initializeWebsocket();


    }

    GarandeServerProvider serverProvider;

    private void initializeWebsocket() {
        serverProvider = new GarandeServerProvider(this);
        serverProvider.setGarandeServerObserver(this);
        serverProvider.connectWebSocket();
    }

    @Override
    public void onBackPressed() {
        if (rightSheetBehavior.getState() == RightSheetBehavior.STATE_EXPANDED){
            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
        }else if(binding.drawerLayout.isDrawerOpen(binding.navigationView)) {
            binding.drawerLayout.closeDrawers();
        }else
          {
            super.onBackPressed();
        }
    }




    private void initRightSheet(){
        rightSheetBehavior = RightSheetBehavior.from(binding.serversLayout.rightSheet);
        binding.serversLayout.serverList.setNestedScrollingEnabled(false);
        binding.serversLayout.serverList.setHasFixedSize(false);

        serversListAdapter = new ServersListAdapter(getApplicationContext(), servers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        binding.serversLayout.serverList.setLayoutManager(linearLayoutManager);
        binding.serversLayout.serverList.setAdapter(serversListAdapter);
        binding.serversLayout.rightSheetBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
            }
        });

        serversListAdapter.setOnServerClickListener(new ServersListAdapter.OnServerClickListener() {
            @Override
            public void onServerClick(Server server1) {
                rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
                server = server1;
                serversViewModel.saveServer(server1);
                stopVpn();
                connectVPN();

//                Toast.makeText(getApplicationContext(), server.country, Toast.LENGTH_SHORT).show();
//                showToast(server.country);
            }
        });
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

    private void observerViewModel(){
        serversViewModel.fetchActiveServer().observe(this, server1 -> {
            server = server1;
        });

        serversViewModel.fetchServers().observe(this, servers1 -> {
            serversListAdapter.updateServers(servers1);
        });
    }

    private void initViewModel(){
        serversViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ServersViewModel.class);

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
                disconnectVPN();
                break;
            case START_VPN:
                connectVPN();
                break;
            case CLOSE_APP:
                finish();
                break;
            default:
                break;
        }
    }
}