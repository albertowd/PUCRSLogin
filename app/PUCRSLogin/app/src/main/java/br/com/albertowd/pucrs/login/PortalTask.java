package br.com.albertowd.pucrs.login;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

final class PortalTask implements Listener<String>, Response.ErrorListener {
    /**
     * Classe de callback para a resposta da requisição.
     */
    private final PortalInterface callback;

    /**
     * Construtor com uma classe de callback para uma requisição, usuário e senha.
     *
     * @param callback Classe de callback para a requisição.
     * @param user     Usuário para login, null se for logout.
     * @param pass     Senha para login, null se for logout.
     */
    PortalTask(PortalInterface callback, final String user, final String pass) {
        this.callback = callback;

        // Cria a requisição para login/logout.
        StringRequest request = new StringRequest(Request.Method.POST, Preferences.PORTAL_URL, this, this) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", user);
                params.put("password", pass);
                params.put("buttonClicked", "4");
                params.put("err_flag", "0");
                return params;
            }
        };

        // Manda a requisição para a fila do android.
        Log.v(Preferences.LOG_NAME, "Mandando requisição de login.");
        RequestQueue requestQueue = Volley.newRequestQueue(callback.getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        int message = R.string.toast_problems;
        Log.e(Preferences.LOG_NAME, callback.getApplicationContext().getString(message), error);
        callback.onError(message);
    }

    @Override
    public void onResponse(String response) {
        int message;
        if (response.contains("<title>Logged In</title>"))
            message = R.string.toast_success;
        else {
            if (response.contains("The username specified cannot be used at this time."))
                message = R.string.toast_used;
            else if (response.contains("You are already logged in."))
                message = R.string.toast_authenticated;
            else
                message = R.string.toast_data_problems;
        }
        Log.v(Preferences.LOG_NAME, callback.getApplicationContext().getString(message));
        callback.onSuccess(message);
    }
}
