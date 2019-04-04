package com.blogspot.blogsetyaaji.istagramclone;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class RegisterActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    RelativeLayout parentrootregsiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        parentrootregsiter = findViewById(R.id.parentrootregsiter);

        // onCreate
        animationDrawable = (AnimationDrawable) parentrootregsiter.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onResume
        animationDrawable.start();
    }
}
