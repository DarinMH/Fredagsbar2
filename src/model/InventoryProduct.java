package model;

public class InventoryProduct {

    private Inventory inventory;
    private int quantityInStock;
    private Product product;

    public InventoryProduct() {
    }

    public InventoryProduct(Inventory inventory, int quantityInStock, Product product) {
        this.inventory = inventory;
        this.quantityInStock = quantityInStock;
        this.product = product;
    }

    public int getQuantity() {
        return quantityInStock; // returnerer korrekt felt
    }

    public void setQuantity(int quantity) {
        this.quantityInStock = quantity;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override 
    public String toString() {
        return String.valueOf(product) + ": " + quantityInStock;
    }
}