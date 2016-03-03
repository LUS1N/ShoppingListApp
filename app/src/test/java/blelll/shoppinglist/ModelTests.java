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
import Model.ShoppingList;
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
        shoppingListManager.addShoppingList("Test");
        Shop bilka = new Shop("Bilka");
        storage.addShop(bilka);
        Product p1 = new Product("Milk", bilka, 6);

        storage.addProduct("Milk", p1);
        storage.addProduct("Milk", p1);
        storage.addProduct("milk", new Product("Arla", bilka, 7));
        storage.addProduct("Butter", new Product("LURPAK", bilka, 20));
        storage.addProduct("Eggs", new Product("Chicken factory", bilka, 22));
        storage.addProduct("Eggnog", new Product("Eggnog factory", bilka, 100));
        storage.addProduct("Egg Salad", new Product("Salad factory", bilka, 16));
    }

    @Test
    public void testStorageGetExistingCategory()
    {
        Assert.assertTrue(!storage.getCategoryProducts("Milk").isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testGetNonExistingCategoryThrowsException()
    {
        Assert.assertTrue(storage.getCategoryProducts("Apples").isEmpty());
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
    public void testAdd_and_remove_products_from_shopping_list()
    {
        Product p1 = new Product("Peanut butter", new Shop("Fakta"), 20);
        shoppingListManager.getShoppingList().addProduct(p1);
        assertTrue(shoppingListManager.getShoppingList().getAmountOfProduct(p1) == 1);
        shoppingListManager.getShoppingList().addProduct(p1);
        assertTrue(shoppingListManager.getShoppingList().getAmountOfProduct(p1) == 2);
        shoppingListManager.getShoppingList().removeProduct(p1);
        assertTrue(shoppingListManager.getShoppingList().getAmountOfProduct(p1) == 1);
        shoppingListManager.getShoppingList().removeProduct(p1);
        assertTrue(shoppingListManager.getShoppingList().getAmountOfProduct(p1) == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testShoppingList_throws_exception_when_trying_to_remove_from_0_amount()
    {
        Product p1 = new Product("Peanut butter", new Shop("Fakta"), 20);
        shoppingListManager.getShoppingList().removeProduct(p1);
    }

    @Test
    public void testGet_Amount_Of_Product_Added_To_The_ShoppingList()
    {
        shoppingListManager.createProductAndAddToShoppingList("Chcocolate", "Milka", 20,
                new Shop("Fakta"));
        shoppingListManager.getShoppingList().addProduct(
                new Product("Milka", new Shop("Fakta"), 20));
        assertTrue(shoppingListManager.getShoppingList().getAmountOfProduct(
                new Product("Milka", new Shop("Fakta"), 20)) == 2);
    }

    @Test
    public void testSLM_canCreatePandAddToShoppingList()
    {
        String cat = "Butter";
        shoppingListManager.createProductAndAddToShoppingList(cat, "Smor", 10,
                storage.getShops().get(0));

        assertTrue(shoppingListManager.getShoppingList().getAmountOfProduct(
                new Product("Smor", storage.getShops().get(0), 10)) > 0);
    }

    @Test
    public void testSL_get_items()
    {
        ShoppingList sl = shoppingListManager.getShoppingList();
        sl.addProduct(new Product("Arla milk", new Shop("Bilka"), 6));
        assertTrue(sl.getItemsAmount() == 1);
        assertTrue(sl.getSize() == 1);

        sl.addProduct(new Product("Arla milk", new Shop("Bilka"), 6));
        assertTrue(sl.getItemsAmount() == 2);
        assertTrue(sl.getSize() == 1);

        sl.addProduct(new Product("Dansk milk", new Shop("Bilka"), 6));
        assertTrue(sl.getItemsAmount() == 3);
        assertTrue(sl.getSize() == 2);

    }
}
