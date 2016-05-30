package br.uva.goldenservices.ui;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import br.uva.goldenservices.MainActivity;
import br.uva.goldenservices.R;
import golden.services.http.ConnectorWebService;
import golden.services.model.usuarios.Usuario;

/**
 * Created by csiqueira on 29/05/2016.
 */
public class OnClick {

    private final static ArrayList<View> lastSearch = new ArrayList();
    private static MainActivity mainActivity;

    public static void resolve(int viewId) {
        if(viewId == R.id.ButtonLogin) {
            login();
        }
    }

    private static void enableLastSearch() {
        for(View v : lastSearch) {
            v.setEnabled(true);
        }
    }

    private static String[] getStringValues(boolean disable, int... ids) {
        String[] ret = new String[ids.length];

        lastSearch.clear();

        for(int i = 0; i < ids.length; i++) {
            int id = ids[i];
            View v = mainActivity.findViewById(id);
            if(v instanceof EditText) {
                ret[i] = ((EditText) v).getText().toString();
            } else {
                ret[i] = null;
            }

            if(disable) {
                v.setEnabled(false);
            }

            lastSearch.add(v);
        }

        return ret;
    }

    private static void login() {
        String[] values = getStringValues(true, R.id.email, R.id.senha);
        ConnectorWebService.logarUsuario(values[0], values[1]);
        Usuario usuarioLogado = ConnectorWebService.getUsuarioLogado();
        if(usuarioLogado == null) {
            mainActivity.alert.show("Erro ao logar!");
        }
        enableLastSearch();
    }

    public static void initialize(MainActivity extActivity) {
        mainActivity = extActivity;

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                OnClick.resolve(id);
            }
        };

        View buttonLogin = mainActivity.findViewById(R.id.ButtonLogin);
        buttonLogin.setOnClickListener(onClickListener);
    }

}