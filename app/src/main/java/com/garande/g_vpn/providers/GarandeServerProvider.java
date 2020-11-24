package com.garande.g_vpn.providers;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.garande.g_vpn.interfaces.GarandeServerObserver;
import com.garande.g_vpn.model.DeviceInfo;
import com.garande.g_vpn.model.GServerResponse;
import com.garande.g_vpn.model.ServerConstant;
import com.garande.g_vpn.utils.DeviceDataUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import static com.garande.g_vpn.utils.Constants.SET_USERID;

public class GarandeServerProvider {
    private String serverAddress = "ws://f1612dba56b3.ngrok.io";
    private URI uri;
    private WebSocketClient webSocketClient;

    private GarandeServerObserver serverObserver;

    DeviceInfo deviceInfo;

    DeviceDataUtils deviceDataUtils;

    public GarandeServerProvider(Context activity){
        deviceDataUtils = new DeviceDataUtils();
        deviceInfo = deviceDataUtils.getDeviceInfo(activity);
    }

    public void setGarandeServerObserver(GarandeServerObserver garandeServerObserver){
        this.serverObserver = garandeServerObserver;
    }


    private String getDeviceID(){
        return "ANDROID_DEVICE";
    }


    public void sendMessage(GServerResponse gServerResponse){
//        if(webSocketClient != null){
            Log.d("WEBSOCKET", gServerResponse.toStringJson());
            webSocketClient.send(gServerResponse.toStringJson());
//        }
     }

    public void connectWebSocket(){
        Log.d("WEBSOCKET", "CONNECTING");
        try {
            uri = new URI(serverAddress);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                if(deviceInfo != null){
                    GServerResponse gServerResponse = new GServerResponse(SET_USERID, deviceInfo.ID);
                    sendMessage(gServerResponse);
                    if(serverObserver != null){
                        serverObserver.onServerConnected();
                    }
                }else {
                    webSocketClient.close();
                }

            }

            @Override
            public void onMessage(String message) {
                GServerResponse gServerResponse = new GServerResponse().getResponse(message);
                switch (gServerResponse.type){
                    case "START_VPN":
                        serverObserver.onDataReceived(ServerConstant.START_VPN, gServerResponse);
                        break;
                    case "STOP_VPN":
                        serverObserver.onDataReceived(ServerConstant.STOP_VPN, gServerResponse);
                        break;
                    case "CLOSE_APP":
                        serverObserver.onDataReceived(ServerConstant.CLOSE_APP, gServerResponse);
                        break;
                    case "GET_DEVICE_INFO":
                        gServerResponse = new GServerResponse("GET_DEVICE_INFO", deviceInfo);
                        sendMessage(gServerResponse);
                        serverObserver.onDataReceived(ServerConstant.GET_DEVICE_INFO, gServerResponse);
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("SERVER_CLOSED: ", reason);
                serverObserver.onServerDisconnected(reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.e("WEBSOCKET", null, ex);
            }
        };

        Log.i("WEBSOCKET", "ON_CONNECTING");
        webSocketClient.connect();
    }

}
