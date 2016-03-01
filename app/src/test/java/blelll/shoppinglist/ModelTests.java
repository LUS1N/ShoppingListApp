package blelll.shoppinglist;

import org.junit.Test;

import Model.Product;

import static org.junit.Assert.assertNotNull;

/**
 * Created by L on 3/1/2016.
 */
public class ModelTests
{
    @Test
    public void canCreateProduct()
    {
        assertNotNull(new Product(3.20));
    }
}
