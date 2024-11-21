package com.example.wildpaths.adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImagensAdapter extends Thread {

    public byte [] encode (Drawable img){

        //Converte imagen em bytes
        if(img!=null){
            Bitmap bitmap = ((BitmapDrawable) img).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        }

        return null;
    }


    public Drawable decode (byte [] encodedBytes, String nomeFile, Context context){

        //Decodigica a string de base64 para bytes
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(nomeFile + ".png", MODE_PRIVATE);
            fos.write(encodedBytes);
            fos.close();

            FileInputStream fis = context.openFileInput(nomeFile + ".png");
            Bitmap bitmap = BitmapFactory.decodeStream(fis);

            return new BitmapDrawable(context.getResources(), bitmap);
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException : " + ex);
        } catch (IOException ioe) {
            System.out.println("IOException : " + ioe);
        }

        return null;

    }

}
