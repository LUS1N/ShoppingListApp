package Model;

/**
 * Created by L on 3/1/2016.
 */
public class Shop
{
    String title;

    public Shop(String title)
    {
        this.title = title;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Shop)) return false;

        Shop shop = (Shop) o;

        return title.equals(shop.title);

    }

    @Override
    public String toString()
    {
        return title;
    }
}
