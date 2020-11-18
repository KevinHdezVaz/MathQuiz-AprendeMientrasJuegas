package com.kev.mathquiz.juego1;

import java.io.Serializable;

public class QuizContext implements Serializable {

    private static QuizContext instance = null;

    private static int answer;
    private static int num1;
    private static int num2;
    private static int num1_multipli;
    private static int num2_multipli;



    private static int points;
    private static int numberOfQuestions = 1;
    private static long millisLeft = 0;
    private static String operation;

    private QuizContext() {
    }

    public static void updateInstance(QuizContext updatedInstance){
        instance = updatedInstance;
    }

    static QuizContext getInstance() {
        if (instance == null) {
            return new QuizContext();
        }
        return instance;
    }

    int getAnswer() {
        return answer;
    }

    void setAnswer(int answerUser) {
        answer = answerUser;
    }

    int getPoints() {
        return points;
    }

    void setPoints(int pointsUser) {
        points = pointsUser;
    }

    int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    void setNumberOfQuestions(int questions) {
        numberOfQuestions = questions;
    }

    String getOperation() {
        return this.operation;
    }


    public void setOperation(String operationUser) {
        operation = operationUser;
     }

    int getNum1() {
        return num1;
    }

    void setNum1(int randomNum1) {
        num1 = randomNum1;
    }

    int getNum2() {
        return num2;
    }

    void setNum2(int randomNum2) {
        num2 = randomNum2;
    }

    int getNum1_multipli() {
        return num1_multipli;
    }

    void setNum1_multipli(int randonNumMulti1) {
        num1_multipli = randonNumMulti1;
    }

     int getNum2_multipli() {
        return num2_multipli;
    }

    void setNum2_multipli(int randonNumMulti2) {
        num2_multipli = randonNumMulti2;
    }

    //Reset QuizContext
    void resetContext() {
        QuizContext.getInstance().setPoints(0);
        QuizContext.getInstance().setNumberOfQuestions(1);
    }

    public long getMillisLeft() {
        return millisLeft;
    }

    public void setMillisLeft(long millisLeft) {
        this.millisLeft = millisLeft;
    }
}