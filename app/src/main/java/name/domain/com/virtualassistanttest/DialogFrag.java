package name.domain.com.virtualassistanttest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ayush Agarwal on 6/27/2016.
 */
public class DialogFrag extends DialogFragment implements DialogInterface.OnClickListener{
    List<String> phoneno;
    ArrayAdapter<String> arrayAdapter;


    public void setPhoneno(List<String> p) {
        phoneno =p;
    }//this function need to be called before showing the dialog

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(phoneno.size()==0)
        {
            Log.i("Dialog Frag:","Phone not initialised");
            return null;
        }
        else if(phoneno.size()==1)
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phoneno.get(0)));
            if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION","NOPE");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            startActivity(callIntent);
            return null;
        }
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(phoneno);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Call Which no.").setAdapter(arrayAdapter, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phoneno.get(i)));
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISSION","NOPE");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
}