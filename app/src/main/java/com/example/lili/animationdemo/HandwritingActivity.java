package com.example.lili.animationdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author li.li
 * @Description:
 * @date 18-5-31
 * @copyright MIBC
 */
public class HandwritingActivity extends AppCompatActivity implements HandwritingView.OnClickListener{
    private HandwritingView mHandwriting;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handwriting);
        mHandwriting=findViewById(R.id.handwriting_view);
        findViewById(R.id.handwriting_reset).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.handwriting_reset){
            mHandwriting.reset();
        }
    }
}
