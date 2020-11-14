package com.shivinc.downtube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.zip.Inflater;

public class Donation extends AppCompatActivity {

    private Button bttOne;
    private Button bttTwo;
    private Button bttTen;
    private Button bttFifty;
    private Button paypal;
    private ImageView back;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        ini();
        inibuttons();

    }
    private void ini() {
        bttOne=findViewById(R.id.oneCoffee);
        bttTwo=findViewById(R.id.twoCoffee);
        bttTen=findViewById(R.id.threeCoffee);
        bttFifty=findViewById(R.id.fourCoffee);
        paypal=findViewById(R.id.fiveCoffee);
        back=findViewById(R.id.bttBack);
    }

    private void inibuttons(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bttOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_VIEW , Uri.parse("https://paytm.me/R-LMB7G"));
                startActivity(intent);

            }
        });
        bttTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_VIEW , Uri.parse("https://paytm.me/Q-waoJx"));
                startActivity(intent);

            }
        });
        bttTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_VIEW ,Uri.parse("https://paytm.me/Bc-nTr8"));
                startActivity(intent);

            }
        });
        bttFifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_VIEW , Uri.parse("https://paytm.me/5-TCqjP"));
                startActivity(intent);

            }
        });
        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(Intent.ACTION_VIEW , Uri.parse("https://www.paypal.me/shivshacks"));
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}