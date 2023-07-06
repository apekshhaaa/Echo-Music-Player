package com.project.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Handler handler;
    EditText em,pw;
    String e="applehead@gmail.com";
    String p="orangeoverapples";
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();
        handler = new Handler();
        Intent intent = getIntent();
         if (intent != null) {
         String data = intent.getStringExtra("keyName");
        // Do something with the data
        }
        em=findViewById(R.id.email);
        pw=findViewById(R.id.password);
        b=findViewById(R.id.login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String re=em.getText().toString();
                String rp=pw.getText().toString();
                boolean b=android.util.Patterns.EMAIL_ADDRESS.matcher(re).matches();
                if (!b)
                {
                    Toast.makeText(LoginActivity.this, "invalid email", Toast.LENGTH_SHORT).show();}
                else{
                    if(re.equals(e) && rp.equals(p)){
                        //Toast.makeText(LoginActivity.this, "login succesful", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "email and password mismatch", Toast.LENGTH_SHORT).show();
                    }}
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Start the next activity or perform any other action
                        Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                        startActivity(intent);
                        finish(); // Optional: Close the login activity
                    }
                }, 2000);
            }
        });

    }
    @Override
    protected void onDestroy() {
        // Remove any pending callbacks to prevent memory leaks
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}