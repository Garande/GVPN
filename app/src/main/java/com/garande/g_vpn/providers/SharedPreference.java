package com.garande.g_vpn.providers;

import android.content.Context;
import android.content.SharedPreferences;

import com.garande.g_vpn.model.Server;

public class SharedPreference {
    private static final String SERVER_COUNTRY = "country";
    private static final String SERVER_OVPN = "ovpn";
    private static final String SERVER_USER_NAME = "server_user";
    private static final String SERVER_PASSWORD = "server_password";


    private final String PREF_KEY = "gVpnKeyReference";

    private SharedPreferences mPreference;
    private SharedPreferences.Editor mPreferenceEditor;
    private Context context;

    public SharedPreference(Context context) {
        this.mPreference = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        this.mPreferenceEditor = mPreference.edit();
        this.context = context;
    }



    public void saveServer(Server server) {
        mPreferenceEditor.putString(SERVER_COUNTRY, server.country);
        mPreferenceEditor.putString(SERVER_OVPN, server.ovpn);
        mPreferenceEditor.putString(SERVER_USER_NAME, server.userName);
        mPreferenceEditor.putString(SERVER_PASSWORD, server.password);
        mPreferenceEditor.commit();
    }

    public Server getServer(){
        Server server = new Server();
        server.country = mPreference.getString(SERVER_COUNTRY, "Japan");
        server.ovpn = mPreference.getString(SERVER_OVPN,"japan.ovpn");
        server.userName = mPreference.getString(SERVER_USER_NAME, "ovpn");
        mPreference.getString(SERVER_PASSWORD, "vpn");

        return server;
    }
}
