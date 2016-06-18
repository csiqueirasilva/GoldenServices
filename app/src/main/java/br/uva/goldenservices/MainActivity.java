package br.uva.goldenservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;

import br.uva.goldenservices.database.DatabaseHelper;
import br.uva.goldenservices.ui.Helper;
import br.uva.goldenservices.ui.MenuHelper;

public class MainActivity extends Activity {

    final public Alert alert = new Alert();

    private Menu optionsMenu;
    private int currentView = -1;

    private DatabaseHelper dbHelper;

    public DatabaseHelper getDBHelper() {
        return dbHelper;
    }

    final public class Alert {

        public void showWithTitle(String title, String msg) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(msg);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        public void show(String msg) {
            showWithTitle("SISTEMA", msg);
        }
    }

    public Menu getMenu () {
        return this.optionsMenu;
    }

    public void setCurrentView(int id) {
        this.currentView = id;
        this.setContentView(id);
        this.invalidateOptionsMenu();
    }

    public int getCurrentView() {
        return this.currentView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuHelper.fillOptions(this.currentView, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean ret = true;

        if(!MenuHelper.resolve(this.currentView, item.getItemId())) {
            ret = super.onOptionsItemSelected(item);
        }

        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Helper.initialize(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.login);

        dbHelper = new DatabaseHelper(this);

        /* Enable Network */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}