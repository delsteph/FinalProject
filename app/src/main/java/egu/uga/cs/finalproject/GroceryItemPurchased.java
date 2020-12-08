package egu.uga.cs.finalproject;

public class GroceryItemPurchased {
    private String groceryName;
    private String price;
    private String purchaserName;

    public GroceryItemPurchased()
    {
        this.groceryName = null;
        this.price = null;
        this.purchaserName = null;
    }

    public GroceryItemPurchased( String groceryName, String price, String purchaserName) {
        this.groceryName = groceryName;
        this.price = price;
        this.purchaserName = purchaserName;
    }

    public String getGroceryName() {
        return groceryName;
    }

    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String quantity) {
        this.purchaserName = quantity;
    }


    public String toString() {
        return groceryName + " " + purchaserName + " " + price;
    }
}
