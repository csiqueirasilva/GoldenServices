package br.uva.goldenservices.ui;

import android.widget.TextView;

import br.uva.goldenservices.R;
import golden.services.http.ConnectorWebService;
import golden.services.model.usuarios.Usuario;

/**
 * Created by caio on 30/05/16.
 */
public class ViewLoader {
    public static void initialize(int id) {
        if(id != R.layout.login && id != R.layout.cadastrousuario) {
            Usuario usuarioLogado = ConnectorWebService.getUsuarioLogado();
            Helper.setUsuarioConectado(usuarioLogado);
            if (usuarioLogado == null) {
                Helper.changeView(R.layout.login);
            } else if (id == R.layout.telainiciallogado) {
                TextView label = (TextView) Helper.getActivity().findViewById(R.id.telaInicialLogadoUsuarioConectado);
                label.setText(usuarioLogado.getEmail() + " " + usuarioLogado.getNome());
            }
        }
    }
}
