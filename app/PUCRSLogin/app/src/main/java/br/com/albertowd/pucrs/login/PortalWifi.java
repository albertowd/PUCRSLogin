package br.com.albertowd.pucrs.login;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

final class PortalWifi {
    /**
     * Única instância da classe no aplicativo.
     */
    private static PortalWifi ourInstance = new PortalWifi();

    /**
     * Construtor privado para não ser chamado.
     */
    private PortalWifi() {
        // Não faz nada mesmo.
    }

    /**
     * Verifica se existe conexão com a rede PORTAL.
     *
     * @param context Contexto para pegar as informações da rede.
     * @return True se pode tentar autenticar.
     */
    boolean hasPortalConnection(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR)
                    return wifiInfo.getSSID().equalsIgnoreCase("PORTAL");
            }
        }
        return false;
    }

    /**
     * @return A única instância da classe no aplicativo.
     */
    static PortalWifi getInstance() {
        return ourInstance;
    }
}
