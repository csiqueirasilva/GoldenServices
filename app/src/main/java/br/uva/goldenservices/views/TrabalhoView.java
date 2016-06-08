package br.uva.goldenservices.views;

import android.os.Handler;

import br.uva.goldenservices.R;
import br.uva.goldenservices.ui.Helper;
import golden.services.http.ConnectorWebService;
import golden.services.model.trabalhos.EstadoTrabalho;
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
}
