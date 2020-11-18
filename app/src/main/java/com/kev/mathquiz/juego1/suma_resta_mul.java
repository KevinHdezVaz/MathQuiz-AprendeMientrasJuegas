package com.kev.mathquiz.juego1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kev.mathquiz.AjustesDelJuego.Ajustes;
import com.kev.mathquiz.MenuJuegos;
import com.kev.mathquiz.R;
import com.kev.mathquiz.juego1.Fragments.RetainedFragment;
import com.mrntlu.toastie.Toastie;
import com.scwang.wave.MultiWaveHeader;

import java.util.Random;

public class suma_resta_mul extends AppCompatActivity  {
    private RetainedFragment dataFragment;
    TextView operationText, timeText;
    TextView answerText;
    Button submitButton,salir,seguir;
    Button clearButton;
    CountDownTimer cdt;
    TextView num1Text;
    View line;
    TextView num2Text;
    TextView timer;
    TextView questionNumber;
    Dialog epicDialog;
    ImageView cerrar;
    TextView resultado;
    long millisLeft;
    Button botonvamo;
    TextView mensajeResultado;
    LottieAnimationView animationWithLottie;
    ImageButton pausa;
    private MediaPlayer mediaPlayer;
    int length;
    ImageButton muteButton;
    private Boolean mute;
    SharedPreferences sharedPref;
     private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suma_resta_mul);
        line = findViewById(R.id.line);
        questionNumber = (TextView) findViewById(R.id.qNumber);
        operationText = (TextView) findViewById(R.id.operation);
        num1Text = (TextView) findViewById(R.id.numText1);
        num2Text = (TextView) findViewById(R.id.numText2);
        timer = (TextView) findViewById(R.id.timer);
        answerText = (TextView) findViewById(R.id.answer);
        submitButton = (Button) findViewById(R.id.enter);
        clearButton = (Button) findViewById(R.id.clear);
        animationWithLottie = (LottieAnimationView) findViewById(R.id.Animacion);
        MultiWaveHeader waveHeader;
        pausa = findViewById(R.id.pauseButton);
        timeText = findViewById(R.id.timeText);



        //anuncios admob, cambiar el ID a tu cuenta
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.anuncio));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });


        waveHeader = findViewById(R.id.wave_header);

        waveHeader.setVelocity(6);
        waveHeader.setProgress(1);
        waveHeader.isRunning();
        waveHeader.setGradientAngle(45);
        waveHeader.setWaveHeight(70);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        mute = sharedPref.getBoolean(Ajustes.KEY_MUTE_MUSIC, false);
        mediaPlayer = MediaPlayer.create(this, R.raw.the_duel);
        mediaPlayer.setLooping(true);
        Boolean darkModePref = sharedPref.getBoolean(Ajustes.KEY_DARK_MODE_SWITCH, false);

        if (!mute) {
            mediaPlayer.start();
        } else {
        }
        if (darkModePref) {//habilitado el modo oscuro
            RelativeLayout relativelayout = (findViewById(R.id.activity_quiz));
            relativelayout.setBackgroundColor(getResources().getColor(R.color.cgray));
            waveHeader.setStartColor(R.color.colorOlas);
            line.setBackgroundColor(getResources().getColor(R.color.white));
            operationText.setTextColor(getResources().getColor(R.color.white));
            answerText.setTextColor(getResources().getColor(R.color.blanquito));

            pausa.setImageResource(R.drawable.ic_pause_black2_24dp);


            timeText.setTextColor(getResources().getColor(R.color.white));
            num2Text.setTextColor(getResources().getColor(R.color.white));
            num1Text.setTextColor(getResources().getColor(R.color.white));
            getWindow().setStatusBarColor(getResources().getColor(R.color.qboard_black));
        }




        //Get operation from launcher activity
        Bundle extras = getIntent().getExtras();
        final String operation = extras.getString("operation");

        if (!isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        FragmentManager fm = getSupportFragmentManager();

        if (savedInstanceState != null) {

            QuizContext quizData = (QuizContext) savedInstanceState.getSerializable("data");

            if (quizData != null) {
                System.err.println("quizdata " + quizData.getMillisLeft());
            } else {
                System.err.println("quizdata null");
            }

            dataFragment = (RetainedFragment) fm.findFragmentByTag("data");
            QuizContext instance = dataFragment.getData();

            QuizContext.getInstance().updateInstance(instance);
            System.err.println("instance " + instance.getMillisLeft() + " quizContext " + QuizContext.getInstance().getMillisLeft());
            if (QuizContext.getInstance().getMillisLeft() != 0) {
                millisLeft = QuizContext.getInstance().getMillisLeft();
                cdt = new CountDownTimer(millisLeft, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        setOnTick(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        nextQuestion();
                    }
                }.start();
            }
        } else {
            dataFragment = new RetainedFragment();
            getSupportFragmentManager().beginTransaction().add(dataFragment, "data").commit();
            dataFragment.setData(QuizContext.getInstance());
            QuizContext.getInstance().setOperation(operation);

            Random randomNumberGenerator = new Random();

            //Generar los numeros aleatorios
            int num1_mult = randomNumberGenerator.nextInt((12) + 1) + 1;

            int num1 = randomNumberGenerator.nextInt((70) + 1) + 1;

            QuizContext.getInstance().setNum1(num1);
            QuizContext.getInstance().setNum1_multipli(num1_mult);


            if (operation.equals("sub")) {
                //For subtraction generate number smaller than num1
                int num2 = randomNumberGenerator.nextInt(num1) + 1;


                QuizContext.getInstance().setNum2(num2);
            } else {
                int num2_mult = randomNumberGenerator.nextInt((14) + 1) + 1;
                QuizContext.getInstance().setNum2_multipli(num2_mult);


                int num2 = randomNumberGenerator.nextInt((80) + 1) + 1;
                QuizContext.getInstance().setNum2(num2);
            }


            //Count Down Timer for 5 seconds
            cdt = new CountDownTimer(10000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setOnTick(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    nextQuestion();
                }
            }.start();
        }

        pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitQuizDialog();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answerText.getText().toString();
                if (answer.equals("")) {
                    Toastie.centerInfo(suma_resta_mul.this, "Vacio", Toast.LENGTH_SHORT).show();

                } else {
                    QuizContext.getInstance().setAnswer(Integer.parseInt(answer));
                    int solution = 0;
                    String operation = QuizContext.getInstance().getOperation();
                    switch (operation) {
                        case "add":
                            solution = QuizContext.getInstance().getNum1() + QuizContext.getInstance().getNum2();
                            break;
                        case "mul":
                            solution = QuizContext.getInstance().getNum1_multipli() * QuizContext.getInstance().getNum2_multipli();
                            break;
                        case "sub":
                            solution = QuizContext.getInstance().getNum1() - QuizContext.getInstance().getNum2();
                            break;
                    }

                    if (solution == QuizContext.getInstance().getAnswer()) {
                        int points = QuizContext.getInstance().getPoints();
                        QuizContext.getInstance().setPoints(++points);
                        Toastie.centerSuccess(suma_resta_mul.this, "Correcto", Toast.LENGTH_SHORT).show();

                        nextQuestion();
                    } else {

                        Toastie.centerInfo(suma_resta_mul.this, "Incorrecto", Toast.LENGTH_SHORT).show();
                        nextQuestion();
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerText.setText("");
            }
        });

    }

    //Generate a Dialog on back key pressed
    @Override
    public void onBackPressed() {
        showExitQuizDialog();
    }


    public void showExitQuizDialog() {

        cdt.cancel();
        animationWithLottie.pauseAnimation();

        mostrarSalir();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    public void setOnTick(long millisUntilFinished) {
        millisLeft = millisUntilFinished;
        int timeLeft = (int) millisUntilFinished / 1000;

        timer.setText(String.valueOf(String.valueOf(timeLeft)));
        switch (QuizContext.getInstance().getOperation()) {
            case "add":
                operationText.setText("+");
                break;
            case "sub":
                operationText.setText("-");
                break;
            case "mul":
                operationText.setText("x");
                break;
        }
        String answer = answerText.getText().toString();
        if (!answer.equals("")) {
            QuizContext.getInstance().setAnswer(Integer.parseInt(answer));
            int solution = 0;
            String operation = QuizContext.getInstance().getOperation();
            switch (operation) {
                case "add":
                    solution = QuizContext.getInstance().getNum1() + QuizContext.getInstance().getNum2();
                    break;
                case "mul":
                    solution = QuizContext.getInstance().getNum1_multipli() * QuizContext.getInstance().getNum2_multipli();
                    break;
                case "sub":
                    solution = QuizContext.getInstance().getNum1() - QuizContext.getInstance().getNum2();
                    break;
            }

            if (solution == QuizContext.getInstance().getAnswer()) {
                int points = QuizContext.getInstance().getPoints();
                QuizContext.getInstance().setPoints(++points);

                Toastie.centerSuccess(suma_resta_mul.this, "Correcto", Toast.LENGTH_SHORT).show();
                nextQuestion();
            }
        }
        if (!questionNumber.getText().equals("#10"))

            questionNumber.setText(QuizContext.getInstance().getNumberOfQuestions() + "/10");


        num1Text.setText(String.valueOf(QuizContext.getInstance().getNum1()));
        num2Text.setText(String.valueOf(QuizContext.getInstance().getNum2()));

        String operation = QuizContext.getInstance().getOperation();

        switch (operation) {
            case "mul":
                num1Text.setText(String.valueOf(QuizContext.getInstance().getNum1_multipli()));
                num2Text.setText(String.valueOf(QuizContext.getInstance().getNum2_multipli()));

        }

    }

    //Set Answer Text
    public void setValue(View v) {
        String answer = answerText.getText().toString();
        if (answer.length() <= 2)
            answerText.append(v.getTag().toString());
    }

    //Go to next question
    public void nextQuestion() {
        cdt.cancel(); //Cancel current timer and move to next question
        QuizContext.getInstance().setNumberOfQuestions(QuizContext.getInstance().getNumberOfQuestions() + 1);
        if (QuizContext.getInstance().getNumberOfQuestions() <= 10) {
            //Still Questions left, Reload this activity
            finish();
            startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        } else {
            animationWithLottie.cancelAnimation();


            mostrarInfo();
        }
    }

    //Lock background screen on dialog
    private void lockDialogBackground(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying() && !mute) {
            mediaPlayer.pause();
            length = mediaPlayer.getCurrentPosition();
        }
        cdt.cancel();
        if (millisLeft != 0) {
            QuizContext.getInstance().setMillisLeft(millisLeft);
        }
        dataFragment.setData(QuizContext.getInstance());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        mediaPlayer.release();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        cdt = new CountDownTimer(millisLeft, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                int timeLeft = (int) millisUntilFinished / 1000;
                timer.setText(String.valueOf(String.valueOf(timeLeft)));
                switch (QuizContext.getInstance().getOperation()) {
                    case "add":
                        operationText.setText("+");
                        break;
                    case "sub":
                        operationText.setText("-");
                        break;
                    case "mul":
                        operationText.setText("x");
                        break;
                }
                String answer = answerText.getText().toString();
                if (!answer.equals("")) {
                    QuizContext.getInstance().setAnswer(Integer.parseInt(answer));
                    int solution = 0;
                    String operation = QuizContext.getInstance().getOperation();
                    switch (operation) {
                        case "add":
                            solution = QuizContext.getInstance().getNum1() + QuizContext.getInstance().getNum2();
                            break;
                        case "mul":
                            solution = QuizContext.getInstance().getNum1_multipli() * QuizContext.getInstance().getNum2_multipli();
                            break;
                        case "sub":
                            solution = QuizContext.getInstance().getNum1() - QuizContext.getInstance().getNum2();
                            break;
                    }

                    if (solution == QuizContext.getInstance().getAnswer()) {
                        int points = QuizContext.getInstance().getPoints();
                        QuizContext.getInstance().setPoints(++points);


                        nextQuestion();
                    }
                }
                if (!questionNumber.getText().equals("#10"))
                    questionNumber.setText(QuizContext.getInstance().getNumberOfQuestions() + "/10");


                num1Text.setText(String.valueOf(QuizContext.getInstance().getNum1()));
                num2Text.setText(String.valueOf(QuizContext.getInstance().getNum2()));


                String operation = QuizContext.getInstance().getOperation();

                switch (operation) {
                    case "mul":
                        num1Text.setText(String.valueOf(QuizContext.getInstance().getNum1_multipli()));
                        num2Text.setText(String.valueOf(QuizContext.getInstance().getNum2_multipli()));

                }
            }

            @Override
            public void onFinish() {
                nextQuestion();
            }
        }.start();

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("data", QuizContext.getInstance());
        outState.putInt("testInt", 99);

        super.onSaveInstanceState(outState);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        QuizContext quizData = (QuizContext) savedInstanceState.getSerializable("data");

        if (quizData != null) {
            System.err.println("quizdata " + quizData.getMillisLeft());
        } else {
            System.err.println("quizdata null");
        }

        System.err.println("testInt " + savedInstanceState.getInt("testInt"));

    }

    public void mostrarInfo() {
        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.custom_resultado);
        cerrar = (ImageView) epicDialog.findViewById(R.id.cerrarVentana);
        resultado = (TextView) epicDialog.findViewById(R.id.txtResultado);
        mensajeResultado = (TextView) epicDialog.findViewById(R.id.msjResultado);

        if (QuizContext.getInstance().getPoints() <= 5) {
            mensajeResultado.setText("Sigue intentandolo, no todo se logra a la primera ;) ");
        } else if (QuizContext.getInstance().getPoints() <= 7) {
            mensajeResultado.setText("¡Animo, la paciencia y esfuerzo son las mejores virtudes!");
        } else if (QuizContext.getInstance().getPoints() == 8) {
            mensajeResultado.setText("8 aciertos, casi le das... Como a tu crush.");
        } else if (QuizContext.getInstance().getPoints() == 9) {
            mensajeResultado.setText("9 aciertos chaval");
        } else if (QuizContext.getInstance().getPoints() == 10) {
            mensajeResultado.setText("Me asombra tu intelecto, ¿Acaso eres humano? :o");
        }

        resultado.setText(QuizContext.getInstance().getPoints() + "/10");
        botonvamo = (Button) epicDialog.findViewById(R.id.botonvamo);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuizContext.getInstance().resetContext();
                Intent intent = new Intent(suma_resta_mul.this, MenuJuegos.class);
                startActivity(intent);

                epicDialog.dismiss();
                finish();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });

        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        epicDialog.setCancelable(false);
        epicDialog.setCanceledOnTouchOutside(false);
        epicDialog.show();
        botonvamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QuizContext.getInstance().resetContext();
                Intent intent = new Intent(suma_resta_mul.this, MenuJuegos.class);
                startActivity(intent);
                finish();

            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!mediaPlayer.isPlaying() && !mute) {
            mediaPlayer.seekTo(length);
            mediaPlayer.pause();
        }
    }

    public void mostrarSalir() {
        epicDialog = new Dialog(this);
        epicDialog.setContentView(R.layout.custom_salir);

        seguir = (Button) epicDialog.findViewById(R.id.seguir);
        salir = (Button) epicDialog.findViewById(R.id.salir);
        seguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 cdt = new CountDownTimer(millisLeft, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        setOnTick(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        nextQuestion();
                    }
                }.start();
                epicDialog.dismiss();
                animationWithLottie.resumeAnimation();

            }
        });
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                QuizContext.getInstance().resetContext();
                cdt.cancel();

                Intent intent = new Intent(suma_resta_mul.this, MenuJuegos.class);
                startActivity(intent);


                 finish();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        epicDialog.setCancelable(false);
        epicDialog.setCanceledOnTouchOutside(false);
        epicDialog.show();



    }


}

