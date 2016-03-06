package Model;

import java.util.ArrayList;
import java.util.Arrays;
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
            storage.mockData();
        }
        return storage;
    }

    private HashMap<String, ArrayList<Product>> products;
    private ArrayList<Shop> shops;
    private ArrayList<ShoppingList> shoppingLists;


    /**
     * Returns all categories that contain the @cat string
     */
    public ArrayList<String> getPossibleCategories(String cat)
    {
        ArrayList<String> possible = new ArrayList<>();

        for (String category : getCategories())
        {
            if (category.contains(cat.toUpperCase()))
                possible.add(category);
        }
        return possible;
    }
    public void addShoppingList(ShoppingList shoppingList)
    {
        shoppingLists.add(shoppingList);
    }

    public void removeShoppingList(ShoppingList sl)
    {
        shoppingLists.remove(sl);
    }

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
     * Adds a new product to the category, keeps values under each category unique
     */
    public void addProduct(Product product)
    {
        String category = product.getCategory().toUpperCase();

        // if there's not key with category add new list to the map
        if (!products.containsKey(category))
            products.put(category, new ArrayList<Product>());

        // add it to the list only if there are no duplicate items
        ArrayList<Product> prods = this.products.get(category);
        if (!prods.contains(product))
            prods.add(product);
    }

    public ArrayList<Product> getCategoryProducts(String category)
    {
        category = category.toUpperCase();
        if (!getCategories().contains(category))
        {
            throw new RuntimeException("No such category.");
        }
        return this.products.get(category);
    }

    public Set<String> getCategories()
    {
        return this.products.keySet();
    }

    public void mockData()
    {
        shops.addAll(new ArrayList<>(
                Arrays.asList(new Shop("Fakta"), new Shop("Netto"), new Shop("Bilka"))));

        shoppingLists.addAll(new ArrayList<>(
                Arrays.asList(new ShoppingList("Breakfast"), new ShoppingList("Sunday"))));

        shoppingLists.get(0).addProduct(new Product("Arla", "Milk", shops.get(0), 6));
        shoppingLists.get(0).addProduct(new Product("Arla", "Milk", shops.get(0), 6));
        shoppingLists.get(0).addProduct(
                new Product("Some other milk abcde", "Milk", shops.get(0), 6));
        shoppingLists.get(0).addProduct(new Product("Beef meats", "meat", shops.get(0), 6));

    }
}
