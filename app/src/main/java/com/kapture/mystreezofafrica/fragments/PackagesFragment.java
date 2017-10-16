package com.kapture.mystreezofafrica.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapture.mystreezofafrica.R;
import com.kapture.mystreezofafrica.pojos.Tour;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackagesFragment extends Fragment {

    View v;
    String tourType;
    ListView lvPackages;
    ArrayAdapter<Tour> adapter;
    ArrayList<Tour> tours = new ArrayList<>();
    ProgressDialog progressDialog;


    public PackagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            tourType = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_packages, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        v = view;

        lvPackages = (ListView)v.findViewById(R.id.lvPackages);

        progressDialog = ProgressDialog.show(v.getContext(),null,"Loading...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tours");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tours.clear();

                Tour mTours = new Tour();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mTours = snapshot.getValue(Tour.class);
                    if (mTours.getTourType().equals(tourType)){
                        tours.add(mTours);
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });

        adapter = new ArrayAdapter<Tour>(v.getContext(),R.layout.layout_tour_list_item,tours){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = convertView;
                if (v == null){
                    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.layout_tour_list_item,parent,false);
                }

                TextView tvPackageName = (TextView)v.findViewById(R.id.tvTourName);
                TextView tvPackageDescription = (TextView)v.findViewById(R.id.tvTourDescription);
                ImageView ivPackageImage = (ImageView)v.findViewById(R.id.ivTourImage);

                Tour item = (Tour) getItem(position);
                tvPackageName.setText(item.getName());
                tvPackageDescription.setVisibility(View.GONE);
                Glide.with(v.getContext()).load(item.getImage()).into(ivPackageImage);
                return v;
            }
        };

        lvPackages.setAdapter(adapter);
        lvPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onClick(tours.get(i).getName());
            }
        });

    }

    public interface OnPackageSelectListener{
        void onClick(String tour);
    }

    private OnPackageSelectListener listener;
    public void setOnPackageSelectListener(OnPackageSelectListener listener){
        this.listener = listener;
    }

}
