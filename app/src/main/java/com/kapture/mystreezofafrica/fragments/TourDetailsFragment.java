package com.kapture.mystreezofafrica.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapture.mystreezofafrica.R;
import com.kapture.mystreezofafrica.pojos.Package;
import com.kapture.mystreezofafrica.pojos.Tour;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourDetailsFragment extends Fragment implements SpringListener{

    private static double TENSION = 400;
    private static double DAMPER = 300; //friction

    ImageView ivTourPicture;
    ListView lvPackages;
    TextView tvTourName;
    TextView tvTourDescription;
    Tour mTour = new Tour();
    ScrollView svTourDetails;

    ArrayList<Package> packages = new ArrayList<>();
    ArrayAdapter<Package> adapter;
    ArrayList<String> pics = new ArrayList<String>();
    View v;

    Spring mSpring;

    String tourName;
    ProgressDialog progressDialog;



    public TourDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            tourName = getArguments().getString("tour");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tour_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        v = view;
        ivTourPicture = (ImageView)v.findViewById(R.id.ivTourPicture);
        tvTourName = (TextView)v.findViewById(R.id.tvTourName);
        tvTourDescription = (TextView)v.findViewById(R.id.tvTourDescription);
        lvPackages = (ListView)v.findViewById(R.id.lvPackages);
        svTourDetails = (ScrollView)v.findViewById(R.id.svTourDetails);


        progressDialog = ProgressDialog.show(v.getContext(),null,"Loading...");
        fetchTourInfo();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("packages");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                packages.clear();
                Package aPackage;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    aPackage = snapshot.getValue(Package.class);
                    if (aPackage.getTour().equals(tourName)){
                        packages.add(aPackage);
                    }
                }
                adapter.notifyDataSetChanged();
                //svTourDetails.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                lvPackages.setFocusable(false);
                svTourDetails.scrollTo(0,0);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();
            }
        });


        adapter = new ArrayAdapter<Package>(v.getContext(),R.layout.layout_tour_option,packages){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                View v = convertView;
                if (v == null){
                    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.layout_tour_option,parent,false);
                }

                TextView tvPackageName = (TextView)v.findViewById(R.id.tvPackageName);

                Package item = (Package) getItem(position);
                tvPackageName.setText(item.getName());
                return v;
            }
        };

        lvPackages.setAdapter(adapter);

        //Add the onclick listener
        lvPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onSelect(packages.get(i).getName());
            }
        });
        lvPackages.setFocusable(false);

        // Create a system to run the physics loop for a set of springs.
        SpringSystem springSystem = SpringSystem.create();

        mSpring = springSystem.createSpring();
        mSpring.addListener(this);

        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        mSpring.setSpringConfig(config);

        ivTourPicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // When pressed start solving the spring to 1.
                        mSpring.setEndValue(1);

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // When released start solving the spring to 0.
                        mSpring.setEndValue(0);

                        pics.add("https://firebasestorage.googleapis.com/v0/b/mystreez-5a20b.appspot.com/o/packages%2Fvic_falls%2Fvictoria-falls-sunset-660x386.jpg?alt=media&token=cb8591a5-e4cc-4632-9ad0-fcac15798929");

                        //Has Nice Image Transitions
                        new ImageViewer.Builder(view.getContext(), pics)
                                .setStartPosition(000000)
                                .show();
                        break;
                }
                return true;
            }
        });



    }


    private void fetchTourInfo(){
        FirebaseDatabase.getInstance().getReference("tours").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("name").getValue(String.class).equals(tourName)){
                        //mTour.setImage(snapshot.child("image").getValue(String.class));
                        //mTour.setName(snapshot.child("name").getValue(String.class));
                        //mTour.setTourType(snapshot.child("tourType").getValue(String.class));

                        mTour = snapshot.getValue(Tour.class);
                    }
                }

                Glide.with(v.getContext()).load(mTour.getImage()).into(ivTourPicture);
                tvTourName.setText(mTour.getName());
                if (mTour.getDescription() != null)
                    tvTourDescription.setText(mTour.getDescription());
                lvPackages.setFocusable(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Overrides of SpringListener interface
    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);


        ivTourPicture.setScaleX(scale);
        ivTourPicture.setScaleY(scale);
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

    public interface OnPackageSelectListener{
        void onSelect(String packageName);
    }
    private OnPackageSelectListener listener;
    public void setOnPackageSelectListener(OnPackageSelectListener listener){
        this.listener = listener;
    }
}
