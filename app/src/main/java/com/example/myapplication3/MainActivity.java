package com.example.myapplication3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean resp = false;

    private Button btPrimeiro = null;
    private Button btRetrocede = null;
    private Button btInsere = null;
    private Button btAltera = null;
    private Button btExclui = null;
    private Button btAvanca = null;
    private Button btUltimo = null;
    private Button btAtualiza = null;
    private EditText etNome = null;
    private EditText etFone = null;
    private EditText etEndereco = null;
    private Button btGravarRota = null;

    private Button btOk = null;
    private Button btCancelar = null;


    private SQLiteDatabase db =null;
    private Cursor cur = null;

    private char operacao = 'I';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponentes();
        // travaCampos();

        // MODE_PRIVATE: banco somente na aplicação
        // MODE_WORLD_READABLE: outras aplicações lerem
        // MODE_WORLD_WRITEABLE: outras aplicação gravarem
        //db = this.openOrCreateDatabase("Dados", Context.MODE_PRIVATE, null);
        db = new DatabaseManager(this, "BancoDados", null, 1).getWritableDatabase();
        atualizaDados();

        if (cur.moveToFirst()) {
            mostraDados();
        }
    }

    private void iniciaComponentes() {
        btPrimeiro = (Button) (findViewById(R.id.bt_primeiro));
        btPrimeiro.setOnClickListener(this);

        btRetrocede = (Button) (findViewById(R.id.bt_retrocede));
        btRetrocede.setOnClickListener(this);

        btInsere = (Button) (findViewById(R.id.bt_insere));
        btInsere.setOnClickListener(this);

        btAltera = (Button) (findViewById(R.id.bt_altera));
        btAltera.setOnClickListener(this);

        btExclui = (Button) (findViewById(R.id.bt_exclui));
        btExclui.setOnClickListener(this);

        btAvanca = (Button) (findViewById(R.id.bt_avanca));
        btAvanca.setOnClickListener(this);

        btUltimo = (Button) (findViewById(R.id.bt_ultimo));
        btUltimo.setOnClickListener(this);

        btAtualiza = (Button) (findViewById(R.id.bt_atualiza));
        btAtualiza.setOnClickListener(this);

        etNome = (EditText) (findViewById(R.id.et_nome));
        etFone = (EditText) (findViewById(R.id.et_fone));
        etEndereco = (EditText) (findViewById(R.id.et_endereco));

        btOk = (Button) (findViewById(R.id.bt_ok));
        btOk.setOnClickListener(this);

        btCancelar = (Button) (findViewById(R.id.bt_cancelar));
        btCancelar.setOnClickListener(this);

        btGravarRota = (Button) (findViewById(R.id.bt_gravar_rota));
        btGravarRota.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_primeiro:
                if (cur.moveToFirst()) {
                    mostraDados();
                }
                break;

            case R.id.bt_retrocede:
                if (cur.moveToPrevious()) {
                    mostraDados();
                }
                break;

            case R.id.bt_insere:
                insereDados();
                break;

            case R.id.bt_altera:
                alteraDados();
                break;

            case R.id.bt_exclui:
                excluiDados();
                break;

            case R.id.bt_avanca:
                if (cur.moveToNext()) {
                    mostraDados();
                }
                break;

            case R.id.bt_ultimo:
                if (cur.moveToLast()) {
                    mostraDados();
                }
                break;

            case R.id.bt_atualiza:
                atualizaDados();
                if (cur.moveToFirst()) {
                    mostraDados();
                }
                break;

            case R.id.bt_ok:
                botaoOk();
                // travaCampos();
                mostraManutencao();
                ocultaOkCancelar();
                break;

            case R.id.bt_cancelar:
                // travaCampos();
                mostraManutencao();
                ocultaOkCancelar();
                break;
            case R.id.bt_gravar_rota:
                //acessa o maps
                botaoGravarRota();
                ocultaOkCancelar();

            default:
                break;
        }
    }

    private void botaoOk() {
        ContentValues dados = new ContentValues();
        dados.put("nome", etNome.getText().toString());
        dados.put("fone", etFone.getText().toString());
        dados.put("endereco", etEndereco.getText().toString());
        if (operacao == 'I') {
            db.insert("agenda", null, dados);
            atualizaDados();
            if (cur.moveToLast()) {
                mostraDados();
            }
        } else {
            String registroAtual = cur.getString(0);
            db.update("agenda", dados, "id=?", new String[]{registroAtual});
            atualizaDados();
            if (cur.moveToFirst()) {
                mostraDados();
            }
        }

    }

    public void botaoGravarRota()
    {
        Intent intent = new Intent(this,MapsActivity.class);
        this.startActivity(intent);
    }

    private void excluiDados() {
        String registroAtual = cur.getString(0);
        db.delete("agenda", "id=?", new String[]{registroAtual});
        atualizaDados();
        if (cur.moveToFirst()) {
            mostraDados();
        }
    }

    private void atualizaDados() {
        cur = db.query("agenda", new String[]{"id", " nome", "fone", "endereco"}, null, null, null, null, null);
    }

    private void travaCampos() {
        etNome.setClickable(false);
        etNome.setFocusable(false);

        etFone.setClickable(false);
        etFone.setFocusable(false);

        etEndereco.setClickable(false);
        etEndereco.setFocusable(false);
    }

    private void liberaCampos() {
        etNome.setClickable(true);
        etNome.setFocusable(true);

        etFone.setClickable(true);
        etFone.setFocusable(true);

        etEndereco.setClickable(true);
        etEndereco.setFocusable(true);
    }

    private void ocultaManutencao() {
        btPrimeiro.setVisibility(View.INVISIBLE);
        btRetrocede.setVisibility(View.INVISIBLE);
        btInsere.setVisibility(View.INVISIBLE);
        btAltera.setVisibility(View.INVISIBLE);
        btExclui.setVisibility(View.INVISIBLE);
        btAvanca.setVisibility(View.INVISIBLE);
        btUltimo.setVisibility(View.INVISIBLE);
        btAtualiza.setVisibility(View.INVISIBLE);
    }

    private void mostraManutencao() {
        btPrimeiro.setVisibility(View.VISIBLE);
        btRetrocede.setVisibility(View.VISIBLE);
        btInsere.setVisibility(View.VISIBLE);
        btAltera.setVisibility(View.VISIBLE);
        btExclui.setVisibility(View.VISIBLE);
        btAvanca.setVisibility(View.VISIBLE);
        btUltimo.setVisibility(View.VISIBLE);
        btAtualiza.setVisibility(View.VISIBLE);
    }

    private void mostraOkCancelar() {
        btOk.setVisibility(View.VISIBLE);
        btCancelar.setVisibility(View.VISIBLE);
    }

    private void ocultaOkCancelar() {
        btOk.setVisibility(View.INVISIBLE);
        btCancelar.setVisibility(View.INVISIBLE);
    }

    private void mostraDados() {
        etNome.setText(cur.getString(1));
        etFone.setText(cur.getString(2));
        etEndereco.setText(cur.getString(3));
    }

    private void insereDados() {
        liberaCampos();
        ocultaManutencao();
        mostraOkCancelar();
        operacao = 'I';
        etNome.setText("");
        etFone.setText("");
        etEndereco.setText("");

        etNome.requestFocus();
    }

    private void alteraDados() {
        liberaCampos();
        ocultaManutencao();
        mostraOkCancelar();
        operacao = 'A';
        etNome.requestFocus();
    }
}
