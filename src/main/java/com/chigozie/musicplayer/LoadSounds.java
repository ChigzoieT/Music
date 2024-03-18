package com.chigozie.musicplayer;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;


/*String targetDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MyApp/Files";
        File file = new File(targetDirectory, rawResourceNames[i]);
// ... (rest of the code for creating directory and writing file)*/

public class LoadSounds {

    Context context;
    int x;

    public LoadSounds(Context context){
    this.context = context;
    }
    int[] resourceRawIds= {R.raw.colors, R.raw.deka, R.raw.elasticheart};

    String[] rawResourceNames = new String[resourceRawIds.length];

    //Resources resources = Resources.getSystem();

    public int AssetsNames(){
        Resources resources = context.getResources();
        for(int i = 0; i<resourceRawIds.length; i++){
            String name = resources.getResourceEntryName(resourceRawIds[i]);
            rawResourceNames[i] = name;

            x =i;
        }

        return x;
    }


    public void Readfiles() throws IOException {
       for(int i =0; i<resourceRawIds.length; i++){
           String targetDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/";
           File file = new File(targetDirectory, rawResourceNames[i]);

           int length;
           byte[] buffer = new byte[1024];

           try {
               InputStream inputStream = context.getResources().openRawResource(resourceRawIds[i]);
               OutputStream outputStream = new FileOutputStream(file);
               Log.e("hello", "Starting file dump" + i);

               while ((length = inputStream.read(buffer)) >0){
                   outputStream.write(buffer, 0, length);
                   Log.e("file dump", "file dump successful");
               }

           } catch (FileNotFoundException e) {
               throw new RuntimeException(e);
           }
       }
    }
}
