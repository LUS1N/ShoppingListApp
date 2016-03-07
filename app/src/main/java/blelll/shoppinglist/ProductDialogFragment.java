package blelll.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import Model.Product;
import Model.Shop;
import Model.Storage;

/**
 * Created by L on 3/7/2016.
 */
public class ProductDialogFragment extends DialogFragment
{
    private int group;
    private BaseExpandableListAdapter baseExpandableListAdapter;

    public void setBaseExpandableListAdapter(BaseExpandableListAdapter baseExpandableListAdapter)
    {
        this.baseExpandableListAdapter = baseExpandableListAdapter;
    }

    public void setGroup(int group)
    {
        this.group = group;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View shopDialogView = inflater.inflate(R.layout.add_product_inputs, null);

        // setup spinner and its adapter
        final Spinner spinner = (Spinner) shopDialogView.findViewById(R.id.shop_spinner);

        ArrayAdapter<Shop> shopsAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                Storage.getInstance().getShops());
        spinner.setAdapter(shopsAdapter);

        builder.setView(shopDialogView)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // get title
                        EditText title = (EditText) shopDialogView.findViewById(
                                R.id.new_product_title);
                        String titleString = title.getText().toString();

                        // parse price
                        String priceString = ((EditText) shopDialogView.findViewById(
                                R.id.new_product_price)).getText().toString();
                        double priceDouble = priceString.isEmpty() ? 0.0 : Double.parseDouble(
                                priceString);


                        Spinner shop = (Spinner) shopDialogView.findViewById(R.id.shop_spinner);
                        String shopString = shop.getSelectedItem().toString();

                        if (!titleString.isEmpty())
                        {
                            Storage storage = Storage.getInstance();
                            storage.getShoppingLists().get(group).addProduct(
                                    new Product(titleString, null, storage.getShop(shopString),
                                            priceDouble));

                            baseExpandableListAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ProductDialogFragment.this.getDialog().cancel();
                    }
                });


        return super.onCreateDialog(savedInstanceState);
    }
}
