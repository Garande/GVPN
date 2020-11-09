package com.yellowbird.g_vpn.repositories;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.yellowbird.g_vpn.model.Server;
import com.yellowbird.g_vpn.providers.SharedPreference;

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
                "416248023"
        ));
        servers.add(new Server("Japan",
                "japan.ovpn",
                "vpn",
                "vpn"
        ));
        servers.add(new Server("Sweden",
                "sweden.ovpn",
                "vpn",
                "vpn"
        ));
        servers.add(new Server("Korea",
                "korea.ovpn",
                "vpn",
                "vpn"
        ));

        return servers;
    }

}
