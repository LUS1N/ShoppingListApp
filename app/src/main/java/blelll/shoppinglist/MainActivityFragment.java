package blelll.shoppinglist;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Pair;
import Model.Product;
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

        ExpandableListView shoppingListView = (ExpandableListView) view.findViewById(
                R.id.ShoppingListsExpandableListView);
        final Storage storage = Storage.getInstance();

        ExpandableListAdapter a = new ExpandableListAdapter()
        {
            ArrayList<ShoppingList> shoppingLists = storage.getShoppingLists();

            @Override
            public void registerDataSetObserver(DataSetObserver observer)
            {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer)
            {

            }

            @Override
            public int getGroupCount()
            {
                return shoppingLists.size();
            }

            @Override
            public int getChildrenCount(int groupPosition)
            {
                return shoppingLists.get(groupPosition).getSize();
            }

            @Override
            public Object getGroup(int groupPosition)
            {
                return shoppingLists.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition)
            {
                return shoppingLists.get(groupPosition).getProductsWithAmounts().get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition)
            {
                return 0;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition)
            {
                return 0;
            }

            @Override
            public boolean hasStableIds()
            {
                return false;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
            {
                View shoppingListView = convertView;

                if (shoppingListView == null)
                    shoppingListView = inflater.inflate(
                            R.layout.shopping_list_row, parent, false);

                ShoppingList current = (ShoppingList) getGroup(groupPosition);

                TextView titleTV = (TextView) shoppingListView.findViewById(
                        R.id.title_textView);
                titleTV.setText(current.getTitle());

                TextView sizeTV = (TextView) shoppingListView.findViewById(
                        R.id.size_textView);
                sizeTV.setText(getString(R.string.shoppingList_size, current.getSize(),
                        current.getItemsAmount()));

                return shoppingListView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
            {
                View productListView = convertView;

                if (productListView == null)
                    productListView = inflater.inflate(
                            R.layout.shopping_list_product, parent, false);

                ShoppingList currentSL = (ShoppingList) getGroup(groupPosition);
                Pair<Product, Integer> currentProduct = currentSL.getProductsWithAmounts().get(
                        childPosition);

                TextView titleTV = (TextView) productListView.findViewById(R.id.product_textView);
                titleTV.setText(currentProduct.first.getTitle());


                return productListView;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition)
            {
                return false;
            }

            @Override
            public boolean areAllItemsEnabled()
            {
                return false;
            }

            @Override
            public boolean isEmpty()
            {
                return false;
            }

            @Override
            public void onGroupExpanded(int groupPosition)
            {

            }

            @Override
            public void onGroupCollapsed(int groupPosition)
            {

            }

            @Override
            public long getCombinedChildId(long groupId, long childId)
            {
                return 0;
            }

            @Override
            public long getCombinedGroupId(long groupId)
            {
                return 0;
            }
        };

        shoppingListView.setAdapter(a);
//        setAdapter(inflater, shoppingListView, storage);
        return view;
    }

    private void setAdapter(final LayoutInflater inflater, ListView shoppingListView, final Storage storage)
    {
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
                        sizeTV.setText(getString(R.string.shoppingList_size, current.getSize(),
                                current.getItemsAmount()));

                        return shoppingListView;
                    }
                };
        shoppingListView.setAdapter(aa);
    }
}
