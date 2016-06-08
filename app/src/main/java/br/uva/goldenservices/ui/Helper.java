package br.uva.goldenservices.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import br.uva.goldenservices.MainActivity;
import br.uva.goldenservices.R;
import golden.services.model.usuarios.Usuario;

/**
 * Created by caio on 30/05/16.
 */
public class Helper {

    private static MainActivity mainActivity;
    private final static ArrayList<View> lastSearch = new ArrayList();
    private static Usuario usuarioConectado = null;

    public static View getViewFromParent(int viewId, int layoutId) {
        View v = null;

        if(mainActivity != null) {
            v = mainActivity.findViewById(viewId);

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View context = inflater.inflate(layoutId, null);
                v = context.findViewById(viewId);
            }

        }

        return v;
    }

    public static void enableLastSearch() {
        for (View v : lastSearch) {
            v.setEnabled(true);
        }
    }

    public static String[] getStringValues(boolean disable, int... ids) {
        String[] ret = new String[ids.length];

        lastSearch.clear();

        for (int i = 0; i < ids.length; i++) {
            int id = ids[i];
            View v = mainActivity.findViewById(id);
            if (v instanceof EditText) {
                ret[i] = ((EditText) v).getText().toString();
            } else {
                ret[i] = null;
            }

            if (disable) {
                v.setEnabled(false);
            }

            lastSearch.add(v);
        }

        return ret;
    }

    public static void initialize(MainActivity ma) {
        mainActivity = ma;
        OnClick.fillOnClickCallbacks();
        OnClick.setOnClickListener();
        ViewLoader.initialize(mainActivity.getCurrentView());
    }

    public static void simpleChangeView(int id) {
        mainActivity.setCurrentView(id);
        OnClick.fillOnClickCallbacks();
        OnClick.setOnClickListener();
    }

    public static void changeView(int id) {
        mainActivity.setCurrentView(id);
        Helper.initialize(mainActivity);
    }

    public static void alert(String msg) {
        mainActivity.alert.show(msg);
    }

    public static Activity getActivity() {
        return mainActivity;
    }

    public static Usuario getUsuarioConectado() {
        return usuarioConectado;
    }

    public static void setUsuarioConectado(Usuario usuarioConectado) {
        Helper.usuarioConectado = usuarioConectado;
    }
}