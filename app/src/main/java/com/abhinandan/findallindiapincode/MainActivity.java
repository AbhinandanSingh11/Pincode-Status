package com.abhinandan.findallindiapincode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinandan.findallindiapincode.Adapter.AdapterRecyclerViewLocation;
import com.abhinandan.findallindiapincode.AppData.AppDataPincode;
import com.abhinandan.findallindiapincode.Model.Pincode;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ligl.android.widget.iosdialog.IOSDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private ImageView close;
    private TextView textDelivery, textLocation, textGetLocation;
    private TextView NamePlaceHolder, PINCodePlaceHolder, BranchTypePlaceHolder, DeliveryStatusPlaceHoler, CirclePlaceHolder, DistrictPlaceHolder, DivisionPlaceHolder, BlockPlaceHolder, RegionPlaceHolder, StatePlaceHolder, CountryPlaceHolder;
    private TextView Name, Pincode, Branch, Delivery,Circle, District, Division, Block, Region, State, Country;
    private EditText searchBar;
    private LinearLayout gps;
    private Button check;
   // private CardView overlaycard;
    private ArrayList<Pincode> pincodes = new ArrayList<>();
    private RequestQueue requestQueue;
    private JsonArrayRequest jsonArrayRequest;
    private String searchQuery;
    //private AdapterRecyclerViewLocation adapter;
    private String URL;
    //private RecyclerView recyclerView;
    private final String API = "https://api.postalpincode.in/pincode/";
    private ProgressDialog progressdialog;
    private FrameLayout overlay;
    private AlphaAnimation inAnimation, outAnimation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.pink));


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        progressdialog = new ProgressDialog(MainActivity.this, R.style.CustomDialog);
        progressdialog.setMessage("Searching....");
        progressdialog.setCanceledOnTouchOutside(false);
        progressdialog.create();


        Name = findViewById(R.id.textName);
        Pincode = findViewById(R.id.textPINCode);
        Branch = findViewById(R.id.textBranchType);
        Delivery = findViewById(R.id.textDeliveryStatus);
        Circle = findViewById(R.id.textCircle);
        District = findViewById(R.id.textDistrict);
        Division = findViewById(R.id.textDivision);
        Block = findViewById(R.id.textBlock);
        Region = findViewById(R.id.textRegion);
        State = findViewById(R.id.textState);
        Country = findViewById(R.id.textCountry);


        NamePlaceHolder = findViewById(R.id.textNamePlaceHolder);
        PINCodePlaceHolder = findViewById(R.id.textPINcodePlaceHolder);
        BranchTypePlaceHolder = findViewById(R.id.textBranchTypePlaceHolder);
        DeliveryStatusPlaceHoler = findViewById(R.id.textDeliveryStatusPlaceHolder);
        CirclePlaceHolder = findViewById(R.id.textCirclePlaceHolder);
        DistrictPlaceHolder = findViewById(R.id.textDistrictPlaceHolder);
        DivisionPlaceHolder = findViewById(R.id.textDivisionPlaceHolder);
        BlockPlaceHolder = findViewById(R.id.textBlockPlaceHolder);
        RegionPlaceHolder = findViewById(R.id.textRegionPlaceHolder);
        StatePlaceHolder = findViewById(R.id.textStatePlaceHolder);
        CountryPlaceHolder = findViewById(R.id.textCountryPlaceHolder);


        close = findViewById(R.id.closeLocation);
        textDelivery = findViewById(R.id.textDelivery);
        textLocation = findViewById(R.id.textLocation);
        textGetLocation = findViewById(R.id.textSelectLoaction);
        check = findViewById(R.id.check);
        searchBar = findViewById(R.id.pincode);
        //recyclerView = findViewById(R.id.ConsL);
        overlay = findViewById(R.id.progressOverlay);
        gps = findViewById(R.id.TextBarLocation);
        //overlaycard = findViewById(R.id.overlaycard);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "clicked!!", Toast.LENGTH_SHORT).show();
               new IOSDialog.Builder(MainActivity.this)
                       .setTitle("Alert!")
                       .setMessage("Do you really want to exit")
                       .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                           }
                       }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       finish();
                   }
               }).setCancelable(false)
                       .show();
            }
        });


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(MainActivity.this);

                searchBar.setText(null);

                Name.setVisibility(View.GONE);
                Pincode.setVisibility(View.GONE);
                Branch.setVisibility(View.GONE);
                Delivery.setVisibility(View.GONE);
                Circle.setVisibility(View.GONE);
                District.setVisibility(View.GONE);
                Division.setVisibility(View.GONE);
                Region.setVisibility(View.GONE);
                Block.setVisibility(View.GONE);
                State.setVisibility(View.GONE);
                Country.setVisibility(View.GONE);

                NamePlaceHolder.setVisibility(View.GONE);
                PINCodePlaceHolder.setVisibility(View.GONE);
                BranchTypePlaceHolder.setVisibility(View.GONE);
                DeliveryStatusPlaceHoler.setVisibility(View.GONE);
                CirclePlaceHolder.setVisibility(View.GONE);
                DistrictPlaceHolder.setVisibility(View.GONE);
                DivisionPlaceHolder.setVisibility(View.GONE);
                RegionPlaceHolder.setVisibility(View.GONE);
                BlockPlaceHolder.setVisibility(View.GONE);
                StatePlaceHolder.setVisibility(View.GONE);
                CountryPlaceHolder.setVisibility(View.GONE);


                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                overlay.setAnimation(inAnimation);
                overlay.setVisibility(View.VISIBLE);
                checkLocationPermission();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(count==5){
                    Toast.makeText(MainActivity.this, "completed", Toast.LENGTH_SHORT).show();
                    //progressdialog.show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                //if(s.length() == 6){
                //  if(!progressdialog.isShowing()){
                //    progressdialog.show();
                //  }
                // }

                //       if (s.length() != 0) {
                //         searchQuery = s.toString().trim();
                //       fetch(searchQuery);
                //     requestQueue.add(jsonArrayRequest);
                //   loadRecyclerView(recyclerView);
                //  }

                if(s.length()<6){
                    Name.setVisibility(View.GONE);
                    Pincode.setVisibility(View.GONE);
                    Branch.setVisibility(View.GONE);
                    Delivery.setVisibility(View.GONE);
                    Circle.setVisibility(View.GONE);
                    District.setVisibility(View.GONE);
                    Division.setVisibility(View.GONE);
                    Region.setVisibility(View.GONE);
                    Block.setVisibility(View.GONE);
                    State.setVisibility(View.GONE);
                    Country.setVisibility(View.GONE);

                    NamePlaceHolder.setVisibility(View.GONE);
                    PINCodePlaceHolder.setVisibility(View.GONE);
                    BranchTypePlaceHolder.setVisibility(View.GONE);
                    DeliveryStatusPlaceHoler.setVisibility(View.GONE);
                    CirclePlaceHolder.setVisibility(View.GONE);
                    DistrictPlaceHolder.setVisibility(View.GONE);
                    DivisionPlaceHolder.setVisibility(View.GONE);
                    RegionPlaceHolder.setVisibility(View.GONE);
                    BlockPlaceHolder.setVisibility(View.GONE);
                    StatePlaceHolder.setVisibility(View.GONE);
                    CountryPlaceHolder.setVisibility(View.GONE);
                }


            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(MainActivity.this);

                searchQuery = searchBar.getText().toString().trim();

                if(searchQuery.isEmpty() || searchQuery.length()<6){
                    Toast.makeText(MainActivity.this, "Invalid Postal Code", Toast.LENGTH_SHORT).show();
                }
                else{
                    //progressdialog.show();
                    //fetch(searchQuery);
                    Name.setVisibility(View.GONE);
                    Pincode.setVisibility(View.GONE);
                    Branch.setVisibility(View.GONE);
                    Delivery.setVisibility(View.GONE);
                    Circle.setVisibility(View.GONE);
                    District.setVisibility(View.GONE);
                    Division.setVisibility(View.GONE);
                    Region.setVisibility(View.GONE);
                    Block.setVisibility(View.GONE);
                    State.setVisibility(View.GONE);
                    Country.setVisibility(View.GONE);

                    NamePlaceHolder.setVisibility(View.GONE);
                    PINCodePlaceHolder.setVisibility(View.GONE);
                    BranchTypePlaceHolder.setVisibility(View.GONE);
                    DeliveryStatusPlaceHoler.setVisibility(View.GONE);
                    CirclePlaceHolder.setVisibility(View.GONE);
                    DistrictPlaceHolder.setVisibility(View.GONE);
                    DivisionPlaceHolder.setVisibility(View.GONE);
                    RegionPlaceHolder.setVisibility(View.GONE);
                    BlockPlaceHolder.setVisibility(View.GONE);
                    StatePlaceHolder.setVisibility(View.GONE);
                    CountryPlaceHolder.setVisibility(View.GONE);
                    new Search().execute(searchQuery);
                    //requestQueue.add(jsonArrayRequest);
                    //loadRecyclerView(recyclerView);
                }

            }
        });


        Typeface regular = ResourcesCompat.getFont(getApplicationContext(),R.font.mregular);
        Typeface medium = ResourcesCompat.getFont(getApplicationContext(),R.font.mmedium);
        Typeface light = ResourcesCompat.getFont(getApplicationContext(),R.font.mlight);


        textDelivery.setTypeface(regular);
        textGetLocation.setTypeface(regular);
        textLocation.setTypeface(medium);
        searchBar.setTypeface(regular);
        check.setTypeface(medium);


        Name.setTypeface(medium);
        Pincode.setTypeface(medium);
        Branch.setTypeface(medium);
        Delivery.setTypeface(medium);
        Circle.setTypeface(medium);
        District.setTypeface(medium);
        Division.setTypeface(medium);
        Region.setTypeface(medium);
        Block.setTypeface(medium);
        State.setTypeface(medium);
        Country.setTypeface(medium);

        NamePlaceHolder.setTypeface(medium);
        PINCodePlaceHolder.setTypeface(medium);
        BranchTypePlaceHolder.setTypeface(medium);
        DeliveryStatusPlaceHoler.setTypeface(medium);
        CirclePlaceHolder.setTypeface(medium);
        DistrictPlaceHolder.setTypeface(medium);
        DivisionPlaceHolder.setTypeface(medium);
        RegionPlaceHolder.setTypeface(medium);
        BlockPlaceHolder.setTypeface(medium);
        StatePlaceHolder.setTypeface(medium);
        CountryPlaceHolder.setTypeface(medium);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }

                } else {
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    overlay.setAnimation(outAnimation);
                    overlay.setVisibility(View.GONE);
                    Toast.makeText(this, "Fuck you!!", Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

    void fetch(String query){
        URL = null;
        URL = API+query;

        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                AppDataPincode.getInstance().getPincodes().clear();
                pincodes.clear();

                try {
                    JSONObject object = response.getJSONObject(0);
                    String status = object.getString("Status");


                    if(status.equals("Success")){
                        JSONArray array = object.getJSONArray("PostOffice");
                        // for(int i=0; i<array.length(); i++){
                        // JSONObject postoffice = array.getJSONObject(i);
                        // String name = postoffice.getString("Name");
                        // String deliveryStatus = postoffice.getString("DeliveryStatus");
                        //
                        // String circle = postoffice.getString("Circle");
                        // String district = postoffice.getString("District");
                        // String division = postoffice.getString("Division");
                        //String region = postoffice.getString("Region");
                        // String state  = postoffice.getString("State");
                        // String country = postoffice.getString("Country");
                        // String block = postoffice.getString("Block");
                        // String pincode = postoffice.getString("Pincode");

                        //    AppDataPincode.getInstance().getPincodes().add(new Pincode(name,deliveryStatus,circle,district,division,region,state,country,block,pincode));
                        // }


                        JSONObject postoffice = array.getJSONObject(0);
                        String name = postoffice.getString("Name");
                        String deliveryStatus = postoffice.getString("DeliveryStatus");
                        String circle = postoffice.getString("Circle");
                        String district = postoffice.getString("District");
                        String division = postoffice.getString("Division");
                        String region = postoffice.getString("Region");
                        String state  = postoffice.getString("State");
                        String country = postoffice.getString("Country");
                        String block = postoffice.getString("Block");
                        String pincode = postoffice.getString("Pincode");
                        String branch = postoffice.getString("BranchType");

                        //AppDataPincode.getInstance().getPincodes().add(new Pincode(name,deliveryStatus,circle,district,division,region,state,country,block,pincode));


                        Name.setVisibility(View.VISIBLE);
                        Pincode.setVisibility(View.VISIBLE);
                        Branch.setVisibility(View.VISIBLE);
                        Delivery.setVisibility(View.VISIBLE);
                        Circle.setVisibility(View.VISIBLE);
                        District.setVisibility(View.VISIBLE);
                        Division.setVisibility(View.VISIBLE);
                        Region.setVisibility(View.VISIBLE);
                        Block.setVisibility(View.VISIBLE);
                        State.setVisibility(View.VISIBLE);
                        Country.setVisibility(View.VISIBLE);

                        NamePlaceHolder.setVisibility(View.VISIBLE);
                        PINCodePlaceHolder.setVisibility(View.VISIBLE);
                        BranchTypePlaceHolder.setVisibility(View.VISIBLE);
                        DeliveryStatusPlaceHoler.setVisibility(View.VISIBLE);
                        CirclePlaceHolder.setVisibility(View.VISIBLE);
                        DistrictPlaceHolder.setVisibility(View.VISIBLE);
                        DivisionPlaceHolder.setVisibility(View.VISIBLE);
                        RegionPlaceHolder.setVisibility(View.VISIBLE);
                        BlockPlaceHolder.setVisibility(View.VISIBLE);
                        StatePlaceHolder.setVisibility(View.VISIBLE);
                        CountryPlaceHolder.setVisibility(View.VISIBLE);


                        Name.setText(name);
                        Pincode.setText(pincode);
                        Branch.setText(branch);
                        Delivery.setText(deliveryStatus);
                        Circle.setText(circle);
                        District.setText(district);
                        Division.setText(division);
                        Region.setText(region);
                        Block.setText(block);
                        State.setText(state);
                        Country.setText(country);


                       // pincodes.addAll(AppDataPincode.getInstance().getPincodes());

                      //  pincodes.get(0)

                        Context context = MainActivity.this;
                        //SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.SF),Context.MODE_PRIVATE);
                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                        //editor.putString(getString(R.string.location),pincodes.get(0).getDistrict());
                        //editor.putString(getString(R.string.pincode),pincodes.get(0).getPincode());
                        //editor.commit();
                        Toast.makeText(context, "Pincode Verified successfully", Toast.LENGTH_SHORT).show();




                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            overlay.setAnimation(outAnimation);
                            overlay.setVisibility(View.GONE);
                            //overlaycard.setVisibility(View.GONE);




                    }

                    else{
                        Toast.makeText(MainActivity.this, "Invalid Postal Code", Toast.LENGTH_SHORT).show();
                        //.dismiss();
                    }

                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    overlay.setAnimation(outAnimation);
                    overlay.setVisibility(View.GONE);

                    //pincodes.addAll(AppDataPincode.getInstance().getPincodes());
                    //adapter.notifyDataSetChanged();
                    //progressdialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    //progressdialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                overlay.setAnimation(outAnimation);
                overlay.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error: "+error, Toast.LENGTH_SHORT).show();
                //progressdialog.dismiss();
            }
        });

    }

    private class Search extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            overlay.setAnimation(inAnimation);
            overlay.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected Void doInBackground(String... urls) {
            fetch(urls[0]);
            requestQueue.add(jsonArrayRequest);
            return null;
        }
    }



    void loadRecyclerView(RecyclerView recyclerView){
  //      adapter = new AdapterRecyclerViewLocation(pincodes,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
    //    recyclerView.setAdapter(adapter);
    }

    public static void hideKeyboard(Activity activity){
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        inputManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public void checkLocationPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    void getLocation(){
        if(isLocationEnabled()){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if(location != null){

                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        List<Address> addresses = null;

                        if(lat != 0&& lon!= 0){
                            try {
                                final Geocoder geocoder = new Geocoder(MainActivity.this);
                                addresses = geocoder.getFromLocation(lat,lon,1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if(addresses != null) {
                                for (Address address : addresses) {
                                    if (address.getLocality() != null && address.getPostalCode() != null) {
                                        fetch(address.getPostalCode());
                                        requestQueue.add(jsonArrayRequest);
                                    }
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Trouble Finding address, please enter your pincode manually", Toast.LENGTH_SHORT).show();
                                outAnimation = new AlphaAnimation(1f, 0f);
                                outAnimation.setDuration(200);
                                overlay.setAnimation(outAnimation);
                                overlay.setVisibility(View.GONE);
                            }

                        }

                        else{
                            Toast.makeText(MainActivity.this, "Trouble Finding address, please enter your pincode manually", Toast.LENGTH_SHORT).show();
                            outAnimation = new AlphaAnimation(1f, 0f);
                            outAnimation.setDuration(200);
                            overlay.setAnimation(outAnimation);
                            overlay.setVisibility(View.GONE);}
                    }

                    else {
                        Toast.makeText(MainActivity.this, "location not Found, Please try again", Toast.LENGTH_SHORT).show();
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        overlay.setAnimation(outAnimation);
                        overlay.setVisibility(View.GONE);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Failed!! "+ e, Toast.LENGTH_SHORT).show();
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    overlay.setAnimation(outAnimation);
                    overlay.setVisibility(View.GONE);
                }
            });
        }
        else{
            Toast.makeText(this, "Please turn on your location", Toast.LENGTH_SHORT).show();
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            overlay.setAnimation(outAnimation);
            overlay.setVisibility(View.GONE);
        }
    }

    public boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    protected void onStop() {
        super.onStop();

    }
}
