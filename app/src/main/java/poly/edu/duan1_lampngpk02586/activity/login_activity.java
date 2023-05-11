package poly.edu.duan1_lampngpk02586.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

import poly.edu.duan1_lampngpk02586.DBHelper;
import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.dao.account_dao;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;

public class login_activity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText ed_username, ed_password;
    Button btn_login;
    String PREFS = "Save_login";
    SharedPreferences Save_login;
    CheckBox checkBox;
    LottieAnimationView lottie;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String user = ed_username.getText().toString().trim();
            String pass = ed_password.getText().toString().trim();
            if (!user.isEmpty() && !pass.isEmpty()) {
                btn_login.setEnabled(true);
                btn_login.setBackground(getResources().getDrawable(R.drawable.button_submit));
                Log.i("TAG", "onTextChanged: pink orange");
            } else {
                btn_login.setEnabled(false);
                btn_login.setBackground(getResources().getDrawable(R.drawable.button_disable));
                Log.i("TAG", "onTextChanged: gray");
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findID();

        //
        try {
            cat_dao cat_dao = new cat_dao(this);
            byte[] i1 = getByte(R.drawable.caphesuanong);
            cat_dao.tempt(1, "Coffee", i1);

//
            drinks_dao drinks_dao = new drinks_dao(this);
            drinks_dao.tempt(1, 1, "Cà Phê Sữa Nóng", i1, "", 35.0);

            byte[] i2 = getByte(R.drawable.the_coffee_house_sua_da);
            drinks_dao.tempt(2, 1, "The Coffee House Sữa Đá", i2, "Cà phê được pha phin truyền thống kết hợp với sữa đặc tạo nên hương vị đậm đà", 35.0);

            byte[] i3 = getByte(R.drawable.bacxiu);
            drinks_dao.tempt(3, 1, "Bạc Sỉu", i3, "Bạc sỉu chính là Ly sữa trắng kèm một chút cà phê.", 29.0);

            byte[] i4 = getByte(R.drawable.bacxiunong);
            drinks_dao.tempt(4, 1, "Bạc Sỉu Nóng", i4, "Bạc sỉu chính là Ly sữa trắng kèm một chút cà phê.", 35.0);

            byte[] i5 = getByte(R.drawable.caphedenda);
            drinks_dao.tempt(5, 1, "Cà Phê Đen Đá", i5, "Bạc sỉu chính là Ly sữa trắng kèm một chút cà phê.", 29.0);

            byte[] i6 = getByte(R.drawable.caphesuanong);
            drinks_dao.tempt(6, 1, "Cà Phê Đen Nóng", i6, "Không ngọt ngào như Bạc sỉu hay Cà phê sữa", 35.0);

            byte[] c = getByte(R.drawable.cloudteaolongnuongkemdiadaxay);
            cat_dao.tempt(2, "Cloud Tea", c);

            drinks_dao.tempt(7, 2, "CloudTea Oolong Nướng Kem Dừa Đá Xay", c, "Trà sữa đá xay - phiên bản nâng cấp đầy mới lạ của trà sữa truyền thống", 55.0);

            byte[] c2 = getByte(R.drawable.cloudteaolongnuongcaramel);
            drinks_dao.tempt(8, 2, "CloudTea Oolong Nướng Caramel", c2, "Chiếc trà sữa chân ái dành cho tín đồ hảo ngọt gọi tên CloudTea Oolong Nướng Caramel.", 55.0);

            byte[] c3 = getByte(R.drawable.cloudteaolongnuongkemcheese);
            drinks_dao.tempt(9, 2, "CloudTea Oolong Nướng Kem Cheese", c3, "Hội mê cheese sao có thể bỏ lỡ chiếc trà sữa siêu mlem này.", 55.0);

            byte[] c4 = getByte(R.drawable.cloudteasmorechocomarshmallow);
            drinks_dao.tempt(10, 2, "CloudTea S’more Choco Marshmallow", c4, "Thức uống đậm chất mùa lễ hội dành cho ai mê đắm sô cô la tới rồi đây.", 55.0);

            byte[] c5 = getByte(R.drawable.cloudteaveryberrymochi);
            drinks_dao.tempt(11, 2, "CloudTea Very Berry Mochi", c5, "Thức uống đậm chất mùa lễ hội dành cho ai mê đắm sô cô la tới rồi đây.", 55.0);

            byte[] c6 = getByte(R.drawable.cloudteahongtraarabica);
            drinks_dao.tempt(12, 2, "CloudTea Hồng Trà Arabica", c6, "Thức uống \"chân ái\" dành cho những ai vừa thích trà sữa ngọt ngào", 55.0);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        lottie = findViewById(R.id.lottie);
        lottie.setRepeatCount(LottieDrawable.INFINITE);

        account_dao account_dao = new account_dao(this);
        ed_username.addTextChangedListener(textWatcher);
        ed_password.addTextChangedListener(textWatcher);

        Save_login = getSharedPreferences(PREFS, 0);
        boolean rememberMe = Save_login.getBoolean("rememberMe", false);
        if (rememberMe == true) {
            //get previously stored login details
            String login = Save_login.getString("login", null);
            String upass = Save_login.getString("password", null);

            if (login != null && upass != null) {
                //fill input boxes with stored login and pass
                EditText loginEbx = findViewById(R.id.ed_username);
                EditText passEbx = findViewById(R.id.ed_password);
                loginEbx.setText(login);
                passEbx.setText(upass);

                //set the check box to 'checked'
                CheckBox rememberMeCbx = (CheckBox) findViewById(R.id.checkBox);
                rememberMeCbx.setChecked(true);
            }
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Kiểm tra đăng nhập
                String u = ed_username.getText().toString();
                String p = ed_password.getText().toString();
                //khi điều kiện đúng
                if (account_dao.login(u, p)) {
                    //Lưu mật khẩu
                    CheckBox rememberMeCbx = (CheckBox) findViewById(R.id.checkBox);
                    boolean isChecked = rememberMeCbx.isChecked();
                    if (isChecked) {
                        //Nếu checkbox = true
                        saveLoginDetails();
                        Log.i("", "save login ");
                    } else {
                        //Nếu checkbox = false
                        removeLoginDetails();
                        Log.i("", "remove save login ");
                    }
                    startActivity(new Intent(login_activity.this, main_activity.class).setFlags((Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)));
                    Animatoo.INSTANCE.animateSlideLeft(login_activity.this);
                    //khi điều kiện sai, thông báo Toast
                } else {
                    FancyToast.makeText(login_activity.this, "Infomation don't match!", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }
            }
        });
        //Forgot
        findViewById(R.id.tv_forgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_activity.this, forgot_activity.class));
                Animatoo.INSTANCE.animateSlideLeft(login_activity.this);
            }
        });
        //Register
        findViewById(R.id.tv_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_activity.this, register_activity.class));
                Animatoo.INSTANCE.animateSlideLeft(login_activity.this);
            }
        });
    }

    private void findID() {
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);
        checkBox = findViewById(R.id.checkBox);
    }

    //Remember
    private void saveLoginDetails() {
        EditText loginEbx = findViewById(R.id.ed_username);
        EditText passEbx = findViewById(R.id.ed_password);
        String login = loginEbx.getText().toString();
        String upass = passEbx.getText().toString();
        SharedPreferences.Editor e = Save_login.edit();
        e.putBoolean("rememberMe", true);
        e.putString("login", login);
        e.putString("password", upass);
        e.commit();
    }

    private void removeLoginDetails() {
        SharedPreferences.Editor e = Save_login.edit();
        e.putBoolean("rememberMe", false);
        e.remove("login");
        e.remove("password");
        e.commit();
    }

    private byte[] getByte(int drawable) {
        Resources res = getResources();
        Drawable dra = res.getDrawable(drawable);
        Bitmap bitmap = ((BitmapDrawable) dra).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();
        return bitMapData;
    }
}