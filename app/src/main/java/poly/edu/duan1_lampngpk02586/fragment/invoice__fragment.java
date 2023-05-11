package poly.edu.duan1_lampngpk02586.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.adapter.cart_order_adapter;
import poly.edu.duan1_lampngpk02586.adapter.invoice_adapter;
import poly.edu.duan1_lampngpk02586.customizable.invoiceClickListener;
import poly.edu.duan1_lampngpk02586.dao.cart_dao;
import poly.edu.duan1_lampngpk02586.dao.order_dao;
import poly.edu.duan1_lampngpk02586.model.cart_model;
import poly.edu.duan1_lampngpk02586.model.order_model;

public class invoice__fragment extends Fragment implements invoiceClickListener {
    RecyclerView rv_invoice;

    order_dao order_dao;
    ArrayList<order_model> list;
    invoice_adapter adapter;

    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        findID(view);
        sharedPreferences = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
        loadInvoice();
        return view;
    }

    private void findID(View view) {
        rv_invoice = view.findViewById(R.id.rv_invoice);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadInvoice() {
        String customer_ID = sharedPreferences.getString("username", "");
        String getRole = sharedPreferences.getString("role", "");
        int role = Integer.parseInt(getRole);
        if (role == 3) {
            order_dao = new order_dao(getContext());
            list = order_dao.getListCustomer(customer_ID);

        } else {
            order_dao = new order_dao(getContext());
            list = order_dao.getListStaff();
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_invoice.setLayoutManager(linearLayoutManager);
        adapter = new invoice_adapter(getContext(), list, this);
        rv_invoice.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onConfirmClick(int position, Button btn_confirm) {
        order_model location = list.get(position);
        String getRole = sharedPreferences.getString("role", "");
        String customer_ID = sharedPreferences.getString("username", "");
        int role = Integer.parseInt(getRole);
        if (role == 3) {
            cart_dao cart_dao = new cart_dao(getContext());
            order_dao order_dao = new order_dao(getContext());

            cart_dao.deleteByOrderID(location.getId_order());
            order_dao.deleteByOrderID(location.getId_order());
            loadInvoice();
        }
        order_dao order_dao = new order_dao(getContext());
        order_dao.setStatus(customer_ID, location.getId_order());
        loadInvoice();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemView(int position, RecyclerView rv_item_cart) {
        order_model location = list.get(position);

        ArrayList<cart_model> list_cart;
        cart_order_adapter cart_order_adapter;

        int id_order = location.getId_order();

        cart_dao cart_dao = new cart_dao(getContext());
        list_cart = cart_dao.getListCartOrder(id_order);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_item_cart.setLayoutManager(linearLayoutManager);
        cart_order_adapter = new cart_order_adapter(getContext(), list_cart);
        rv_item_cart.setAdapter(cart_order_adapter);
        cart_order_adapter.notifyDataSetChanged();

        int visibility = rv_item_cart.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
        rv_item_cart.setVisibility(visibility);
    }
}