package br.com.albertowd.pucrs.login;

import android.content.Context;
import android.content.SharedPreferences;

final class Preferences {
    /**
     * Única instância da classe no aplicativo.
     */
    private static final Preferences SINGLETON = new Preferences();

    /**
     * Chave para a configuração de serviço do aplicativo.
     */
    private static final String SERVICE_ACTIVE = "SERVICE_ACTIVE";

    /**
     * Chave para a configuração de senha de login.
     */
    private static final String PASS = "PASS";

    /**
     * Chave para a configuração de usuário de login.
     */
    private static final String USER = "USER";

    /**
     * @return A única instância da classe no aplicativo.
     */
    static Preferences getInstance() {
        return SINGLETON;
    }

    /**
     * Nome usado no log do aplicativo.
     */
    static final String LOG_NAME = "PUCRSLogin";

    /**
     * URL usada no login/logout da rede.
     */
    static final String PORTAL_URL = "https://wifiportal.pucrs.br/login.html";

    /**
     * Classe de configurações do aplicativo.
     */
    private SharedPreferences preferences;

    /**
     * Construtor privado para não ser usado.
     */
    private Preferences() {
    }

    /**
     * Métodos para carregar a classe de preferências do android.
     *
     * @param context Contexto usado para pegar o contexto do aplicativo, usado para as configurações.
     * @return Classe de preferências do aplicativo.
     */
    private SharedPreferences getPreferences(Context context) {
        if (preferences == null && context != null)
            preferences = context.getApplicationContext().getSharedPreferences("PUCRS_LOGIN", Context.MODE_PRIVATE);
        return preferences;
    }

    /**
     * @param context Contexto para carregar as configurações, caso necessário.
     * @return Flag indicando se é para o aplicativo rodar como serviço.
     */
    Boolean isServiceActive(Context context) {
        return this.getPreferences(context).getBoolean(SERVICE_ACTIVE, false);
    }

    /**
     * @param context Contexto para carregar as configurações, caso necessário.
     * @return Senha de login do aplicativo.
     */
    String getPass(Context context) {
        return this.getPreferences(context).getString(PASS, "");
    }

    /**
     * @param context Contexto para carregar as configurações, caso necessário.
     * @return Usuário de login do aplicativo.
     */
    String getUser(Context context) {
        return this.getPreferences(context).getString(USER, "");
    }

    /**
     * Atualiza a flag para o aplicativo rodar como serviço ou não.
     *
     * @param context    Contexto para carregar as configurações, caso necessário.
     * @param keepLogged Flag para indicar se o aplicativo roda como serviço ou não.
     */
    void setServiceActive(Context context, Boolean keepLogged) {
        SharedPreferences.Editor editor = this.getPreferences(context).edit();
        editor.putBoolean(SERVICE_ACTIVE, keepLogged);
        editor.apply();
    }

    /**
     * Atualiza a senha de login do aplicativo.
     *
     * @param context Contexto para carregar as configurações, caso necessário.
     * @param pass    Nova senha de login do aplicativo.
     */
    void setPass(Context context, String pass) {
        SharedPreferences.Editor editor = this.getPreferences(context).edit();
        editor.putString(PASS, pass);
        editor.apply();
    }

    /**
     * Atualiza o usuário de login do aplicativo.
     *
     * @param context Contexto para carregar as configurações, caso necessário.
     * @param user    Novo usuário de login do aplicativo.
     */
    void setUser(Context context, String user) {
        SharedPreferences.Editor editor = this.getPreferences(context).edit();
        editor.putString(USER, user);
        editor.apply();
    }
}
