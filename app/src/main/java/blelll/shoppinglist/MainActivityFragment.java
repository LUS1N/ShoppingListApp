package blelll.shoppinglist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import Model.ShoppingList;
import Model.Storage;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ListView shoppingListView = (ListView) view.findViewById(R.id.ShoppinglistView);
        Storage storage = Storage.getInstance();
        storage.mockData();

        ArrayAdapter<ShoppingList> aa =
                new ArrayAdapter<>(getContext(), R.layout.shopping_list_row,
                        storage.getShoppingLists());
        shoppingListView.setAdapter(aa);
        return view;
    }
}
