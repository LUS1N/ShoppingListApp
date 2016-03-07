package Model;

import java.util.ArrayList;

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

    private ArrayList<Product> products;
    private ArrayList<Shop> shops;
    private ArrayList<ShoppingList> shoppingLists;


    public ShoppingList addShoppingList(ShoppingList shoppingList)
    {
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
        this.products = new ArrayList<>();
        this.shops = new ArrayList<>();
        this.shoppingLists = new ArrayList<>();
    }

    /**
     * Adds a new product to the category, keeps values under each category unique
     * returns added product
     */
    public Product addProduct(Product product)
    {
        if (products.contains(product))
            return products.get(products.indexOf(product));
        else
            products.add(product);
        return product;
    }


//    public void mockData()
//    {
//        shops.addAll(new ArrayList<>(
//                Arrays.asList(new Shop("Fakta"), new Shop("Netto"), new Shop("Bilka"))));
//
//        shoppingLists.addAll(new ArrayList<>(
//                Arrays.asList(new ShoppingList("Breakfast"), new ShoppingList("Sunday"))));
//
//        shoppingLists.get(0).addProduct(new Product("Arla", "Milk", shops.get(0), 6));
//        shoppingLists.get(0).addProduct(new Product("Arla", "Milk", shops.get(0), 6));
//        shoppingLists.get(0).addProduct(
//                new Product("Some other milk", "Milk", shops.get(0), 6));
//        shoppingLists.get(0).addProduct(new Product("Beef meats", "meat", shops.get(0), 6));
//
//    }

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

    public void addProductToShoppingList(ShoppingList sl, Product product)
    {
        Product pro = addProduct(product);
        sl.addProduct(pro);
    }

    public void addProductToShoppingList(int index, Product product)
    {
        Product pro = addProduct(product);
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
}
