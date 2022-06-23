package com.kev.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kev.game.AjustesDelJuego.Ajustes;
import com.kev.game.juego1.suma_resta_mul;
import com.kev.game.juego2.adivina_numero;
import com.kev.game.juego3.JuegoTiempo;
import com.kev.game.juego4.correcto_incorrecto;
import com.kev.game.juego5.operaciones;

public class MenuJuegos extends AppCompatActivity {
    CardView card1,card2,card3,card4,card5,card6,card7;
    private MediaPlayer mediaPlayer;
    private int musicLength;
    SharedPreferences sharedPref;
    TextView t1,t2,t3,t4,t5,t6,t7;
    int length;

    Boolean mute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_juegos);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card6 = findViewById(R.id.card6);
        card7 = findViewById(R.id.card7);

        t1 = findViewById(R.id.xd);
        t2 = findViewById(R.id.xd2);
        t3 = findViewById(R.id.xd3);
        t4 = findViewById(R.id.xd4);
        t5 = findViewById(R.id.xd5);
        t6 = findViewById(R.id.xd6);
        t7 = findViewById(R.id.xd7);

        final ImageButton addButton = (ImageButton) findViewById(R.id.add);
        final ImageButton subButton = (ImageButton) findViewById(R.id.sub);
        final ImageButton mulButton = (ImageButton) findViewById(R.id.mul);
        final ImageButton adivinaNumer = (ImageButton) findViewById(R.id.adivinaNumero);
        final ImageButton adivinaNumer2 = (ImageButton) findViewById(R.id.pregunta);
        final ImageButton adivinaNumer3 = (ImageButton) findViewById(R.id.ultimoGame);

        card1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_right_0));
        card2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_left_quick_math));
        card3.startAnimation(AnimationUtils.loadAnimation(this,R.anim.advanced_init));
        card4.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_left_time_trials));
        card5.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_left_advanced));
        card6.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_left_time_trials));
        card7.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_left_advanced));

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mute = sharedPref.getBoolean(Ajustes.KEY_MUTE_MUSIC,false);
        mediaPlayer = MediaPlayer.create(this,R.raw.littleide);
        mediaPlayer.setLooping(true);
        Boolean darkModePref = sharedPref.getBoolean(Ajustes.KEY_DARK_MODE_SWITCH, false);

        if(!mute) {
            mediaPlayer.start();
        }else{
        }
        if(darkModePref){
            RelativeLayout relativelayout = (findViewById(R.id.menumain));
            relativelayout.setBackground(getDrawable(R.drawable.main_menu_button_rd_dk));

            card1.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            card2.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            card3.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            card4.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            card5.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            card6.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            card7.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            t1.setTextColor(getResources().getColor(R.color.white));
            t2.setTextColor(getResources().getColor(R.color.white));
            t3.setTextColor(getResources().getColor(R.color.white));
            t4.setTextColor(getResources().getColor(R.color.white));
            t5.setTextColor(getResources().getColor(R.color.white));
            t6.setTextColor(getResources().getColor(R.color.white));
            t7.setTextColor(getResources().getColor(R.color.white));




            getWindow().setStatusBarColor(getResources().getColor(R.color.qboard_black));
        }

     }
    public void Suma(View view )
    {
        Intent intent = new Intent(MenuJuegos.this, suma_resta_mul.class);
        intent.putExtra("operation", "add");
        startActivity(intent);
        finish();

     }

    public void Resta(View view )
    {
        Intent intent = new Intent(MenuJuegos.this, suma_resta_mul.class);
        intent.putExtra("operation", "sub");
        startActivity(intent);
        finish();

     }

    public void Multiplicacion(View view )
    {
        Intent intent = new Intent(MenuJuegos.this, suma_resta_mul.class);
        intent.putExtra("operation", "mul");
        startActivity(intent);
        finish();

    }

    public void Adivina(View view ){
        startActivity(new Intent(MenuJuegos.this, adivina_numero.class));
        finish();
        }
    public void Operaciones(View view){
        startActivity(new Intent(MenuJuegos.this, operaciones.class));

        finish();

    }
    public void correctoInco(View view){
        startActivity(new Intent(MenuJuegos.this, correcto_incorrecto.class));

        finish(); }
    public void Tiempo(View view){
        startActivity(new Intent(MenuJuegos.this, JuegoTiempo.class));

        finish();
    }    @Override
        protected void onPause() {
            super.onPause();
            if(mediaPlayer.isPlaying() && !mute){
                mediaPlayer.pause();
                length = mediaPlayer.getCurrentPosition();
            }

        }

        @Override
        protected void onStop() {
            super.onStop();
            mediaPlayer.stop();
        }

        @Override
        protected void onRestart() {
            super.onRestart();
            if(!mediaPlayer.isPlaying() && !mute) {
                mediaPlayer.seekTo(length);
                mediaPlayer.pause();
            }
    }
}
