package com.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Максим on 12.08.2016.
 */
public class SplashActivity extends AppCompatActivity {
    TextView splashtext;
    AlphaAnimation alphaAnimation;
    ImageView splashimage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2500);
        splashtext=(TextView)findViewById(R.id.splashtext);
      splashimage=(ImageView)findViewById(R.id.splashimage);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2500);
        splashimage.setAnimation(rotate);

        splashtext.setAnimation(alphaAnimation);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2500);
                    Intent intent = new Intent(getApplication(),MainActivity.class);
                    startActivity(intent); finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        };
        thread.start();
    }
}
