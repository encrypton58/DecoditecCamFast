package com.marc.decoditeccamfast.ui.gallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.marc.decoditeccamfast.R;
import com.marc.decoditeccamfast.clases.dialogs.LoaderDialog;
import com.marc.decoditeccamfast.clases.dialogs.SuccessOrErrorDialog;
import com.marc.decoditeccamfast.clases.trabajadores.Trabajadores;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private ImageView imageShow;
    private String imgStr;
    private Uri uriImage;
    private String mCurrentPhotoPath;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.rt_label_cameraPick);
        final Button registrar = root.findViewById(R.id.rt_button_register);
        final TextInputLayout layNombre = root.findViewById(R.id.rt_layout_nombre);
        final TextInputLayout layNumero = root.findViewById(R.id.rt_layout_numero);
        final ImageButton pickPhoto = root.findViewById(R.id.r_t_pick_photo);
        final Spinner area = root.findViewById(R.id.r_t_area);

        imageShow = root.findViewById(R.id.r_t_show_image);

        pickPhoto.setOnClickListener(listenerPickPhoto);

        registrar.setOnClickListener( v -> {
            LoaderDialog ld = new LoaderDialog(requireActivity());
            ld.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ld.show();
            String nombre = Objects.requireNonNull(layNombre.getEditText()).getText().toString();
            String numero = Objects.requireNonNull(layNumero.getEditText()).getText().toString();
            String areaStr = area.getSelectedItem().toString();
            new Trabajadores().registrarTrabajador(requireContext(), nombre, numero, areaStr, imgStr, registrado -> {
                ld.dismiss();
                if(registrado){
                    SuccessOrErrorDialog sd = new SuccessOrErrorDialog(requireActivity(), registrado);
                    sd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    sd.show();
                }else{

                }
            });
        });

        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap img = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imageShow.setImageBitmap(img);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    Bitmap convertStr = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uriImage);
                    imgStr = imageToString(convertStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getContext(), "No Soporta La aplicacion el dispositivo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String imageToString(Bitmap map){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        map.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imgBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imgBytes);
    }

    @SuppressLint("QueryPermissionsNeeded")
    View.OnClickListener listenerPickPhoto = v -> {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(in.resolveActivity(requireContext().getPackageManager()) != null){
            File foto = null;
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    foto = createImageFile();
                }else{
                    Toast.makeText(requireContext(), "No se puede ejecutar la función", Toast.LENGTH_SHORT).show();
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            if(foto != null){
                Uri uriFoto = FileProvider.getUriForFile(requireContext(),
                        "com.marc.decoditeccamfast.file", foto);
                uriImage = uriFoto;
                in.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
                startActivityForResult(in, 1);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    File createImageFile()throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        Toast.makeText(requireContext(), mCurrentPhotoPath, Toast.LENGTH_SHORT).show();
        return image;
    }

}

