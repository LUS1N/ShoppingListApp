package Model;

import java.util.ArrayList;
import java.util.Arrays;

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
//            storage.mockData();
        }
        return storage;
    }

    public ArrayList<Product> getProducts()
    {
        return products;
    }

    private ArrayList<Product> products;
    private ArrayList<Shop> shops;
    private ArrayList<ShoppingList> shoppingLists;


    public ShoppingList addShoppingList(ShoppingList shoppingList)
    {
        if (shoppingLists.contains(shoppingList))
            return shoppingLists.get(shoppingLists.indexOf(shoppingList));
        shoppingLists.add(shoppingList);
        return shoppingList;
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
        if (this.shops.contains(shop))
            shops.remove(shops.indexOf(shop));
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
        this.products = new ArrayList<>();
        this.shops = new ArrayList<>();
        this.shoppingLists = new ArrayList<>();
    }

    /**
     * Returns the identical product object to the the passed one, so no duplicates occur.
     *
     * Adds a new product to the list if it's not there and returns added product
     */
    public Product getProduct(Product product)
    {
        if (products.contains(product))
            return products.get(products.indexOf(product));
        else
            products.add(product);
        return product;
    }


    public void mockData()
    {
        for (Shop shop : new ArrayList<>(
                Arrays.asList(new Shop("Fakta"), new Shop("Netto"), new Shop("Bilka"))))
        {
            addShop(shop);
        }

        for (ShoppingList shoppingList : new ArrayList<>(
                Arrays.asList(new ShoppingList("Breakfast"), new ShoppingList("Sunday"))))
        {
            addShoppingList(shoppingList);
        }

        addProductToShoppingList(0, new Product("Arla", shops.get(0), 6));
        addProductToShoppingList(0, new Product("Arla", shops.get(0), 6));
        addProductToShoppingList(0, new Product("Some other milk", shops.get(0), 6));
        addProductToShoppingList(0, new Product("Beef meats", shops.get(0), 6));

    }

    public Shop getShop(String shopName)
    {
        for (Shop shop : shops)
        {
            if (shop.getTitle().equals(shopName))
                return shop;
        }

        Shop shop = new Shop(shopName);
        shops.add(shop);
        return shop;
    }

    public void addProductToShoppingList(ShoppingList sl, Product product, int amount)
    {
        Product pro = getProduct(product);
        sl.addProduct(pro, amount);
    }

    public void addProductToShoppingList(int index, Product product, int amount)
    {
        Product pro = getProduct(product);
        shoppingLists.get(index).addProduct(pro, amount);
    }

    public void addProductToShoppingList(ShoppingList sl, Product product)
    {
        Product pro = getProduct(product);
        sl.addProduct(pro);
    }

    public void addProductToShoppingList(int index, Product product)
    {
        Product pro = getProduct(product);
        shoppingLists.get(index).addProduct(pro);
    }

    public void removeProductFromShoppingList(ShoppingList shoppingList, Product product)
    {
        shoppingList.removeProduct(product);
    }

    public void removeProductFromShoppingList(int SLindex, int prodIndx)
    {
        shoppingLists.get(SLindex).removeProduct(prodIndx);
    }

    public int getAmountOfProductInList(ShoppingList shoppingList, Product product)
    {
        Product p = getProduct(product);
        return shoppingList.getAmountOfProduct(p);
    }
}
