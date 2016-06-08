package br.uva.goldenservices.views;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.uva.goldenservices.R;
import br.uva.goldenservices.ui.Helper;
import golden.services.http.ConnectorWebService;
import golden.services.model.anuncios.Anuncio;
import golden.services.model.anuncios.TipoServico;
import golden.services.model.usuarios.Usuario;

/**
 * Created by csiqueira on 07/06/16.
 */
public class AnuncioView {

    static private Long currentId;

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