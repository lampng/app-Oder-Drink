package poly.edu.duan1_lampngpk02586.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.customizable.invoiceClickListener;
import poly.edu.duan1_lampngpk02586.model.order_model;

public class invoice_adapter extends RecyclerView.Adapter<invoice_adapter.ViewHolder> {
    SharedPreferences sharedPreferences;
    private ArrayList<order_model> list;
    private invoiceClickListener clickListener;
    private Context context;

    public invoice_adapter(Context context, ArrayList<order_model> list, invoiceClickListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public invoice_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_order, parent, false);
        return new invoice_adapter.ViewHolder(view);
    }

    // invoice sẽ hiển thị tên khách hàng, ngày đặt, tổng đơn hàng, button confirm
    //hiển thị dành cho khành hàng sẽ có button confirm sẽ thành nút hủy đơn hàng
    @SuppressLint({"SetTextI18n", "ResourceAsColor", "NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull invoice_adapter.ViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences("login_info", MODE_PRIVATE);
        String getRole = sharedPreferences.getString("role", "");
        order_model location = list.get(position);
        holder.tv_order_id.setText(String.valueOf(location.getId_order()));
        holder.tv_date.setText(location.getDate_order());
        holder.tv_status.setText("Waiting");
        holder.tv_totalPrice.setText("$" + String.valueOf(location.getPrice_total()));
        int role = Integer.parseInt(getRole);
        if (role == 3) {
            holder.btn_confirm.setBackgroundDrawable(context.getResources().getDrawable(com.shashank.sony.fancytoastlib.R.drawable.error_shape));
            holder.btn_confirm.setText("Cancel");
        }
        String status = location.getOrder_status();
        if (!(status == null)) {
            holder.btn_confirm.setBackgroundDrawable(context.getResources().getDrawable(com.shashank.sony.fancytoastlib.R.drawable.success_shape));
            holder.btn_confirm.setText("Is Confirmed");
            holder.tv_status.setText(status + " is Confirming");
            if (role == 3) {
                holder.btn_confirm.setBackgroundDrawable(context.getResources().getDrawable(com.shashank.sony.fancytoastlib.R.drawable.info_shape));
                holder.btn_confirm.setText("shipping");
                holder.tv_status.setText(status + " is Confirming");
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_confirm;
        TextView tv_order_id, tv_date, tv_status, tv_totalPrice;
        RecyclerView rv_item_cart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_totalPrice = itemView.findViewById(R.id.tv_totalPrice);
            rv_item_cart = itemView.findViewById(R.id.rv_item_cart);

            itemView.setOnClickListener(view -> clickListener.onItemView(getAdapterPosition(), rv_item_cart));
            btn_confirm.setOnClickListener(view -> clickListener.onConfirmClick(getAdapterPosition(), btn_confirm));
        }
    }


}
