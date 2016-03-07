

package blelll.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;

import Model.Shop;
import Model.Storage;

/**
 * Created by Andy on 05/03/2016.
 */
public class ShopListDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        ArrayList<Shop> shops = Storage.getInstance().getShops();
        ArrayList<String> shopsName = new ArrayList<>();
        for(Shop shop :shops)
            shopsName.add(shop.toString());

        builder.setTitle(R.string.get_shops)
                .setItems(shopsName.toArray(new String[shopsName.size()]), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ShopListDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

