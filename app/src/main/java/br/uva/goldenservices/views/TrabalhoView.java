package br.uva.goldenservices.views;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import br.uva.goldenservices.R;
import br.uva.goldenservices.adapters.GenericListAdapter;
import br.uva.goldenservices.database.recibos.Recibo;
import br.uva.goldenservices.ui.Helper;
import br.uva.goldenservices.ui.OnClick;
import golden.services.http.ConnectorWebService;
import golden.services.model.trabalhos.EstadoTrabalho;
import golden.services.model.trabalhos.ListaTrabalhos;
import golden.services.model.trabalhos.PapelTrabalho;
import golden.services.model.trabalhos.Trabalho;
import golden.services.model.trabalhos.TrabalhoAtual;
import golden.services.model.trabalhos.avaliacoes.Avaliacao;
import golden.services.model.usuarios.Usuario;

/**
 * Created by csiqueira on 07/06/16.
 */
public class TrabalhoView {

    private static final Handler handler = new Handler();
    private static Long trabalhoId;
    private static boolean timer = false;

    public static boolean isTimer() {
        return timer;
    }

    public static void setTimer(boolean timer) {
        TrabalhoView.timer = timer;
    }

    public static void finalizarTrabalho () {
        if(trabalhoId != null) {
            Trabalho trabalho = ConnectorWebService.encerrarTrabalho(trabalhoId.toString());
            if(trabalho == null) {
                Helper.alert("Erro ao encerrar trabalho");
            } else {
                Helper.getActivity().getDBHelper().inserirRecibo("Trabalho prestado realizado: " + trabalho.getAnuncio().getAreaDeAtuacao() + " para " + trabalho.getUsuario().getNome(), Recibo.Tipo.PRESTADOR);
                trabalhoId = null;
                Helper.alert("Trabalho encerrado!");
                Helper.changeView(R.layout.lista_trabalho_prestador);
            }
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
        if(id != null) {
            Trabalho trabalho = ConnectorWebService.confirmarTrabalho(id.toString());
            if(trabalho != null) {
                trabalhoId = trabalho.getId();
                Helper.changeView(R.layout.trabalho_efetuando);
            }
        }
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

    public static void iniciarTimerClienteEfetuando() {
        final int timer = 1000;
        final long trabalhoIdLong = trabalhoId;
        setTimer(true);
        // start timer
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Trabalho trabalhoFetch = ConnectorWebService.obterTrabalho(trabalhoIdLong + "");
                if(isTimer()) {
                    if (trabalhoFetch != null) {
                        if (trabalhoFetch.getEstado() == EstadoTrabalho.EFETUANDO) {
                            handler.postDelayed(this, timer);
                        } else {
                            setTimer(false);
                            Helper.alert("Trabalho concluído! Você pode avaliar o trabalho agora.");
                            AvaliacaoView.setIdTrabalho(trabalhoIdLong);
                            trabalhoId = null;
                            Helper.changeView(R.layout.avaliacaoservico);
                        }
                    }
                }
            }
        }, timer);
    }

    public static void criarTrabalho() {
        Long anuncioId = AnuncioView.getCurrentId();
        if(anuncioId != null) {
            Trabalho trabalho = ConnectorWebService.criarTrabalho(anuncioId.toString());
            if(trabalho == null) {
                Helper.alert("Erro ao criar trabalho");
                Helper.changeView(R.layout.visualizar_anuncio);
            } else {
                trabalhoId = trabalho.getId();
                iniciarTimerClienteAguardandoConfirmacao(trabalho);
            }
        }
    }

    private static void iniciarTimerClienteAguardandoConfirmacao(Trabalho trabalho) {
        final int timer = 1000;
        final long trabalhoIdLong = trabalhoId;
        final EstadoTrabalho estadoInicial = trabalho.getEstado();
        setTimer(true);
        // start timer
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Trabalho trabalhoFetch = ConnectorWebService.obterTrabalho(trabalhoIdLong + "");
                if(isTimer()) {
                    if (trabalhoFetch != null) {
                        if (trabalhoFetch.getEstado() == estadoInicial) {
                            handler.postDelayed(this, timer);
                        } else if (trabalhoFetch.getEstado() == EstadoTrabalho.NEGADO) {
                            setTimer(false);
                            Helper.alert("Trabalho negado pelo prestador!");
                            Helper.changeView(R.layout.visualizar_anuncio);
                        } else if (trabalhoFetch.getEstado() == EstadoTrabalho.EFETUANDO) {
                            Helper.alert("Trabalho iniciado!");
                            Helper.changeView(R.layout.trabalho_efetuando);
                            iniciarTimerClienteEfetuando();
                        } else {
                            setTimer(false);
                            Helper.alert("Erro! O administrador foi informado");
                            Helper.changeView(R.layout.login);
                        }
                    }
                }
            }
        }, timer);
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
                    iniciarTimerClienteAguardandoConfirmacao(trabalho);
                    ret = false;
                } else {
                    ret = true;
                }
            } else if (trabalho.getEstado() == EstadoTrabalho.EFETUANDO) {
                TrabalhoView.trabalhoEfetuando(trabalho);
                if(papel == PapelTrabalho.USUARIO) {
                    iniciarTimerClienteEfetuando();
                }
                ret = false;
            }
        }

        return ret;
    }

    private static void trabalhoEfetuando(Trabalho t) {
        if(t != null) {
            Helper.simpleChangeView(R.layout.trabalho_efetuando);
            Usuario cliente = t.getUsuario();
            Usuario prestador = t.getAnuncio().getPrestador();
            Activity v = Helper.getActivity();
            TextView tituloAnuncio = (TextView) v.findViewById(R.id.trabalhoEfetuandoTituloAnuncio);
            tituloAnuncio.setText(t.getAnuncio().getAreaDeAtuacao());
            TextView infoCliente = (TextView) v.findViewById(R.id.trabalhoEfetuandoInfoCliente);
            infoCliente .setText("Cliente: " + cliente.getNome() + " (" +  cliente.getEmail() + " - " + cliente.getTelefone() + ")");
            TextView infoPrestador = (TextView) v.findViewById(R.id.trabalhoEfetuandoInfoPrestador);
            infoPrestador.setText("Prestador: " + prestador.getNome() + " (" +  prestador.getEmail() + " - " + prestador.getTelefone() + ")");
            TextView infoTempo = (TextView) v.findViewById(R.id.trabalhoEfetuandoInfoTempo);
            DateFormat instance = DateFormat.getInstance();
            infoTempo.setText("Criado em: " + instance.format(t.getDatainicio()));
            Button buttonTerminar = (Button) v.findViewById(R.id.trabalhoEfetuandoTerminarTrabalho);
            Usuario usuarioConectado = Helper.getUsuarioConectado();
            if(usuarioConectado.getId() == cliente.getId()) {
                buttonTerminar.setEnabled(false);
                buttonTerminar.setText("O Prestador irá finalizar o trabalho no App");
            } else {
                buttonTerminar.setOnClickListener(OnClick.getOnClickListener());
            }
        }
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
                    TextView viewNomeCliente = (TextView) v.findViewById(R.id.listTrabalhoInfoCliente);
                    viewNomeCliente.setText(t.getUsuario().getNome() + " (" + t.getUsuario().getEmail() + " " + t.getUsuario().getTelefone() + ")");
                    View view = v.findViewById(R.id.listTrabalhoAceitarBtn);
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

    public static void avaliarTrabalho(Long idTrabalho) {
        if(idTrabalho != null) {
            String comentario = ((TextView) Helper.getActivity().findViewById(R.id.avaliacaoComentario)).getText().toString();
            int nota = (int) ((RatingBar) Helper.getActivity().findViewById(R.id.avaliacaoEstrelas)).getRating();

            if(nota == 0) {
                Helper.alert("Escolha um número de estrelas!");
            } else if (comentario.isEmpty()) {
                Helper.alert("Digite um comentário!");
            } else {
                Avaliacao avaliacao = ConnectorWebService.avaliarTrabalho(idTrabalho.toString(), comentario, new Integer(nota).toString());

                if (avaliacao == null) {
                    Helper.alert("Erro ao avaliar trabalho");
                } else {
                    Trabalho t = ConnectorWebService.obterTrabalho(idTrabalho.toString());
                    if(t != null) {
                        Helper.getActivity().getDBHelper().inserirRecibo("Trabalho contratado realizado: " + t.getAnuncio().getAreaDeAtuacao() + " por " + t.getAnuncio().getPrestador().getNome(), Recibo.Tipo.CLIENTE);
                    }
                    Helper.alert("Trabalho avaliado com sucesso");
                    Helper.changeView(R.layout.lista_avaliacao);
                }
            }
        }
    }
}
