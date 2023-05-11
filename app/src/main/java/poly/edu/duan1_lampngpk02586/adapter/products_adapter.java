package poly.edu.duan1_lampngpk02586.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.customizable.ItemProductClickListener;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class products_adapter extends RecyclerView.Adapter<products_adapter.ViewHolder> {
    private ArrayList<drinks_model> list;
    private Context context;
    private ItemProductClickListener clickListener;

    public products_adapter(Context context, ArrayList<drinks_model> list, ItemProductClickListener clickListener) {
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public products_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_products, parent, false);
        return new products_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull products_adapter.ViewHolder holder, int position) {
        drinks_model pst = list.get(holder.getAdapterPosition());

        Bitmap bmp = BitmapFactory.decodeByteArray(pst.getImage_drinks(), 0, pst.getImage_drinks().length);
        holder.img_drinks.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

        holder.tv_name.setText(pst.getName_drinks());

        cat_dao cat_dao = new cat_dao(context);
        String id_cat = String.valueOf(pst.getCat_ID());
        String catName = cat_dao.getNamePosition(Integer.parseInt(id_cat));
        holder.tv_IdCat.setText(catName);

        holder.tv_price.setText(String.format("$%s", pst.getPrice_drinks()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_drinks;
        TextView tv_name, tv_IdCat, tv_price, tv_more;
        Button btn_add_to_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_drinks = itemView.findViewById(R.id.img_drinks);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_IdCat = itemView.findViewById(R.id.tv_IdCat);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_more = itemView.findViewById(R.id.tv_more);
            btn_add_to_cart = itemView.findViewById(R.id.btn_add_to_cart);

            btn_add_to_cart.setOnClickListener(view -> clickListener.onClick_add(itemView, getAdapterPosition()));
            itemView.setOnClickListener(iew -> clickListener.onClick_more(itemView, getAdapterPosition()));

        }
    }
}
