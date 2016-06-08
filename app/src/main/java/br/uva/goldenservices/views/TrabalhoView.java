package br.uva.goldenservices.views;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.uva.goldenservices.R;
import br.uva.goldenservices.adapters.GenericListAdapter;
import br.uva.goldenservices.ui.Helper;
import br.uva.goldenservices.ui.OnClick;
import golden.services.http.ConnectorWebService;
import golden.services.model.anuncios.Anuncio;
import golden.services.model.trabalhos.EstadoTrabalho;
import golden.services.model.trabalhos.ListaTrabalhos;
import golden.services.model.trabalhos.PapelTrabalho;
import golden.services.model.trabalhos.Trabalho;
import golden.services.model.trabalhos.TrabalhoAtual;
import golden.services.model.usuarios.Usuario;

/**
 * Created by csiqueira on 07/06/16.
 */
public class TrabalhoView {

    private static final Handler handler = new Handler();
    private static Long trabalhoId;

    public static void finalizarTrabalho () {
        if(trabalhoId != null) {

        }
    }

    public static void negarTrabalho(Long id) {
        Trabalho trabalho = ConnectorWebService.negarTrabalho(id.toString());
        if(trabalho == null) {
            Helper.alert("Erro ao negar trabalho");
        } else {
            Helper.alert("Trabalho cancelado");
            Helper.changeView(R.layout.lista_trabalho_prestador);
        }
    }

    public static void aceitarTrabalho(Long id) {

    }

    public static void cancelarTrabalho() {
        if(trabalhoId != null) {
            if (ConnectorWebService.cancelarTrabalho(trabalhoId.toString()) > 0) {
                Helper.changeView(R.layout.visualizar_anuncio);
                Helper.alert("Pedido cancelado");
                trabalhoId = null;
            } else {
                Helper.alert("Erro ao cancelar pedido");
            }
        }
    }

    public static void criarTrabalho() {
        Long anuncioId = AnuncioView.getCurrentId();
        if(anuncioId != null && trabalhoId == null) {
            Trabalho trabalho = ConnectorWebService.criarTrabalho(anuncioId.toString());
            if(trabalho == null) {
                Helper.alert("Erro ao criar trabalho");
                Helper.changeView(R.layout.visualizar_anuncio);
            } else {
                trabalhoId = trabalho.getId();
                Helper.alert(trabalhoId + ""); // debug
                // start timer
            }
        }
    }

    public static Long getTrabalhoId() {
        return trabalhoId;
    }

    public static void setTrabalhoId(Long trabalhoId) {
        TrabalhoView.trabalhoId = trabalhoId;
    }

    public static boolean resolve(TrabalhoAtual trabalhoAtual) {
        boolean ret = true;

        if(trabalhoAtual != null) {
            Usuario usuarioConectado = Helper.getUsuarioConectado();
            PapelTrabalho papel = trabalhoAtual.getPapel();
            Trabalho trabalho = trabalhoAtual.getTrabalho();
            trabalhoId = trabalho.getId();
            AnuncioView.setCurrentId(trabalho.getAnuncio().getId());

            if (trabalho.getEstado() == EstadoTrabalho.NAO_INICIADO) {
                if (papel == PapelTrabalho.USUARIO) {
                    Helper.simpleChangeView(R.layout.trabalho_aguardar_prestador);
                    ret = false;
                } else {
                    ret = true;
                }
            } else if (trabalho.getEstado() == EstadoTrabalho.EFETUANDO) {
                if (papel == PapelTrabalho.USUARIO) {

                } else {
                    //todo
                }

                ret = false;
            }
        }

        return ret;
    }

    public static void listPrestador() {

        ListaTrabalhos listaTrabalhos = ConnectorWebService.listarTrabalhoPrestador();
        if(listaTrabalhos != null && listaTrabalhos.getTrabalhos() != null) {
            List<Trabalho> trabalhos = listaTrabalhos.getTrabalhos();

            GenericListAdapter<Trabalho> trabalhoAdapter = new GenericListAdapter<Trabalho>(Helper.getActivity(), R.layout.list_trabalho_item, trabalhos, false) {
                @Override
                protected void onView(Trabalho t, View v) {
                    TextView viewNomeAnuncio = (TextView) v.findViewById(R.id.listTrabalhoTituloAnuncio);
                    viewNomeAnuncio.setClickable(true);
                    viewNomeAnuncio.setText(t.getAnuncio().getAreaDeAtuacao());
                    viewNomeAnuncio.setTag(t.getAnuncio().getId());
                    viewNomeAnuncio.setOnClickListener(OnClick.getOnClickListener());
                    TextView viewNomeCliente = (TextView) v.findViewById(R.id.listTrabalhoUsuario);
                    viewNomeCliente.setText(t.getUsuario().getNome() + " (" + t.getUsuario().getEmail() + " " + t.getUsuario().getTelefone() + ")");
                    View view = v.findViewById(R.id.listTrabalhoAceitar);
                    view.setTag(t.getId());
                    view.setOnClickListener(OnClick.getOnClickListener());
                    view = v.findViewById(R.id.listTrabalhoNegar);
                    view.setTag(t.getId());
                    view.setOnClickListener(OnClick.getOnClickListener());
                }
            };

            ListView lista = (ListView) Helper.getActivity().findViewById(R.id.listTrabalhoPrestador);
            lista.setAdapter(trabalhoAdapter);

            TextView emptyList = (TextView) Helper.getActivity().findViewById(R.id.listTrabalhoPrestadorVazia);
            lista.setEmptyView(emptyList);

            OnClick.fillOnClickCallbacks();
            OnClick.setOnClickListener();
        }
    }
}
