package com.project.aas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AboutUs;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.project.aas.adapter.AdRecyclerViewAdapter;
import com.project.aas.databinding.ActivityHomePageBinding;
import com.project.aas.model.Ad;
import com.project.aas.ui.AddAds;
import com.project.aas.ui.EditProfile;
import com.project.aas.ui.gallery.GalleryFragment;
import com.project.aas.ui.slideshow.Blogs;
import com.project.aas.ui.slideshow.ContactUs;
import com.project.aas.ui.slideshow.Notifications;
import com.project.aas.ui.slideshow.Settings;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    ImageView notifications;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView locationn;
    NavigationView navigationView;
    private int PERMISSION_ID = 44;
    FloatingActionButton floatingActionButton;
    List<Ad> adsList;
    RecyclerView adsRecyclerView;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adsList = new ArrayList();


        setSupportActionBar(binding.appBarHomePage.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        floatingActionButton = findViewById(R.id.add_ads);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, AddAds.class);
                startActivity(intent);
            }
        });

        initRecyclerView();

        notifications=findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, Notifications.class));
            }
        });

        locationn = findViewById(R.id.location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav, R.id.o, R.id.info, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        // NavigationUI.setupWithNavController(navigationView, navController);

        bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_profilebo:
                        Intent intent1 = new Intent(HomePage.this, EditProfile.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });

        final ImageSlider imageSlider = findViewById(R.id.flipper);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.tra, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.agr, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.fer, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels);
        imageSlider.startSliding(3000);
    }

    private void populateList() {
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));

        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));

        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));

        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));
        adsList.add(new Ad("id","Xyz Transport","8 hours ago","Andhra Pradesh","AnanthAgriServices",
                "Rs. 1000 per KG","+918989898989",
                "https://ananthagriservices.in/wp-content/uploads/classified-listing/2021/04/IMG_20210314_123034.jpg"));

        adsRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void initRecyclerView() {
        adsRecyclerView = findViewById(R.id.ad_recycler_view);
        RecyclerView.LayoutManager adsLayoutManager = new GridLayoutManager(this,2);
        adsRecyclerView.setHasFixedSize(true);
        adsRecyclerView.setNestedScrollingEnabled(false);
        adsRecyclerView.setLayoutManager(adsLayoutManager);
        AdRecyclerViewAdapter adapter = new AdRecyclerViewAdapter(adsList,this);
        adsRecyclerView.setAdapter(adapter);
        populateList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        final int id = item.getItemId();
        if (id == R.id.blogs) {
            Intent intent2 = new Intent(HomePage.this, Blogs.class);
            startActivity(intent2);
        }
        if (id == R.id.about_usMessage) {
            startActivity(new Intent(HomePage.this, AboutUs.class));
        }
        if (id == R.id.logout) {
            openDialog();
        }
        if (id == R.id.contact) {
            startActivity(new Intent(HomePage.this, ContactUs.class));
        }
        if (id == R.id.settings) {
            startActivity(new Intent(HomePage.this, Settings.class));
        }
        return false;
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(HomePage.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        locationn.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Locality : </b><br></font>"+
                                        addresses.get(0).getLocality()
                        ));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}