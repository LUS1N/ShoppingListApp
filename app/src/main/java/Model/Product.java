package Model;

import java.util.ArrayList;

/**
 * Created by L on 3/1/2016.
 */
public class Product
{
    private ArrayList<Shop> shops;
    private double normalPrice;

    public Product(double normalPrice)
    {
        this.normalPrice = normalPrice;
        shops = new ArrayList<>();
    }

    public ArrayList<Shop> getShops()
    {
        return shops;
    }

    public void addShop(Shop shop)
    {
        this.shops.add(shop);
    }

    public double getNormalPrice()
    {
        return normalPrice;
    }

    public void setNormalPrice(double normalPrice)
    {
        this.normalPrice = normalPrice;
    }
}
