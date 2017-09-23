package com.kapture.mystreezofafrica.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kapture.mystreezofafrica.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursFragment extends Fragment{


    View img1,img2,img3,img4,img5,img6;
    View v;
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
        v = view;
        img1 = view.findViewById(R.id.vicfallsview);

        img2 = view.findViewById(R.id.vicfallsview2);
        img3 = view.findViewById(R.id.vicfallsview3);
        img4 = view.findViewById(R.id.vicfallsview4);
        img5 = view.findViewById(R.id.vicfallsview5);
        img6 = view.findViewById(R.id.vicfallsview6);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });

        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });
    }


    public interface DetailsListener{
        void onClick();
    }

    private DetailsListener listener;
    public void setDetailsListener(DetailsListener listener){
        this.listener = listener;
    }
}
