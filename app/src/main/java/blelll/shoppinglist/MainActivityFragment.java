package blelll.shoppinglist;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
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

    ExpandableListView shoppingListView;
    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        shoppingListView = (ExpandableListView) view.findViewById(
                R.id.ShoppingListsExpandableListView);

        setupAddShoppingListHeader(inflater);
        setupAddListButtonListener(view);
        setupShoppingListExpandableListAdapter(inflater);

        return view;
    }

    private void setupShoppingListExpandableListAdapter(LayoutInflater inflater)
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

    private void setupAddShoppingListHeader(LayoutInflater inflater)
    {
        shoppingListView.addHeaderView(
                inflater.inflate(R.layout.add_list_header, shoppingListView, false));
    }

    /**
     * Listener for adding new products to selected list
     */
    private class DecreaseProductAmountListener implements View.OnClickListener
    {
        int group, child;

        public DecreaseProductAmountListener(int group, int child)
        {
            this.group = group;
            this.child = child;
        }


        @Override
        public void onClick(View v)
        {
            Storage.getInstance().getShoppingLists().get(group).removeProduct(child);
            ((BaseExpandableListAdapter) shoppingListView.getExpandableListAdapter()).notifyDataSetChanged();
        }
    }

    /**
     * Listener for clicked products
     */
    private class AddProductAmountListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {

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
            View editText = parent.findViewById(R.id.add_list_textView);

            String title = ((TextView) editText).getText().toString();
            if (!title.isEmpty())
            {
                if (title.length() > 15)
                    title = title.substring(0, 15);

                Storage.getInstance().addShoppingList(
                        new ShoppingList(title));
                editText.clearFocus();
                ((TextView) editText).setText("");
                hideKeyboard(v);
            }
        }
    }

    /**
     * Listener for removing lists
     */
    private class RemoveListListener implements View.OnClickListener
    {
        int groupId;

        public RemoveListListener(int groupId)
        {
            this.groupId = groupId;

        }

        @Override
        public void onClick(View v)
        {
            Storage.getInstance().getShoppingLists().remove(groupId);
            View parent = (View) v.getParent().getParent();
            ((BaseExpandableListAdapter) ((ExpandableListView) parent).getExpandableListAdapter()).notifyDataSetChanged();
        }
    }

    private class ShoppingList_ExpandableListAdapter extends BaseExpandableListAdapter
    {
        /* extra 'child' is added as the header, so some index manipulation is in tact */
        private ArrayList<ShoppingList> shoppingLists;
        private LayoutInflater inflater;
        private Storage storage;
        int lastExpandedGroupPosition;
        ExpandableListView listView;

        public ShoppingList_ExpandableListAdapter(final LayoutInflater inflater, ExpandableListView listView)
        {
            storage = Storage.getInstance();
            this.shoppingLists = storage.getShoppingLists();
            this.inflater = inflater;
            this.listView = listView;

        }

        @Override
        public void onGroupExpanded(int groupPosition)
        {
            //collapse the old expanded group, if not the same
            //as new group to expand
            if (groupPosition != lastExpandedGroupPosition)
            {
                listView.collapseGroup(lastExpandedGroupPosition);
            }

            super.onGroupExpanded(groupPosition);
            lastExpandedGroupPosition = groupPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            // for the header 0th index is used and all the items in the array are pushed by 1
            if (childPosition == 0)
            {
                return setupHeader(groupPosition);
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

            // set the text for the product title
            ((TextView) productListView.findViewById(R.id.product_title_textView)).
                    setText(
                            currentProduct.first.getTitle());

            // add prices formatted as a money format from current locale
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            ((TextView) productListView.findViewById(R.id.product_price_textView)).setText(
                    currencyFormat.format(currentProduct.first.getNormalPrice()));

            ((TextView) productListView.findViewById(R.id.total_price_textView)).setText(
                    getString(R.string.total_products_price,
                            currencyFormat.format(
                                    currentProduct.first.getNormalPrice() * currentProduct.second)
                    ));

            ((TextView) ((productListView.findViewById(R.id.product_amount_textview)))).setText(
                    currentProduct.second + "");

            ImageButton decreaseButton = (ImageButton) productListView.findViewById(
                    R.id.product_decrease_amount_button);
            decreaseButton.setOnClickListener(
                    new DecreaseProductAmountListener(groupPosition, childPosition - 1));

//            DecreaseProductAmountListener decreaseListener = new DecreaseProductAmountListener(
//                    groupPosition, childPosition, ((ExpandableListView) productListView));

//            decreaseButton.setOnClickListener(decreaseListener);

            Log.d("BB", productListView.getParent() + "");


            return productListView;
        }

        @NonNull
        private View setupHeader(int group)
        {
            AddProductExpandable addProductExpandable = new AddProductExpandable(getContext(),
                    group);
            addProductExpandable.setAdapter(
                    new Product_ExpandableListAdapter(addProductExpandable,
                            addProductExpandable.getGroup(), inflater));
            addProductExpandable.setGroupIndicator(null);

            return addProductExpandable;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            View shoppingListGroupView = convertView;

            if (shoppingListGroupView == null)
                shoppingListGroupView = inflater.inflate(
                        R.layout.shopping_list_row, parent, false);
            ShoppingList current = (ShoppingList) getGroup(groupPosition);

            // set title
            ((TextView) shoppingListGroupView.findViewById(
                    R.id.title_textView)).setText(current.getTitle());

            View removeButton = shoppingListGroupView.findViewById(R.id.removeListButton);
            removeButton.setOnClickListener(new RemoveListListener(groupPosition));

            //set size
            ((TextView) shoppingListGroupView.findViewById(R.id.size_textView))
                    .setText(
                            getString(R.string.shoppingList_size, // string with placeholders
                                    current.getSize(),
                                    current.getItemsAmount()));

            return shoppingListGroupView;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer)
        {
            super.registerDataSetObserver(observer);
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
            return storage.getShoppingLists().get(groupPosition).hashCode();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            if (childPosition == 0) return 0;
            return storage.getShoppingLists().get(groupPosition).getProductsWithAmounts().get(
                    childPosition - 1).hashCode();
        }

        @Override
        public boolean hasStableIds()
        {
            return true;
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
        int outterGroup;
        AddProductExpandable addProductExpandable;

        public Product_ExpandableListAdapter(AddProductExpandable addProductExpandable, int outterGroup, LayoutInflater inflater)
        {
            this.inflater = inflater;
            this.outterGroup = outterGroup;
            this.addProductExpandable = addProductExpandable;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
        {
            convertView = inflater.inflate(R.layout.add_product_header, parent, false);
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
        {
            View inputs = (inflater.inflate(R.layout.add_product_inputs, parent, false));
            final Spinner spinner = (Spinner) inputs.findViewById(R.id.shop_spinner);

            ArrayAdapter<Shop> shopsAdapter = new ArrayAdapter<>(getContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    Storage.getInstance().getShops());
            spinner.setAdapter(shopsAdapter);

            ImageButton addButton = (ImageButton) inputs.findViewById(R.id.add_product_button);
            addButton.setOnClickListener(new View.OnClickListener()
            {


                @Override
                public void onClick(View v)
                {
                    View parent = (View) (v.getParent()).getParent();

                    String productItem = ((EditText) parent.findViewById(
                            R.id.new_product_title)).getText().toString();
                    String productCategory = ((EditText) parent.findViewById(
                            R.id.new_product_category)).getText().toString();

                    String priceString = ((EditText) parent.findViewById(
                            R.id.new_product_price)).getText().toString();


                    if (productItem.length() > 0)
                    {

                        if (productItem.length() > 15)
                            productItem = productItem.substring(0, 15);
                        double productPrice;
                        if (priceString.length() < 1)
                            productPrice = 0;
                        else
                            productPrice = Double.parseDouble(priceString);

                        Product pro = new Product(productItem, productCategory,
                                new Shop(spinner.getSelectedItem().toString()), productPrice);
                        Storage.getInstance().addProduct(pro);
                        Storage.getInstance().getShoppingLists().get(outterGroup).addProduct(pro);
                        addProductExpandable.collapseGroup(groupPosition);

                        // collapse and expand to refresh the list
                        ExpandableListView expandableListView = ((ExpandableListView) addProductExpandable.getParent());
                        expandableListView.collapseGroup(outterGroup);
                        expandableListView.expandGroup(outterGroup);

                        addProductExpandable.refreshDrawableState();

                        hideKeyboard(addProductExpandable);
                    }
                }
            });

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

    private class AddProductExpandable extends ExpandableListView
    {
        private int group;

        public int getGroup()
        {
            return group;
        }

        public AddProductExpandable(Context context, int group)
        {
            super(context);
            this.group = group;
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

    private void hideKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
