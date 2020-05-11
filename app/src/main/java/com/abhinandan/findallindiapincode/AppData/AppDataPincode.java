package com.abhinandan.findallindiapincode.AppData;

import com.abhinandan.findallindiapincode.Model.Pincode;

import java.util.ArrayList;

public class AppDataPincode {
    private static AppDataPincode instance;
    private ArrayList<Pincode> pincodes;




    public  static  AppDataPincode getInstance(){
        if(instance == null){
            instance = new AppDataPincode();
        }
        return instance;
    }

    AppDataPincode(){
        pincodes = new ArrayList<>();
    }


    public ArrayList<Pincode> getPincodes(){
        return pincodes;
    }
}
