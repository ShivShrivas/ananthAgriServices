package com;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.ui.home.Portfolio;
import com.project.aas.ui.slideshow.Blogs;

public class AboutUs extends AppCompatActivity {

    ImageView back,folio;
    TextView backt,port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        port=findViewById(R.id.port);
        folio=findViewById(R.id.folio);
        port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUs.this, Portfolio.class));
            }
        });
        folio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUs.this, Portfolio.class));
            }
        });
        TextView textView = (TextView)findViewById(R.id.aboutmessage);

        StringBuilder stringBuilder = new StringBuilder();
        String someMessage = "ANANTH AGRI SERVICES is established with clear motto of helping indian farmers and agricultural related communities. Our company helps to access services like hiring drivers, machinery. Buying and selling of machinery and other agriculture services from seed to mouth and all in between.\n" +
                "\n" +
                "This online platform will help the individual farmers willing to provide their agriculture machineries and equipments on rental basis to increase their income.\n" +
                "\n" +
                "Our online platform connect FARMERS ,DEALERS ,TRADERS AND MANUFACTURERS,also helps people to access services like Business to Business, Customer to Customer and Business to Customer.\n" +
                "\n" +
                "Farmer can buy and sell their products like seeds,fruits, vegetables, crops and all other Agricultural Products and machineries across the country.\n" +
                "Dealers and manufacturers can sell their products like pesticides, fertilizers, insecticides ,machineries,agricultural implements ,sapling etc more quickly and effectively.\n" +
                "\n" +
                "ANANTH AGRI SERVICES is recognized as a start up by government of India and companies act. ";
        stringBuilder.append(someMessage);
        textView.setText(stringBuilder.toString());


        back=findViewById(R.id.backk);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUs.this, HomePage.class));
            }
        });
        backt=findViewById(R.id.backkk);
        backt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUs.this, HomePage.class));
            }
        });
    }
}