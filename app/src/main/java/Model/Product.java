package Model;

/**
 * Created by L on 3/1/2016.
 */
public class Product
{
    private String title;
    private Shop shop;
    private double normalPrice;

    public Product(String title, Shop shop, double normalPrice)
    {
        this.title = title;
        this.shop = shop;
        this.normalPrice = normalPrice;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Shop getShop()
    {
        return shop;
    }

    public void setShop(Shop shop)
    {
        this.shop = shop;
    }

    public double getNormalPrice()
    {
        return normalPrice;
    }

    public void setNormalPrice(double normalPrice)
    {
        this.normalPrice = normalPrice;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (Double.compare(product.getNormalPrice(), getNormalPrice()) != 0) return false;
        if (!getTitle().equals(product.getTitle())) return false;
        return !(getShop() != null ? !getShop().equals(
                product.getShop()) : product.getShop() != null);

    }

    @Override
    public String toString()
    {
        return "Product{" +
                "title='" + title + '\'' +
                ", shop=" + shop +
                ", normalPrice=" + normalPrice +
                '}';
    }
}
