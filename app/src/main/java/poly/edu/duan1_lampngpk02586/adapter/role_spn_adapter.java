package poly.edu.duan1_lampngpk02586.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import poly.edu.duan1_lampngpk02586.model.role_model;

public class role_spn_adapter extends BaseAdapter {
    private Context context;
    private List<role_model> categories;

    public role_spn_adapter(Context context, List<role_model> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        if (i > 0 && i <= categories.size()) {
            return categories.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            // view = LayoutInflater.from(context).inflate(R.layout.layout_nhanvien_item, null);
            view = LayoutInflater.from(context).inflate(com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, null);
        }
        //anh xa
        TextView ten = view.findViewById(android.R.id.text1);
        role_model cat = categories.get(i);
        if (cat != null) {
            ten.setText(cat.getName_role());
            Log.i("TEST", cat.toString());
            return view;
        }
        return null;
    }
}