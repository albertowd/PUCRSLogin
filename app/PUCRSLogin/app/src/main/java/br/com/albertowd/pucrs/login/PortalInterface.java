package br.com.albertowd.pucrs.login;

import android.content.Context;

interface PortalInterface {
    /**
     * @return O contexto do aplicativo.
     */
    Context getApplicationContext();

    /**
     * Callback para quando houve erro na requisição de login/logout.
     *
     * @param message Identificador da mensagem para o usuário.
     */
    void onError(int message);

    /**
     * Callback para quando houve sucesso na requisição de login/logout.
     *
     * @param message Identificador da mensagem para o usuário
     */
    void onSuccess(int message);
}
