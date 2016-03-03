package blelll.shoppinglist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ListView shoppingListView = (ListView) view.findViewById(R.id.ShoppinglistView);
        Storage storage = Storage.getInstance();

        ArrayAdapter aa =
                new ArrayAdapter<ShoppingList>(getContext(), R.layout.shopping_list_row,
                        storage.getShoppingLists())
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        View shoppingListView = convertView;

                        if (shoppingListView == null)
                            shoppingListView = inflater.inflate(
                                    R.layout.shopping_list_row, parent, false);

                        ShoppingList current = getItem(position);
                        TextView titleTV = (TextView) shoppingListView.findViewById(
                                R.id.title_textView);
                        titleTV.setText(current.getTitle());
                        TextView sizeTV = (TextView) shoppingListView.findViewById(
                                R.id.size_textView);
                        sizeTV.setText(getString(R.string.shoppingList_size, current.getSize()));
                        return shoppingListView;
                    }
                };
        shoppingListView.setAdapter(aa);
        return view;
    }
}
