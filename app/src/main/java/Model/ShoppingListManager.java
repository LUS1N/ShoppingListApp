package Model;


import java.util.ArrayList;

/**
 * Created by L on 3/1/2016.
 */
public class ShoppingListManager
{
    private ShoppingList shoppingList;
    private Storage storage;

    public ShoppingListManager()
    {
        storage = Storage.getInstance();
        shoppingList = new ShoppingList();
    }

    public ArrayList<Pair<Product, Integer>> getProducts()
    {
        return shoppingList.getProducts();
    }

    public void addProductToList(Product p)
    {
        shoppingList.addProduct(p);
    }

    public ShoppingList getShoppingList()
    {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList)
    {
        this.shoppingList = shoppingList;
    }

    /**
     * Creates a product and adds it to current list
     */
    public void createProductAndAddToShoppingList(String category, String title, double price, Shop shop)
    {
        this.shoppingList.addProduct(createProduct(category, title, price, shop));
    }

    /**
     * Creates a product without adding it to the list
     */
    public Product createProduct(String category, String title, double price, Shop shop)
    {
        Product product = new Product(title, shop, price);
        storage.addProduct(category, product);
        return product;
    }

    /**
     * Returns all categories that contain the @cat string
     */
    public ArrayList<String> getPossibleCategories(String cat)
    {
        ArrayList<String> possible = new ArrayList<>();

        for (String category : storage.getCategories())
        {
            if (category.contains(cat.toUpperCase()))
                possible.add(category);
        }
        return possible;
    }
}
