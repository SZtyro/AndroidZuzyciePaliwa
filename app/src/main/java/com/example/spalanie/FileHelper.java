package com.example.spalanie;

import android.content.Context;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    private static final String fileName = "applicationData2";

    public static JSONArray openFile(Context context) throws FileNotFoundException, JSONException {
        return new JSONArray(SerializationUtils.deserialize(context.openFileInput(fileName)).toString());

    }

    public static void saveFile(JSONArray object, Context context) {
        FileOutputStream outputStream = null;
        try {
            System.out.println("Saving file:");
            System.out.println("\n" + object.toString(2));
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(SerializationUtils.serialize(object.toString()));
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    static void createFile(Context context) {

        File file = new File(context.getFilesDir(), fileName);
        try {
            file.createNewFile();
            JSONArray arr = new JSONArray();
            FileHelper.saveFile(arr, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
