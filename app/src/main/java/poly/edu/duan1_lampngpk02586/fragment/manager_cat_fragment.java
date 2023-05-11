package poly.edu.duan1_lampngpk02586.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.adapter.catManager_adapter;
import poly.edu.duan1_lampngpk02586.customizable.ItemCatManagerClickListener;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.model.cat_model;

public class manager_cat_fragment extends Fragment implements ItemCatManagerClickListener {
    RecyclerView rv_catManager;
    EditText ed_search;
    FloatingActionButton btn_add;

    cat_dao cat_dao;
    ArrayList<cat_model> list;
    catManager_adapter adapter;

    ImageView img_catManager, img_catManager_edit;

    int REQUEST_CODE_CAMERA_CAT = 123;
    int REQUEST_CODE_FOLDER_CAT = 456;
    int REQUEST_CODE_CAMERA_CAT_EDIT = 789;
    int REQUEST_CODE_FOLDER_CAT_EDIT = 890;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_cat, container, false);
        findID(view);

        loadCat();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_add();
            }
        });
        return view;
    }

    private void findID(View view) {
        rv_catManager = view.findViewById(R.id.rv_catManager);
        ed_search = view.findViewById(R.id.ed_search);
        btn_add = view.findViewById(R.id.btn_add);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCat() {
        cat_dao = new cat_dao(getContext());
        list = cat_dao.getList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_catManager.setLayoutManager(linearLayoutManager);
        adapter = new catManager_adapter(getContext(), list);
        rv_catManager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        final cat_model locate = list.get(position);
        Button btn_edit;
        btn_edit = (Button) view.findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = locate.getId_cat();
                String name = locate.getName_cat();
                byte[] old_image = locate.getImage_cat();
                dialog_edit(id, name, old_image);
            }
        });
    }

    private void dialog_add() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_cat, null);
        builder.setView(view);
        EditText ed_addName = view.findViewById(R.id.ed_addName);
        img_catManager = view.findViewById(R.id.img_catManager);
        Button btn_folder = view.findViewById(R.id.btn_folder);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_submit = view.findViewById(R.id.btn_submit);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_CAT);
            }
        });
        btn_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER_CAT);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_addName.getText().toString();
                cat_dao cat_dao = new cat_dao(getContext());
                cat_model cat_model = new cat_model();

                boolean isDuplicate = cat_dao.isDuplicate(name);
                if (isDuplicate) {
                    //Chuyển dữ liệu hình qua bitmap
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) img_catManager.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] image_cat = byteArrayOutputStream.toByteArray();
                    //thêm dữ liệu vào model
                    cat_model.setName_cat(name);
                    cat_model.setImage_cat(image_cat);
                    boolean isCreateCat = cat_dao.create(cat_model);
                    if (isCreateCat) {
                        FancyToast.makeText(getContext(), "Add successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        loadCat();
                        dialog.cancel();
                    } else {

                        FancyToast.makeText(getContext(), "Add Fail", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } else {
                    ed_addName.setError("This category already exists");
                }
            }
        });
    }

    private void dialog_edit(int id, String old_name, byte[] old_image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_cat, null);
        builder.setView(view);

        EditText ed_EditName = view.findViewById(R.id.ed_EditName);
        img_catManager_edit = view.findViewById(R.id.img_catManager_edit);
        Button btn_folder = view.findViewById(R.id.btn_folder);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_submit = view.findViewById(R.id.btn_edit);

        ed_EditName.setText(old_name);

        Bitmap bmp = BitmapFactory.decodeByteArray(old_image, 0, old_image.length);

        img_catManager_edit.setImageBitmap(bmp);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                String new_name = ed_EditName.getText().toString();

                cat_dao cat_dao = new cat_dao(getContext());
                cat_model cat_model = new cat_model();

                boolean isDuplicate = cat_dao.isDuplicate(new_name);
                if (new_name.equals(old_name)) {
                    ed_EditName.setError("Please give the category a new name!");
                } else if (isDuplicate) {
                    //Chuyển dữ liệu hình qua bitmap
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) img_catManager_edit.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] image_cat = byteArrayOutputStream.toByteArray();
                    //Cập nhập dữ liệu
                    cat_model.setId_cat(id);
                    cat_model.setName_cat(new_name);
                    cat_model.setImage_cat(image_cat);
                    boolean isUpdateCat = cat_dao.update(cat_model);
                    if (isUpdateCat) {
                        FancyToast.makeText(getContext(), "Add successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        loadCat();
                        dialog.cancel();
                    } else {

                        FancyToast.makeText(getContext(), "Add Fail", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                } else {
                    ed_EditName.setError("This category already exists");
                }
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_CAT_EDIT);
            }
        });
        btn_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER_CAT_EDIT);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    //get image cat
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //ADD
        if (requestCode == REQUEST_CODE_CAMERA_CAT && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_catManager.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER_CAT && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_catManager.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
//        //EDIT
        if (requestCode == REQUEST_CODE_CAMERA_CAT_EDIT && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
            img_catManager_edit.setImageBitmap(bitmap2);
        }
        if (requestCode == REQUEST_CODE_FOLDER_CAT_EDIT && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                img_catManager_edit.setImageBitmap(bitmap2);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}