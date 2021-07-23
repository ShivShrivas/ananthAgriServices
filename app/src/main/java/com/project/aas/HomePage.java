package com.project.aas;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.aas.adapter.AdRecyclerViewAdapter;
import com.project.aas.databinding.ActivityHomePageBinding;
import com.project.aas.model.AdPost;
import com.project.aas.model.UserProfile;
import com.project.aas.ui.AddAds;
import com.project.aas.ui.slideshow.Blogs;
import com.project.aas.ui.slideshow.ContactUs;
import com.project.aas.ui.slideshow.Feedback;
import com.project.aas.ui.slideshow.InternshipForm;
import com.project.aas.ui.slideshow.MyOrders;
import com.project.aas.ui.slideshow.Notifications;
import com.project.aas.ui.slideshow.PhoneNumber;
import com.project.aas.ui.slideshow.SavedAds;
import com.project.aas.ui.slideshow.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    ImageView notifications,profilePhoto;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView locationn;
    NavigationView navigationView;
    private int PERMISSION_ID = 44; 
    FloatingActionButton floatingActionButton;
    List<AdPost> adsList;
    private String TAG = "HomePage";
    RecyclerView adsRecyclerView;
    private FirebaseDatabase mDatabaseReference;
    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adsList = new ArrayList();
        mDatabaseReference = FirebaseDatabase.getInstance();

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mUserRef=FirebaseDatabase.getInstance().getReference("Users");

        profilePhoto=findViewById(R.id.profileNavigationImage);

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

        bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                final int id = item.getItemId();
                /*     case R.id.edit_profilebo:
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if(currentUser==null){
                            Toast.makeText(getApplicationContext(),"You need to Login first.",Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("showSignUpPage",true);
                            Intent intent = new Intent(HomePage.this, MainActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                       // }else {
                         //   startActivity(new Intent(HomePage.this, EditProfile.class));
                        }
                        break;*/
                    if(id==R.id.orders) {
                        startActivity(new Intent(HomePage.this,MyOrders.class));
                }
                    if(id==R.id.edit_profilebo){
                       // startActivity(new Intent(HomePage.this, EditProfile.class));
                    }
                    if(id==R.id.homePage){
                        startActivity(new Intent(HomePage.this,HomePage.class));
                    }
                    if(id==R.id.savedAds){
                        startActivity(new Intent(HomePage.this, SavedAds.class));
                    }
                return false;
            }
        });

        final ImageSlider imageSlider = findViewById(R.id.flipper);
        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.ss1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.ss2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.ss3, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels);
        imageSlider.startSliding(3000);
    }


    private void populateList() {
        fetchAds();
    }


    private void fetchAds() {
        DatabaseReference adsRef = mDatabaseReference.getReference("Ads");
        adsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i(TAG, "onDataChange: Found ad with id : "+snapshot.getKey());
                AdPost ad = snapshot.getValue(AdPost.class);
                mDatabaseReference.getReference("Users").child(ad.getPostedBy()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            UserProfile user = task.getResult().getValue(UserProfile.class);
                            // ad.setPostedBy(user.getName());
                            adsList.add(ad);
                            adsRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        if(id==R.id.Education){
            startActivity(new Intent(HomePage.this, Feedback.class));
        }
        if (id==R.id.privacy){
            openDialog1();
        }
        if(id==R.id.internshipform){
            startActivity(new Intent(HomePage.this, InternshipForm.class));
        }
        if(id == R.id.orders){
            startActivity(new Intent(HomePage.this, MyOrders.class));
        }
        return false;
    }

    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    private void openDialog1(){
        PrivacyDialog privacyDialog = new PrivacyDialog();
        privacyDialog.show(getSupportFragmentManager(),"dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (isLocationPermissionGranted()) {
            // Permissions are granted
            Log.i(TAG, "getLocation: Permissions in place");
            if(isLocationEnabled()) {
                Log.i(TAG, "getLocation: Location Enabled");
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    Geocoder geocoder = new Geocoder(HomePage.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(
                                                location.getLatitude(), location.getLongitude(), 1
                                        );
                                        Log.i(TAG, "onSuccess: Found Location");
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
            } else {
                // Prompt user to turn on location
                Toast.makeText(this, "Please turn on" + " your location.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }else {
            // Else ask for permissions
            Log.i(TAG, "getLocation: Permissions required");
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
        }
    }

    private boolean isLocationPermissionGranted() {
        // Returns true if permissions are allowed
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}