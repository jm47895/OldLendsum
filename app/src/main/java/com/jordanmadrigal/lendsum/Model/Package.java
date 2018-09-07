package com.jordanmadrigal.lendsum.Model;

public class Package {

    private String packageName;
    private String itemList;
    private float itemPrice;
    private String itemDescription;
    private boolean indefinite;
    private boolean lendToOwn;
    private String packageRate;
    private int lendTimePeriod;
    private int maturityDate;
    private String returnDate;
    private String borrowerName;
    private String borrowerEmail;

    public Package() {
    }

    public Package(String packageName, String borrowerEmail, String itemList, boolean indefinite, String returnDate) {
        this.packageName = packageName;
        this.borrowerEmail = borrowerEmail;
        this.itemList = itemList;
        this.indefinite = indefinite;
        this.returnDate = returnDate;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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
}
