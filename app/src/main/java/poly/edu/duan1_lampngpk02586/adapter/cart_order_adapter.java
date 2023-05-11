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

import java.text.NumberFormat;
import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.cart_model;

public class cart_order_adapter extends RecyclerView.Adapter<cart_order_adapter.ViewHolder> {
    private Context context;
    private ArrayList<cart_model> list;

    public cart_order_adapter(Context context, ArrayList<cart_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public cart_order_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_cart_order, parent, false);
        return new cart_order_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cart_order_adapter.ViewHolder holder, int position) {
        cart_model location = list.get(position);
        drinks_dao drinks_dao = new drinks_dao(context);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String name = drinks_dao.getName(location.getDrink_id());
        byte[] bmp = drinks_dao.getImage(location.getDrink_id());
        Bitmap image = BitmapFactory.decodeByteArray(bmp, 0, bmp.length);

        Double price = drinks_dao.getPrice(location.getDrink_id());

        holder.img_drink.setImageBitmap(image);
        holder.tv_name.setText(name);
        holder.tv_price.setText(formatter.format(price));
        holder.tv_quantity.setText(String.valueOf(location.getQuantity()));
        Double total = location.getQuantity() * price;
        holder.tv_totalPrice.setText(formatter.format(total));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_drink;
        TextView tv_name, tv_price, tv_quantity, tv_totalPrice;

        public ViewHolder(@NonNull View item) {
            super(item);
            img_drink = item.findViewById(R.id.img_drink);
            tv_name = item.findViewById(R.id.tv_name);
            tv_price = item.findViewById(R.id.tv_price);
            tv_quantity = item.findViewById(R.id.tv_quantity);
            tv_totalPrice = item.findViewById(R.id.tv_totalPrice);
        }
    }
}
