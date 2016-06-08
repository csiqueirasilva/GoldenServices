package br.uva.goldenservices.views;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import br.uva.goldenservices.R;
import br.uva.goldenservices.adapters.GenericListAdapter;
import br.uva.goldenservices.ui.Helper;
import br.uva.goldenservices.ui.OnClick;
import golden.services.http.ConnectorWebService;
import golden.services.model.trabalhos.ListaTrabalhos;
import golden.services.model.trabalhos.Trabalho;

/**
 * Created by csiqueira on 08/06/16.
 */
public class AvaliacaoView {

    private static Long idTrabalho;

    public static void list() {
        ListaTrabalhos listaTrabalhos = ConnectorWebService.listarTrabalhoClienteNaoAvaliado();
        if(listaTrabalhos != null && listaTrabalhos.getTrabalhos() != null) {
            List<Trabalho> trabalhos = listaTrabalhos.getTrabalhos();

            GenericListAdapter<Trabalho> trabalhoAdapter = new GenericListAdapter<Trabalho>(Helper.getActivity(), R.layout.list_avaliacao_item, trabalhos, false) {
                @Override
                protected void onView(Trabalho t, View v) {
                    TextView viewNomeAnuncio = (TextView) v.findViewById(R.id.listAvaliacaoTituloAnuncio);
                    viewNomeAnuncio.setClickable(true);
                    viewNomeAnuncio.setText(t.getAnuncio().getAreaDeAtuacao() + " - Prestador: " + t.getAnuncio().getPrestador().getNome());
                    viewNomeAnuncio.setTag(t.getAnuncio().getId());
                    viewNomeAnuncio.setOnClickListener(OnClick.getOnClickListener());
                    TextView infoTempo = (TextView) v.findViewById(R.id.listAvaliacaoInfoTempo);
                    infoTempo.setText("Encerrado em: " + DateFormat.getInstance().format(t.getDatafim()));
                    View view = v.findViewById(R.id.listAvaliacaoAvaliarBtn);
                    view.setTag(t.getId());
                    view.setOnClickListener(OnClick.getOnClickListener());
                }
            };

            ListView lista = (ListView) Helper.getActivity().findViewById(R.id.listAvaliacao);
            lista.setAdapter(trabalhoAdapter);

            TextView emptyList = (TextView) Helper.getActivity().findViewById(R.id.listAvaliacaoVazia);
            lista.setEmptyView(emptyList);

            OnClick.fillOnClickCallbacks();
            OnClick.setOnClickListener();
        }

    }

    public static Long getIdTrabalho() {
        return idTrabalho;
    }

    public static void setIdTrabalho(Long idTrabalho) {
        AvaliacaoView.idTrabalho = idTrabalho;
    }

    public static void populate() {
        if(idTrabalho != null) {
            Activity v = Helper.getActivity();
            Trabalho trabalho = ConnectorWebService.obterTrabalho(idTrabalho.toString());
            if(trabalho != null) {
                TextView dataServico = (TextView) v.findViewById(R.id.avaliacaoDataServico);
                TextView nomeUsuarioPrestador = (TextView) v.findViewById(R.id.avaliacaoNomeUsuario);
                TextView avaliacaoTipoServico = (TextView) v.findViewById(R.id.avaliacaoTipoServico);
                Button btn = (Button) v.findViewById(R.id.avaliacaoBtnEnviar);
                DateFormat df = DateFormat.getInstance();
                dataServico.setText("De " + df.format(trabalho.getDatainicio()) + " até " + df.format(trabalho.getDatafim()));
                avaliacaoTipoServico.setText(trabalho.getAnuncio().getAreaDeAtuacao() + " - " + trabalho.getAnuncio().getTipoDeServico().toString());
                nomeUsuarioPrestador.setText(trabalho.getAnuncio().getPrestador().getNome());
                btn.setOnClickListener(OnClick.getOnClickListener());
                btn.setEnabled(true);
            } else {
                Helper.alert("Erro ao obter trabalho");
                Helper.changeView(R.id.login);
            }
        }
    }
}