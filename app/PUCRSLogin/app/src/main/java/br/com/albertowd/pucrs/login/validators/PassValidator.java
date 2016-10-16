package br.com.albertowd.pucrs.login.validators;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.albertowd.pucrs.login.MainActivity;
import br.com.albertowd.pucrs.login.R;

public final class PassValidator implements TextWatcher {
    /**
     * Input da senha de login.
     */
    private final EditText etPass;

    /**
     * Atividade para habilitar/desabilitar botões.
     */
    private final MainActivity activity;

    /**
     * Construtor padrão que recebe o input de senha de login.
     *
     * @param etPass   Input da senha de login.
     * @param activity Atividade para habilitar/desabilitar botões.
     */
    public PassValidator(EditText etPass, MainActivity activity) {
        this.activity = activity;
        this.etPass = etPass;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            etPass.setError(activity.getText(R.string.password_invalid));
            activity.toggleLoginSwitch(false);
        } else {
            etPass.setError(null);
            activity.toggleLoginSwitch(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não faz nada mesmo.
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não faz nada mesmo.
    }
}
