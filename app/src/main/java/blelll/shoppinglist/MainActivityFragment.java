package blelll.shoppinglist;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        final View addListHeaderView = inflater.inflate(R.layout.add_list_header, null, false);
        shoppingListView.addHeaderView(addListHeaderView);

        addListHeaderView.setOnClickListener(new ParentOnClickListener());

        setupAndSetAdapter(inflater, shoppingListView, storage);

        return view;
    }

    /**
     * Listener for adding new lists
     */
    private class ParentOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Toast.makeText(getContext(),
                    ((TextView) v.findViewById(R.id.add_list_textView)).getText().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Listener for adding new products to selected list
     */
    private class ChildOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {

            Toast.makeText(getContext(),
                    ((TextView) v.findViewById(                     // also access to parent node
                            R.id.add_product_header)).getText().toString() + " " + v.getParent().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }


    private void setupAndSetAdapter(final LayoutInflater inflater, ExpandableListView shoppingListView, final Storage storage)
    {
        ExpandableListAdapter a = new ExpandableListAdapter()
        {
            /* extra 'child' is added as the header, so some index manipulation is in tact */
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
                // +1 for index manipulation
                return shoppingLists.get(groupPosition).getSize() + 1;
            }

            @Override
            public Object getGroup(int groupPosition)
            {
                return shoppingLists.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition)
            {
                if (childPosition == 0) return null;

                return shoppingLists.get(groupPosition).getProductsWithAmounts().get(
                        childPosition - 1);
            }

            @Override
            public long getGroupId(int groupPosition)
            {
                return 0;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition)
            {
                if (childPosition == 0)
                    return 0;

                return getChild(groupPosition, childPosition).hashCode();
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
                // for the header 0th index is used and all the items in the array are pushed by 1
                if (childPosition == 0)
                {
                    View view = inflater.inflate(R.layout.add_product_header, parent, false);
                    view.setOnClickListener(new ChildOnClickListener());
                    return view;
                }
                else
                {
                    // have to check if view is not null or instance of header layout so
                    // it can be reused
                    View productListView;
                    if (convertView == null || !(convertView instanceof LinearLayout))
                    {
                        productListView = inflater.inflate(
                                R.layout.shopping_list_product, parent, false);
                    }
                    else
                        productListView = convertView;

                    ShoppingList currentSL = (ShoppingList) getGroup(groupPosition);

                    // take away 1 to access the real index
                    Pair<Product, Integer> currentProduct = currentSL.getProductsWithAmounts().get(
                            childPosition - 1);

                    // set the text for the product view
                    TextView titleTV = (TextView) productListView.findViewById(
                            R.id.product_textView);
                    titleTV.setText(currentProduct.first.getTitle());

                    return productListView;
                }
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition)
            {
                return true;
            }

            @Override
            public boolean areAllItemsEnabled()
            {
                return false;
            }

            @Override
            public boolean isEmpty()
            {
                return shoppingLists.isEmpty();
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
    }



}
