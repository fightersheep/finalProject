package com.example.finalProjectV1.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageHandler {
    public static byte[] bitmapToByteArray(Bitmap in)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return  bytes.toByteArray();
    }
    public static Bitmap byteArrayToBitmap(byte[]bytes)
    {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap stringToBitmap(String stringPicture)
    {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static String bitmapToString(Bitmap in){
        byte[] b = bitmapToByteArray(in);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}

