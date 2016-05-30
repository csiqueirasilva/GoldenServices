package br.uva.goldenservices.ui;

import android.view.Menu;

import br.uva.goldenservices.R;

/**
 * Created by caio on 30/05/16.
 */
public class MenuHelper {

    public static void fillOptions(int id, Menu menu) {
        switch(id) {
            case R.layout.cadastrousuario:
                menu.add(id, 1, 1, "Voltar a tela de login");
                break;
        }
    }

    public static boolean resolve(int id, int op) {
        boolean ret = true;

        switch(id) {
            case R.layout.cadastrousuario:
                cadastroUsuario(op);
                break;
        }

        return ret;
    }

    // cases

    public static boolean cadastroUsuario(int op) {
        boolean ret = false;

        if(op == 1) {
            Helper.changeView(R.layout.login);
        }

        return ret;
    }

}
