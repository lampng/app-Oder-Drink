package poly.edu.duan1_lampngpk02586.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.activity.main_activity;
import poly.edu.duan1_lampngpk02586.adapter.cat_adapter;
import poly.edu.duan1_lampngpk02586.adapter.drinks_adapter;
import poly.edu.duan1_lampngpk02586.customizable.ItemCatClickListener;
import poly.edu.duan1_lampngpk02586.customizable.ItemDrinksClickListener;
import poly.edu.duan1_lampngpk02586.dao.cart_dao;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.cart_model;
import poly.edu.duan1_lampngpk02586.model.cat_model;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class home_fragment extends Fragment implements ItemDrinksClickListener, ItemCatClickListener {
    int quantity;
    EditText ed_search;
    ImageSlider image_slider;
    private RecyclerView rv_cat, rv_drinks;

    private cat_dao cat_dao;
    private ArrayList<cat_model> cat_list;
    private cat_adapter cat_adapter;

    private drinks_dao drinks_dao;
    private ArrayList<drinks_model> drinks_list;
    private drinks_adapter drinks_adapter;
    private SharedPreferences sharedPreferences;

    @SuppressLint("WrongThread")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findID(view);
        loadCat();
        loadDrinks();
        //Slider image
        ArrayList<SlideModel> imageList = new ArrayList<SlideModel>();
        imageList.add(new SlideModel(R.drawable.slider_1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.slider_2, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.slider_3, ScaleTypes.CENTER_CROP));
        image_slider.setImageList(imageList, ScaleTypes.CENTER_CROP);

        ed_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String search = ed_search.getText().toString();

                    Bundle bundle = new Bundle();
                    bundle.putString("search", search);

                    Fragment fragment1 = null;
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragment1 = new products_fragment();
                    fragment1.setArguments(bundle);
                    Animatoo.INSTANCE.animateSlideLeft(getActivity());
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, fragment1)
                            .commit();
                    ((main_activity) getActivity()).setActionBarTitle("Products");
                }
                return false;
            }
        });
        return view;
    }

    private void findID(View view) {
        rv_cat = view.findViewById(R.id.rv_cat);
        rv_drinks = view.findViewById(R.id.rv_drinks);
        image_slider = view.findViewById(R.id.image_slider);
        ed_search = view.findViewById(R.id.ed_search);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCat() {
        cat_dao = new cat_dao(getContext());
        cat_list = cat_dao.getList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_cat.setLayoutManager(linearLayoutManager);
        cat_adapter = new cat_adapter(getContext(), cat_list, this);
        rv_cat.setAdapter(cat_adapter);
        cat_adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDrinks() {
        drinks_dao = new drinks_dao(getContext());
        drinks_list = drinks_dao.getList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_drinks.setLayoutManager(linearLayoutManager);
        drinks_adapter = new drinks_adapter(getContext(), drinks_list);
        rv_drinks.setAdapter(drinks_adapter);
        drinks_adapter.notifyDataSetChanged();
        drinks_adapter.setClickListener(this);
    }

    @Override
    public void onClick_Cat(View view, int position) {
        final cat_model Cat_locate = cat_list.get(position);
        String name = Cat_locate.getName_cat();

        Bundle bundle = new Bundle();
        bundle.putString("search", name);

        Fragment fragment1 = null;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragment1 = new products_fragment();
        fragment1.setArguments(bundle);
        fragment1.setEnterTransition(new Slide(Gravity.END));
        fragment1.setExitTransition(new Slide(Gravity.START));
        Animatoo.INSTANCE.animateSlideLeft(getActivity());
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment1)
                .commit();
        ((main_activity) getActivity()).setActionBarTitle("Products");
    }

    @Override
    public void onClick_drinks(View view, int position) {
        drinks_model Drink_locate = drinks_list.get(position);

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
        tv_price.setText((Double) price + "$");
        tv_des.setText(des);
        tv_total_price.setText("Total price: " + (Double) price * quantity + "$");
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (!(quantity == 0)) {
                    quantity--;
                    displayQuantity(tv_amount, quantity);
                }
                tv_total_price.setText("Total price: " + (Double) price * quantity + "$");
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                quantity++;
                displayQuantity(tv_amount, quantity);
                tv_total_price.setText("Total price: " + price * quantity + "$");
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

    private void displayQuantity(TextView tv_amount, int x) {
        tv_amount.setText(String.valueOf(x));
    }
}