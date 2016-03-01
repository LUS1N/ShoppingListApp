package Model;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by L on 3/1/2016.
 */
public class Shop
{
    private ArrayList<Pair<Product, Double>> productsWithPrices;

    public Shop()
    {
        productsWithPrices = new ArrayList<>();
    }

    public ArrayList<Pair<Product, Double>> getProductsWithPrices()
    {
        return productsWithPrices;
    }

    public void addProductWithPrice(Pair<Product, Double> product)
    {
        this.productsWithPrices.add(product);
    }

    public void removeProductWithPrice(Pair<Product, Double> product)
    {
        this.productsWithPrices.remove(product);
    }
}
