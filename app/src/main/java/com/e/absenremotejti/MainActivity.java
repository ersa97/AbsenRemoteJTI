package com.e.absenremotejti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();

    private boolean doubleBackPressed;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doubleBackPressed = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackPressed){
            super.onBackPressed();
            return;
        }

        this.doubleBackPressed = true;
        Toast.makeText(this, "press again to exit app", Toast.LENGTH_SHORT).show();
        handler.postDelayed(runnable, 2000);
    }
}
