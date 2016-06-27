package name.domain.com.virtualassistanttest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ayush Agarwal on 6/27/2016.
 */
public class DialogFrag extends DialogFragment {
    ArrayList<String> phoneno;

    public void setPhoneno(ArrayList<String> ph)
    {
        phoneno=ph;
        int ar=R.array.names;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose no.").setItems(R.array.names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }
}
