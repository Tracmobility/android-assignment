package com.example.trac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        img =findViewById(R.id.imageView);
         YoYo.with(Techniques.FlipInX)
                 .duration(800)
                    .repeat(1)
                    .playOn(img);


        YoYo.with(Techniques.BounceInDown)
                .duration(700)
                .repeat(1)
                .playOn(img);

        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openNewActivity();
            }
        });

    }

    public void openNewActivity(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
