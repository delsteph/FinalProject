package egu.uga.cs.finalproject;

public class GroceryItem {
    private String groceryName;
    //private String price;
    private String quantity;

    public GroceryItem()
    {
        this.groceryName = null;
        //this.price = null;
        this.quantity = null;
    }

    public GroceryItem( String groceryName, String quantity) {
        this.groceryName = groceryName;
        //this.price = price;
        this.quantity = quantity;

    }

    public String getGroceryName() {
        return groceryName;
    }

    public void setGroceryName(String groceryName) {
        this.groceryName = groceryName;
    }

//    public String getPrice() {
//        return price;
//    }

//    public void setPrice(String price) {
//        this.price = price;
//    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String toString() {
        return groceryName + " " + quantity;
    }
}
