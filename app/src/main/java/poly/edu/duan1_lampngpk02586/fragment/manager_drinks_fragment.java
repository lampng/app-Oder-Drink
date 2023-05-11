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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import java.util.List;

import poly.edu.duan1_lampngpk02586.R;
import poly.edu.duan1_lampngpk02586.adapter.DrinksManager_adapter;
import poly.edu.duan1_lampngpk02586.adapter.cat_spn_adapter;
import poly.edu.duan1_lampngpk02586.customizable.ItemDrinksManagerClickListener;
import poly.edu.duan1_lampngpk02586.dao.cat_dao;
import poly.edu.duan1_lampngpk02586.dao.drinks_dao;
import poly.edu.duan1_lampngpk02586.model.cat_model;
import poly.edu.duan1_lampngpk02586.model.drinks_model;

public class manager_drinks_fragment extends Fragment implements ItemDrinksManagerClickListener {
    RecyclerView rv_drinksManager;
    EditText ed_search;
    FloatingActionButton btn_add;

    cat_dao cat_dao;
    ArrayList<cat_model> cat_models;
    cat_model cat_selected = null;

    drinks_dao drinks_dao;
    ArrayList<drinks_model> list;
    DrinksManager_adapter adapter;

    ImageView img_DrinksManager, img_DrinkManager_edit;

