package com.kapture.mystreezofafrica.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kapture.mystreezofafrica.R;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;



/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {


    ViewGroup transitionsContainer;
    View items1,items2;
    Boolean visible;

    TextView tvSignUp;
    Button btnSignIn;
    Button btnEnter;
    View v;

    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
        transitionsContainer = (ViewGroup)v.findViewById(R.id.transition_container);

        tvSignUp = (TextView) v.findViewById(R.id.tvSignUp);
        btnSignIn = (Button)v.findViewById(R.id.btnSignIn);
        btnEnter = (Button)v.findViewById(R.id.btnEnter);

        items1 = transitionsContainer.findViewById(R.id.startitems);
        items2 = transitionsContainer.findViewById(R.id.signinitems);
        visible = true;
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickSignUp();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //listener.onClickSignIn();



                visible = !visible;

                TransitionSet set = new TransitionSet()
                        .addTransition(new Scale(0.2f))
                        .addTransition(new Fade())
                        .setInterpolator(visible ? new LinearOutSlowInInterpolator() :
                                new FastOutLinearInInterpolator());

                TransitionManager.beginDelayedTransition(transitionsContainer, set);
                items1.setVisibility(visible ? View.VISIBLE : View.GONE);
                items2.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickSignIn();
            }
        });

    }

    public interface StartFragmentListener{
        void onClickSignIn();
        void onClickSignUp();
    }

    private StartFragmentListener listener;
    public void setOnClickSignIn(StartFragmentListener listener){
        this.listener = listener;
    }
    public void setOnClickSignUp(StartFragmentListener listener){
        this.listener = listener;
    }
}
