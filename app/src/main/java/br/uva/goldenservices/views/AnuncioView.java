package br.uva.goldenservices.views;

import android.view.View;
import android.widget.TextView;

import br.uva.goldenservices.R;
import br.uva.goldenservices.ui.Helper;
import golden.services.http.ConnectorWebService;
import golden.services.model.anuncios.Anuncio;
import golden.services.model.anuncios.TipoServico;

/**
 * Created by csiqueira on 07/06/16.
 */
public class AnuncioView {

    static private Long currentId;

    public static void load() {
        String idAnuncio = currentId != null ? currentId.toString() : "-1";
        Anuncio anuncio = ConnectorWebService.obterAnuncio(idAnuncio);
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

            View btn = Helper.getActivity().findViewById(R.id.anuncioAceitarViewBtn);
            btn.setTag(idAnuncio);
        }
    }


    public static Long getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(Long currentId) {
        AnuncioView.currentId = currentId;
    }

}