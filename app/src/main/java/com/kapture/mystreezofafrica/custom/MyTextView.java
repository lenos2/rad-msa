package com.kapture.mystreezofafrica.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;

import com.kapture.mystreezofafrica.R;

/**
 * Created by lenos on 15/10/2017.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    public MyTextView(Context context,String classType) {
        super(context);

        switch (classType){
            case "h1":
                setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setPadding(20,5,20,20);
                setTextSize(25);
                setTextColor(getResources().getColor(R.color.colorBrown));
                //Set Text to bold
                setAllCaps(true);
                break;
            case "h2":
                setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setPadding(20,5,20,20);
                setTextSize(20);
                setTextColor(getResources().getColor(R.color.colorBlack));
                setAllCaps(true);
                setGravity(Gravity.CENTER);
                break;
            case "body":
                setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setPadding(20,5,20,20);
                setTextSize(15);
                setTextColor(getResources().getColor(R.color.colorBlack));
                setAllCaps(false);
                break;
            default:
                setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setPadding(20,5,20,20);
                setTextSize(15);
                setTextColor(getResources().getColor(R.color.colorBlack));
                setAllCaps(false);

        }

    }


    @Override
    public void setTypeface(Typeface tf) {
        super.setTypeface(tf);
    }


}
