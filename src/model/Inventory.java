package model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {


    private int inventoryId;
    private String location;
    private int capacity;
    private List<InventoryProduct> products; 


    public Inventory() {

    }

    public Inventory(int inventoryId) {
        this.inventoryId=inventoryId; 
    }

    public Inventory(int inventoryId, String location, int capacity) {
        super();
        this.inventoryId = inventoryId;
        this.location = location;
        this.capacity = capacity;
        products = new ArrayList<>(); 
    }
    
    
	// Getters and setters

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addInventoryProduct(InventoryProduct inventoryProduct) {
        products.add(inventoryProduct); 
    }

    public List<InventoryProduct> getInventoryProduct() {
        return products; 
    }









    @Override 
    public String toString() {
        return location; 
    }
}