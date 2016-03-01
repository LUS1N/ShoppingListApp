package blelll.shoppinglist;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import Model.Product;
import Model.Shop;
import Model.ShoppingListManager;
import Model.Storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by L on 3/1/2016.
 */
public class ModelTests
{
    Storage storage;
    ShoppingListManager shoppingListManager;
    @Test
    public void canCreateProduct()
    {
        assertNotNull(new Product("Milk", new Shop("Bilka"), 3.20));
    }

    @Before
    public void SetUp()
    {
        storage = Storage.getInstance();
        shoppingListManager = new ShoppingListManager();
        Shop bilka = new Shop("Bilka");
        storage.addShop(bilka);
        Product p1 = new Product("Milk", bilka, 6);

        storage.addProduct("Milk", p1);
        storage.addProduct("milk", new Product("Arla", bilka, 7));
        storage.addProduct("Butter", new Product("LURPAK", bilka, 20));
        storage.addProduct("Eggs", new Product("Chicken factory", bilka, 22));
        storage.addProduct("Eggnog", new Product("Eggnog factory", bilka, 100));
        storage.addProduct("Egg Salad", new Product("Salad factory", bilka, 16));
    }

    @Test
    public void testStorageGetCategory()
    {
        Assert.assertTrue(!storage.getCategoryProducts("Milk").isEmpty());
    }

    @Test
    public void testGetCategories()
    {
        Set<String> categories = storage.getCategories();
        ArrayList<String> testCategories = new ArrayList<>(
                Arrays.asList("Milk", "milk", "butter", "eggs"));

        for (String cat : testCategories)
        {
            assertTrue(categories.contains(cat.toUpperCase()));
        }
    }

    @Test
    public void testSLM_findsMatchingCategories()
    {
        List<String> categories = shoppingListManager.getPossibleCategories("Eg");
        assertTrue(categories.containsAll(Arrays.asList("EGGS", "EGGNOG", "EGG SALAD")));
        assertFalse(categories.containsAll(Arrays.asList("MILK", "BUTTER")));
    }

    @Test
    public void testSLM_canCreateProduct()
    {
        String cat = "Butter";
        Product product = shoppingListManager.createProduct(cat, "Smor", 10,
                storage.getShops().get(0));
        assertTrue(storage.getCategoryProducts(cat).contains(product));
    }

    @Test
    public void testSLM_canCreatePandAddToShoppingList()
    {
        String cat = "Butter";
        shoppingListManager.createProductAndAddToShoppingList(cat, "Smor", 10,
                storage.getShops().get(0));

        assertTrue(shoppingListManager.getProducts().contains(
                new Product("Smor", storage.getShops().get(0), 10)));
    }
}
