package com.garande.g_vpn.repositories;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.garande.g_vpn.R;
import com.garande.g_vpn.model.Server;
import com.garande.g_vpn.providers.SharedPreference;
import com.garande.g_vpn.utils.Utils;

import java.util.ArrayList;

public class ServersRepository {
    private Context context;
    private SharedPreference sharedPreference;

    public ServersRepository(Application application){
        this.context = application.getApplicationContext();
        sharedPreference = new SharedPreference(context);
    }


    public MutableLiveData<Server> fetchActiveServer(){
        MutableLiveData<Server> serverMutableLiveData = new MutableLiveData<>();
        Server server = sharedPreference.getServer();
        if(server != null){
            serverMutableLiveData.setValue(server);
        }else {
            server = getServerList().get(0);
            serverMutableLiveData.setValue(server);
        }
        return serverMutableLiveData;
    }


    public void saveActiveServer(Server server) {
//        MutableLiveData<Server> serverMutableLiveData = new MutableLiveData<>();

        sharedPreference.saveServer(server);

//        return serverMutableLiveData;
    }

    /**
     * Generate server array list
     */
    private ArrayList<Server> getServerList() {

        ArrayList<Server> servers = new ArrayList<>();

        servers.add(new Server("United States",
                "us.ovpn",
                "freeopenvpn",
                "416248023",
                R.drawable.united_states_of_america
//                "@drawable/"+R.drawable.united_states_of_america
//                Utils.getDrawablePath(R.drawable.united_states_of_america)
        ));
        servers.add(new Server("Japan",
                "japan.ovpn",
                "vpn",
                "vpn",
                R.drawable.japan
//                "@drawable/"+ R.drawable.japan
//                Utils.getDrawablePath(R.drawable.japan)
        ));
        servers.add(new Server("Sweden",
                "sweden.ovpn",
                "vpn",
                "vpn",
                R.drawable.sweden
//                "@drawable/"+R.drawable.sweden
//                Utils.getDrawablePath(R.drawable.sweden)

        ));
        servers.add(new Server("Korea",
                "korea.ovpn",
                "vpn",
                "vpn",
                R.drawable.south_korea
//                "@drawable/"+R.drawable.south_korea
//                Utils.getDrawablePath(R.drawable.south_korea)
        ));
//        ));

        return servers;
    }

    public MutableLiveData<ArrayList<Server>> fetchServers() {
        MutableLiveData<ArrayList<Server>> listMutableLiveData = new MutableLiveData<>();
        ArrayList<Server> servers = getServerList();
        listMutableLiveData.setValue(servers);

        return listMutableLiveData;
    }
}
