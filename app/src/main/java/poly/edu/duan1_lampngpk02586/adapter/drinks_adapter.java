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
import poly.edu.duan1_lampngpk02586.customizable.ItemDrinksClickListener;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class drinks_adapter extends RecyclerView.Adapter<drinks_adapter.ViewHolder> {

    private ArrayList<drinks_model> list;
    private Context context;
    private ItemDrinksClickListener clickListener;

    public drinks_adapter(Context context, ArrayList<drinks_model> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_drinks, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        drinks_model getPosition = list.get(holder.getAdapterPosition());

        holder.tv_drinksName.setText(getPosition.getName_drinks());
        Double price = getPosition.getPrice_drinks();
        holder.tv_price.setText(formatter.format(price));

        Bitmap bmp = BitmapFactory.decodeByteArray(getPosition.getImage_drinks(), 0, getPosition.getImage_drinks().length);
        holder.img_drinks.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemDrinksClickListener itemDrinksClickListener) {
        this.clickListener = itemDrinksClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_drinks;
        TextView tv_drinksName;
        TextView tv_price;
        Button btn_add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_drinks = itemView.findViewById(R.id.img_drinks);
            tv_drinksName = itemView.findViewById(R.id.tv_drinksName);
            tv_price = itemView.findViewById(R.id.tv_price);
            btn_add = itemView.findViewById(R.id.btn_add);
            btn_add.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick_drinks(view, getAdapterPosition());
        }
    }
}
