package br.com.albertowd.pucrs.login;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class LoginService extends Service implements PortalInterface {
    /**
     * Flag para não mandar uma requisição nova sem a anterior responder.
     */
    private boolean waitingResponse;

    /**
     * Thread para ficar fazendo o login.
     */
    private final Thread loginThread;

    /**
     * Construtor padrão para criar a thread de login.
     */
    public LoginService() {
        super();
        loginThread = new Thread() {
            @Override
            public void run() {
                waitingResponse = false;
                while (!super.isInterrupted()) {
                    // Não envia outra requisição enquanto a anterior não responder.
                    if (!waitingResponse)
                        new PortalTask(LoginService.this, Preferences.getInstance().getUser(LoginService.this), Preferences.getInstance().getPass(LoginService.this));
                    try {
                        // Pausa por 60 segundos.
                        Thread.sleep(60 * 1000);
                    } catch (Exception exception) {
                        Log.e(Preferences.LOG_NAME, "Thread de login interrompida.", exception);
                        break;
                    }
                }
            }
        };
    }

    private void stopServiceAndBuildNotification(int message) {
        Preferences.getInstance().setServiceActive(this, false);

        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle(super.getText(R.string.app_name));
        builder.setContentText(super.getText(message));
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_stat_name);

        NotificationManager mNotifyMgr = (NotificationManager) super.getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, builder.build());
        super.stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(Preferences.LOG_NAME, "Serviço de login criado.");
        loginThread.start();
    }

    @Override
    public void onDestroy() {
        Log.v(Preferences.LOG_NAME, "Serviço de login destruído.");
        loginThread.interrupt();
        super.onDestroy();
    }

    @Override
    public void onError(int message) {
        waitingResponse = false;
        this.stopServiceAndBuildNotification(message);
    }

    @Override
    public void onSuccess(int message) {
        waitingResponse = false;
        if (message != R.string.toast_authenticated && message != R.string.toast_success)
            this.stopServiceAndBuildNotification(message);
    }
}
