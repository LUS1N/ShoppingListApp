package Model;

import java.util.ArrayList;

/**
 * Created by L on 3/1/2016.
 */
public class ShoppingList
{
    private ArrayList<Pair<Product, Integer>> productsWithAmount;
    private String title;

    public double getTotalPrice()
    {
        double total = 0;

        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            total += pair.first.getPrice() * pair.second;
        }

        return total;
    }
    public ShoppingList(String title)
    {
        productsWithAmount = new ArrayList<>();
        this.title = title;
    }

    public int getSize()
    {
        return productsWithAmount.size();
    }

    public int getItemsAmount()
    {
        int items = 0;
        for (Pair<Product, Integer> uniqueItem : productsWithAmount)
        {
            items += uniqueItem.second;
        }
        return items;
    }
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public ArrayList<Pair<Product, Integer>> getProductsWithAmounts()
    {
        return productsWithAmount;
    }

    /**
     * To be used through storage
     */
    protected void addProduct(Product product)
    {
        addOrRemoveProduct(product, 1);
    }

    protected void addProduct(Product product, int amount)
    {
        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            if (pair.first.equals(product))
            {
                pair.second += amount;
                return;
            }
        }
        productsWithAmount.add(new Pair<>(product, amount));
    }

    protected void removeProduct(Product product)
    {
        addOrRemoveProduct(product, -1);
    }

    private void addOrRemoveProduct(Product product, int amount)
    {
        if (contains(product))
        {
            if (getProductAndAmount(product).second + amount < 0)
                removePairFromList(product);

            getProductAndAmount(product).second += amount;
            if (getProductAndAmount(product).second == 0)
                removePairFromList(product);
        }
        else
        {
            if (amount > 0)
                this.productsWithAmount.add(new Pair<>(product, amount));
            else
                throwException();
        }
    }

    private void removePairFromList(Product product)
    {
        for (Pair<Product, Integer> pair : this.productsWithAmount)
        {
            if (pair.first.equals(product))
            {
                this.productsWithAmount.remove(pair);
                return;
            }
        }
    }

    private void throwException()
    {
        throw new RuntimeException("No such product in list");
    }

    public int getAmountOfProduct(Product product)
    {
        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            if (pair.first.equals(product))
                return pair.second;
        }
        return 0;
    }

    public void removeProduct(int index)
    {
        Product p = productsWithAmount.get(index).first;
        removeProduct(p);
    }

    public void addProduct(int index)
    {
        Product p = productsWithAmount.get(index).first;
        addProduct(p);
    }

    public boolean isEmpty()
    {
        return productsWithAmount.isEmpty();
    }

    public Product getProduct(int index)
    {
        return productsWithAmount.get(index).first;
    }

    private Pair<Product, Integer> getProductAndAmount(Product product)
    {
        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            if (pair.first.equals(product))
                return pair;
        }
        return null;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ShoppingList)) return false;

        ShoppingList that = (ShoppingList) o;

        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            if (!((ShoppingList) o).productsWithAmount.contains(pair))
                return false;
        }

        return !(getTitle() != null ? !getTitle().equals(
                that.getTitle()) : that.getTitle() != null);

    }

    public boolean contains(Product product)
    {
        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            if (pair.first.equals(product))
                return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "ShoppingList " + title + " size: " + productsWithAmount.size();
    }

    @Override
    public int hashCode()
    {
        int result = productsWithAmount.hashCode();
        result = 31 * result + getTitle().hashCode();
        return result;
    }
}
