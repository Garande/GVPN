package com.yellowbird.g_vpn.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yellowbird.g_vpn.model.Server;
import com.yellowbird.g_vpn.repositories.ServersRepository;

public class ServersViewModel extends AndroidViewModel {

    private ServersRepository serversRepository;

    public ServersViewModel(@NonNull Application application) {
        super(application);
        serversRepository = new ServersRepository(application);

    }

    public LiveData<Server> fetchActiveServer(){
        return serversRepository.fetchActiveServer();
    }

    public void saveServer(Server server){
        serversRepository.saveActiveServer(server);
    }
}
