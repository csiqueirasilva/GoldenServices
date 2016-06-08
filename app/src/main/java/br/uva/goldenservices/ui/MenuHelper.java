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
            case R.layout.trabalho_aguardar_prestador:
            case R.layout.login:
                break;
            default:
                defaultMenu(id, menu);
        }
    }

    public static void defaultMenu(int id, Menu menu) {
        menu.add(id, 1, 1, "Listar ofertas de servicos");
        menu.add(id, 2, 2, "Listar trabalhos pendentes");
        menu.add(id, 3, 3, "Criar servico");
        menu.add(id, 4, 4, "Ver recibo de servicos oferecidos");
        menu.add(id, 5, 5, "Ver recibo de servicos consumidos");
        menu.add(id, 9, 9, "Tela de boas vindas");
        menu.add(id, 10, 10, "Desconectar");
    }

    public static boolean resolve(int id, int op) {
        boolean ret = true;

        switch(id) {
            case R.layout.cadastrousuario:
                ret = cadastroUsuario(op);
                break;
            case R.layout.trabalho_aguardar_prestador:
            case R.layout.login:
                break;
            default:
                ret = defaultMenuOps(op);
        }

        return ret;
    }

    // cases

    private static boolean defaultMenuOps(int op) {
        boolean ret = false;

        if(op == 1) {
             //todo
            Helper.changeView(R.layout.listar_servicos);
            ret = true;
        } else if (op == 2) {
            //todo
            ret = true;
        } else if (op == 3) {
            //todo
            Helper.changeView(R.layout.criarservico);
            ret = true;
        } else if (op == 4) {
            //todo
            Helper.changeView(R.layout.listatrabalhoefetuado);
            ret = true;
        } else if (op == 5) {
            //todo
            ret = true;
        } else if (op == 9) {
            Helper.changeView(R.layout.telainiciallogado);
            ret = true;
        } else if (op == 10) {
            Helper.changeView(R.layout.login);
            ret = true;
        }

        return ret;
    }

    private static boolean cadastroUsuario(int op) {
        boolean ret = false;

        if(op == 1) {
            Helper.changeView(R.layout.login);
            ret = true;
        }

        return ret;
    }

}
