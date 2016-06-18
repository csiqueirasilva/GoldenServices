package br.uva.goldenservices.views;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import br.uva.goldenservices.R;
import br.uva.goldenservices.adapters.GenericListAdapter;
import br.uva.goldenservices.database.recibos.Recibo;
import br.uva.goldenservices.ui.Helper;
import br.uva.goldenservices.ui.OnClick;
import golden.services.model.trabalhos.Trabalho;

/**
 * Created by csiqueira on 18/06/16.
 */
public class ReciboView {
    public static Recibo.Tipo tipo;


    public static void list() {
        List<Recibo> recibos;

        if(tipo == Recibo.Tipo.PRESTADOR) {
            recibos = Helper.getActivity().getDBHelper().obterRecibosPrestador();
        } else {
            recibos = Helper.getActivity().getDBHelper().obterRecibosCliente();
        }

        GenericListAdapter<Recibo> reciboAdapter = new GenericListAdapter<Recibo>(Helper.getActivity(), R.layout.list_recibo_item, recibos, false) {
            @Override
            protected void onView(Recibo t, View v) {
                TextView reciboDesc = (TextView) v.findViewById(R.id.listReciboDesc);
                TextView reciboTipo = (TextView) v.findViewById(R.id.listReciboTipo);
                reciboDesc.setText(t.getDesc());
                reciboTipo.setText("Recibo tipo: " + t.getTipo().toString());
            }
        };

        ListView lista = (ListView) Helper.getActivity().findViewById(R.id.listRecibo);
        lista.setAdapter(reciboAdapter);

        TextView emptyList = (TextView) Helper.getActivity().findViewById(R.id.listReciboVazia);
        lista.setEmptyView(emptyList);

        OnClick.fillOnClickCallbacks();
        OnClick.setOnClickListener();
    }
}
