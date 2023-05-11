package poly.edu.duan1_lampngpk02586.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.customizable.itemViewClickUserListener;
import poly.edu.duan1_lampngpk02586.dao.role_dao;
import poly.edu.duan1_lampngpk02586.model.account_model;

public class manager_staff_adapter extends RecyclerView.Adapter<manager_staff_adapter.ViewHolder> {
    private Context context;
    private ArrayList<account_model> list;
    private itemViewClickUserListener listener;

    public manager_staff_adapter(Context context, ArrayList<account_model> list, itemViewClickUserListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) parent.getContext()).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_user, parent, false);
        return new manager_staff_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        account_model location = list.get(position);
        String fullname = location.getFirst_name() + " " + location.getMiddle_name() + " " + location.getLast_name();
        holder.tv_fullname.setText(fullname);
        String username = location.getId_ac();
        holder.tv_username.setText(username);
        String phone = location.getPhone_ac();
        holder.tv_phone.setText(phone);
        String address = location.getAddress_ac();
        holder.tv_address.setText(address);

        role_dao role_dao = new role_dao(context);
        int roleId = location.getRole_ID();
        String roleName = role_dao.getNameRole(roleId);
        holder.tv_role.setText(roleName);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_avatar;
        TextView tv_fullname, tv_username, tv_phone, tv_address, tv_role, setrole, setpassword;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            tv_fullname = itemView.findViewById(R.id.tv_fullname);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_role = itemView.findViewById(R.id.tv_role);

            setrole = itemView.findViewById(R.id.setrole);
            setpassword = itemView.findViewById(R.id.setpassword);

            setrole.setOnClickListener(view -> listener.onClickSetRole(setrole, getAdapterPosition()));
            setpassword.setOnClickListener(view -> listener.onClickSetPassword(setpassword, getAdapterPosition()));
            itemView.setOnClickListener(view -> listener.onClick(itemView, getAdapterPosition()));
        }
    }
}
