package poly.edu.duan1_lampngpk02586.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import poly.edu.duan1_lampngpk02586.R;

public class flash_screen extends AppCompatActivity {
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        lottie = findViewById(R.id.lottie);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), login_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }, 5000);
    }
}