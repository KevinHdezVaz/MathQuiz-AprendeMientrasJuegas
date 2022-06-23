package com.kev.game.juego2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kev.game.AjustesDelJuego.Ajustes;
import com.kev.game.MenuJuegos;
import com.kev.game.R;
import com.mrntlu.toastie.Toastie;
import com.scwang.wave.MultiWaveHeader;

public class juegoAdivina extends AppCompatActivity {
    EditText et_num_usuario;
    TextView tv_intentos,intento;
    Button btn_comprobar;
    View v;
    Dialog epicDialog;
    ImageView cerrar;
    CardView cardView;
    MultiWaveHeader waveHeader,waveHeader2;
    private MediaPlayer mediaPlayer;
    TextView mensajeResultado;
    TextView resultado,resultado2,titulo;
    Button botonvamo;
    private int musicLength;
    SharedPreferences sharedPref;
    SharedPreferences preferences;
    Boolean mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_adivina);
        et_num_usuario=findViewById(R.id.et_num_usuario);
        tv_intentos=findViewById(R.id.tv_intentos);
        btn_comprobar=findViewById(R.id.btn_comprobar);
        preferences = getSharedPreferences("highScore",MODE_PRIVATE);
        waveHeader = findViewById(R.id.wave_header);
        waveHeader2 = findViewById(R.id.wave_header2);
        titulo = findViewById(R.id.texto);
        cardView = findViewById(R.id.cardviews);
        intento = findViewById(R.id.tv_tit);
        //anuncios admob
        //anuncios admob, cambiar el ID a tu cuenta


  sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mute = sharedPref.getBoolean(Ajustes.KEY_MUTE_MUSIC,false);
        mediaPlayer = MediaPlayer.create(this,R.raw.retrosou);
        mediaPlayer.setLooping(true);
        Boolean darkModePref = sharedPref.getBoolean(Ajustes.KEY_DARK_MODE_SWITCH, false);

        if(!mute) {
            mediaPlayer.start();
        }else{
        }
        if(darkModePref){
            RelativeLayout relativelayout = (findViewById(R.id.fonodo));
            relativelayout.setBackgroundColor(getResources().getColor(R.color.cgray));
            waveHeader.setStartColor(R.color.colorOlas);
            waveHeader2.setStartColor(R.color.colorOlas);
            titulo.setTextColor(getResources().getColor(R.color.white));
            cardView.setBackground(getDrawable(R.drawable.main_menu_button_rd_dk));
            intento.setTextColor(getResources().getColor(R.color.white));


            getWindow().setStatusBarColor(getResources().getColor(R.color.qboard_black));
        }



    }
    public int obtenerAleatorio(){
        double d=Math.random();
        int n_maximo = adivina_numero.darNumMaximo();
        double aleatorio=d*n_maximo;
        int n_aleatorio=(int)aleatorio;
        return n_aleatorio;

    }


    int intentos = 0;
    int numero_aleatorio = obtenerAleatorio();

    public void comprobar(View v) {
        String numero = et_num_usuario.getText().toString();

        int n_maximo= adivina_numero.darNumMaximo();


        if (numero.equals(""))
        {
            Toastie.warning(this, "Introduzca un número, y luego pulse enviar.", Toast.LENGTH_LONG).show();
        }
        else
        {
            int numero_usuario = Integer.parseInt(numero);

            if (numero_usuario > n_maximo)
            {

                Toastie.topError(this, "Creo que te pasaste de número JAJAJA", Toast.LENGTH_SHORT).show();
            }
            else
            {

                if (numero_usuario == numero_aleatorio) {
                    mostrarInfo();




                }

                else if (numero_usuario < numero_aleatorio) {
                    intentos++;
                    tv_intentos.setText(String.valueOf(intentos));
                    Toastie.topError(this,"El número que estoy pensando ES MAYOR.",Toast.LENGTH_SHORT).show();

                }

                else if (numero_usuario > numero_aleatorio) {
                    intentos++;
                    tv_intentos.setText(String.valueOf(intentos));
                    Toastie.topInfo(this,"El número que estoy pensando ES MENOR.",Toast.LENGTH_SHORT).show();
                }

            }
        }
        et_num_usuario.setText("");


    }

    public void mostrarInfo(){
        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.custom_resultado2);
        cerrar = (ImageView)epicDialog.findViewById(R.id.cerrarVentana2);
        resultado =(TextView)epicDialog.findViewById(R.id.txtResultado);
        mensajeResultado =(TextView)epicDialog.findViewById(R.id.msjResultado);
        resultado2 =(TextView)epicDialog.findViewById(R.id.txtResultado2);


        intentos++;
        tv_intentos.setText(String.valueOf(intentos));

        resultado.setText(""+intentos);

        preferences.edit().putString("highScore","Intentos: "+resultado.getText().toString()).apply();

        if(intentos >= 6){
            resultado2.setText("Sigue intentandolo, no todo se logra a la primera ;) ");
        }

        else if(intentos == 5){
            resultado2.setText("¡Animo, la paciencia y esfuerzo son las mejores virtudes!");
        }
        else if(intentos == 4){
            resultado2.setText("4 aciertos, casi le das... Como a tu crush.");
        }
        else if(intentos == 3){
            resultado2.setText("De seguro estudias ingeniería.");
        }
        else if(intentos == 1){
            resultado2.setText("Me asombra tu intelecto, ¿Acaso eres humano? :o");
        }


        botonvamo =(Button)epicDialog.findViewById(R.id.botonvamo);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   QuizContext.getInstance().resetContext();
                Intent intent = new Intent(juegoAdivina.this, MenuJuegos.class);
                startActivity(intent);
                finish();
                epicDialog.dismiss();


            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        epicDialog.setCancelable(false);
        epicDialog.setCanceledOnTouchOutside(false);
        epicDialog.show();
        botonvamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //QuizContext.getInstance().resetContext();
                Intent intent = new Intent(juegoAdivina.this, adivina_numero.class);
                startActivity(intent);
                finish();



            }
        });
    }
    public void onClick1(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "1");
    }

    public void onClick2(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "2");
    }    public void onClick3(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "3");
    }    public void onClick4(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "4");
    }    public void onClick5(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "5");
    }    public void onClick6(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "6");
    }
    public void onClick7(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "7");
    }
    public void onClick8(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "8");
    }
    public void onClick9(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "9");
    }
    public void onClick0(View view) {
        et_num_usuario.setText(et_num_usuario.getText() + "0");
    }



    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying()) {
            musicLength = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mediaPlayer.isPlaying() ) {
            mediaPlayer.seekTo(musicLength);
            mediaPlayer.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        mediaPlayer.release();

    }
    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

}
