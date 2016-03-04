package Model;

/**
 * Created by L on 3/1/2016.
 */
public class Product
{
    private String title;
    private String category;
    private Shop shop;
    private double normalPrice;


    public Product(String title, String category, Shop shop, double normalPrice)
    {
        this.title = title;
        this.category = category;
        this.shop = shop;
        this.normalPrice = normalPrice;
    }


    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
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
        if (getTitle() != null ? !getTitle().equals(
                product.getTitle()) : product.getTitle() != null)
            return false;
        if (getCategory() != null ? !getCategory().equals(
                product.getCategory()) : product.getCategory() != null) return false;
        return !(getShop() != null ? !getShop().equals(
                product.getShop()) : product.getShop() != null);

    }

    @Override
    public String toString()
    {
        return "Product{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", shop=" + shop +
                ", normalPrice=" + normalPrice +
                '}';
    }
}
