package com.jordanmadrigal.lendsum;

public class Package {

    private String packageName;
    private String itemName;
    private float itemPrice;
    private String itemDescription;
    private boolean freeBundle;
    private boolean lendToOwn;
    private String packageRate;
    private int lendTimePeriod;
    private int maturityDate;
    private String returnDate;
    private String userName;

    public Package(String packageName, String itemDescription, String packageRate, String returnDate, String userName) {
        this.packageName = packageName;
        this.itemDescription = itemDescription;
        this.packageRate = packageRate;
        this.returnDate = returnDate;
        this.userName = userName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
