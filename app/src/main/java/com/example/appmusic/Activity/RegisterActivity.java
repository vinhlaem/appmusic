package com.example.appmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appmusic.R;

import java.util.jar.Attributes;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText Name, Gmail, Password;
    Button btnRegister;
    final String url_Register = "https://vinhmp3zing.000webhostapp.com/Server/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = (EditText) findViewById(R.id.name);
        Gmail = (EditText) findViewById(R.id.reggmail);
        Password = (EditText) findViewById(R.id.regpassword);
        btnRegister = (Button) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String gmail = Gmail.getText().toString();
                String password = Password.getText().toString();
                new RegisterUser().execute(name,gmail,password);
            }
        });
    }


    public class RegisterUser extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String Name = strings[0];
            String Gmail = strings[1];
            String Passwword = strings[2];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("name",Name)
                    .add("gmail",Gmail)
                    .add("password",Passwword)
                    .build();
            Request request = new Request.Builder()
                    .url(url_Register)
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String result = response.body().string();
                    if(result.equalsIgnoreCase("User registered successfully")){
                        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
                    }else if(result.equalsIgnoreCase("User already exists")) {
                        Toast.makeText(RegisterActivity.this,"Gmail already", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(RegisterActivity.this,"oop! please try again!", Toast.LENGTH_LONG).show();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
