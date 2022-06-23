package com.kev.game.juego2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kev.game.AjustesDelJuego.Ajustes;
import com.kev.game.R;
import com.mrntlu.toastie.Toastie;
import com.scwang.wave.MultiWaveHeader;

public class adivina_numero extends AppCompatActivity {
    static EditText et_num_maximo;
    private MediaPlayer mediaPlayer;
    private int musicLength;
    SharedPreferences sharedPref;
    CardView card;
    TextView titulo,texto,texto2;
    MultiWaveHeader waveHeader,waveHeader2;
    Button btn_jugar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adivina_numero);
        et_num_maximo=findViewById(R.id.et_num_maximo);
        btn_jugar=findViewById(R.id.btn_jugar);
         card = findViewById(R.id.cardd);
        waveHeader = findViewById(R.id.wave_header);
        waveHeader2 = findViewById(R.id.wave_header2);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        titulo = findViewById(R.id.texto);
        texto = findViewById(R.id.txtResultado3);

        texto2 = findViewById(R.id.msjResultado);

        Boolean darkModePref = sharedPref.getBoolean(Ajustes.KEY_DARK_MODE_SWITCH, false);
        waveHeader.setVelocity(6);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(340);
        waveHeader.setWaveHeight(70);
         waveHeader.setCloseColor(Color.argb(100,97,200,180));


        waveHeader2.setVelocity(6);
        waveHeader2.setProgress(1);
        waveHeader2.isRunning();

        waveHeader2.setGradientAngle(340);
        waveHeader2.setWaveHeight(70);
         waveHeader2.setCloseColor(Color.argb(100,97,200,180));

             if(darkModePref){
            RelativeLayout relativelayout = (findViewById(R.id.relativex));
            relativelayout.setBackgroundColor(getResources().getColor(R.color.cgray));
            waveHeader.setStartColor(R.color.colorOlas);
            waveHeader2.setStartColor(R.color.colorOlas);
            card.setBackground(getDrawable(R.drawable.main_menu_button_rd_dk));
            titulo.setTextColor(getResources().getColor(R.color.white));
            texto.setTextColor(getResources().getColor(R.color.white));
            texto2.setTextColor(getResources().getColor(R.color.blanquito));
            getWindow().setStatusBarColor(getResources().getColor(R.color.qboard_black));
        }


    }

    public static int darNumMaximo() {
        String numero = et_num_maximo.getText().toString();
        int numero_maximo = Integer.parseInt(numero);
        return numero_maximo;
    }


    public void jugar(View v){


        if (et_num_maximo.getText().toString().equals(""))
        {
            Toastie.topError(this, "Introduzca un número máximo, y luego pulse jugar", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int n_maximo =  darNumMaximo();

            if(darNumMaximo()>200)
            {
                Toastie.topError(this, "Tampoco te hagas sufrir así, que sea un número menor de 200.", Toast.LENGTH_SHORT).show();
                et_num_maximo.setText("");
            }
            else
            {
                Intent i = new Intent(this, juegoAdivina.class);
                startActivity(i);
            }

        }
    }




}

