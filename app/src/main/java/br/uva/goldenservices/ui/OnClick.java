package br.uva.goldenservices.ui;

import android.app.Activity;
import android.view.View;

import java.util.HashMap;
import java.util.Set;

import br.uva.goldenservices.R;
import br.uva.goldenservices.views.AnuncioView;
import br.uva.goldenservices.views.TrabalhoView;
import golden.services.http.ConnectorWebService;
import golden.services.model.usuarios.Usuario;

/**
 * Created by csiqueira on 29/05/2016.
 */
public class OnClick {

    private final static HashMap<Integer, OnClickCallback> callbacks = new HashMap();

    private final static View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            OnClick.resolve(v);
        }
    };

    public static void resolve(View v) {
        OnClickCallback onClickCallback = callbacks.get(v.getId());
        if(onClickCallback != null) {
            onClickCallback.onClick(v);
        }
    }

    private static void login() {
        String[] values = Helper.getStringValues(true, R.id.email, R.id.senha);
        ConnectorWebService.logarUsuario(values[0], values[1]);
        Usuario usuarioLogado = ConnectorWebService.getUsuarioLogado();
        if (usuarioLogado == null) {
            Helper.alert("Erro ao logar!");
        } else {
            Helper.setUsuarioConectado(usuarioLogado);
            Helper.changeView(R.layout.telainiciallogado);
        }
        Helper.enableLastSearch();
    }

    public static void fillOnClickCallbacks() {

        callbacks.put(R.id.ButtonLogin, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        callbacks.put(R.id.formLoginBtnCadastrar, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Helper.changeView(R.layout.cadastrousuario);
            }
        });

        callbacks.put(R.id.fromCriarBtnVoltar, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Helper.changeView(R.layout.login);
            }
        });

        callbacks.put(R.id.telaInicialLogadoLogoff, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Helper.changeView(R.layout.login);
            }
        });

        callbacks.put(R.id.ButtonCriar, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Activity activity = Helper.getActivity();
                FormSubmit.sendCadastroUsuario(activity);
            }
        });

        callbacks.put(R.id.listAnuncioItemView, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Long anuncioId = (Long) v.getTag();
                AnuncioView.setCurrentId(anuncioId);
                Helper.changeView(R.layout.visualizar_anuncio);
            }
        });

        callbacks.put(R.id.anuncioAceitarViewBtn, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Helper.changeView(R.layout.trabalho_aguardar_prestador);
            }
        });

        callbacks.put(R.id.anuncioVoltarAListaViewBtn, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Helper.changeView(R.layout.listar_servicos);
            }
        });

        callbacks.put(R.id.trabalhoLockCancelarBtn, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                TrabalhoView.cancelarTrabalho();
            }
        });

        callbacks.put(R.id.anuncioCriarBtnConfirmar, new OnClickCallback() {
            @Override
            public void onClick(View v) {
                Activity activity = Helper.getActivity();
                FormSubmit.sendCadastroAnuncio(activity);
            }
        });

    }

    public static void setOnClickListener() {

        Set<Integer> ids = callbacks.keySet();
        for (int id : ids) {
            View v = Helper.getActivity().findViewById(id);
            if(v != null && !v.hasOnClickListeners()) {
                v.setOnClickListener(listener);
            }
        }

    }

    public static View.OnClickListener getOnClickListener() {
        return listener;
    }

}