package com.chigozie.musicplayer;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles>musicfiles;
    String[] permissio = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        //callthread();

        initViewpager();
    }


    private void permission(){
        if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, permissio, REQUEST_CODE);
        }else {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            musicfiles = getAllAudio(this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                musicfiles = getAllAudio(this);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,permissio, REQUEST_CODE);
            }
        }
    }

    private void initViewpager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        Viewpageradapter viewpageradapter = new Viewpageradapter(getSupportFragmentManager());
        viewpageradapter.addFragements(new songsfragment(), "songs");
        viewpageradapter.addFragements(new albumFragment(), "album");
        viewPager.setAdapter(viewpageradapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /** @noinspection deprecation*/
    public static class Viewpageradapter extends FragmentPagerAdapter{

        private ArrayList<Fragment>fragments;
        private  ArrayList<String>titles;


        /** @noinspection deprecation*/
        public Viewpageradapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        void addFragements(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    public static ArrayList<MusicFiles>getAllAudio(Context context){
        ArrayList<MusicFiles>tempaudio = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if(cursor!=null){

            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String title= cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);
                Log.e("Path :" + path, "Album: " + album);
                tempaudio.add(musicFiles);
            }
            cursor.close();

        }
        return tempaudio;

    }

    public static class running implements Runnable{

        private final LoadSounds ls;
        //private final Context context;
        public running(Context context) {
            //this.context = context;
            this.ls = new LoadSounds(context);
        }
        @Override
        public void run() {
            int x = ls.AssetsNames();
            if(x>1){
                try {
                    Log.e("xx", "the value of X is"+x);
                    ls.Readfiles();
                } catch (IOException e) {
                    Log.e("Loader", "Error reading files: " + e.getMessage());
                }
            }else {
                Log.e("Loader", "Read file unsuccessful");
            }
        }
    }

    public void callthread(){
        running runn = new running(this);
        Thread thread = new Thread(runn);
        thread.start();
    }
}


