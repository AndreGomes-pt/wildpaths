package com.example.wildpaths.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wildpaths.MainActivity;

public class GalleryAdapter extends AppCompatActivity {
    public Bitmap tempBitMap;

    private Context context;
    private Activity activity;
    public GalleryAdapter(Context context,Activity activity){
       this.context = context;
       this.activity = activity;
    }

    public void selecImgGaleria() {
        final CharSequence[] options;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(MainActivity.lang == 0){
            options = new CharSequence[]{"Escolher da galeria", "Cancelar"};
            builder.setTitle("Adicionar foto");
        }else{
            builder.setTitle("Add Photo");
            options = new CharSequence[]{"Choose from Gallery", "Cancel"};
        }

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery") || options[item].equals("Escolher da galeria"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activity.startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Cancel") || options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                tempBitMap = (BitmapFactory.decodeFile(picturePath));

                System.out.println("A IMAGEM FOI PORCURADA E GUARDADA");

            }
        }
    }

}
