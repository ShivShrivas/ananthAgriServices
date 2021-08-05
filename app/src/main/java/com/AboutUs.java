package com;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.ui.home.Portfolio;


public class AboutUs extends AppCompatActivity {

    ImageView back, folio;
    TextView backt, port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        port = findViewById(R.id.port);
        folio = findViewById(R.id.folio);
        port.setOnClickListener(v -> startActivity(new Intent(AboutUs.this, Portfolio.class)));
        folio.setOnClickListener(v -> startActivity(new Intent(AboutUs.this, Portfolio.class)));
        TextView textView = findViewById(R.id.aboutmessage);

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
        textView.setText(someMessage);


        back = findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(AboutUs.this, HomePage.class)));
        backt = findViewById(R.id.backkk);
        backt.setOnClickListener(v -> startActivity(new Intent(AboutUs.this, HomePage.class)));
    }
}