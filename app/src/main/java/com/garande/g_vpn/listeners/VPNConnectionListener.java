package com.garande.g_vpn.listeners;

import com.garande.g_vpn.model.VPNConnection;

public class VPNConnectionListener {

    public interface OnVPNConnectionChange {
        void onConnectionChanged(VPNConnection vpnConnection);
    }

    private OnVPNConnectionChange onVpnConnectionChange;

    public void setOnVpnConnectionChange(OnVPNConnectionChange onVpnConnectionChange1){
        this.onVpnConnectionChange = onVpnConnectionChange1;
    }


    public void changeVPNConnection(VPNConnection vpnConnection) {
        if (onVpnConnectionChange != null) {
            onVpnConnectionChange.onConnectionChanged(vpnConnection);
        }
    }
}
