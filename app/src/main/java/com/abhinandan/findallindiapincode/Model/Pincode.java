package com.abhinandan.findallindiapincode.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pincode implements Parcelable {
    String name;
    String branchType;
    String deliveryStatus;
    String circle;
    String district;
    String division;
    String region;
    String state;
    String country;
    String block;
    String pincode;

    public Pincode(String name, String branchType, String deliveryStatus, String circle, String district, String division, String region, String state, String country, String block, String pincode) {
        this.name = name;
        this.branchType = branchType;
        this.deliveryStatus = deliveryStatus;
        this.circle = circle;
        this.district = district;
        this.division = division;
        this.region = region;
        this.state = state;
        this.country = country;
        this.block = block;
        this.pincode = pincode;
    }

    protected Pincode(Parcel in) {
        name = in.readString();
        deliveryStatus = in.readString();
        circle = in.readString();
        district = in.readString();
        division = in.readString();
        region = in.readString();
        state = in.readString();
        country = in.readString();
        block = in.readString();
        pincode = in.readString();
        branchType = in.readString();
    }

    public static final Creator<Pincode> CREATOR = new Creator<Pincode>() {
        @Override
        public Pincode createFromParcel(Parcel in) {
            return new Pincode(in);
        }

        @Override
        public Pincode[] newArray(int size) {
            return new Pincode[size];
        }
    };

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(deliveryStatus);
        dest.writeString(circle);
        dest.writeString(district);
        dest.writeString(division);
        dest.writeString(region);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(block);
        dest.writeString(pincode);
    }
}
