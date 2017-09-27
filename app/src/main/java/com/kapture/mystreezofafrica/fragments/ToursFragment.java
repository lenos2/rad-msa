package com.kapture.mystreezofafrica.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.kapture.mystreezofafrica.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursFragment extends Fragment implements SpringListener, View.OnTouchListener{


    View img1,img2,img3,img4,img5,img6;
    View v;
    static View mView;

    private static double TENSION = 400;
    private static double DAMPER = 300; //friction
    Spring mSpring;


    public ToursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tours, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create a system to run the physics loop for a set of springs.
        SpringSystem springSystem = SpringSystem.create();

        mSpring = springSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);


        v = view;
        img1 = view.findViewById(R.id.vicfallsview);

        img2 = view.findViewById(R.id.vicfallsview2);
        img3 = view.findViewById(R.id.vicfallsview3);
        img4 = view.findViewById(R.id.vicfallsview4);
        img5 = view.findViewById(R.id.vicfallsview5);
        img6 = view.findViewById(R.id.vicfallsview6);


        img1.setOnTouchListener(this);


        img2.setOnTouchListener(this);

        img3.setOnTouchListener(this);
        img4.setOnTouchListener(this);
        img5.setOnTouchListener(this);

        img6.setOnTouchListener(this);
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);


        mView.setScaleX(scale);
        mView.setScaleY(scale);
    }

    @Override
    public void onSpringAtRest(Spring spring) {

    }

    @Override
    public void onSpringActivate(Spring spring) {

    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // When pressed start solving the spring to 1.
                mView = view;
                mSpring.setEndValue(1);
                break;
            case MotionEvent.ACTION_UP:
                // When released start solving the spring to 0.
                mSpring.setEndValue(0);
                listener.onClick();
                break;
            case MotionEvent.ACTION_CANCEL:
                // When released start solving the spring to 0.
                mSpring.setEndValue(0);

                break;
        }
        return true;
    }


    public interface DetailsListener{
        void onClick();
    }

    private DetailsListener listener;
    public void setDetailsListener(DetailsListener listener){
        this.listener = listener;
    }
}
