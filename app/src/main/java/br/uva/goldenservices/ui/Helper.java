package br.uva.goldenservices.ui;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import br.uva.goldenservices.MainActivity;

/**
 * Created by caio on 30/05/16.
 */
public class Helper {

    private static MainActivity mainActivity;
    private final static ArrayList<View> lastSearch = new ArrayList();

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
    }

    public static void changeView(int id) {
        mainActivity.setContentView(id);
        Helper.initialize(mainActivity);
    }

    public static void alert(String msg) {
        mainActivity.alert.show(msg);
    }

    public static Activity getActivity() {
        return mainActivity;
    }

}