    int REQUEST_CODE_CAMERA_DRINK = 123;
    int REQUEST_CODE_FOLDER_DRINK = 456;
    int REQUEST_CODE_CAMERA_DRINK_EDIT = 789;
    int REQUEST_CODE_FOLDER_DRINK_EDIT = 890;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manager_drinks, container, false);

        findID(view);
        loadDrinks();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_add();
            }
        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDrinks() {
        drinks_dao = new drinks_dao(getContext());
        list = drinks_dao.getList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_drinksManager.setLayoutManager(linearLayoutManager);
        adapter = new DrinksManager_adapter(getContext(), list);
        rv_drinksManager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setClickListener(this);
    }

    private void findID(View view) {
        btn_add = view.findViewById(R.id.btn_add);
        rv_drinksManager = view.findViewById(R.id.rv_drinksManager);
    }

    @Override
    public void onClick(View view, int position) {
        final drinks_model locate = list.get(position);
        Button btn_edit;
        btn_edit = (Button) view.findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = locate.getId_drinks();
                int idCat = locate.getCat_ID();
                String name = locate.getName_drinks();
                byte[] old_image = locate.getImage_drinks();
                Double price = locate.getPrice_drinks();
                String des = locate.getDes_drinks();
                dialog_edit(id, idCat, name, old_image, price, des);
            }
        });
    }

    private void dialog_add() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_drinks, null);
        builder.setView(view);
        EditText ed_Name = view.findViewById(R.id.ed_Name);
        EditText ed_price = view.findViewById(R.id.ed_price);
        EditText ed_des = view.findViewById(R.id.ed_des);
        Spinner spn_Cat = view.findViewById(R.id.spn_Cat);
        Button btn_add = view.findViewById(R.id.btn_add);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        cat_dao catDao = new cat_dao(getContext());
        List<cat_model> categories = catDao.getList();
        cat_spn_adapter catAdapter = new cat_spn_adapter(getContext(), categories);
        spn_Cat.setAdapter(catAdapter);
        spn_Cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat_selected = categories.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        img_DrinksManager = view.findViewById(R.id.img_drinksManager);
        Button btn_folder = view.findViewById(R.id.btn_folder);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_DRINK);
            }
        });
        btn_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER_DRINK);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                drinks_dao drinks_dao = new drinks_dao(getContext());
                drinks_model drinks_model = new drinks_model();
                String name = ed_Name.getText().toString();
                String price = ed_price.getText().toString();
                String des = ed_des.getText().toString();

                //Chuyển dữ liệu hình qua bitmap
                BitmapDrawable bitmapDrawable = (BitmapDrawable) img_DrinksManager.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] image_drinks = byteArrayOutputStream.toByteArray();
                boolean isDuplicate = drinks_dao.isDuplicate(name);
                if (img_DrinksManager.getDrawable() == null) {
                    FancyToast.makeText(getContext(), "Please select image for drink", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else if (isDuplicate) {

                    //add
                    //get id cat by name cat
                    cat_dao cat_dao = new cat_dao(getContext());
                    String nameCat = cat_selected.getName_cat();
                    int idCat = cat_dao.getId(nameCat);

                    drinks_model.setName_drinks(name);
                    drinks_model.setCat_ID(idCat);
                    drinks_model.setImage_drinks(image_drinks);
                    drinks_model.setPrice_drinks(Double.valueOf(price));
                    drinks_model.setDes_drinks(des);
                    boolean isCreate = drinks_dao.create(drinks_model);
                    if (isCreate) {
                        FancyToast.makeText(getContext(), "Add successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        loadDrinks();
                        dialog.cancel();
                    } else {
                        FancyToast.makeText(getContext(), "Add Fail", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            }
        });
    }

    private void dialog_edit(int id, int idCat, String old_name, byte[] old_image, Double price, String des) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = ((Activity) getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_drinks, null);
        builder.setView(view);

        EditText ed_Name = view.findViewById(R.id.ed_Name);
        Spinner spn_Cat = view.findViewById(R.id.spn_Cat);
        ImageView img_DrinkManager_edit = view.findViewById(R.id.img_drinksManager);
        Button btn_folder = view.findViewById(R.id.btn_folder);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        EditText ed_price = view.findViewById(R.id.ed_price);
        EditText ed_des = view.findViewById(R.id.ed_des);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_update = view.findViewById(R.id.btn_update);

        ed_Name.setText(old_name);

        Bitmap bmp = BitmapFactory.decodeByteArray(old_image, 0, old_image.length);

        img_DrinkManager_edit.setImageBitmap(bmp);

        ed_price.setText(String.valueOf(price));

        ed_des.setText(des);

        cat_dao catDao = new cat_dao(getContext());
        List<cat_model> categories = catDao.getList();
        cat_spn_adapter catAdapter = new cat_spn_adapter(getContext(), categories);
        spn_Cat.setAdapter(catAdapter);
        spn_Cat.setSelected(false);
        spn_Cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat_selected = categories.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cat_selected = categories.get(idCat);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.airbnb.lottie.R.style.Animation_AppCompat_DropDownUp;
        dialog.show();

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA_DRINK_EDIT);
            }
        });
        btn_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER_DRINK_EDIT);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_name = ed_Name.getText().toString();
                String price = ed_price.getText().toString();
                String des = ed_des.getText().toString();
                drinks_dao drinks_dao = new drinks_dao(getContext());
                drinks_model drinks_model = new drinks_model();

                boolean isDuplicate = drinks_dao.isDuplicate(new_name);
                if (new_name.equals(old_name)) {
                    ed_Name.setError("Please give the drinks a new name!");
                } else if (isDuplicate) {
                    //Chuyển dữ liệu hình qua bitmap
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) img_DrinkManager_edit.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] image_drinks = byteArrayOutputStream.toByteArray();
                    //add
                    //get id cat by name cat
                    cat_dao cat_dao = new cat_dao(getContext());
                    String nameCat = cat_selected.getName_cat();
                    int idCat = cat_dao.getId(nameCat);

                    drinks_model.setId_drinks(id);
                    drinks_model.setName_drinks(new_name);
                    drinks_model.setCat_ID(idCat);
                    drinks_model.setImage_drinks(image_drinks);
                    drinks_model.setPrice_drinks(Double.valueOf(price));
                    drinks_model.setDes_drinks(des);
                    boolean isUpdate = drinks_dao.update(drinks_model);
                    if (isUpdate) {
                        FancyToast.makeText(getContext(), "Update successful", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        loadDrinks();
                        dialog.cancel();
                    } else {
                        FancyToast.makeText(getContext(), "Update Fail", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            }
        });
    }

    //get image cat
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //ADD
        if (requestCode == REQUEST_CODE_CAMERA_DRINK && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img_DrinksManager.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FOLDER_DRINK && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                img_DrinksManager.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //EDIT
        if (requestCode == REQUEST_CODE_CAMERA_DRINK_EDIT && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
            img_DrinkManager_edit.setImageBitmap(bitmap2);
        }
        if (requestCode == REQUEST_CODE_FOLDER_DRINK_EDIT && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                img_DrinkManager_edit.setImageBitmap(bitmap2);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}