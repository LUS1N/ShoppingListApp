package blelll.shoppinglist;

import org.junit.Before;
import org.junit.Test;

import Model.Product;
import Model.Shop;
import Model.ShoppingList;
import Model.Storage;

import static org.junit.Assert.assertEquals;
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


    @Before
    public void SetUp()
    {
        storage = Storage.getInstance();
        storage.addShoppingList(new ShoppingList("Test"));
        shoppingList = storage.getShoppingLists().get(0);

        Shop bilka = new Shop("Bilka");
        storage.addShop(bilka);
        Product p1 = new Product("Milk 3,5", bilka, 6);

        storage.getProduct(p1);
        storage.getProduct(p1);
        storage.getProduct(new Product("Arla", bilka, 7));
        storage.getProduct(new Product("LURPAK", bilka, 20));
        storage.getProduct(new Product("Chicken factory", bilka, 22));
        storage.getProduct(new Product("Eggnog factory", bilka, 100));
        storage.getProduct(new Product("Salad factory", bilka, 16));
    }

    @Test
    public void canCreateProduct()
    {
        assertNotNull(new Product("Milk", new Shop("Bilka"), 3.20));
    }

    @Test
    public void testAdd_Product_returns_same_product_from_list()
    {
        Product p = storage.getProduct(new Product("Milk", new Shop("Bilka"), 5));
        Product b = storage.getProduct(new Product("Milk", new Shop("Bilka"), 5));
        assertEquals(p, b);
    }

    @Test
    public void test_different_lists_contain_the_same_product()
    {
        ShoppingList sl1 = storage.addShoppingList(new ShoppingList("First"));
        ShoppingList sl2 = storage.addShoppingList(new ShoppingList("Second"));

        storage.addProductToShoppingList(sl1, new Product("Chocolate", new Shop("Bilka"), 5));
        storage.addProductToShoppingList(sl2, new Product("Chocolate", new Shop("Bilka"), 5));

        Product p1 = sl1.getProduct(0);
        Product p2 = sl2.getProduct(0);

        assertEquals(p1, p2);

        assertTrue(sl1.getAmountOfProduct(p1) == sl2.getAmountOfProduct(p1));
        storage.addProductToShoppingList(sl1, p1);
        assertTrue(sl1.getAmountOfProduct(p1) == 2);
    }

    @Test
    public void test_add_product_to_list_with_amount()
    {
        storage.addProductToShoppingList(shoppingList, new Product("Meat", new Shop("Bilka"), 20),
                5);
        assertTrue(
                storage.getAmountOfProductInList(shoppingList,
                        new Product("Meat", new Shop("Bilka"), 20)) == 5);
    }

    @Test
    public void testAdd_and_remove_products_from_shopping_list()
    {

        Product p1 = new Product("Peanut butter", new Shop("Fakta"), 20);
        storage.addProductToShoppingList(shoppingList, p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 1);
        storage.addProductToShoppingList(shoppingList, p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 2);
        storage.removeProductFromShoppingList(shoppingList, p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 1);
        storage.removeProductFromShoppingList(shoppingList, p1);
        assertTrue(shoppingList.getAmountOfProduct(p1) == 0);
    }

    @Test(expected = RuntimeException.class)
    public void testShoppingList_throws_exception_when_trying_to_remove_item_not_in_list()
    {
        Product p1 = new Product("Peanut butter", new Shop("Fakta"), 20);
        storage.removeProductFromShoppingList(shoppingList, p1);
    }

    @Test
    public void testRemovesItemFromListWhenAmountReaches0()
    {
        Product p1 = new Product("Peanut butter", new Shop("Fakta"), 20);
        storage.addProductToShoppingList(shoppingList, p1);
        assertTrue(shoppingList.contains(p1));
        storage.removeProductFromShoppingList(shoppingList, p1);
        assertFalse(shoppingList.contains(p1));
    }


    @Test
    public void testSL_canCreatePandAddToShoppingList()
    {
        Product product = new Product("Smor", storage.getShops().get(0), 10);
        storage.addProductToShoppingList(shoppingList, product);

        assertTrue(shoppingList.getAmountOfProduct(
                new Product("Smor", storage.getShops().get(0), 10)) == 1);
    }

    @Test
    public void testGetShopByString()
    {
        String shopName = "TestShop";
        Shop shop = new Shop(shopName);
        storage.addShop(shop);
        assertTrue(storage.getShop(shopName) == shop);
    }

    @Test
    public void testGetShopThatDoesntExistYet()
    {
        String shopName = "TestShopABC";
        Shop shop = storage.getShop(shopName);
        assertNotNull(shop);
        assertTrue(shop.getTitle().equals(shopName));
    }

    @Test
    public void testSL_get_items()
    {
        ShoppingList shoppingList = new ShoppingList("A");
        storage.addProductToShoppingList(shoppingList,
                new Product("Arla milk", new Shop("Bilka"), 6));
        assertTrue(shoppingList.getItemsAmount() == 1);
        assertTrue(shoppingList.getSize() == 1);

        storage.addProductToShoppingList(shoppingList,
                new Product("Arla milk", new Shop("Bilka"), 6));
        assertTrue(shoppingList.getItemsAmount() == 2);
        assertTrue(shoppingList.getSize() == 1);

        storage.addProductToShoppingList(shoppingList,
                new Product("Dansk milk", new Shop("Bilka"), 6));
        assertTrue(shoppingList.getItemsAmount() == 3);
        assertTrue(shoppingList.getSize() == 2);
    }
}
