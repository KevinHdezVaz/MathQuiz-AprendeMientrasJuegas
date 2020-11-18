package com.kev.mathquiz.juego3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kev.mathquiz.AjustesDelJuego.Ajustes;
import com.kev.mathquiz.MenuJuegos;
import com.kev.mathquiz.R;
import com.scwang.wave.MultiWaveHeader;

import java.util.ArrayList;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class JuegoTiempo extends AppCompatActivity  {
    private TextView winningMessage, timeLeftText,whichOneIsCorrect,iqMessage,scoreMessage,userFeedback,scoreText;
    private Button button0, button1, button2, button3;
    private ArrayList<String> questions = new ArrayList<String>();
    int locationOfCorrectAnswer, score = 0, numberOfQuestions = 0,scoreincorrecta = 0, onARoll = 0, feedBackNum, wrongOrCorrect, musicLength;
    private CountDownTimer countDownTimer;
    private Animation correctAnimation, feedBackAnimation;
    private Dialog scorePopUp;
    private MediaPlayer mediaPlayer;
    private Boolean mute, flashingText,vibracion;
    boolean ranBefore;
    MultiWaveHeader waveHeader,waveHeader2;
    private long elapsedMillis;
    SharedPreferences sharedPref;
    ImageButton muteButton;
    private Vibrator vibrator;
    private Chronometer chronometer;
     //Boolean values to check user preference.
    private Boolean addition, subtraction, multiplication, division, timer, kidsmode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_tiempo);
         chronometer = new Chronometer(this);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        timeLeftText = findViewById(R.id.timeLeftText);
        scoreText = findViewById(R.id.scoreText);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        userFeedback = findViewById(R.id.userFeedback);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        whichOneIsCorrect = findViewById(R.id.whichOneIsCorrect);
        scorePopUp = new Dialog(this);
        scorePopUp.setContentView(R.layout.score_popup);
        scorePopUp.getWindow().getAttributes().windowAnimations = R.style.ScorePopUpAnimation;
        scorePopUp.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.MATCH_PARENT);
        winningMessage = scorePopUp.findViewById(R.id.winningMessage);
        scoreMessage = scorePopUp.findViewById(R.id.scoreMessage);
        iqMessage = scorePopUp.findViewById(R.id.iqMessage);
        muteButton = findViewById(R.id.muteButton);
        waveHeader = findViewById(R.id.wave_header);
        waveHeader2 = findViewById(R.id.wave_header2);

        //Variables to hold animation values.
        correctAnimation = AnimationUtils.loadAnimation(this,R.anim.correct_animation);
        feedBackAnimation = AnimationUtils.loadAnimation(this,R.anim.flicker_animation);
        //anuncios admob

        // Use an activity context to get the rewarded video instance.
        //anuncios admob, cambiar el ID a tu cuenta


        //animaciones iniciales
        button0.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_right_0));
        button1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_right_1));
        button2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_right_2));
        button3.startAnimation(AnimationUtils.loadAnimation(this,R.anim.from_right_3));

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mute = sharedPref.getBoolean(Ajustes.KEY_MUTE_MUSIC,false);
        mediaPlayer = MediaPlayer.create(this,R.raw.the_duel);
        vibracion = sharedPref.getBoolean(Ajustes.KEY_VIBRACION,false);
        mediaPlayer.setLooping(true);
        Boolean darkModePref = sharedPref.getBoolean(Ajustes.KEY_DARK_MODE_SWITCH, false);

        if(!mute) {
            mediaPlayer.start();
        }else{
        }

        //obtener las preferencias
        addition = sharedPref.getBoolean(Ajustes.KEY_ADDITION_ONLY_TIMETRIALS,false);
        subtraction = sharedPref.getBoolean(Ajustes.KEY_SUBTRACTION_ONLY_TIMETRIALS,false);
        multiplication = sharedPref.getBoolean(Ajustes.KEY_MULTIPLICATION_ONLY_TIMETRIALS,false);
        division = sharedPref.getBoolean(Ajustes.KEY_DIVISION_ONLY_TIMETRIALS,false);
        timer = sharedPref.getBoolean(Ajustes.KEY_TIMER_TIMETRIALS,false);
        kidsmode = sharedPref.getBoolean(Ajustes.KEY_KIDS_MODE_SWITCH,false);
        flashingText = sharedPref.getBoolean(Ajustes.KEY_FLASHING_TEXT,true);
        if(darkModePref){
            RelativeLayout relativelayout = (findViewById(R.id.timetrialsbg));
            relativelayout.setBackgroundColor(getResources().getColor(R.color.cgray));

            waveHeader.setStartColor(R.color.colorOlas);
            waveHeader2.setStartColor(R.color.colorOlas);

            button0.setBackground(getDrawable(R.drawable.main_menu_button_bg_dk));
            button1.setBackground(getDrawable(R.drawable.main_menu_button_og_dk_2));
            button2.setBackground(getDrawable(R.drawable.main_menu_button_rd_dk));
            button3.setBackground(getDrawable(R.drawable.main_menu_button_gr_dk));
             timeLeftText.setTextColor(getResources().getColor(R.color.white));
            scoreText.setTextColor(getResources().getColor(R.color.white));
            userFeedback.setTextColor(getResources().getColor(R.color.white));

             getWindow().setStatusBarColor(getResources().getColor(R.color.qboard_black));
        }

        if(timer)
            timer();
        else {
            timeLeftText.setText(getString(R.string.timer_disabled));
        }


        generateQuestions();

        if(isFirstTime()){
            countDownTimer.cancel();
            whichOneIsCorrect.setText(getString(R.string.aprende));
            ShowcaseConfig config = new ShowcaseConfig();
            final MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "timeTrialsOnBoarding");
            config.setMaskColor(getResources().getColor(R.color.colorAccent50));
            config.setRenderOverNavigationBar(true);
            config.setShapePadding(50);
            config.setDelay(500);
            sequence.setConfig(config);

            timeLeftText.post(new Runnable() {
                @Override
                public void run() {
                    sequence.addSequenceItem(timeLeftText, getString(R.string.saludos_ti1),getString(R.string.next));


                    switch (locationOfCorrectAnswer){
                        case 0: sequence.addSequenceItem(
                                new MaterialShowcaseView.Builder(JuegoTiempo.this)
                                        .setTarget(button0)
                                        .setContentText(getString(R.string.saludos_ti2) + button0.getText() + getString(R.string.saludos_ti3))
                                        .setMaskColour(getResources().getColor(R.color.colorAccent50))
                                        .setDismissOnTargetTouch(true)
                                        .setTargetTouchable(true)
                                        .withRectangleShape()
                                        .build()
                        );
                            break;
                        case 1: sequence.addSequenceItem(
                                new MaterialShowcaseView.Builder(JuegoTiempo.this)
                                        .setTarget(button1)
                                        .setContentText(getString(R.string.saludos_ti2) + button1.getText() + getString(R.string.saludos_ti3))
                                        .setMaskColour(getResources().getColor(R.color.colorAccent50))
                                        .setDismissOnTargetTouch(true)
                                        .setTargetTouchable(true)
                                        .withRectangleShape()
                                        .build()
                        );
                            break;
                        case 2: sequence.addSequenceItem(
                                new MaterialShowcaseView.Builder(JuegoTiempo.this)
                                        .setTarget(button2)
                                        .setContentText(getString(R.string.saludos_ti2) + button2.getText() + getString(R.string.saludos_ti3))
                                        .setMaskColour(getResources().getColor(R.color.colorAccent50))
                                        .setDismissOnTargetTouch(true)
                                        .setTargetTouchable(true)
                                        .withRectangleShape()
                                        .build()
                        );
                            break;
                        case 3: sequence.addSequenceItem(
                                new MaterialShowcaseView.Builder(JuegoTiempo.this)
                                        .setTarget(button3)
                                        .setContentText(getString(R.string.saludos_ti2) + button3.getText() + getString(R.string.saludos_ti3))
                                        .setMaskColour(getResources().getColor(R.color.colorAccent50))
                                        .setDismissOnTargetTouch(true)
                                        .setTargetTouchable(true)
                                        .withRectangleShape()
                                        .build()
                        );
                            break;
                    }
                    sequence.addSequenceItem(userFeedback,getString(R.string.saludos_ti4),getString(R.string.next));
                    sequence.addSequenceItem(scoreText, getString(R.string.saludos_ti5
                    ),getString(R.string.go));
                    sequence.addSequenceItem(new MaterialShowcaseView.Builder(JuegoTiempo.this)
                            .setTarget(whichOneIsCorrect)
                            .setContentText(getString(R.string.mat))
                            .setMaskColour(getResources().getColor(R.color.colorAccent50))
                            .setDismissOnTargetTouch(true)
                            .setTargetTouchable(true)
                            .withRectangleShape()
                            .build()
                    );





                    sequence.start();


                }
            });
         }
    }

    public void muteTemp(View view){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            sharedPref.edit().putBoolean(Ajustes.KEY_MUTE_MUSIC,true).apply();

        }else{
            mediaPlayer.start();
            sharedPref.edit().putBoolean(Ajustes.KEY_MUTE_MUSIC,false).apply();
         }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer.isPlaying() && !mute) {
            musicLength = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
        if(addition && subtraction && multiplication && division && timer && !kidsmode) {
            finish();
        }
        if(timer){
            finish();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!mediaPlayer.isPlaying() && !mute) {
            mediaPlayer.seekTo(musicLength);
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scorePopUp.dismiss();
        mediaPlayer.stop();
        mediaPlayer.release();
    }





    public void timer(){ //aqui se coloca el tiempo que quieras, yo coloque 11 segundos cuenta hacia atras
        countDownTimer = new CountDownTimer(11000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 3000)
                    if(flashingText) {
                        timeLeftText.startAnimation(AnimationUtils.loadAnimation(JuegoTiempo.this, R.anim.timer_flicker));
                    }
                if (millisUntilFinished > 10000)
                    timeLeftText.setText(getString(R.string.time_left, (int) millisUntilFinished / 1000));
                else
                    timeLeftText.setText(getString(R.string.time_left_ten_less, (int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                showPopUp();
            }
        }.start();
    }
    public void showPopUp(){
        boolean newHigh = false;
        if(timer) {
            countDownTimer.cancel();
        }
        elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        int timeTaken = (int) elapsedMillis / 1000;

        if(newHigh){
            winningMessage.setText("NEW HIGH SCORE!");
        }else {
            if (numberOfQuestions - score < 4 && numberOfQuestions > 10) {
                winningMessage.setText(getString(R.string.hey_there_genius));
            } else if (numberOfQuestions - score > 0 && numberOfQuestions - score < 5) {
                winningMessage.setText(getString(R.string.unbelievable));
            } else if (numberOfQuestions < 10 && numberOfQuestions > 1) {
                winningMessage.setText(getString(R.string.are_you_even));
            } else if (numberOfQuestions == 0) {
                winningMessage.setText(getString(R.string.afk_text));
            } else {
                winningMessage.setText(getString(R.string.need_more_practice));
            }
        }
        scoreMessage.setText(Integer.toString(score));
        iqMessage.setText(Integer.toString(scoreincorrecta));
        scorePopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        scorePopUp.setCanceledOnTouchOutside(false);
        if(!JuegoTiempo.this.isFinishing()) {
            scorePopUp.show();
        }
        scorePopUp.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
    }
    public void playAgain(View view){


        startActivity(new Intent(getApplicationContext(), JuegoTiempo.class));
        finish();

    }



    //Pop up quit button
    public  void quit(View view){


        startActivity(new Intent(getApplicationContext(), MenuJuegos.class));

        finish();


    }


    public String sumQuestion(){
        Random rd = new Random();
        int a = rd.nextInt((15-4)+1)+4;
        int b = rd.nextInt((15-4)+1)+4;
        return getString(R.string.sum, a, b, a + b);

    }
    public String subtractQuestion(){
        Random rd = new Random();
        int a = rd.nextInt((15-4)+1)+4;
        int b = rd.nextInt((15-a)+1)+a;
        return getString(R.string.sub,b,a,b-a);
    }
    public String multiplyQuestion(){
        Random rd = new Random();
        int a = rd.nextInt((12-1)+1)+1;
        int b = rd.nextInt((12-1)+1)+1;
        return getString(R.string.mult,a,b,a*b);

    }
    public String divisionQuestion(){
        Random rd = new Random();
        int a = rd.nextInt(10)+1;
        int b = rd.nextInt(50)+1;
        while(b % a != 0) {
            a = rd.nextInt(10)+1;
            b = rd.nextInt(50)+1;
        }
        return getString(R.string.div,b,a,b/a);

    }
    public String wrongTypeOfQuestion(){
        Random rd = new Random();
        switch (rd.nextInt(4)){
            case 0:
                if(addition){
                    int a = rd.nextInt(15)+1;
                    int b = rd.nextInt(12)+1;
                    int c = rd.nextInt(40)+1;
                    while(c == a+b){
                        c = rd.nextInt(40)+1;
                    }
                    return getString(R.string.sum,a,b,c);
                }else{
                    if(subtraction){
                        int a = rd.nextInt(15)+1;
                        int b = rd.nextInt((10-5)+1)+5;
                        int c = rd.nextInt(15)+1;
                        while(c == a-b){
                            c = rd.nextInt(15)+1;
                        }
                        return getString(R.string.sub,a,b,c);
                    }else{
                        if(multiplication){
                            int a = rd.nextInt(12)+1;
                            int b = rd.nextInt(12)+1;
                            int c = rd.nextInt(50)+1;
                            while(c == a*b){
                                c = rd.nextInt(50)+1;
                            }
                            return getString(R.string.mult,a,b,c);
                        }else{
                            if(division){
                                int a = rd.nextInt(25)+1;
                                int b = rd.nextInt(12)+1;
                                int c = rd.nextInt(12)+1;
                                while(c == a/b){
                                    c = rd.nextInt(12)+1;
                                }
                                return getString(R.string.div,a,b,c);
                            }
                        }
                    }
                }
            case 1:
                if(subtraction){
                    int a = rd.nextInt(15)+1;
                    int b = rd.nextInt((10-5)+1)+5;
                    int c = rd.nextInt(15)+1;
                    while(c == a-b){
                        c = rd.nextInt(15)+1;
                    }
                    return getString(R.string.sub,a,b,c);
                }else{
                    if(addition){
                        int a = rd.nextInt(15)+1;
                        int b = rd.nextInt(12)+1;
                        int c = rd.nextInt(40)+1;
                        while(c == a+b){
                            c = rd.nextInt(40)+1;
                        }
                        return getString(R.string.sum,a,b,c);
                    }else{
                        if(multiplication){
                            int a = rd.nextInt(12)+1;
                            int b = rd.nextInt(12)+1;
                            int c = rd.nextInt(50)+1;
                            while(c == a*b){
                                c = rd.nextInt(50)+1;
                            }
                            return getString(R.string.mult,a,b,c);
                        }else{
                            if(division){
                                int a = rd.nextInt(25)+1;
                                int b = rd.nextInt(12)+1;
                                int c = rd.nextInt(12)+1;
                                while(c == a/b){
                                    c = rd.nextInt(12)+1;
                                }
                                return getString(R.string.div,a,b,c);
                            }
                        }
                    }
                }
            case 2:
                if(multiplication){
                    int a = rd.nextInt(12)+1;
                    int b = rd.nextInt(12)+1;
                    int c = rd.nextInt(50)+1;
                    while(c == a*b){
                        c = rd.nextInt(50)+1;
                    }
                    return getString(R.string.mult,a,b,c);
                }else{
                    if(addition){
                        int a = rd.nextInt(15)+1;
                        int b = rd.nextInt(12)+1;
                        int c = rd.nextInt(40)+1;
                        while(c == a+b){
                            c = rd.nextInt(40)+1;
                        }
                        return getString(R.string.sum,a,b,c);
                    }else{
                        if(subtraction){
                            int a = rd.nextInt(15)+1;
                            int b = rd.nextInt((10-5)+1)+5;
                            int c = rd.nextInt(15)+1;
                            while(c == a-b){
                                c = rd.nextInt(15)+1;
                            }
                            return getString(R.string.sub,a,b,c);
                        }else{
                            if(division){
                                int a = rd.nextInt(25)+1;
                                int b = rd.nextInt(12)+1;
                                int c = rd.nextInt(12)+1;
                                while(c == a/b){
                                    c = rd.nextInt(12)+1;
                                }
                                return getString(R.string.div,a,b,c);
                            }
                        }
                    }
                }
            case 3:
                if(division){
                    int a = rd.nextInt(25)+1;
                    int b = rd.nextInt(12)+1;
                    int c = rd.nextInt(12)+1;
                    while(c == a/b){
                        c = rd.nextInt(12)+1;
                    }
                    return getString(R.string.div,a,b,c);
                }else{
                    if(addition){
                        int a = rd.nextInt(15)+1;
                        int b = rd.nextInt(12)+1;
                        int c = rd.nextInt(40)+1;
                        while(c == a+b){
                            c = rd.nextInt(40)+1;
                        }
                        return getString(R.string.sum,a,b,c);
                    }else{
                        if(subtraction){
                            int a = rd.nextInt(15)+1;
                            int b = rd.nextInt((10-5)+1)+5;
                            int c = rd.nextInt(15)+1;
                            while(c == a-b){
                                c = rd.nextInt(15)+1;
                            }
                            return getString(R.string.sub,a,b,c);
                        }else{
                            if(multiplication){
                                int a = rd.nextInt(12)+1;
                                int b = rd.nextInt(12)+1;
                                int c = rd.nextInt(50)+1;
                                while(c == a*b){
                                    c = rd.nextInt(50)+1;
                                }
                                return getString(R.string.mult,a,b,c);
                            }
                        }
                    }
                }
            default:
                return "Enable at least one type of question.";
        }
    }
    public String typeOfQuestion(){
        Random rd = new Random();
        switch (rd.nextInt(4)){
            case 0:
                if(addition) {
                    return sumQuestion();
                }else{
                    if(subtraction){
                        return subtractQuestion();
                    }else{
                        if(division){
                            return divisionQuestion();
                        }else{
                            if(multiplication){
                                return multiplyQuestion();
                            }
                        }
                    }
                }
            case 1:
                if(subtraction) {
                    return subtractQuestion();
                }else{
                    if(addition){
                        return sumQuestion();
                    }else{
                        if(division){
                            return divisionQuestion();
                        }else{
                            if(multiplication){
                                return multiplyQuestion();
                            }
                        }
                    }
                }
            case 2:
                if(multiplication){
                    return multiplyQuestion();
                }else{
                    if(addition){
                        return sumQuestion();
                    }else{
                        if(subtraction){
                            return subtractQuestion();
                        }else{
                            if(division){
                                return divisionQuestion();
                            }
                        }
                    }
                }
            case 3:
                if(division){
                    return divisionQuestion();
                }else{
                    if(addition){
                        return sumQuestion();
                    }else{
                        if(subtraction){
                            return subtractQuestion();
                        }else{
                            if(multiplication){
                                return multiplyQuestion();
                            }
                        }
                    }
                }
            default:
                return "Enable at least one type of question.";
        }
    }
    public void generateQuestions(){
        Random rd = new Random();
        wrongOrCorrect = rd.nextInt(2);
        locationOfCorrectAnswer = rd.nextInt(4);
        for(int i = 0; i < 4; i++){
            if(i == locationOfCorrectAnswer){
                questions.add(typeOfQuestion());
            }else{
                questions.add(wrongTypeOfQuestion());
            }
        }
        feedBackNum = rd.nextInt(12);
        scoreText.setText(getString(R.string.score, score));
        button0.setText(questions.get(0));
        button1.setText(questions.get(1));
        button2.setText(questions.get(2));
        button3.setText(questions.get(3));
        questions.clear();
    }

    public void choose(View view){
        if(flashingText) {
            userFeedback.startAnimation(feedBackAnimation);
        }

        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){
            score++;
            numberOfQuestions++;
            generateQuestions();
            if(timer && ranBefore) {
                countDownTimer.start();
            }
            onARoll++;
            if(feedBackNum == 0 || numberOfQuestions == 1){
                userFeedback.setText(getString(R.string.good_job));
            }else if(feedBackNum == 1){
                userFeedback.setText(getString(R.string.amazing));
            }else if(feedBackNum == 2){
                userFeedback.setText(getString(R.string.fantastic));
            }else if(feedBackNum == 3){
                userFeedback.setText(getString(R.string.damn));
            }else if(feedBackNum == 4){
                userFeedback.setText(getString(R.string.genius));
            }else if(feedBackNum == 5){
                userFeedback.setText(getString(R.string.sweet));
            }else if(feedBackNum == 6){
                userFeedback.setText(getString(R.string.crazy));
            }else if(feedBackNum == 7){
                userFeedback.setText(getString(R.string.keep));
            }else if(feedBackNum == 8){
                userFeedback.setText(getString(R.string.unbelievable));
            }else if(feedBackNum == 9){
                userFeedback.setText(getString(R.string.surprised));
            }else if(feedBackNum == 10){
                userFeedback.setText(getString(R.string.brilliant));
            }
        }else {
            if(vibracion){
                vibrator.vibrate(500);

            }
            onARoll = 0;
            scoreincorrecta++;

            if(locationOfCorrectAnswer == 0){
                button0.startAnimation(correctAnimation);
            }else if(locationOfCorrectAnswer == 1){
                button1.startAnimation(correctAnimation);
            }else if(locationOfCorrectAnswer == 2){
                button2.startAnimation(correctAnimation);
            }else if(locationOfCorrectAnswer == 3){
                button3.startAnimation(correctAnimation);
            }
            Random rd = new Random();
            switch (rd.nextInt(3)){
                case 0:
                    userFeedback.setText(getString(R.string.ohno));
                    break;
                case 1:
                    userFeedback.setText(getString(R.string.next));
                    break;
                case 2:
                    userFeedback.setText(getString(R.string.sad));
                    break;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopUp();
                }
            }, 1000);


        }
    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }
    public void onBackPressed() {
        Intent intent = new Intent(JuegoTiempo.this, MenuJuegos.class);
        startActivity(intent);


        finish();
    }


}