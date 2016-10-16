package br.com.albertowd.pucrs.login.validators;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.albertowd.pucrs.login.MainActivity;
import br.com.albertowd.pucrs.login.R;

public final class UserValidator implements TextWatcher {
    /**
     * Input do usuário de login.
     */
    private final EditText etUser;

    /**
     * Atividade para habilitar/desabilitar botões.
     */
    private final MainActivity activity;

    /**
     * Construtor padrão que recebe o input de usuário de login.
     *
     * @param etUser   Input do usuário de login.
     * @param activity Atividade para habilitar/desabilitar botões.
     */
    public UserValidator(EditText etUser, MainActivity activity) {
        this.activity = activity;
        this.etUser = etUser;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() != 8) {
            etUser.setError(activity.getText(R.string.username_invalid));
            activity.toggleLoginSwitch(false);
        } else {
            etUser.setError(null);
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
