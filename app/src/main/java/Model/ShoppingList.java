package Model;

import java.util.ArrayList;

/**
 * Created by L on 3/1/2016.
 */
public class ShoppingList
{
    private ArrayList<Pair<Product, Integer>> productsWithAmount;
    private String title;

    @Override
    public String toString()
    {
        return "ShoppingList " + title + " size: " + productsWithAmount.size();
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

    public void addProduct(Product product)
    {
        addOrRemoveProduct(product, 1);
    }

    public void removeProduct(Product product)
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


    @Override
    public int hashCode()
    {
        int result = productsWithAmount.hashCode();
        result = 31 * result + getTitle().hashCode();
        return result;
    }

    private void throwException()
    {
        throw new RuntimeException("Can't remove product with amount 0");
    }

    public int getAmountOfProduct(Product product)
    {
        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            Product prod = pair.first;
            if (prod.equals(product))
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

    public boolean contains(Product product)
    {
        for (Pair<Product, Integer> pair : productsWithAmount)
        {
            if (pair.first.equals(product))
                return true;
        }
        return false;
    }
}
