package com.example.houserentproject;

import java.io.Serializable;

public class HomePageDataModel implements Serializable {
    private String image;
    private String rentAmount;
    private String location;
    private String buildingName;
    private String floorNumber;
    private String detailsAboutHostel;
    private String valueOfGender;
    private String valueOfRentType;
    private String valueOfRentCount;
    private String datePick;
    private String adUserId;
    private String id;
    private String postStatus;
    private double hostelLat;
    private double hostelLon;
    private String electricityBill;
    private String gasBill;
    private String wifiBill;
    private String othersBill;
    private String security;
    private String parking;
    private String generator;
    private String elevator;
    private String totalRoom;
    private String flatSize;
    private String totalWashroom;
    private String totalBalcony;

    public HomePageDataModel() {
    }

    public HomePageDataModel(String image, String rentAmount, String location, String buildingName, String floorNumber, String detailsAboutHostel, String valueOfGender, String valueOfRentType, String valueOfRentCount, String datePick, String adUserId, String id, String postStatus, double hostelLat, double hostelLon, String electricityBill, String gasBill, String wifiBill, String othersBill, String security, String parking, String generator, String elevator, String totalRoom, String flatSize, String totalWashroom, String totalBalcony) {
        this.image = image;
        this.rentAmount = rentAmount;
        this.location = location;
        this.buildingName = buildingName;
        this.floorNumber = floorNumber;
        this.detailsAboutHostel = detailsAboutHostel;
        this.valueOfGender = valueOfGender;
        this.valueOfRentType = valueOfRentType;
        this.valueOfRentCount = valueOfRentCount;
        this.datePick = datePick;
        this.adUserId = adUserId;
        this.id = id;
        this.postStatus = postStatus;
        this.hostelLat = hostelLat;
        this.hostelLon = hostelLon;
        this.electricityBill = electricityBill;
        this.gasBill = gasBill;
        this.wifiBill = wifiBill;
        this.othersBill = othersBill;
        this.security = security;
        this.parking = parking;
        this.generator = generator;
        this.elevator = elevator;
        this.totalRoom = totalRoom;
        this.flatSize = flatSize;
        this.totalWashroom = totalWashroom;
        this.totalBalcony = totalBalcony;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(String rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getDetailsAboutHostel() {
        return detailsAboutHostel;
    }

    public void setDetailsAboutHostel(String detailsAboutHostel) {
        this.detailsAboutHostel = detailsAboutHostel;
    }

    public String getValueOfGender() {
        return valueOfGender;
    }

    public void setValueOfGender(String valueOfGender) {
        this.valueOfGender = valueOfGender;
    }

    public String getValueOfRentType() {
        return valueOfRentType;
    }

    public void setValueOfRentType(String valueOfRentType) {
        this.valueOfRentType = valueOfRentType;
    }

    public String getValueOfRentCount() {
        return valueOfRentCount;
    }

    public void setValueOfRentCount(String valueOfRentCount) {
        this.valueOfRentCount = valueOfRentCount;
    }

    public String getDatePick() {
        return datePick;
    }

    public void setDatePick(String datePick) {
        this.datePick = datePick;
    }

    public String getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(String adUserId) {
        this.adUserId = adUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public double getHostelLat() {
        return hostelLat;
    }

    public void setHostelLat(double hostelLat) {
        this.hostelLat = hostelLat;
    }

    public double getHostelLon() {
        return hostelLon;
    }

    public void setHostelLon(double hostelLon) {
        this.hostelLon = hostelLon;
    }

    public String getElectricityBill() {
        return electricityBill;
    }

    public void setElectricityBill(String electricityBill) {
        this.electricityBill = electricityBill;
    }

    public String getGasBill() {
        return gasBill;
    }

    public void setGasBill(String gasBill) {
        this.gasBill = gasBill;
    }

    public String getWifiBill() {
        return wifiBill;
    }

    public void setWifiBill(String wifiBill) {
        this.wifiBill = wifiBill;
    }

    public String getOthersBill() {
        return othersBill;
    }

    public void setOthersBill(String othersBill) {
        this.othersBill = othersBill;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getElevator() {
        return elevator;
    }

    public void setElevator(String elevator) {
        this.elevator = elevator;
    }

    public String getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(String totalRoom) {
        this.totalRoom = totalRoom;
    }

    public String getFlatSize() {
        return flatSize;
    }

    public void setFlatSize(String flatSize) {
        this.flatSize = flatSize;
    }

    public String getTotalWashroom() {
        return totalWashroom;
    }

    public void setTotalWashroom(String totalWashroom) {
        this.totalWashroom = totalWashroom;
    }

    public String getTotalBalcony() {
        return totalBalcony;
    }

    public void setTotalBalcony(String totalBalcony) {
        this.totalBalcony = totalBalcony;
    }
}
