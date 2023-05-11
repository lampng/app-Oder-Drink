package poly.edu.duan1_lampngpk02586.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.activity.main_activity;
import poly.edu.duan1_lampngpk02586.adapter.cat_adapter;
import poly.edu.duan1_lampngpk02586.adapter.products_adapter;
import poly.edu.duan1_lampngpk02586.customizable.ItemProductClickListener;
import poly.edu.duan1_lampngpk02586.dao.cart_dao;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.cart_model;
import poly.edu.duan1_lampngpk02586.model.cat_model;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class products_fragment extends Fragment implements ItemProductClickListener {

    EditText ed_search;
    RecyclerView rv_products;

    drinks_dao drinks_dao;
    ArrayList<drinks_model> list;
    products_adapter products_adapter;
    int quantity;
    FloatingActionButton floatingActionButton;
    private cat_dao cat_dao;
    private ArrayList<cat_model> cat_list;
    private cat_adapter cat_adapter;
    private SharedPreferences sharedPreferences;
    @SuppressWarnings("FieldMayBeFinal")
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String ed = ed_search.getText().toString();
            loadProdcuts(ed);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        findID(view);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment1 = null;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragment1 = new cart_fragment();
                fragment1.setEnterTransition(new Slide(Gravity.END));
                fragment1.setExitTransition(new Slide(Gravity.START));
                Animatoo.INSTANCE.animateSlideLeft(getActivity());
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, fragment1)
                        .commit();
                ((main_activity) getActivity()).setActionBarTitle("Your cart");
            }
        });
        Bundle bundle = getArguments();
        String search = "";
        try {
            search = bundle.getString("search", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ed_search.setText(search);
        ed_search.addTextChangedListener(textWatcher);
        loadProdcuts(ed_search.getText().toString());
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadProdcuts(String text) {
        drinks_dao = new drinks_dao(getContext());
        list = drinks_dao.search(text);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_products.setLayoutManager(linearLayoutManager);
        products_adapter = new products_adapter(getContext(), list, this);
        rv_products.setAdapter(products_adapter);
        products_adapter.notifyDataSetChanged();
    }

    private void findID(View view) {
        ed_search = view.findViewById(R.id.ed_search);
        rv_products = view.findViewById(R.id.rv_products);
        floatingActionButton = view.findViewById(R.id.btn_add);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick_add(View view, int position) {
        drinks_model Drink_locate = list.get(position);

        int id = Drink_locate.getId_drinks();

        Double price = Drink_locate.getPrice_drinks();
        String des = Drink_locate.getDes_drinks();
        String name = Drink_locate.getName_drinks();

        Bitmap image = BitmapFactory.decodeByteArray(Drink_locate.getImage_drinks(), 0, Drink_locate.getImage_drinks().length);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_to_cart, null);
        builder.setView(v);
        ImageView img_drink = v.findViewById(R.id.img_drink);
        TextView tv_Name = v.findViewById(R.id.tv_Name);
        TextView tv_price = v.findViewById(R.id.tv_price);
        TextView tv_des = v.findViewById(R.id.tv_des);
        TextView tv_total_price = v.findViewById(R.id.tv_total_price);
        TextView tv_amount = v.findViewById(R.id.tv_amount);
        Button btn_minus = v.findViewById(R.id.btn_minus);
        Button btn_plus = v.findViewById(R.id.btn_plus);
        Button btn_add_to_cart = v.findViewById(R.id.btn_add_to_cart);


        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        //Khi nước đã có trong giỏ hàng, thì sẽ lấy số lượng hiển thì trong dialog, ngược lại = 1
        cart_dao cart_dao = new cart_dao(getContext());
        sharedPreferences = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
        String gTusername = sharedPreferences.getString("username", "");

        int x = cart_dao.getQuantity(id, gTusername);
        if (x != 0) {
            quantity = x;
        } else {
            quantity = 1;
        }
        displayQuantity(tv_amount, quantity);

        img_drink.setImageBitmap(Bitmap.createScaledBitmap(image, 100, 100, false));
        tv_Name.setText(name);
        tv_price.setText("$" + (Double) price);
        tv_des.setText(des);
        tv_total_price.setText("Total price: $" + (Double) price * quantity);
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (!(quantity == 0)) {
                    quantity--;
                    displayQuantity(tv_amount, quantity);
                }
                tv_total_price.setText("Total price: $" + (Double) price * quantity);
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                quantity++;
                displayQuantity(tv_amount, quantity);
                tv_total_price.setText("Total price: $" + price * quantity);
            }
        });
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
                String gTusername = sharedPreferences.getString("username", "");


                boolean findCart = cart_dao.findCart(gTusername, id);
                if (findCart) {
                    cart_model cart_model = new cart_model();
                    cart_dao cart_dao = new cart_dao(getContext());
                    cart_model.setAc_id_cart(gTusername);
                    cart_model.setDrink_id(id);
                    cart_model.setQuantity(quantity);
                    cart_model.setOrder_ID(0);
                    boolean isCreateCart = cart_dao.create(cart_model);
                    if (isCreateCart) {
                        FancyToast.makeText(getContext(), "Add to cart success", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        Log.i("TAG", "onClick: create");
                        dialog.cancel();
                    } else {
                        FancyToast.makeText(getContext(), "Add to cart Fail", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } else {
                    cart_dao cart_d = new cart_dao(getContext());
                    cart_model list = new cart_model();
                    list.setAc_id_cart(gTusername);
                    list.setDrink_id(id);
                    list.setOrder_ID(0);
                    list.setQuantity(quantity);
                    boolean update = cart_d.update(list);
                    if (update) {
                        Log.i("TAG", "onClick: update");
                        FancyToast.makeText(getContext(), "Update success", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        dialog.cancel();
                    }
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick_more(View view, int position) {
        drinks_model Drink_locate = list.get(position);

        int id = Drink_locate.getId_drinks();

        Double price = Drink_locate.getPrice_drinks();
        String des = Drink_locate.getDes_drinks();
        String name = Drink_locate.getName_drinks();

        Bitmap image = BitmapFactory.decodeByteArray(Drink_locate.getImage_drinks(), 0, Drink_locate.getImage_drinks().length);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_to_cart, null);
        builder.setView(v);
        ImageView img_drink = v.findViewById(R.id.img_drink);
        TextView tv_Name = v.findViewById(R.id.tv_Name);
        TextView tv_price = v.findViewById(R.id.tv_price);
        TextView tv_des = v.findViewById(R.id.tv_des);
        TextView tv_total_price = v.findViewById(R.id.tv_total_price);
        TextView tv_amount = v.findViewById(R.id.tv_amount);
        Button btn_minus = v.findViewById(R.id.btn_minus);
        Button btn_plus = v.findViewById(R.id.btn_plus);
        Button btn_add_to_cart = v.findViewById(R.id.btn_add_to_cart);


        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        //Khi nước đã có trong giỏ hàng, thì sẽ lấy số lượng hiển thì trong dialog, ngược lại = 1
        cart_dao cart_dao = new cart_dao(getContext());
        sharedPreferences = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
        String gTusername = sharedPreferences.getString("username", "");

        int x = cart_dao.getQuantity(id, gTusername);
        if (x != 0) {
            quantity = x;
        } else {
            quantity = 1;
        }
        displayQuantity(tv_amount, quantity);

        img_drink.setImageBitmap(Bitmap.createScaledBitmap(image, 100, 100, false));
        tv_Name.setText(name);
        tv_price.setText("$" +(Double) price);
        tv_des.setText(des);
        tv_total_price.setText("Total price: $" + (Double) price * quantity);
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (!(quantity == 0)) {
                    quantity--;
                    displayQuantity(tv_amount, quantity);
                }
                tv_total_price.setText("Total price: $" + (Double) price * quantity);
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                quantity++;
                displayQuantity(tv_amount, quantity);
                tv_total_price.setText("Total price: $" + price * quantity);
            }
        });
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
                String gTusername = sharedPreferences.getString("username", "");


                boolean findCart = cart_dao.findCart(gTusername, id);
                if (findCart) {
                    cart_model cart_model = new cart_model();
                    cart_dao cart_dao = new cart_dao(getContext());
                    cart_model.setAc_id_cart(gTusername);
                    cart_model.setDrink_id(id);
                    cart_model.setQuantity(quantity);
                    cart_model.setOrder_ID(0);
                    boolean isCreateCart = cart_dao.create(cart_model);
                    if (isCreateCart) {
                        Toast.makeText(getContext(), "Add to your cart success", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "onClick: create");
                        dialog.cancel();
                    } else {
                    }
                } else {
                    cart_dao cart_d = new cart_dao(getContext());
                    cart_model list = new cart_model();
                    list.setAc_id_cart(gTusername);
                    list.setDrink_id(id);
                    list.setOrder_ID(0);
                    list.setQuantity(quantity);
                    boolean update = cart_d.update(list);
                    if (update) {
                        Log.i("TAG", "onClick: update");
                        Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
            }
        });
    }

    private void displayQuantity(TextView tv_amount, int x) {
        tv_amount.setText(String.valueOf(x));
    }
}