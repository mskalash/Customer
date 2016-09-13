package mskalash.customer;

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

import com.customer.R;


public class ActivitySplash extends AppCompatActivity {
    private TextView splashText;
    private AlphaAnimation alphaAnimation;
    private ImageView splashImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2500);
        splashText = (TextView) findViewById(R.id.splashtext);
        splashImage = (ImageView) findViewById(R.id.splashimage);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(2500);
        splashImage.setAnimation(rotate);

        splashText.setAnimation(alphaAnimation);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2500);
                    Intent intent = new Intent(getApplication(), ActivityMain.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        };
        thread.start();
    }
}
