package com.smeghani.citydirectoryapp.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by shoaibmeghani on 07/01/2018.
 */

public class JsonReaderHelper {

    public static String loadJsonFromAssets(Context context, String fileName){
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
