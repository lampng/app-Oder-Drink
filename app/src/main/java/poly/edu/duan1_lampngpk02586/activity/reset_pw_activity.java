package poly.edu.duan1_lampngpk02586.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.dao.account_dao;


public class reset_pw_activity extends AppCompatActivity {
    Button btn_submit;
    EditText ed_password, ed_confirm_password;

    AwesomeValidation validation;

    LottieAnimationView lottie;
    SharedPreferences sharedPreferences;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String user = ed_password.getText().toString().trim();
            String pass = ed_confirm_password.getText().toString().trim();
            if (!user.isEmpty() && !pass.isEmpty()) {
                findViewById(R.id.btn_submit).setEnabled(true);
                findViewById(R.id.btn_submit).setBackground(getResources().getDrawable(R.drawable.button_submit));
                Log.i("TAG", "onTextChanged: pink orange");
            } else {
                findViewById(R.id.btn_submit).setEnabled(false);
                findViewById(R.id.btn_submit).setBackground(getResources().getDrawable(R.drawable.button_disable));
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
        setContentView(R.layout.activity_reset_pw);

        lottie = findViewById(R.id.lottie);
        lottie.setRepeatCount(LottieDrawable.INFINITE);

        //get username from forgot_activity
        sharedPreferences = getSharedPreferences("ac_information", MODE_PRIVATE);
        String username = sharedPreferences.getString("ac_id_forgot", "");
        account_dao account_dao = new account_dao(getBaseContext());
        ed_password = findViewById(R.id.ed_password);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        ed_password.addTextChangedListener(textWatcher);
        ed_confirm_password.addTextChangedListener(textWatcher);
        //Validate
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(reset_pw_activity.this, R.id.ed_password, "^" + "(?=\\S+$)" + ".{6,}" + "$", R.string.err_password);
        validation.addValidation(reset_pw_activity.this, R.id.ed_confirm_password, R.id.ed_password, R.string.err_password_confirmation);

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation.validate()) {
                    String password = ed_password.getText().toString();
                    String confirm_password = ed_confirm_password.getText().toString();
                    if (confirm_password.equals(password)) {
                        account_dao.reset_password(username, password);
                    }
                }
            }
        });
        findViewById(R.id.tv_back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(reset_pw_activity.this, login_activity.class).setFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)));
                Animatoo.INSTANCE.animateSlideRight(reset_pw_activity.this);
            }
        });
    }
}