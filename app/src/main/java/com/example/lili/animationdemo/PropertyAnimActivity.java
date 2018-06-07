package com.example.lili.animationdemo;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author li.li
 * @Description:
 * @date 18-5-31
 * @copyright MIBC
 */
public class PropertyAnimActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txt_anim;
    private LinearLayout ll_object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_anim);
        initView();
    }

    private void initView() {
        txt_anim = findViewById(R.id.shape_red);
        ll_object = findViewById(R.id.ll_object);
        findViewById(R.id.btn_value).setOnClickListener(this);
        findViewById(R.id.btn_object).setOnClickListener(this);
        findViewById(R.id.btn_xml).setOnClickListener(this);
        findViewById(R.id.alpha).setOnClickListener(this);
        findViewById(R.id.rotation).setOnClickListener(this);
        findViewById(R.id.translationX).setOnClickListener(this);
        findViewById(R.id.scaleY).setOnClickListener(this);
        findViewById(R.id.animation_set).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_value:
                valueAnim();
                break;
            case R.id.btn_object:
                ll_object.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_xml:
                xmlAnimation();
                break;
            case R.id.alpha:
                objectAnim("alpha", 1f, 0f, 1f);
                break;
            case R.id.rotation:
                objectAnim("rotation", 0f, 360f, 0f);
                break;
            case R.id.translationX:
                float currentX = txt_anim.getTranslationX();
                objectAnim("translationX", currentX, -400f, currentX);
                break;
            case R.id.scaleY:
                objectAnim("scaleY", 1f, 3f, 1f);
                break;
            case R.id.animation_set:
                objectAnimSet();
                //propertyAnim();
                break;
        }
    }

    private void valueAnim() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f, 0f);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (float) animation.getAnimatedValue();
                txt_anim.setScaleY(1 + currentValue);
                txt_anim.setScaleX(1 - currentValue);
                Log.i("li.li", "MainActivity|onAnimationUpdate:==" + currentValue);
            }
        });
        animator.start();
    }

    private void objectAnim(String type, float before, float after, float end) {
        final ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(txt_anim, type, before, after, end);
        objectAnimator.setDuration(3000);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    private void objectAnimSet() {
        float currentX = txt_anim.getTranslationX();
        float currentY = txt_anim.getTranslationY();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(txt_anim, "translationX", currentX, 600f, currentX);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(txt_anim, "translationY", currentY, -1000f, currentY);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(txt_anim, "rotation", 0, 360f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(txt_anim, "alpha", 1f, 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(txt_anim, "scaleY", 1f, 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(txt_anim, "scaleX", 1f, 0f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(2000);
        set.playTogether(translationX, translationY, rotation, alpha, scaleX, scaleY);
        //set.play(translationX).with(alpha).before(scaleY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
    }

    private void propertyAnim() {
        //针对同一个对象多个属性，同时作用多种动画
        float currentX = txt_anim.getTranslationX();
        PropertyValuesHolder valuesHolderT = PropertyValuesHolder.ofFloat("translationX", currentX, 500f, currentX);
        PropertyValuesHolder valuesHolderA = PropertyValuesHolder.ofFloat("alpha", 1f, 0.5f, 1f);
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0, 1f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(txt_anim, valuesHolderT, valuesHolderA, valuesHolderX, valuesHolderY)
                .setDuration(5000).start();
    }

    private void xmlAnimation() {
        @SuppressLint("ResourceType")
        Animator animator = AnimatorInflater.loadAnimator(this, R.anim.xml_animator);
        animator.setTarget(txt_anim);
        animator.start();
    }
}
