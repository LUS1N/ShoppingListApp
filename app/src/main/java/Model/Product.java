package Model;

/**
 * Created by L on 3/1/2016.
 */
public class Product
{
    private String title;
    private Shop shop;
    private double price;

    public Product(String title, Shop shop, double price)
    {
        this.title = title;
        this.shop = shop;
        this.price = price;
    }

    public double getPrice()
    {
        return price;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (Double.compare(product.getPrice(), getPrice()) != 0) return false;
        if (!getTitle().equals(product.getTitle())) return false;
        return !(getShop() != null ? !getShop().equals(
                product.getShop()) : product.getShop() != null);

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = getTitle().hashCode();
        result = 31 * result + (getShop() != null ? getShop().hashCode() : 0);
        temp = Double.doubleToLongBits(getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String getTitle()
    {
        return title;
    }


    public Shop getShop()
    {
        return shop;
    }



}
