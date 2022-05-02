package com.example.emongani;

public class ProductHelper {
    String prodImg, prodName, prodLocation, prodCategory, prodDesc, catKg, date, time;
    long startingBid;

    public ProductHelper()
        { // empty constructor
        }


    public String getProdImg(){
        return prodImg;
    }

    public void setProdImg(String prodImg) {
        this.prodImg = prodImg;
    }

    public String getProdName() {
        return prodName;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdLocation() {
        return prodLocation;
    }

    public void setProdLocation(String prodLocation) {
        this.prodLocation = prodLocation;
    }

    public String getProdCategory() {
        return prodCategory;
    }

    public void setProdCategory(String prodCategory) {
        this.prodCategory = prodCategory;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getCatKg() {
        return catKg;
    }

    public void setCatKg(String catKg) {
        this.catKg = catKg;
    }

    public long getStartingBid() {
        return startingBid;
    }

    public void setStartingBid(long startingBid) {
        this.startingBid = startingBid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}

