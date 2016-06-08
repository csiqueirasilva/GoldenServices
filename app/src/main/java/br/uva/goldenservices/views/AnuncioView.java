package br.uva.goldenservices.views;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.uva.goldenservices.R;
import br.uva.goldenservices.adapters.GenericListAdapter;
import br.uva.goldenservices.ui.Helper;
import br.uva.goldenservices.ui.OnClick;
import golden.services.http.ConnectorWebService;
import golden.services.model.anuncios.Anuncio;
import golden.services.model.anuncios.ListaAnuncios;
import golden.services.model.anuncios.TipoServico;
import golden.services.model.usuarios.Usuario;

/**
 * Created by csiqueira on 07/06/16.
 */
public class AnuncioView {

    static private Long currentId;

    public static void list() {

        ListaAnuncios listaAnuncio = ConnectorWebService.listarAnuncio();

        if (listaAnuncio != null && listaAnuncio.getListaAnuncios() != null) {

            List<Anuncio> listaAnuncios = listaAnuncio.getListaAnuncios();

            GenericListAdapter<Anuncio> anuncioAdapter = new GenericListAdapter<Anuncio>(Helper.getActivity(), R.layout.list_servico_item, listaAnuncios, true) {
                @Override
                protected void onView(Anuncio a, View v) {
                    TextView viewNomeAnuncio = (TextView) v.findViewById(R.id.txtNomeServico);
                    viewNomeAnuncio.setText(a.getAreaDeAtuacao());
                    v.setTag(a.getId());
                }
            };

            ListView lista = (ListView) Helper.getActivity().findViewById(R.id.listAnuncio);
            lista.setAdapter(anuncioAdapter);

            TextView emptyList = (TextView) Helper.getActivity().findViewById(R.id.listAnuncioVazia);
            lista.setEmptyView(emptyList);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OnClick.resolve(view);
                }
            });
        }

    }

    public static void load() {
        String idAnuncio = currentId != null ? currentId.toString() : "-1";
        Anuncio anuncio = ConnectorWebService.obterAnuncio(idAnuncio);
        Usuario usuario = ConnectorWebService.getUsuarioLogado();
        if(anuncio != null) {
            TextView t = (TextView) Helper.getActivity().findViewById(R.id.anuncioTituloView);
            t.setText(anuncio.getAreaDeAtuacao());

            t = (TextView) Helper.getActivity().findViewById(R.id.anuncioDescricaoText);
            t.setText(anuncio.getDescricao());

            t = (TextView) Helper.getActivity().findViewById(R.id.anuncioPrecoView);
            String precoString = anuncio.getTipoDeServico().toString();

            if(anuncio.getTipoDeServico() == TipoServico.PAGO) {
                precoString += " - " + anuncio.getPreco().floatValue();
            }

            t.setText(precoString);

            t = (TextView) Helper.getActivity().findViewById(R.id.anuncioRegiaoView);
            t.setText(anuncio.getRegiao());

            Button btn = (Button) Helper.getActivity().findViewById(R.id.anuncioAceitarViewBtn);

            if(usuario.getId() == anuncio.getPrestador().getId()) {
                btn.setText("Você criou esse anúncio");
                btn.setEnabled(false);
            } else {
                btn.setText("Eu quero");
                btn.setEnabled(true);
            }
        }
    }


    public static Long getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(Long currentId) {
        AnuncioView.currentId = currentId;
    }

}