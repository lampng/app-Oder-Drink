package poly.edu.duan1_lampngpk02586.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.dao.account_dao;
import poly.edu.duan1_lampngpk02586.model.account_model;

public class register_activity extends AppCompatActivity {
    EditText ed_username, ed_first_name, ed_middle_name, ed_last_name, ed_phone, ed_password, ed_confirm_password, ed_address;
    Button btnRegister;

    private AwesomeValidation validation;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        //        String user, String fn, String mn, String ls, String email, String address, String pass, String confirmPass
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String user = ed_username.getText().toString().trim();
            String fn = ed_first_name.getText().toString().trim();
            String mn = ed_middle_name.getText().toString().trim();
            String ln = ed_last_name.getText().toString().trim();
            String phone = ed_phone.getText().toString().trim();
            String pass = ed_password.getText().toString().trim();
            String confirm_pass = ed_confirm_password.getText().toString().trim();
            String address = ed_address.getText().toString().trim();

            if (isEmpty(user) && isEmpty(fn) && isEmpty(mn) && isEmpty(ln) && isEmpty(phone) && isEmpty(pass) && isEmpty(confirm_pass) && isEmpty(address)) {
                btnRegister.setEnabled(true);
                btnRegister.setBackground(getResources().getDrawable(R.drawable.button_submit));
            } else {
                btnRegister.setEnabled(false);
                btnRegister.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findID();

        account_dao account_dao = new account_dao(this);

        ed_username.addTextChangedListener(textWatcher);
        ed_first_name.addTextChangedListener(textWatcher);
        ed_middle_name.addTextChangedListener(textWatcher);
        ed_last_name.addTextChangedListener(textWatcher);
        ed_phone.addTextChangedListener(textWatcher);
        ed_password.addTextChangedListener(textWatcher);
        ed_confirm_password.addTextChangedListener(textWatcher);
        ed_address.addTextChangedListener(textWatcher);

        //Validation
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(register_activity.this, R.id.ed_username, "^" + "(?=\\S+$)" + ".{6,}" + "$", R.string.err_username);
        validation.addValidation(register_activity.this, R.id.ed_first_name, "[a-zA-Z\\s]+", R.string.err_fn);
        validation.addValidation(register_activity.this, R.id.ed_middle_name, "[a-zA-Z\\s]+", R.string.err_mn);
        validation.addValidation(register_activity.this, R.id.ed_last_name, "[a-zA-Z\\s]+", R.string.err_ln);
        validation.addValidation(register_activity.this, R.id.ed_phone, "0[0-9]{9,10}", R.string.err_phone);
        validation.addValidation(register_activity.this, R.id.ed_password, "^" + "(?=\\S+$)" + ".{6,}" + "$", R.string.err_password);
        validation.addValidation(register_activity.this, R.id.ed_confirm_password, R.id.ed_password, R.string.err_password_confirmation);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation.validate()) {
                    String id = ed_username.getText().toString();
                    int role = 3;
                    String fn = ed_first_name.getText().toString().trim();
                    String mn = ed_middle_name.getText().toString().trim();
                    String ln = ed_last_name.getText().toString().trim();
                    String phone = ed_phone.getText().toString();
                    String pw = ed_password.getText().toString();
                    String cpw = ed_confirm_password.getText().toString();
                    String address = ed_address.getText().toString();
                    account_model account_model = new account_model(id, role, fn, mn, ln, phone, pw, address);
                    boolean add = account_dao.add(account_model);
                    //Thêm dữ liệu vào model và push vào sqlite
                    if (add) {
                        startActivity(new Intent(register_activity.this, login_activity.class));
                        Animatoo.INSTANCE.animateSlideRight(register_activity.this);
                    }
                } else {

                }
            }
        });
        findViewById(R.id.tv_back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register_activity.this, login_activity.class).setFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)));
                Animatoo.INSTANCE.animateSlideRight(register_activity.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(register_activity.this);
    }

    private void findID() {
        ed_username = findViewById(R.id.ed_username);
        ed_first_name = findViewById(R.id.ed_first_name);
        ed_middle_name = findViewById(R.id.ed_middle_name);
        ed_last_name = findViewById(R.id.ed_last_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_password = findViewById(R.id.ed_password);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        ed_address = findViewById(R.id.ed_address);
        btnRegister = findViewById(R.id.btnRegister);
    }

//    private boolean isValidate(String user, String fn, String mn, String ln, String pw, String cpw) {
//        if (pw.length() <= 5 || cpw.equals(pw) || user.length() <= 5 || !fn.matches("[a-zA-Z\\s]+") || !mn.matches("[a-zA-Z\\s]+") || !ln.matches("[a-zA-Z\\s]+")) {
//            if (user.length() <= 5) {
//                ed_username.setError("Your username is too short!");
//            } else {
//                ed_username.setError(null);
//            }
//            if (pw.length() <= 5) {
//                ed_password.setError("Your password is too short!");
//            } else {
//                ed_password.setError(null);
//            }
//            if (!cpw.equals(pw)) {
//                ed_confirm_password.setError("Your password does not match!");
//            } else {
//                ed_confirm_password.setError(null);
//            }
//            if (!fn.matches("[a-zA-Z\\s]+")) {
//                ed_first_name.setError("Your first name incorrect format!");
//            } else {
//                ed_first_name.setError(null);
//            }
//            if (!mn.matches("[a-zA-Z\\s]+")) {
//                ed_middle_name.setError("Your middle name incorrect format!");
//            } else {
//                ed_middle_name.setError(null);
//            }
//            if (!ln.matches("[a-zA-Z\\s]+")) {
//                ed_last_name.setError("Your last name incorrect format!");
//            } else {
//                ed_last_name.setError(null);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }

    private boolean isEmpty(String x) {
        if (!x.isEmpty()) {
            return true;
        }
        return false;
    }
}