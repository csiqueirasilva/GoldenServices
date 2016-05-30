package br.uva.goldenservices;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;

import br.uva.goldenservices.ui.Helper;
import br.uva.goldenservices.ui.MenuHelper;
import br.uva.goldenservices.ui.OnClick;
import golden.services.http.ConnectorWebService;

public class MainActivity extends Activity {/*
  ActionBar bar;
  EditText editNome,editTelefone,editMatricula,editEndereco,editEmail,editConfirmeEmail,editSenha,editConfirmSenha;
  RadioButton rbtn1,rbtn2;
 
  Button botaoCriar;
  ScrollView sv;
  LinearLayout ll;
 */

    final public Alert alert = new Alert();

    private Menu optionsMenu;
    private int currentView = -1;

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

        /* Enable Network */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

 /* editNome = (EditText) findViewById(R.id.editNome);
  editTelefone = (EditText) findViewById(R.id.EditTelefone);
  editEndereco = (EditText) findViewById(R.id.editEndereco);
  editMatricula = (EditText) findViewById(R.id.editTMatricula);
  editEmail  = (EditText) findViewById(R.id.editEmail);
  editConfirmeEmail = (EditText) findViewById(R.id.editConfirmeEmail);
  editSenha = (EditText) findViewById(R.id.Senha);
  editConfirmSenha  = (EditText) findViewById(R.id.cofirmeSenha);
  sv = (ScrollView) findViewById(R.id.scrollView1);
  FrameLayout.LayoutParams lpsv = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
  sv.setLayoutParams(lpsv);

  ll =(LinearLayout)findViewById(R.id.linearLayout);
  ll.setOrientation(LinearLayout.VERTICAL);
  LinearLayout.LayoutParams lpll = new  LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);  
  ll.setLayoutParams(lpll);
  sv.addView(ll);
  sv.addView(editNome);
  sv.addView(editSenha);
  sv.addView(editTelefone);
  sv.addView(editConfirmeEmail);
  sv.addView(editEmail);
  sv.addView(editConfirmSenha);
  sv.addView(editMatricula);
  sv.addView(editEndereco);
  bar = getActionBar();
  bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000ff")));
  */


    }
}
