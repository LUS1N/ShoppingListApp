package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by L on 3/1/2016.
 */
public class Storage
{
    private static Storage storage = null;

    public static Storage getInstance()
    {
        if (storage == null)
        {
            storage = new Storage();
        }
        return storage;
    }

    private HashMap<String, ArrayList<Product>> products;
    private ArrayList<Shop> shops;
    private ArrayList<ShoppingList> shoppingLists;

    public void addShop(Shop shop)
    {
        if (!this.shops.contains(shop))
        this.shops.add(shop);
    }

    public void removeShop(Shop shop)
    {
        this.shops.remove(shop);
    }

    public ArrayList<Shop> getShops()
    {
        return shops;
    }

    public ArrayList<ShoppingList> getShoppingLists()
    {
        return shoppingLists;
    }

    private Storage()
    {
        this.products = new HashMap<>();
        this.shops = new ArrayList<>();
        this.shoppingLists = new ArrayList<>();


    }

    /**
     * Adds a new product to the category
     */
    public void addProduct(String category, Product product)
    {
        category = category.toUpperCase();
        if (!products.containsKey(category))
            products.put(category, new ArrayList<Product>());

        ArrayList<Product> prods = this.products.get(category);
        if (!prods.contains(product))
            prods.add(product);
    }

    public ArrayList<Product> getCategoryProducts(String category)
    {
        category = category.toUpperCase();
        return this.products.get(category);
    }

    public Set<String> getCategories()
    {
        return this.products.keySet();
    }
}
