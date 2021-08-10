package com.example.appmusic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmusic.R;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    EditText Gmail, Password ;
    TextView txtDangki;
    Button Btnlogin;
    final String url_Login = "https://vinhmp3zing.000webhostapp.com/Server/login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Gmail = (EditText) findViewById(R.id.gmail);
        Password = (EditText) findViewById(R.id.password);
        Btnlogin = (Button) findViewById(R.id.btn_login);
        txtDangki = (TextView) findViewById(R.id.dangki);


        Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmail = Gmail.getText().toString();
                String password = Password.getText().toString();
                new LoginUser().execute(gmail,password);
            }
        });

        txtDangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    public class LoginUser extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String gmail = strings[0];
            String password = strings[1];
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("gmail",gmail)
                    .add("password",password)
                    .build();
            Request request = new Request.Builder()
                    .url(url_Login)
                    .post(formBody)
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String result = response.body().string();
                    if(result.equalsIgnoreCase("")){
                        Toast.makeText(LoginActivity.this,"Gmail và Mặt khẩu sai", Toast.LENGTH_LONG).show();
                    }else {
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username",result);
                        editor.apply();
                        startActivity(i);
                        finish();
                        //Toast.makeText(LoginActivity.this,"Gmail và Mặt khẩu sai", Toast.LENGTH_LONG).show();

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
