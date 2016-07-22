package com.codepath.example.lockscreendemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codepath.example.lockscreendemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_close_app_and_go_joke = (Button) findViewById(R.id.button_close_app_and_go_joke);
        button_close_app_and_go_joke.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent service = new Intent(getApplicationContext(), JokeService.class);
                startService(service); // Lancement du service
                finish(); // Fermeture de l'application


            }
        });
    }
}