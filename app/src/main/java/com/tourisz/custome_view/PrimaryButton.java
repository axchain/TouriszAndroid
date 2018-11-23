package com.tourisz.custome_view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tourisz.Application;
import com.tourisz.R;
import com.tourisz.util.helper.AndroidHelper;

public class PrimaryButton extends android.support.v7.widget.AppCompatButton{
    private Context context;
    public PrimaryButton(Context context) {
        super( context );
        this.context = context;
        initView();
    }

    public PrimaryButton(Context context, AttributeSet attrs) {
        super( context, attrs );
        this.context = context;
        initView();
    }

    public PrimaryButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        this.context = context;
        initView();
    }

    private void initView() {
        setBackground( AndroidHelper.getButtonShape() );
        setTextAppearance( context, R.style.PrimaryButtonTextStyle );

    }



}
