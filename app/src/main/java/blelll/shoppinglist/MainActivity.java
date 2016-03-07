package blelll.shoppinglist;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import Model.Product;
import Model.Shop;
import Model.ShoppingList;
import Model.Storage;

public class MainActivity extends AppCompatActivity
{
    private SharedPreferences mPrefs;
    private Gson gson;
    private Storage storage;
    private static final String PRODUCTS = "Products";
    private static final String SHOPPINGLISTS = "ShoppingLists";
    private static final String SHOPS = "Shops";
    private static final String DEFAULT = "";
    public static final String TAG = "BB";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPrefs = getPreferences(MODE_PRIVATE);
        gson = new Gson();
        storage = Storage.getInstance();

        retrieveData();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        // Load changes knowing that the Activity has already
        // been visible within this process.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_shop)
        {
            DialogFragment dialog = new ShopDialogFragment();
            dialog.show(getFragmentManager(), "NoticeDialogFragment");

        }
        else if (id == R.id.get_shops)
        {
            DialogFragment dialog = new ShopListDialogFragment();
            dialog.show(getFragmentManager(),"NoticeDialogFragment");
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveData()
    {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        // remove old values
        clearPrefs(prefsEditor);

        String productsJSON = gson.toJson(storage.getProducts().toArray());
        String shoppingListsJSON = gson.toJson(storage.getShoppingLists().toArray());
        String shopsJSON = gson.toJson(storage.getShops().toArray());
        prefsEditor.putString(PRODUCTS, productsJSON);
        prefsEditor.putString(SHOPPINGLISTS, shoppingListsJSON);
        prefsEditor.putString(SHOPS, shopsJSON);
        prefsEditor.commit();
        Log.w(TAG, shoppingListsJSON + "blela");
    }

    private void clearPrefs(SharedPreferences.Editor prefsEditor)
    {
        prefsEditor.remove(PRODUCTS);
        prefsEditor.remove(SHOPPINGLISTS);
        prefsEditor.remove(SHOPS);
        prefsEditor.apply();
    }

    private void retrieveData()
    {
        String productsJSON = mPrefs.getString(PRODUCTS, DEFAULT);
        String shoppingListsJSON = mPrefs.getString(SHOPPINGLISTS, DEFAULT);
        String shopsJSON = mPrefs.getString(SHOPS, DEFAULT);

        Product[] products = gson.fromJson(productsJSON, Product[].class);
        ShoppingList[] shoppingLists = gson.fromJson(shoppingListsJSON, ShoppingList[].class);
        Shop[] shops = gson.fromJson(shopsJSON, Shop[].class);

        importValuesToStorage(products, shoppingLists, shops);


    }

    private void importValuesToStorage(Product[] products, ShoppingList[] shoppingLists, Shop[] shops)
    {
        if (products != null)
            for (Product product : products)
            {
                storage.getProduct(product);
            }

        if (shoppingLists != null)
            for (ShoppingList sh : shoppingLists)
            {
                storage.addShoppingList(sh);
            }

        if (shops != null)
            for (Shop shop : shops)
            {
                storage.addShop(shop);
            }
    }

    private void logObjects(Object[] objects)
    {
        for (Object obj : objects)
        {
            Log.w(TAG, obj.toString());
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        saveData();
    }
}
