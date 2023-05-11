package poly.edu.duan1_lampngpk02586.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.customizable.IOnBackPressed;
import poly.edu.duan1_lampngpk02586.dao.account_dao;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.dao.order_dao;
import poly.edu.duan1_lampngpk02586.fragment.cart_fragment;
import poly.edu.duan1_lampngpk02586.fragment.home_fragment;
import poly.edu.duan1_lampngpk02586.fragment.invoice__fragment;
import poly.edu.duan1_lampngpk02586.fragment.manager_cat_fragment;
import poly.edu.duan1_lampngpk02586.fragment.manager_drinks_fragment;
import poly.edu.duan1_lampngpk02586.fragment.manager_staff_fragment;
import poly.edu.duan1_lampngpk02586.fragment.products_fragment;
import poly.edu.duan1_lampngpk02586.fragment.settings_fragment;
import poly.edu.duan1_lampngpk02586.fragment.top_10_fragment;
import poly.edu.duan1_lampngpk02586.model.cat_model;

public class main_activity extends AppCompatActivity {
    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView title;
    LinearLayout contentView;
    SharedPreferences sharedPreferences;
    Fragment fragment = null;
    account_dao account_dao;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("login_info", MODE_PRIVATE);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        animationDrawer();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        contentView = findViewById(R.id.contentView);
        cat_dao cat_dao = new cat_dao(this);
        ArrayList<cat_model> list = cat_dao.getList();

        title = findViewById(R.id.title);
        View hdlu = navigationView.getHeaderView(0);
        TextView welcome = hdlu.findViewById(R.id.name);
        TextView username = hdlu.findViewById(R.id.username);
        String firstName = sharedPreferences.getString("firstName", "");
        String LastName = sharedPreferences.getString("LastName", "");
        String gTusername = sharedPreferences.getString("username", "");
        welcome.setText("Welcome, " + firstName + " " + LastName);
        username.setText(gTusername);
        int role = 0;
        role = Integer.parseInt(sharedPreferences.getString("role", ""));
        if (role == 3) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_manager).setVisible(false);
            menu.findItem(R.id.nav_Statistic).setVisible(false);
        }
        if (role == 2) {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_manager).setVisible(false);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        //set Defaul fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new home_fragment();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id) {
                    case R.id.nav_home:
                        Log.i("fragment", "Home");
                        fragment = new home_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle(R.string.logo_title);
                        title.setText(R.string.logo_title);
                        break;
                    case R.id.nav_cart:
                        Log.i("fragment", "My cart");
                        fragment = new cart_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Cart");
                        title.setText("MY CART");
                        break;
                    case R.id.nav_order:
                        Log.i("fragment", "Order history");
                        fragment = new invoice__fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Cart");
                        title.setText("Order history");
                        break;
                    case R.id.nav_cat_manager:
                        Log.i("fragment", "Cat manager");
                        fragment = new manager_cat_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Cat manager");
                        title.setText("CAT MANAGER");
                        break;
                    case R.id.nav_drinks_manager:
                        Log.i("fragment", "Drink manager");
                        fragment = new manager_drinks_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Drinks manager");
                        title.setText("DRINKS MANAGER");
                        break;
                    case R.id.nav_staff_manager:
                        Log.i("fragment", "Staff manager");
                        fragment = new manager_staff_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
                        title.setText("STAFF MANAGER");
                        break;
                    case R.id.nav_product:
                        Log.i("fragment", "Product manager");
                        fragment = new products_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Products manager");
                        title.setText("PRODUCTS MANAGER");
                        break;
                    case R.id.nav_revenue_statistics:
                        Log.i("fragment", "nav_revenue_statistics");
                        showDialog_revenue();
                        break;
                    case R.id.nav_revenue_top_10:
                        Log.i("fragment", "Top 10 Seller");
                        fragment = new top_10_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Products manager");
                        title.setText("Top 10 Seller");
                        break;
                    case R.id.settings:
                        Log.i("fragment", "Top 10 Seller");
                        fragment = new settings_fragment();
                        Animatoo.INSTANCE.animateSlideLeft(main_activity.this);
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Products manager");
                        title.setText("Profile User");
                        break;
                    case R.id.nav_logout:
                        Intent intent = new Intent(main_activity.this, login_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case R.id.changepass:
                        onClickSetPassword();
                        break;
                    default:
                        fragment = new home_fragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment)
                                .commit();
//                        toolbar.setTitle("Home");
                        title.setText(R.string.logo_title);
                        return true;
                }
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    private void showDialog_revenue() {
        //Calender
        Calendar calendar = Calendar.getInstance();

        //========
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_revenue, null);

        TextInputEditText start_revenue = view.findViewById(R.id.start_revenue);
        TextInputEditText end_revenue = view.findViewById(R.id.end_revenue);
        Button btn_find_revenue = view.findViewById(R.id.btn_find_revenue);
        TextView txt_result_revenue = view.findViewById(R.id.txt_result_revenue);
        start_revenue.setFocusable(false);
        end_revenue.setFocusable(false);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        start_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        main_activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                String ngay = "";
                                String thang = "";
                                if (i2 < 10) {
                                    ngay = String.valueOf(i2);
                                } else {
                                    ngay = String.valueOf(i2);
                                }
                                if (i1 < 10) {
                                    thang = String.valueOf((i1 + 1));
                                } else {
                                    thang = String.valueOf((i1 + 1));
                                }
                                start_revenue.setText(ngay + "/" + thang + "/" + i);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
        end_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        main_activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                String ngay = "";
                                String thang = "";
                                if (i2 < 10) {
                                    ngay = String.valueOf(i2);
                                } else {
                                    ngay = String.valueOf(i2);
                                }
                                if ((i1 + 1) < 10) {
                                    thang = String.valueOf((i1 + 1));
                                } else {
                                    thang = String.valueOf((i1 + 1));
                                }
                                end_revenue.setText(ngay + "/" + thang + "/" + i);
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
        btn_find_revenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order_dao order_dao = new order_dao(main_activity.this);
                String start = start_revenue.getText().toString();
                String end = end_revenue.getText().toString();
                int revenue = order_dao.getRevenue(start, end);
                txt_result_revenue.setText("Revenue: " + revenue + "$");
            }
        });
    }

    public void setActionBarTitle(String titler) {
        title.setText(titler);
    }

    private void animationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaleOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaleOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float XOffset = drawerLayout.getWidth() * slideOffset;
                final float XOffsetDiff = contentView.getWidth() * diffScaleOffset / 2;
                final float Xtranslation = XOffset - XOffsetDiff;
                contentView.setTranslationX(Xtranslation);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickSetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = ((Activity) this).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_newpass, null);
        builder.setView(v);
        EditText ed_pass = v.findViewById(R.id.ed_pass);
        Button btn_submit = v.findViewById(R.id.btn_submit);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_dao = new account_dao(getBaseContext());
                String newpass = ed_pass.getText().toString();
                String username = sharedPreferences.getString("username", "");
                if (newpass.isEmpty()) {
                    ed_pass.setError("PLease fill new password");
                } else if (newpass.trim().length() <= 5) {
                    ed_pass.setError("Your password is too short");
                } else {
                    account_dao.reset_password(username, newpass);
                    dialog.cancel();
                }
            }
        });
    }
}