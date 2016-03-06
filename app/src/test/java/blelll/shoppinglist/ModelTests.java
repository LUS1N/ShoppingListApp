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
    ShoppingList shoppingList;


    @Test
    public void canCreateProduct()
    {
        assertNotNull(new Product("Milk", "Milk", new Shop("Bilka"), 3.20));
    }

    @Before
    public void SetUp()
    {
        storage = Storage.getInstance();
        storage.addShoppingList(new ShoppingList("Test"));
        shoppingList = storage.getShoppingLists().get(0);

        Shop bilka = new Shop("Bilka");
        storage.addShop(bilka);
        Product p1 = new Product("Milk 3,5", "Milk", bilka, 6);

        storage.addProduct(p1);
        storage.addProduct(p1);
        storage.addProduct(new Product("Arla", "Butter", bilka, 7));
        storage.addProduct(new Product("LURPAK", "Butter", bilka, 20));
        storage.addProduct(new Product("Chicken factory", "Eggs", bilka, 22));
        storage.addProduct(new Product("Eggnog factory", "Eggnog", bilka, 100));
        storage.addProduct(new Product("Salad factory", "Egg Salad", bilka, 16));
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
        List<String> categories = storage.getPossibleCategories("Eg");
        assertTrue(categories.containsAll(Arrays.asList("EGGS", "EGGNOG", "EGG SALAD")));
        assertFalse(categories.containsAll(Arrays.asList("MILK", "BUTTER")));
    }

    @Test
    public void testSLM_canCreateProduct()
    {
        String cat = "Butter";
        Product product = new Product("Smor", cat, new Shop("Bilka"), 10);
        storage.addProduct(product);
        assertTrue(storage.getCategoryProducts(cat).contains(product));
    }

    @Test
    public void testAdd_and_remove_products_from_shopping_list()
    {

        Product p1 = new Product("Peanut butter", "Peanut butter", new Shop("Fakta"), 20);
        shoppingList.addProduct(p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 1);
        shoppingList.addProduct(p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 2);
        shoppingList.removeProduct(p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 1);
        shoppingList.removeProduct(p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testShoppingList_throws_exception_when_trying_to_remove_item_not_in_list()
    {
        Product p1 = new Product("Peanut butter", "Peanut butter", new Shop("Fakta"), 20);
        shoppingList.removeProduct(p1);
    }

    @Test
    public void testRemovesItemFromListWhenAmountReaches0()
    {
        Product p1 = new Product("Peanut butter", "Peanut butter", new Shop("Fakta"), 20);
        shoppingList.addProduct(p1);
        assertTrue(shoppingList.contains(p1));
        shoppingList.removeProduct(p1);
        assertFalse(shoppingList.contains(p1));
    }


    @Test
    public void testSL_canCreatePandAddToShoppingList()
    {
        String cat = "Butter";
        Product product = new Product("Smor", cat, storage.getShops().get(0), 10);
        shoppingList.addProduct(product);

        assertTrue(shoppingList.getAmountOfProduct(
                new Product("Smor", "Butter", storage.getShops().get(0), 10)) > 0);
    }

    @Test
    public void testSL_get_items()
    {
        ShoppingList shoppingList = new ShoppingList("A");
        shoppingList.addProduct(new Product("Arla milk", "Milk", new Shop("Bilka"), 6));
        assertTrue(shoppingList.getItemsAmount() == 1);
        assertTrue(shoppingList.getSize() == 1);

        shoppingList.addProduct(new Product("Arla milk", "Milk", new Shop("Bilka"), 6));
        assertTrue(shoppingList.getItemsAmount() == 2);
        assertTrue(shoppingList.getSize() == 1);

        shoppingList.addProduct(new Product("Dansk milk", "Milk", new Shop("Bilka"), 6));
        assertTrue(shoppingList.getItemsAmount() == 3);
        assertTrue(shoppingList.getSize() == 2);

    }
}
