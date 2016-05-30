package br.uva.goldenservices.ui;

import android.app.Activity;
import android.widget.RadioButton;

import br.uva.goldenservices.R;
import golden.services.http.ConnectorWebService;
import golden.services.model.usuarios.Usuario;

/**
 * Created by caio on 30/05/16.
 */
public class FormSubmit {

    public static enum Sexo {
        FEMININO,
        MASCULINO
    }

    public static void sendCadastroUsuario(Activity mainActivity) {

        String[] strings = Helper.getStringValues(false, R.id.editNome, R.id.EditTelefone, R.id.editEndereco,
                R.id.editTMatricula, R.id.editEmail, R.id.editConfirmeEmail, R.id.Senha, R.id.cofirmeSenha);

        if (strings[7].equals(strings[6]) && strings[5].equals(strings[4])) {

            String email = strings[4];
            String nome = strings[0];
            String password = strings[6];
            String endereco = strings[2];
            String telefone = strings[1];
            String sexo = ((RadioButton) mainActivity.findViewById(R.id.radioFeminino)).isSelected() ? Sexo.FEMININO.toString() : Sexo.MASCULINO.toString();
            String sobre = "";

            Usuario usuario = ConnectorWebService.criarUsuario(email, password, nome, endereco, telefone, sexo, sobre);

            if(usuario == null) {
                Helper.alert("Erro ao efetuar cadastro.");
            } else {
                Helper.alert("Cadastro efetuado com sucesso!");
                Helper.changeView(R.layout.login);
            }

        } else {
            Helper.alert("Dados não conferem!");
        }
    }

}
