package Model;

import java.util.ArrayList;

/**
 * Created by L on 3/1/2016.
 */
public class ShoppingList
{
    private ArrayList<Product> products;

    public ShoppingList()
    {
        products = new ArrayList<>();
    }

    public ArrayList<Product> getProducts()
    {
        return products;
    }

    public void addProduct(Product product)
    {
        this.products.add(product);
    }
}
