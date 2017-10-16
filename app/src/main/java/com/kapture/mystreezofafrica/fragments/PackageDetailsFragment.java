package com.kapture.mystreezofafrica.fragments;


import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kapture.mystreezofafrica.R;
import com.kapture.mystreezofafrica.custom.MyTextView;
import com.kapture.mystreezofafrica.custom.SquareLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackageDetailsFragment extends Fragment {

    LinearLayout llPackageDescription, llInner;
    View llDayHeaders;
    View v;
    Map<String,String> packageDetailsMap = new HashMap<>();
    static Map<String,Boolean> booleanRef = new HashMap<>();
    static Map<String,View> headerMap = new HashMap<>();
    static Map<String,LinearLayout> bodyMap = new HashMap<>();
    MyTextView heading,body;
    String packageName;
    ArrayList<LinearLayout> daysLayouts = new ArrayList<>();
    ImageView ivShowContents,ivPackageImage;
    ProgressDialog progressDialog;

    public PackageDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            packageName = getArguments().getString("packageName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_package_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v=view;


        llPackageDescription = (LinearLayout)v.findViewById(R.id.llpackageDetails);
        ivPackageImage = (ImageView)v.findViewById(R.id.ivPackageImage);

        fetchTourDetails();

    }

    private void fetchTourDetails(){

        progressDialog = ProgressDialog.show(v.getContext(),null,"Loading Content...");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("itinerary");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                packageDetailsMap.clear();
                daysLayouts.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //Toast.makeText(v.getContext(),snapshot.child("name").getValue(String.class),Toast.LENGTH_LONG).show();
                    if (snapshot.child("name").getValue(String.class).equals(packageName)) {

                        DataSnapshot days = snapshot.child("days");
                        //packageDetailsMap = (HashMap<String,String>)days.child("1").getValue();

                        for (DataSnapshot daySnapShot : days.getChildren()){

                            //Create a headerView
                            String keyId = "Day " + daySnapShot.getKey();
                            headerMap.put(keyId,LayoutInflater.from(v.getContext()).inflate(R.layout.layout_day,null));


                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                            Resources r = v.getResources();
                            int px = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    5,
                                    r.getDisplayMetrics());
                            params.setMargins(0,0,0,px);
                            headerMap.get(keyId).setLayoutParams(params);
                            //llDayHeaders = new View(v.getContext());
                            //llDayHeaders = ;
                            //llDayHeaders.setOrientation(LinearLayout.HORIZONTAL);

                            //create a body
                            bodyMap.put(keyId,new LinearLayout(v.getContext()));
                            //llInner = new LinearLayout(v.getContext());
                            boolean bool = true;
                            booleanRef.put(keyId,bool);
                            bodyMap.get(keyId).setVisibility(View.GONE);
                            bodyMap.get(keyId).setOrientation(LinearLayout.VERTICAL);
                            bodyMap.get(keyId).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                            headerMap.get(keyId).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    //String test;
                                    TextView h1 = (TextView)view.findViewById(R.id.tvDayNum);
                                    String theKey = h1.getText().toString();
                                    //Toast.makeText(v.getContext(), theKey, Toast.LENGTH_SHORT).show();
                                    boolean open = booleanRef.get(theKey);
                                    //Toast.makeText(v.getContext(), String.format("The id of this item is %d",view.getId()), Toast.LENGTH_SHORT).show();
                                    ImageView ivShowContents = (ImageView)headerMap.get(theKey).findViewById(R.id.ivDayButton);
                                    ivShowContents.setImageResource(open ? R.mipmap.ic_minus : R.mipmap.ic_plus);
                                    bodyMap.get(theKey).setVisibility(open ? View.VISIBLE : View.GONE);
                                    open = !open;
                                    booleanRef.put(theKey,open);
                                }
                            });

                            TextView header = (TextView)headerMap.get(keyId).findViewById(R.id.tvDayNum);
                            ivShowContents = (ImageView)headerMap.get(keyId).findViewById(R.id.ivDayButton);


                            //Add a header for each day
                            header.setText("Day " + daySnapShot.getKey());
                            header.getText().toString();

                            llPackageDescription.addView(headerMap.get(keyId));

                            packageDetailsMap = (HashMap<String,String>)daySnapShot.getValue();

                            for ( Map.Entry<String, String> entry : packageDetailsMap.entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();

                                heading = new MyTextView(v.getContext(),"h2");
                                heading.setText(key);

                                body = new MyTextView(v.getContext(),"h3");
                                body.setText(value);
                                body.setAllCaps(false);

                                bodyMap.get(keyId).addView(heading);
                                bodyMap.get(keyId).addView(body);
                            }

                            llPackageDescription.addView(bodyMap.get(keyId));
                        }
                        //return;
                    }
                }
                ScrollView svPackage = (ScrollView)v.findViewById(R.id.svPackageDetails);
                svPackage.scrollTo(0,ivPackageImage.getBottom());

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(v.getContext(),"Error occured. Error :" + databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
