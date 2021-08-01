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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AboutUs;
import com.bumptech.glide.Glide;
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
import com.google.firebase.database.ValueEventListener;
import com.project.aas.adapter.AdRecyclerViewAdapter;
import com.project.aas.databinding.ActivityHomePageBinding;
import com.project.aas.model.AdPost;
import com.project.aas.model.UserProfile;
import com.project.aas.ui.AddAds;
import com.project.aas.ui.EditProfile;
import com.project.aas.ui.Fertilizers;
import com.project.aas.ui.Seeds;
import com.project.aas.ui.Tractor;
import com.project.aas.ui.home.Weather;
import com.project.aas.ui.slideshow.Blogs;
import com.project.aas.ui.slideshow.ContactUs;
import com.project.aas.ui.slideshow.Feedback;
import com.project.aas.ui.slideshow.InternshipForm;
import com.project.aas.ui.slideshow.MyOrders;
import com.project.aas.ui.slideshow.Notifications;
import com.project.aas.ui.slideshow.PhoneNumber;
import com.project.aas.ui.slideshow.SavedAds;
import com.project.aas.ui.slideshow.Settings;
import com.project.aas.ui.slideshow.UserDetailsIndividual;

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
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    ImageView notifications;
    FusedLocationProviderClient fusedLocationProviderClient;
    private long backPressedTime;
    TextView locationn,name,currentLocation;
    NavigationView navigationView;
    String profileImageUrlV,usernameUrlV;
    ImageView profileImageViewHeader;
    TextView LocationHeader;
    private int PERMISSION_ID = 44; 
    FloatingActionButton floatingActionButton;
    List<AdPost> adsList;
    private String TAG = "HomePage";
    RecyclerView adsRecyclerView;
    private FirebaseDatabase mDatabaseReference;
    DatabaseReference mUserRef,mImageRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ImageButton tractor,fertilizers,seeds;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LocationHeader=findViewById(R.id.Currentlocation);

        adsList = new ArrayList();
        mDatabaseReference = FirebaseDatabase.getInstance();

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mUserRef=FirebaseDatabase.getInstance().getReference("Users");

        navigationView=findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        TextView nameUser= headerView.findViewById(R.id.UserNameaa);
        CircleImageView imageUser=headerView.findViewById(R.id.profileNavigationImage);
        mUserRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot datasnapshot) {
                    profileImageUrlV=datasnapshot.child("UserImage").getValue().toString();
                    String s =  datasnapshot.child("userName").getValue(String.class);
                    Glide.with(HomePage.this).load(profileImageUrlV).into(imageUser);
                    nameUser.setText(s);
                    String l = datasnapshot.child("location").getValue(String.class);
                    LocationHeader.setText(l);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(HomePage.this, "Error in loading data", Toast.LENGTH_SHORT).show();

            }
        });


        tractor=findViewById(R.id.machine);
        fertilizers=findViewById(R.id.fertilizers);
        seeds=findViewById(R.id.plants);

        tractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, Tractor.class));
            }
        });
        fertilizers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, Fertilizers.class));
            }
        });
        seeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this, Seeds.class));
            }
        });

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

       // locationn = findViewById(R.id.location);
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
                    if(id==R.id.orders) {
                        startActivity(new Intent(HomePage.this,MyOrders.class));
                }
                    if(id==R.id.edit_profilebo){
                        startActivity(new Intent(HomePage.this, EditProfile.class));
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
                            //ad.setPostedBy(user.getName());
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
        if(id==R.id.weather){
            startActivity(new Intent(HomePage.this, Weather.class));
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

    @Override
    public void onBackPressed() {
        if(backPressedTime+2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(HomePage.this,"Press back again to exit",Toast.LENGTH_SHORT).show();
        }


        backPressedTime=System.currentTimeMillis();

    }


}