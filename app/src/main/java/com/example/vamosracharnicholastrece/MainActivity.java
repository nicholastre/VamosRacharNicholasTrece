package com.example.vamosracharnicholastrece;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText edtValor,edtGalera;
    TextView tvRes;
    FloatingActionButton share,tocar;
    TextToSpeech ttsPlayer;
    int galera = 2;
    double valor = 0.0;
    String resFormatado = "0,00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtValor = (EditText) findViewById(R.id.editValor);
        edtValor.addTextChangedListener(this);
        edtGalera = (EditText) findViewById(R.id.editPessoas);
        edtGalera.addTextChangedListener(this);
        tvRes = (TextView) findViewById(R.id.textViewResultado);
        share = (FloatingActionButton) findViewById(R.id.btShare);
        share.setOnClickListener(this);
        tocar = (FloatingActionButton) findViewById(R.id.btTocar);
        tocar.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent,1122);
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== 1122){
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                ttsPlayer = new TextToSpeech(this,this);
            } else {
                Intent installTTsIntent = new Intent();
                installTTsIntent
                        .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTsIntent);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        try {
            double res = Double.parseDouble(edtValor.getText().toString());
            DecimalFormat df= new DecimalFormat("#.00");
            double size = Double.parseDouble(edtGalera.getText().toString());
            res= (res / size);
            tvRes.setText("R$" +df.format(res));
        }catch (Exception e){
            tvRes.setText("R$ 0.00");
        }
    }

    @Override
    public void onClick(View v) {
        if(v==share){
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"A conta dividida por pessoa deu" + tvRes.getText().toString());
            startActivity(intent);
        }
        if(v==tocar){
            if (ttsPlayer!=null){
                ttsPlayer.speak("O valor por pessoa é de"+tvRes.getText().toString()+ "reais", TextToSpeech.QUEUE_FLUSH, null,"1D1");
            }
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            Toast.makeText(this,"TTS ativado...",
                Toast.LENGTH_LONG).show();
        } else if(status== TextToSpeech.ERROR){
            Toast.makeText(this,"Sem TTS habilitado..",
                    Toast.LENGTH_LONG).show();
        }
    }
}