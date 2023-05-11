package poly.edu.duan1_lampngpk02586.fragment;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.activity.main_activity;
import poly.edu.duan1_lampngpk02586.adapter.cart_adapter;
import poly.edu.duan1_lampngpk02586.customizable.IOnBackPressed;
import poly.edu.duan1_lampngpk02586.customizable.MinusPlusClickListener;
import poly.edu.duan1_lampngpk02586.dao.cart_dao;
import poly.edu.duan1_lampngpk02586.dao.order_dao;
import poly.edu.duan1_lampngpk02586.model.cart_model;
import poly.edu.duan1_lampngpk02586.model.order_model;

public class cart_fragment extends Fragment implements MinusPlusClickListener, IOnBackPressed {
    RecyclerView rv_cart;
    TextView tv_item_total, tv_delivery_services, tv_total_price, tv_tax;
    EditText ed_address;
    Button btn_confirm;
    cart_dao cart_dao;
    ArrayList<cart_model> list;
    cart_adapter cart_adapter;
    LinearLayout linear_check;
    Double total_price = 0.0;
    int order_id = 0;

    TextView title;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        findID(view);
        sharedPreferences = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
        loadCart();
        loadTotal();
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customer_ID = sharedPreferences.getString("username", "");

                double price_total = total_price;
                Log.i(TAG, "onClick: Total Price: " + price_total);

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date_order = simpleDateFormat.format(date);

                order_model order_model = new order_model();
                order_dao order_dao = new order_dao(getActivity());
                //Create order
                order_model.setCustomer_ID(customer_ID);
                order_model.setPrice_total(price_total);
                order_model.setDate_order(date_order);
                boolean isCreate = order_dao.create(order_model);
                if (isCreate) {
                    order_dao order_daoo = new order_dao(getActivity());
                    order_id = order_daoo.getIdOrder(customer_ID, 0);
                    //Get order id
                    //Add order id to cart
                    cart_dao cart_dao = new cart_dao(getActivity());
                    boolean isUpdateCart = cart_dao.setOrderID(customer_ID, order_id, 0);
                    if (isUpdateCart) {
                        Fragment fragment1 = null;
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragment1 = new invoice__fragment();
                        fragment1.setEnterTransition(new Slide(Gravity.END));
                        fragment1.setExitTransition(new Slide(Gravity.START));
                        Animatoo.INSTANCE.animateSlideLeft(getActivity());
                        fragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, fragment1)
                                .commit();
                        ((main_activity) getActivity()).setActionBarTitle("Order history");
                    }
                }

            }
        });
        return view;
    }

    public void loadTotal() {
        //load total price
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String gTusername = sharedPreferences.getString("username", "");
        double totalItem = cart_dao.getTotalPrice(gTusername);
        tv_item_total.setText(formatter.format(totalItem));

        double percent_tax = 0.05;
        double tax = Math.round(totalItem * percent_tax);
        tv_tax.setText(formatter.format(tax));

        double delivery = 0;
        if (totalItem > 20) {
            delivery = 3;
        }
        tv_delivery_services.setText(formatter.format(delivery));

        double total = 0;
        total = Math.round(totalItem + tax + delivery);
        tv_total_price.setText(formatter.format(total));
        total_price = total;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCart() {
        cart_dao = new cart_dao(getContext());
        String gTusername = sharedPreferences.getString("username", "");
        Log.i(TAG, "loadCart: Username: " + gTusername);

        //Check cart
        int check = cart_dao.checkCart(gTusername);
        switch (check) {
            case -1:
                rv_cart.setVisibility(View.VISIBLE);
                linear_check.setVisibility(View.VISIBLE);

                list = cart_dao.getList(gTusername);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rv_cart.setLayoutManager(linearLayoutManager);
                cart_adapter = new cart_adapter(getContext(), list, this);
                rv_cart.setAdapter(cart_adapter);
                cart_adapter.notifyDataSetChanged();

                break;
            case 1:
                rv_cart.setVisibility(View.GONE);
                linear_check.setVisibility(View.GONE);

                break;
            default:
                break;
        }


    }

    private void findID(View view) {
        rv_cart = view.findViewById(R.id.rv_cart);
        tv_item_total = view.findViewById(R.id.tv_item_total);
        tv_delivery_services = view.findViewById(R.id.tv_delivery_services);
        tv_total_price = view.findViewById(R.id.tv_total_price);
        tv_tax = view.findViewById(R.id.tv_tax);
        btn_confirm = view.findViewById(R.id.btn_confirm);

        linear_check = view.findViewById(R.id.linear_check);
    }

    @Override
    public void onMinusClick(int position, Button btn_minus) {
        cart_model location = list.get(position);

        //Mã nước
        int id_drink = location.getDrink_id();

        //Mã người dùng
        String username = location.getAc_id_cart();

        //Button minus
        if (location.getQuantity() == 1) {
            loadTotal();
            list.remove(location);
            cart_dao.redelete(username, id_drink);
        } else {
            location.setQuantity(location.getQuantity() - 1);
        }
        int quantityy = location.getQuantity();
        cart_dao.reload(username, id_drink, quantityy);
        loadTotal();
        loadCart();
    }

    @Override
    public void onPlusClick(int position, Button btn_plus) {
        cart_model location = list.get(position);
        //Mã nước
        int id_drink = location.getDrink_id();
        //Mã người dùng
        String username = location.getAc_id_cart();

        //Button plus
        location.setQuantity(location.getQuantity() + 1);
        int quantityy = location.getQuantity();
        cart_dao.reload(username, id_drink, quantityy);
        loadTotal();
        loadCart();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}