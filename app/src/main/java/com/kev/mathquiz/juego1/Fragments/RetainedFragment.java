package com.kev.mathquiz.juego1.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.kev.mathquiz.juego1.QuizContext;


/**
 * Created by AnshumanTripathi on 10/8/16.
 */

public class RetainedFragment extends Fragment {

    QuizContext data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
        {
            data = (QuizContext) savedInstanceState.getSerializable("data");

            if(data!=null)
                System.err.println("RetainedFragment millisLeft " + data.getMillisLeft());
            else
                System.err.println("RetainedFragment millisLeft null");
        }
        else
        {
            System.err.println("RetainedFragment saveInstanceState null");
        }
        setRetainInstance(true);
    }

    public void setData(QuizContext instance){
        this.data = instance;
    }

    public QuizContext getData(){
        return data;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("data", data);
    }
}
