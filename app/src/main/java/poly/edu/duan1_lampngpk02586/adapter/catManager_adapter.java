package poly.edu.duan1_lampngpk02586.adapter;

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
import poly.edu.duan1_lampngpk02586.customizable.ItemCatManagerClickListener;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.model.cat_model;

public class catManager_adapter extends RecyclerView.Adapter<catManager_adapter.ViewHolder> {

    private ArrayList<cat_model> list;
    private Context context;
    private ItemCatManagerClickListener clickListener;

    public catManager_adapter(Context context, ArrayList<cat_model> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_catmanager, parent, false);
        return new catManager_adapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        cat_model getPosition = list.get(holder.getAdapterPosition());
        String id = String.valueOf(getPosition.getId_cat());
        holder.tv_catID.setText(id);
        holder.tv_catName.setText(getPosition.getName_cat());
        Bitmap bmp = BitmapFactory.decodeByteArray(getPosition.getImage_cat(), 0, getPosition.getImage_cat().length);
        holder.img_cat.setImageBitmap(Bitmap.createScaledBitmap(bmp, 100, 100, false));
        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_remove(view, getPosition);
                Toast.makeText(view.getContext(), "Remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dialog_remove(View view, cat_model getPosition) {
        Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = ((Activity) view.getContext()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_remove_cat, null);
        builder.setView(v);

        TextView tv_Name = v.findViewById(R.id.tv_Name);
        Button btn_cancel = v.findViewById(R.id.btn_cancel);
        Button btn_remove = v.findViewById(R.id.btn_remove);
        ImageView img_catManager = v.findViewById(R.id.img_catManager);
        tv_Name.setText(getPosition.getName_cat());

        Bitmap bmp = BitmapFactory.decodeByteArray(getPosition.getImage_cat(), 0, getPosition.getImage_cat().length);

        img_catManager.setImageBitmap(bmp);

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
                cat_dao cat_dao = new cat_dao(view.getContext());
                Boolean remove = cat_dao.delete(getPosition.getId_cat());
                if (remove) {
                    Toast.makeText(view.getContext(), "Remove success", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.cancel();
                } else {
                    Toast.makeText(view.getContext(), "Remove fail", Toast.LENGTH_SHORT).show();
                }
                list.clear();
                list = cat_dao.getList();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setClickListener(ItemCatManagerClickListener itemCatManagerClickListener) {
        this.clickListener = itemCatManagerClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_cat;
        TextView tv_catID, tv_catName;
        Button btn_edit, btn_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_catID = itemView.findViewById(R.id.tv_catID_manager);
            tv_catName = itemView.findViewById(R.id.tv_catName_manager);
            img_cat = itemView.findViewById(R.id.img_catManager);
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
