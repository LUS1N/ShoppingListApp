package Model;

import java.util.ArrayList;

/**
 * Created by L on 3/1/2016.
 */
public class ShoppingList
{
    private ArrayList<Pair<Product, Integer>> products;

    public ShoppingList()
    {
        products = new ArrayList<>();
    }

    public ArrayList<Pair<Product, Integer>> getProducts()
    {
        return products;
    }

    public void addProduct(Product product)
    {
        int amount = 0;
        if (productInList(product))
        {
            Pair<Product, Integer> pair = getPairWithProduct(product);
            pair.second++;
        }
        this.products.add(new Pair<>(product, ++amount));
    }

    public int getAmountOfProduct(Product product)
    {
        for (Pair<Product, Integer> pair : products)
        {
            Product prod = pair.first;
            if (prod.equals(product))
                return pair.second;
        }
        return 0;
    }

    private Pair<Product, Integer> getPairWithProduct(Product product)
    {
        for (Pair<Product, Integer> pair : products)
        {
            if (pair.first.equals(product))
                return pair;
        }
        return null;
    }

    private boolean productInList(Product product)
    {
        for (Pair<Product, Integer> pair : products)
        {
            if (pair.first.equals(product))
                return true;
        }
        return false;
    }
}
