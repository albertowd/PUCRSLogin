package br.com.albertowd.pucrs.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.albertowd.pucrs.login.validators.PassValidator;
import br.com.albertowd.pucrs.login.validators.UserValidator;

public class MainActivity extends AppCompatActivity implements PortalInterface {
    /**
     * Botão de login.
     */
    private Button btLogin;

    /**
     * Input da senha de login.
     */
    private EditText etPass;

    /**
     * Input do usuário de login.
     */
    private EditText etUser;

    /**
     * Imagem do logo para o click.
     */
    private ImageView ivLogo;

    /**
     * Switch para rodar o aplicativo em serviço ou não.
     */
    private CheckBox cbService;

    /**
     * Configura os listeners da tela.
     */
    private void setupListeners() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PortalWifi.getInstance().hasPortalConnection(MainActivity.this)) {
                    MainActivity.this.toggleLoginSwitch(false);
                    new PortalTask(MainActivity.this, etUser.getText().toString(), etPass.getText().toString());
                } else
                    Toast.makeText(MainActivity.this, MainActivity.super.getText(R.string.network_unavailable), Toast.LENGTH_SHORT).show();
            }
        });
        ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, MainActivity.super.getText(R.string.about).toString(), Toast.LENGTH_LONG).show();
            }
        });
        cbService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Preferences.getInstance().setServiceActive(MainActivity.this, isChecked);
                btLogin.setEnabled(!isChecked);
                if (isChecked) {
                    Preferences.getInstance().setPass(MainActivity.this, etPass.getText().toString());
                    Preferences.getInstance().setUser(MainActivity.this, etUser.getText().toString());
                    etPass.setEnabled(false);
                    etUser.setEnabled(false);
                    MainActivity.super.startService(new Intent(MainActivity.this, LoginService.class));
                } else {
                    etPass.setEnabled(true);
                    etUser.setEnabled(true);
                    MainActivity.super.stopService(new Intent(MainActivity.this, LoginService.class));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        // Carrega as instâncias do layout na classe.
        btLogin = (Button) super.findViewById(R.id.btLogin);
        etPass = (EditText) super.findViewById(R.id.etPass);
        etUser = (EditText) super.findViewById(R.id.etUser);
        ivLogo = (ImageView) super.findViewById(R.id.ivLogo);
        cbService = (CheckBox) super.findViewById(R.id.cbService);

        // Adiciona os listeners de input.
        etPass.addTextChangedListener(new PassValidator(etPass, this));
        etUser.addTextChangedListener(new UserValidator(etUser, this));

        // Configura os listeners da tela.
        this.setupListeners();
    }

    @Override
    public void onError(int message) {
        Toast.makeText(this, super.getText(message), Toast.LENGTH_LONG).show();
        this.toggleLoginSwitch(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Atualiza os dados.
        etPass.setText(Preferences.getInstance().getPass(this));
        etUser.setText(Preferences.getInstance().getUser(this));
        cbService.setChecked(Preferences.getInstance().isServiceActive(this));
    }

    @Override
    public void onSuccess(int message) {
        Preferences.getInstance().setUser(this, etUser.getText().toString());
        Preferences.getInstance().setPass(this, etPass.getText().toString());
        Toast.makeText(this, super.getText(message), Toast.LENGTH_LONG).show();
        this.toggleLoginSwitch(true);
    }

    /**
     * Altera os botões de login e serviço.
     *
     * @param enable True para habilitar os botões, false para desabilitar.
     */
    public void toggleLoginSwitch(boolean enable) {
        boolean validInputs = etPass.getError() == null && etUser.getError() == null;
        btLogin.setEnabled(enable && !Preferences.getInstance().isServiceActive(this) && validInputs);
        cbService.setEnabled(enable && validInputs);
    }
}
