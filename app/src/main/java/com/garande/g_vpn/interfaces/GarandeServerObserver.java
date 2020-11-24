package com.garande.g_vpn.interfaces;

import com.garande.g_vpn.model.GServerResponse;
import com.garande.g_vpn.model.ServerConstant;

public interface GarandeServerObserver {
    void onServerConnected();

    void onServerDisconnected(String msg);

    void onDataReceived(ServerConstant serverConstant, GServerResponse response);

}
