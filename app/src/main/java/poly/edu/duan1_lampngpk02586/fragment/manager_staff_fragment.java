package poly.edu.duan1_lampngpk02586.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.adapter.Role_Spinner_adapter;
import poly.edu.duan1_lampngpk02586.adapter.manager_staff_adapter;
import poly.edu.duan1_lampngpk02586.customizable.CustomSpinner;
import poly.edu.duan1_lampngpk02586.customizable.itemViewClickUserListener;
import poly.edu.duan1_lampngpk02586.dao.account_dao;
import poly.edu.duan1_lampngpk02586.dao.role_dao;
import poly.edu.duan1_lampngpk02586.model.account_model;
import poly.edu.duan1_lampngpk02586.model.role_model;

public class manager_staff_fragment extends Fragment implements itemViewClickUserListener, CustomSpinner.OnSpinnerEventsListener {
    EditText ed_search;
    RecyclerView rv_user;

    account_dao account_dao;
    ArrayList<account_model> list;
    manager_staff_adapter adapter;

    CustomSpinner spinner;
    Role_Spinner_adapter role_spinner_adapter;
    List<role_model> list_role;
    role_model role_selected = null;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff_manager, container, false);
        findID(view);
        loadUser();

        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    private void loadUser() {
        account_dao = new account_dao(getContext());
        list = account_dao.getListUser();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_user.setLayoutManager(linearLayoutManager);
        adapter = new manager_staff_adapter(getContext(), list, this);
        rv_user.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void findID(View view) {
        ed_search = view.findViewById(R.id.ed_search);
        rv_user = view.findViewById(R.id.rv_user);
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void onClickSetRole(View view, int position) {
        account_model location = list.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_newrole, null);
        builder.setView(v);

        spinner = v.findViewById(R.id.spinner);
        Button btn_submit = v.findViewById(R.id.btn_submit);
        spinner.setSpinnerEventsListener(this);
        role_dao catDao = new role_dao(getContext());
        List<role_model> categories = catDao.getList();
        Role_Spinner_adapter role_spinner_adapter = new Role_Spinner_adapter(getContext(), categories);
        spinner.setAdapter(role_spinner_adapter);

        String username = location.getId_ac();
        account_dao account_dao = new account_dao(getContext());
        int idRoleUser = account_dao.getIdRole(username);
        loadUser();
        spinner.setSelection(idRoleUser - 1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role_selected = categories.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        onPopupWindowOpened(spinner);
        onPopupWindowClosed(spinner);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = location.getId_ac();
                String nameRole = role_selected.getName_role();
                role_dao role_dao = new role_dao(getContext());
                int roleID = role_dao.getIdRole(nameRole);
                account_dao account_dao = new account_dao(getContext());
                account_dao.update_role(username, roleID);
                loadUser();
                dialog.cancel();
            }
        });
    }

    @Override
    public void onClickSetPassword(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_newpass, null);
        builder.setView(v);

        EditText ed_pass = v.findViewById(R.id.ed_pass);
        Button btn_submit = v.findViewById(R.id.btn_submit);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        account_model location = list.get(position);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_dao = new account_dao(getContext());
                String newpass = ed_pass.getText().toString();
                String username = location.getId_ac();
                if (newpass.isEmpty()) {
                    ed_pass.setError("PLease fill new password");
                } else if (newpass.trim().length() <= 5) {
                    ed_pass.setError("Your password is too short");
                } else {
                    account_dao.reset_password(username, newpass);
                    loadUser();
                    dialog.cancel();
                }
            }
        });
    }

    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        spinner.setBackground(getResources().getDrawable(R.drawable.bg_spinner_up));
    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        spinner.setBackground(getResources().getDrawable(R.drawable.bg_spinner));
    }
}