package blelll.shoppinglist;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Model.Pair;
import Model.Product;
import Model.Shop;
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

        setupAddShoppingListHeader(inflater, shoppingListView);

        setupAddListButtonListener(view);

        setupShoppingListExpandableListAdapter(inflater, shoppingListView);

        return view;
    }

    private void setupShoppingListExpandableListAdapter(LayoutInflater inflater, ExpandableListView shoppingListView)
    {
        ExpandableListAdapter a = new ShoppingList_ExpandableListAdapter(inflater,
                shoppingListView);

        shoppingListView.setAdapter(a);
    }

    private void setupAddListButtonListener(View view)
    {
        View icon = view.findViewById(R.id.imageButton);
        icon.setOnClickListener(new AddNewListListener());
    }

    private void setupAddShoppingListHeader(LayoutInflater inflater, ExpandableListView shoppingListView)
    {
        final View addListHeaderView = inflater.inflate(R.layout.add_list_header, null, false);
        shoppingListView.addHeaderView(addListHeaderView);
        shoppingListView.setOnChildClickListener(new ProductOnClickListener());
    }


    /**
     * Listener for clicked products
     */
    private class ProductOnClickListener implements ExpandableListView.OnChildClickListener
    {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
        {

            return false;
        }
    }
    /**
     * Listener for adding new lists
     */
    private class AddNewListListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            View parent = (View) v.getParent();
            View editText = (View) parent.findViewById(R.id.add_list_textView);

            if(!((TextView) editText).getText().toString().isEmpty()) {
                Storage.getInstance().addShoppingList(new ShoppingList(((TextView) editText).getText().toString()));
                ((TextView) editText).clearFocus();
                ((TextView) editText).setText("");
            }

            Toast.makeText(getContext(),
                    "" + editText.getId(),
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
            Log.w("ME", "" + v.getId());
        }
    }

    private class ShoppingList_ExpandableListAdapter implements ExpandableListAdapter
    {
        /* extra 'child' is added as the header, so some index manipulation is in tact */
        private ArrayList<ShoppingList> shoppingLists;
        private LayoutInflater inflater;
        private ExpandableListView shoppingListView;
        private Storage storage;

        public ShoppingList_ExpandableListAdapter(final LayoutInflater inflater, ExpandableListView shoppingListView)
        {
            storage = Storage.getInstance();
            this.shoppingListView = shoppingListView;
            this.shoppingLists = storage.getShoppingLists();
            this.inflater = inflater;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            // for the header 0th index is used and all the items in the array are pushed by 1
            if (childPosition == 0)
            {
                return setupHeader(parent);
            }
            else
            {
                return setupProductRows(groupPosition, childPosition, convertView, parent);

            }
        }

        @NonNull
        private View setupProductRows(int groupPosition, int childPosition, View convertView, ViewGroup parent)
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

        @NonNull
        private View setupHeader(ViewGroup parent)
        {
            View view = inflater.inflate(R.layout.add_product_header, parent, false);

            Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
            ArrayAdapter<Shop> adapter = new ArrayAdapter<>(getContext(),
                    R.layout.support_simple_spinner_dropdown_item, storage.getShops());
            spinner.setAdapter(adapter);

            view.setOnClickListener(new ChildOnClickListener());
            return view;
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
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition - 1;
        }

        @Override
        public boolean hasStableIds()
        {
            return false;
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
    }



}
