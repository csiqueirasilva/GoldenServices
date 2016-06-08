package br.uva.goldenservices.ui;

import android.widget.TextView;

import br.uva.goldenservices.R;
import br.uva.goldenservices.views.AnuncioView;
import br.uva.goldenservices.views.AvaliacaoView;
import br.uva.goldenservices.views.TrabalhoView;
import golden.services.http.ConnectorWebService;
import golden.services.model.trabalhos.TrabalhoAtual;
import golden.services.model.usuarios.Usuario;

/**
 * Created by caio on 30/05/16.
 */
public class ViewLoader {

    public static void initialize(int id) {
        if (id != R.layout.login && id != R.layout.cadastrousuario) {
            Usuario usuarioLogado = ConnectorWebService.getUsuarioLogado();
            Helper.setUsuarioConectado(usuarioLogado);

            if (usuarioLogado == null) {
                Helper.changeView(R.layout.login);
            } else {
                TrabalhoAtual trabalhoAtual = ConnectorWebService.obterTrabalhoAtual();

                boolean redirect = TrabalhoView.resolve(trabalhoAtual);

                if (redirect) {
                    if (id == R.layout.telainiciallogado) {
                        TextView label = (TextView) Helper.getActivity().findViewById(R.id.telaInicialLogadoUsuarioConectado);
                        label.setText(usuarioLogado.getEmail() + " " + usuarioLogado.getNome());
                    } else if (id == R.layout.lista_servicos) {
                        AnuncioView.list();
                    } else if (id == R.layout.visualizar_anuncio) {
                        AnuncioView.load();
                    } else if (id == R.layout.trabalho_aguardar_prestador) {
                        TrabalhoView.criarTrabalho();
                    } else if (id == R.layout.lista_trabalho_prestador) {
                        TrabalhoView.listPrestador();
                    } else if (id == R.layout.lista_avaliacao) {
                        AvaliacaoView.list();
                    } else if (id == R.layout.avaliacaoservico) {
                        AvaliacaoView.populate();
                    }
                }
            }
        }
    }
}
