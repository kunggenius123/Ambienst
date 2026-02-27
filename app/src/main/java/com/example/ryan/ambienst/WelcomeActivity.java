package com.example.ryan.ambienst;

import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    RelativeLayout screen;
    TextView welcome_msg;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcome_msg = (TextView) findViewById(R.id.welcome_msg);

        animateText(getApplicationContext(), R.anim.slide_in_left_anim, welcome_msg);

        screen = (RelativeLayout) findViewById(R.id.relativeLayout);

        //When clicked, starts intent to Main Activity
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_left_trans, R.anim.slide_out_right_trans);
            }
        });
    }

    public void animateText(Context context, int anim, TextView text){
        Animation animation = AnimationUtils.loadAnimation(context, anim);
        text.startAnimation(animation);

    }


}
