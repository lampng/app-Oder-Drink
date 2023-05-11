package poly.edu.duan1_lampngpk02586.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class top_10_drink_adapter extends RecyclerView.Adapter<top_10_drink_adapter.ViewHolder> {
    private Context context;
    private ArrayList<drinks_model> list;

    public top_10_drink_adapter(Context context, ArrayList<drinks_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public top_10_drink_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_top_10, parent, false);
        return new top_10_drink_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull top_10_drink_adapter.ViewHolder holder, int position) {
        drinks_model location = list.get(position);

        Bitmap bmp = BitmapFactory.decodeByteArray(location.getImage_drinks(), 0, location.getImage_drinks().length);
        holder.img_drink.setImageBitmap(bmp);
        holder.tv_name.setText(location.getName_drinks());

        Double price = location.getPrice_drinks();
        holder.tv_price.setText(String.valueOf(price));

        drinks_dao drinks_dao = new drinks_dao(context);
        int sumQuantity = drinks_dao.getSumQuantity(location.getId_drinks());
        holder.tv_quantity.setText(String.valueOf(sumQuantity));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_drink;
        TextView tv_name, tv_price, tv_quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_drink = itemView.findViewById(R.id.img_drink);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}
