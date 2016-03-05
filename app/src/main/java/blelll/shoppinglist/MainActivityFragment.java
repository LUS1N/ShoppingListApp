package blelll.shoppinglist;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
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


        View icon = view.findViewById(R.id.addShoppingListButton);
        icon.setOnClickListener(new AddNewListListener());

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
        View icon = view.findViewById(R.id.addShoppingListButton);
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
    private class ChildOnClickListener implements ExpandableListView.OnChildClickListener
    {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
        {
            return false;
        }
    }

    private class ShoppingList_ExpandableListAdapter extends BaseExpandableListAdapter
    {
        /* extra 'child' is added as the header, so some index manipulation is in tact */
        private ArrayList<ShoppingList> shoppingLists;
        private LayoutInflater inflater;
        private Storage storage;

        public ShoppingList_ExpandableListAdapter(final LayoutInflater inflater, ExpandableListView shoppingListView)
        {
            storage = Storage.getInstance();
            this.shoppingLists = storage.getShoppingLists();
            this.inflater = inflater;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            // for the header 0th index is used and all the items in the array are pushed by 1
            if (childPosition == 0)
            {
                return setupHeader();
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
        private View setupHeader()
        {
            AddProductExpandable addProductExpandable = new AddProductExpandable(getContext());
            addProductExpandable.setAdapter(new Product_ExpandableListAdapter(inflater));
            addProductExpandable.setGroupIndicator(null);
            addProductExpandable.setOnChildClickListener(new ChildOnClickListener());

            return addProductExpandable;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            View shoppingListView = convertView;

            if (shoppingListView == null)
                shoppingListView = inflater.inflate(
                        R.layout.shopping_list_row, parent, false);
            ShoppingList current = (ShoppingList) getGroup(groupPosition);

            // set title
            ((TextView) shoppingListView.findViewById(
                    R.id.title_textView)).setText(current.getTitle());

            //set size
            ((TextView) shoppingListView.findViewById(R.id.size_textView))
                    .setText(
                            getString(R.string.shoppingList_size, // string with placeholders
                                    current.getSize(),
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

    private class Product_ExpandableListAdapter extends BaseExpandableListAdapter
    {
        final int groups = 1;
        final int children = 1;
        private LayoutInflater inflater;

        public Product_ExpandableListAdapter(LayoutInflater inflater)
        {
            this.inflater = inflater;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            convertView = inflater.inflate(R.layout.add_product_header, parent, false);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            View inputs = (inflater.inflate(R.layout.add_product_inputs, parent, false));
            Spinner spinner = (Spinner) inputs.findViewById(R.id.shop_spinner);

            ArrayAdapter<Shop> shopsAdapter = new ArrayAdapter<Shop>(getContext(),
                    R.layout.shop_spinner_item, Storage.getInstance().getShops());
            spinner.setAdapter(shopsAdapter);
            return inputs;
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
            return groups;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return children;
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            return "Group " + groupPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            return "Child " + groupPosition + " " + childPosition;
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
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
    }

    public class AddProductExpandable extends ExpandableListView
    {

        public AddProductExpandable(Context context)
        {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(960,
                    MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(800,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
