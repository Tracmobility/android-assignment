package com.example.trac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    EditText editText, editText3, editText4;
    Button button;
    ImageView img_fb, img_g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        img_fb = findViewById(R.id.imageViewfb);
        img_g = findViewById(R.id.imageViewg);
        button = findViewById(R.id.button);
        editText4 = findViewById(R.id.editText4);
        editText = findViewById(R.id.editText);
        editText3 = findViewById(R.id.editText3);

        img_fb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               FbIsClicked();
            }
        });

        img_g.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GoogleIsClicked();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TheButtonIsClicked();
            }
        });
    }

    public void FbIsClicked(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.facebook.com/"));
        startActivity(intent);
    }

    public void GoogleIsClicked(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("https://www.google.com/"));
        startActivity(intent);
    }

    public void TheButtonIsClicked(){

        if(editText4.getText().length()==0) {
            editText4.setError("Error");
        }
        else if(editText3.getText().length()==0) {
            editText3.setError("Error");
        }
        else if(editText.getText().length()==0) {
            editText.setError("Error");
        }
        else {
            //String str = editText4.getText().toString();
            Intent intent = new Intent(this, MainPage.class);
            //intent.putExtra("username", str);
            startActivity(intent);
        }
    }
}
