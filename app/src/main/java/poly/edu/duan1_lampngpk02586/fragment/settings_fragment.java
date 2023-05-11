package poly.edu.duan1_lampngpk02586.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.dao.account_dao;
import poly.edu.duan1_lampngpk02586.model.account_model;

public class settings_fragment extends Fragment {

    ImageView img_user;
    TextView edit;
    LinearLayout checked;
    Button btnUpdate;
    ArrayList<account_model> list;
    EditText ed_first_name, ed_middle_name, ed_last_name, ed_phone, ed_address;

    int potison;

    SharedPreferences sharedPreferences;
    private AwesomeValidation validation;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String fn = ed_first_name.getText().toString().trim();
            String mn = ed_middle_name.getText().toString().trim();
            String ln = ed_last_name.getText().toString().trim();
            String phone = ed_phone.getText().toString().trim();
            String address = ed_address.getText().toString().trim();

            if (isEmpty(fn) && isEmpty(mn) && isEmpty(ln) && isEmpty(phone) && isEmpty(address)) {
                btnUpdate.setEnabled(true);
                btnUpdate.setBackground(getResources().getDrawable(R.drawable.button_submit));
            } else {
                btnUpdate.setEnabled(false);
                btnUpdate.setBackground(getResources().getDrawable(R.drawable.button_disable));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public static Bitmap convertCompressedByteArrayToBitmap(byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }

    private boolean isEmpty(String x) {
        if (!x.isEmpty()) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        findID(view);
        sharedPreferences = getActivity().getSharedPreferences("login_info", MODE_PRIVATE);
        String gTusername = sharedPreferences.getString("username", "");
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit.getText().toString().equals("Edit")) {
                    edit.setText("View");
                    checked.setEnabled(true);
                    btnUpdate.setVisibility(View.VISIBLE);
                    ed_first_name.setEnabled(true);
                    ed_middle_name.setEnabled(true);
                    ed_last_name.setEnabled(true);
                    ed_phone.setEnabled(true);
                    ed_address.setEnabled(true);
                } else {
                    edit.setText("Edit");
                    ed_first_name.setEnabled(false);
                    ed_middle_name.setEnabled(false);
                    ed_last_name.setEnabled(false);
                    ed_phone.setEnabled(false);
                    ed_address.setEnabled(false);
                    btnUpdate.setVisibility(View.GONE);
                }
            }
        });
        account_dao account_dao = new account_dao(getContext());

        ed_first_name.addTextChangedListener(textWatcher);
        ed_middle_name.addTextChangedListener(textWatcher);
        ed_last_name.addTextChangedListener(textWatcher);
        ed_phone.addTextChangedListener(textWatcher);
        ed_address.addTextChangedListener(textWatcher);
        //Validation
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(getActivity(), R.id.ed_username, "^" + "(?=\\S+$)" + ".{6,}" + "$", R.string.err_username);
        validation.addValidation(getActivity(), R.id.ed_first_name, "[a-zA-Z\\s]+", R.string.err_fn);
        validation.addValidation(getActivity(), R.id.ed_middle_name, "[a-zA-Z\\s]+", R.string.err_mn);
        validation.addValidation(getActivity(), R.id.ed_last_name, "[a-zA-Z\\s]+", R.string.err_ln);
        validation.addValidation(getActivity(), R.id.ed_phone, "0[0-9]{9,10}", R.string.err_phone);
        //set data
        String fn = account_dao.getFirstname(gTusername);
        String mn = account_dao.getMiddle(gTusername);
        String ln = account_dao.getLastname(gTusername);
        String phone = account_dao.getPhone(gTusername);
        String address = account_dao.getAddress(gTusername);

        ed_first_name.setText(fn);
        ed_middle_name.setText(mn);
        ed_last_name.setText(ln);
        ed_phone.setText(phone);
        ed_address.setText(address);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation.validate()) {
                    String fn = ed_first_name.getText().toString().trim();
                    String mn = ed_middle_name.getText().toString().trim();
                    String ln = ed_last_name.getText().toString().trim();
                    String phone = ed_phone.getText().toString();
                    String address = ed_address.getText().toString();
                    if (img_user.getDrawable() == null) {
                        account_dao.update_profile_NoAvatar(fn, mn, ln, phone, address, gTusername);
                    } else {
//                        account_dao.update_profile_HaveAvatar(fn, mn, ln, phone, address, image, gTusername);
                    }

                    edit.setText("Edit");
                    ed_first_name.setEnabled(false);
                    ed_middle_name.setEnabled(false);
                    ed_last_name.setEnabled(false);
                    ed_phone.setEnabled(false);
                    ed_address.setEnabled(false);
                    btnUpdate.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void findID(View view) {
        edit = view.findViewById(R.id.edit);
        img_user = view.findViewById(R.id.img_user);
        checked = view.findViewById(R.id.checked);
        btnUpdate = view.findViewById(R.id.btnRegister);

        ed_first_name = view.findViewById(R.id.ed_first_name);
        ed_middle_name = view.findViewById(R.id.ed_middle_name);
        ed_last_name = view.findViewById(R.id.ed_last_name);
        ed_phone = view.findViewById(R.id.ed_phone);
        ed_address = view.findViewById(R.id.ed_address);
    }

    private byte[] getByte(ImageView drawable) {
        //Chuyển dữ liệu hình qua bitmap
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] image = byteArrayOutputStream.toByteArray();
        return image;
    }
}