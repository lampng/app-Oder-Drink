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
import poly.edu.duan1_lampngpk02586.customizable.ItemCatClickListener;
import poly.edu.duan1_lampngpk02586.model.cat_model;

public class cat_adapter extends RecyclerView.Adapter<cat_adapter.ViewHolder> {

    private ArrayList<cat_model> list;
    private Context context;
    private ItemCatClickListener clickListener;

    public cat_adapter(Context context, ArrayList<cat_model> list, ItemCatClickListener clickListener) {
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_cat, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cat_model getPosition = list.get(holder.getAdapterPosition());

        holder.tv_catName.setText(getPosition.getName_cat());

        Bitmap bmp = BitmapFactory.decodeByteArray(getPosition.getImage_cat(), 0, getPosition.getImage_cat().length);
        holder.img_cat.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cat;
        TextView tv_catName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cat = itemView.findViewById(R.id.img_cat);
            tv_catName = itemView.findViewById(R.id.tv_catName);
            itemView.setOnClickListener(view -> clickListener.onClick_Cat(itemView, getAdapterPosition()));
        }

    }
}
