package br.uva.goldenservices.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import br.uva.goldenservices.R;
import br.uva.goldenservices.ui.OnClick;
import golden.services.model.anuncios.Anuncio;

/**
 * Created by csiqueira on 07/06/16.
 */
public abstract class GenericListAdapter<T> extends ArrayAdapter {

    private List<T> objects;
    private int parentLayoutId;
    private boolean reverse;

    public GenericListAdapter(Context context, int textViewResourceId, List<T> objects, boolean reverse) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.parentLayoutId = textViewResourceId;
        this.reverse = reverse;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(this.parentLayoutId, null);
        }

        T targetObject = objects.get(this.reverse ? objects.size() - 1 - position : position);

        if (targetObject != null) {
            onView(targetObject, v);
        }

        return v;
    }

    protected abstract void onView(T targetObject, View v);

}