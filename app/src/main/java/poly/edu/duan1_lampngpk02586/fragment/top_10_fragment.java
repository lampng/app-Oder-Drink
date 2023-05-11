package poly.edu.duan1_lampngpk02586.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.adapter.top_10_drink_adapter;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class top_10_fragment extends Fragment {
    RecyclerView rv_top_10;
    drinks_dao drinks_dao;
    ArrayList<drinks_model> list;
    top_10_drink_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_10, container, false);

        findID(view);
        loadTop10();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    void loadTop10() {
        drinks_dao = new drinks_dao(getContext());
        list = drinks_dao.getTop10();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_top_10.setLayoutManager(linearLayoutManager);
        adapter = new top_10_drink_adapter(getContext(), list);
        rv_top_10.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void findID(View view) {
        rv_top_10 = view.findViewById(R.id.rv_top_10);
    }
}