package com.jordanmadrigal.lendsum.Model;

import java.util.List;

public class Package {

    private String packageName;
    private String packId;
    private String itemList;
    private List<String> imagePaths;
    private float itemPrice;
    private String itemDescription;
    private boolean indefinite;
    private boolean lendToOwn;
    private String packageRate;
    private int lendTimePeriod;
    private int maturityDate;
    private String returnDate;
    private String lenderName;
    private String borrowerName;
    private String borrowerEmail;

    public Package() {
    }

    public Package(String lenderName, String borrowerName, String borrowerEmail, String packId, String packageName, String itemList, boolean indefinite, String returnDate, List<String> imagePaths) {
        this.lenderName = lenderName;
        this.borrowerName = borrowerName;
        this.borrowerEmail = borrowerEmail;
        this.packId = packId;
        this.packageName = packageName;
        this.itemList = itemList;
        this.indefinite = indefinite;
        this.returnDate = returnDate;
        this.imagePaths = imagePaths;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPackageRate() {
        return packageRate;
    }

    public void setPackageRate(String packageRate) {
        this.packageRate = packageRate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    public void setBorrowerEmail(String borrowerEmail) {
        this.borrowerEmail = borrowerEmail;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public boolean isIndefinite() {
        return indefinite;
    }

    public void setIndefinite(boolean indefinite) {
        this.indefinite = indefinite;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
