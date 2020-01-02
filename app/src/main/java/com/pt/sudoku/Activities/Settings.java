package com.pt.sudoku.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.CameraDevice;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.pt.sudoku.R;
import java.io.File;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadPhoto();

    }

    public void onChangePhoto(View view) {
        Intent intent = new Intent(this, ChangePhoto.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPhoto();
    }

    protected void loadPhoto(){
        ImageView imageView = (ImageView) findViewById(R.id.ivProfilePicture);
        if(imageView == null){
            //return
        }
        final File file = new File(Environment.getExternalStorageDirectory()+getString(R.string.photo_location));
        if(file.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }else{
            imageView.setImageResource(R.drawable.default_pic);
        }
    }
}
