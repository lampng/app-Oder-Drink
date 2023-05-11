package poly.edu.duan1_lampngpk02586.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.model.role_model;

public class Role_Spinner_adapter extends BaseAdapter {
    Context context;
    List<role_model> list;

    public Role_Spinner_adapter(Context context, List<role_model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        if (i > 0 && i <= list.size()) {
            return list.get(i);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_role_spinner, viewGroup, false);
        }
        TextView txtName = view.findViewById(R.id.name);
        role_model model = list.get(i);
        if (model != null) {
            txtName.setText(model.getName_role());
            Log.i(TAG, "getView: " + model.toString());
            return view;
        }
        return null;
    }
}
