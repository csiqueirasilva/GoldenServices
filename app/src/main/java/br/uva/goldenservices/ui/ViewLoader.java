package br.uva.goldenservices.ui;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.uva.goldenservices.R;
import br.uva.goldenservices.adapters.GenericListAdapter;
import golden.services.http.ConnectorWebService;
import golden.services.model.anuncios.Anuncio;
import golden.services.model.anuncios.ListaAnuncios;
import golden.services.model.usuarios.Usuario;

/**
 * Created by caio on 30/05/16.
 */
public class ViewLoader {

    private static void listAnuncio() {

        ListaAnuncios listaAnuncio = ConnectorWebService.listarAnuncioet();

        if (listaAnuncio != null && listaAnuncio.getListaAnuncios() != null) {

            List<Anuncio> listaAnuncios = listaAnuncio.getListaAnuncios();

            GenericListAdapter<Anuncio> anuncioAdapter = new GenericListAdapter<Anuncio>(Helper.getActivity(), R.layout.list_servico_item, listaAnuncios) {
                @Override
                protected void onView(Anuncio a, View v) {
                    TextView viewNomeAnuncio = (TextView) v.findViewById(R.id.txtNomeServico);
                    viewNomeAnuncio.setText(a.getAreaDeAtuacao());
                    v.setTag("anuncioListId" + a.getId());
                }
            };

            ListView lista = (ListView) Helper.getActivity().findViewById(R.id.listAnuncio);
            lista.setAdapter(anuncioAdapter);
        }

    }

    public static void initialize(int id) {
        if (id != R.layout.login && id != R.layout.cadastrousuario) {
            Usuario usuarioLogado = ConnectorWebService.getUsuarioLogado();
            Helper.setUsuarioConectado(usuarioLogado);
            if (usuarioLogado == null) {
                Helper.changeView(R.layout.login);
            } else if (id == R.layout.telainiciallogado) {
                TextView label = (TextView) Helper.getActivity().findViewById(R.id.telaInicialLogadoUsuarioConectado);
                label.setText(usuarioLogado.getEmail() + " " + usuarioLogado.getNome());
            } else if (id == R.layout.listar_servicos) {
                listAnuncio();
            }
        }
    }
}
