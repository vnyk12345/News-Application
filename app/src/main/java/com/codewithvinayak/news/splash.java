package com.codewithvinayak.news;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
    ImageView logo;
    TextView name,owner;
    Animation topanim,bottomanim;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logo = findViewById(R.id.logoimage);
        name = findViewById(R.id.name);
        owner= findViewById(R.id.owner);

       topanim = AnimationUtils.loadAnimation(splash.this,R.anim.top_animation);
       bottomanim = AnimationUtils.loadAnimation(splash.this,R.anim.bottom_animation);

        logo.setAnimation(topanim);
        name.setAnimation(bottomanim);
        owner.setAnimation(bottomanim);







        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}