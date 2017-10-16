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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapture.mystreezofafrica.R;
import com.kapture.mystreezofafrica.pojos.TourType;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllToursFragment extends Fragment {

    View v;
    ListView lvPackages;
    ArrayAdapter adapter;
    ArrayList<TourType> tourTypes = new ArrayList<>();
    ProgressDialog progressDialog;

    public AllToursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_tours, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;

        lvPackages = (ListView)v.findViewById(R.id.lvPackageTypes);
        progressDialog = ProgressDialog.show(v.getContext(),null,"Loading...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("package_types");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tourTypes.clear();
                for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                    tourTypes.add(snapshot.getValue(TourType.class));
                }

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(v.getContext(),"An error occured. Error: " + databaseError.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        adapter = new ArrayAdapter(v.getContext(),R.layout.layout_tour_list_item, tourTypes){
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

                TourType item = (TourType)getItem(position);
                tvPackageName.setText(item.getName());
                tvPackageDescription.setVisibility(View.GONE);
                Glide.with(v.getContext()).load(item.getImage()).into(ivPackageImage);
                return v;
            }
        };


        lvPackages.setDividerHeight(0);
        lvPackages.setAdapter(adapter);
        lvPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onPackageClick(tourTypes.get(i));
            }
        });
    }

    public interface TourpackageListener{
        void onPackageClick(TourType type);
    }

    private TourpackageListener listener;
    public void setTourpackageListener(TourpackageListener listener){
        this.listener = listener;
    }
}
