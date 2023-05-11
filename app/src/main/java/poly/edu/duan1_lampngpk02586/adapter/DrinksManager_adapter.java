package poly.edu.duan1_lampngpk02586.adapter;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.customizable.ItemDrinksManagerClickListener;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class DrinksManager_adapter extends RecyclerView.Adapter<DrinksManager_adapter.ViewHolder> {

    private ArrayList<drinks_model> list;
    private Context context;
    private ItemDrinksManagerClickListener clickListener;

    public DrinksManager_adapter(Context context, ArrayList<drinks_model> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_drinksmanager, parent, false);
        return new DrinksManager_adapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        drinks_model getPosition = list.get(holder.getAdapterPosition());
        String id = String.valueOf(getPosition.getId_drinks());
        holder.tv_Id.setText("ID: "+id);

        cat_dao cat_dao = new cat_dao(context);
        String id_cat = String.valueOf(getPosition.getCat_ID());
        String catName = cat_dao.getNamePosition(Integer.parseInt(id_cat));
        holder.tv_IdCat.setText("Category: "+catName);
        holder.tv_name.setText("Name: "+getPosition.getName_drinks());
        Bitmap bmp = BitmapFactory.decodeByteArray(getPosition.getImage_drinks(), 0, getPosition.getImage_drinks().length);
        holder.img.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_remove(view, getPosition);
                Toast.makeText(view.getContext(), "Remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialog_remove(View view, drinks_model getPosition) {
        Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = ((Activity) view.getContext()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_remove_drinks, null);
        builder.setView(v);

        TextView tv_Name = v.findViewById(R.id.tv_Name);
        TextView tv_price = v.findViewById(R.id.tv_price);
        TextView tv_des = v.findViewById(R.id.tv_des);
        TextView tv_idCat = v.findViewById(R.id.tv_idCat);
        Button btn_cancel = v.findViewById(R.id.btn_cancel);
        Button btn_remove = v.findViewById(R.id.btn_remove);
        ImageView img_drinksManager = v.findViewById(R.id.img_drinksManager);

        tv_Name.setText(getPosition.getName_drinks());
        String price = String.valueOf(getPosition.getPrice_drinks());
        tv_price.setText(price);
        tv_des.setText(getPosition.getDes_drinks());
        Bitmap bmp = BitmapFactory.decodeByteArray(getPosition.getImage_drinks(), 0, getPosition.getImage_drinks().length);

        img_drinksManager.setImageBitmap(bmp);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                drinks_dao drinks_dao = new drinks_dao(view.getContext());
                Boolean remove = drinks_dao.delete(getPosition.getId_drinks());
                if (remove) {
                    Toast.makeText(view.getContext(), "Remove success", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.cancel();
                } else {
                    Toast.makeText(view.getContext(), "Remove fail", Toast.LENGTH_SHORT).show();
                }
                list.clear();
                list = drinks_dao.getList();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemDrinksManagerClickListener itemDrinksManagerClickListener) {
        this.clickListener = itemDrinksManagerClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView tv_Id, tv_IdCat, tv_name, tv_more;
        Button btn_edit, btn_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_drinksManager);
            tv_Id = itemView.findViewById(R.id.tv_Id_drinksManager);
            tv_IdCat = itemView.findViewById(R.id.tv_IdCat_drinksManager);
            tv_name = itemView.findViewById(R.id.tv_name_drinksManager);
            tv_more = itemView.findViewById(R.id.tv_more);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            btn_edit.setOnClickListener(this);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
