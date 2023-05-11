package poly.edu.duan1_lampngpk02586.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.dao.account_dao;

public class forgot_activity extends AppCompatActivity {
    LottieAnimationView lottie;

    EditText ed_username, ed_phone;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String user = ed_username.getText().toString().trim();
            String phone = ed_phone.getText().toString().trim();
            if (!user.isEmpty() && !phone.isEmpty()) {
                findViewById(R.id.btn_reset).setEnabled(true);
                findViewById(R.id.btn_reset).setBackground(getResources().getDrawable(R.drawable.button_submit));
                Log.i("TAG", "onTextChanged: pink orange");
            } else {
                findViewById(R.id.btn_reset).setEnabled(false);
                findViewById(R.id.btn_reset).setBackground(getResources().getDrawable(R.drawable.button_disable));
                Log.i("TAG", "onTextChanged: gray");
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        lottie = findViewById(R.id.lottie);
        lottie.setRepeatCount(LottieDrawable.INFINITE);

        ed_username = findViewById(R.id.ed_username);
        ed_phone = findViewById(R.id.ed_phone);

        ed_username.addTextChangedListener(textWatcher);
        ed_phone.addTextChangedListener(textWatcher);
        findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = ed_username.getText().toString();
                String phone = ed_phone.getText().toString();

                account_dao account_dao = new account_dao(getBaseContext());
                if (account_dao.check_User_Phone(user, phone)) {
                    startActivity(new Intent(forgot_activity.this, reset_pw_activity.class));
                    Animatoo.INSTANCE.animateSlideLeft(forgot_activity.this);
                } else {

                    Toast.makeText(forgot_activity.this, "Your information is incorrect, please re-enter", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.tv_back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgot_activity.this, login_activity.class));
                Animatoo.INSTANCE.animateSlideRight(forgot_activity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(forgot_activity.this);
    }
}