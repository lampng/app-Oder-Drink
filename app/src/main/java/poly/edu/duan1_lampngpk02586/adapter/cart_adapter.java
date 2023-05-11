package poly.edu.duan1_lampngpk02586.adapter;

import android.annotation.SuppressLint;
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

import java.text.NumberFormat;
import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.customizable.MinusPlusClickListener;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.cart_model;

public class cart_adapter extends RecyclerView.Adapter<cart_adapter.ViewHolder> {

    private ArrayList<cart_model> list;
    private MinusPlusClickListener clickListener;
    private Context context;

    public cart_adapter(Context context, ArrayList<cart_model> list, MinusPlusClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        return new cart_adapter.ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        cart_model location = list.get(position);
        drinks_dao drinks_dao = new drinks_dao(context);

        //Mã nước
        int id_drink = location.getDrink_id();
        //Mã người dùng
        String username = location.getAc_id_cart();
        //Hình nước
        byte[] imageDrink = drinks_dao.getImage(id_drink);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageDrink, 0, imageDrink.length);
        //Tên nước
        String nameDrink = drinks_dao.getName(id_drink);
        //Giá nước
        Double priceDrink = drinks_dao.getPrice(id_drink);
        //Total price
        double total_price = (Double) (location.getQuantity() * priceDrink);
        //Số lượng trong cart
        int quantity = location.getQuantity();
        displayQuantity(holder, quantity);
        //Set Text for TextView
        holder.tv_name.setText(nameDrink);
        holder.tv_price.setText(formatter.format(priceDrink));
        holder.tv_total_price.setText(formatter.format(total_price));
        holder.img_drink.setImageBitmap(bmp);
    }

    @SuppressLint("SetTextI18n")
    private void displayQuantity(ViewHolder holder, int quantity) {
        holder.tv_quantity.setText(quantity + "");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_minus;
        Button btn_plus;
        ImageView img_drink;
        TextView tv_name, tv_quantity, tv_price, tv_total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_drink = itemView.findViewById(R.id.img_drink);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            btn_minus = (Button) itemView.findViewById(R.id.btn_minus);
            btn_plus = (Button) itemView.findViewById(R.id.btn_plus);

            btn_minus.setOnClickListener(view -> clickListener.onMinusClick(getAdapterPosition(), btn_minus));
            btn_plus.setOnClickListener(view -> clickListener.onPlusClick(getAdapterPosition(), btn_plus));

        }
    }
}
