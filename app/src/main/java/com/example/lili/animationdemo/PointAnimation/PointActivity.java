package com.example.lili.animationdemo.PointAnimation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.lili.animationdemo.R;

/**
 * @author li.li
 * @Description:
 * @date 18-6-4
 * @copyright MIBC
 */
public class PointActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout mViewContainer;
    private PointMovement pointView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        findViewById(R.id.point_movement).setOnClickListener(this);
        mViewContainer = findViewById(R.id.anim_view_container);
        TextView view = new TextView(this);
        view.animate().alpha(0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.point_movement) {
            if (pointView == null) {
                pointView = new PointMovement(this, mViewContainer.getWidth(), mViewContainer.getHeight());
                mViewContainer.addView(pointView);
            }
            pointView.startSet();
        }
    }
}
