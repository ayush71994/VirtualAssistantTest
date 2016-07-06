package in.dezyne.virtualassistant;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AssistFrag.Communicator,ItemFragment.OnListFragmentInteractionListener{

    List<Person> person;
    FragmentManager fb;
    android.support.v4.app.FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FirebaseCrash.report(new Exception("APP started crash exception"));
        fb=getSupportFragmentManager();
        ft=fb.beginTransaction();
        AssistFrag as=new AssistFrag();
        /*DialogFrag frag=new DialogFrag();
        frag.show(getFragmentManager(),"ABCD");
        */
        /*ItemFragment if1=new ItemFragment();
        FragmentManager fb=getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft=fb.beginTransaction();*/
        ft.add(R.id.Rela,as,"AssistFrag");
        ft.commit();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            PermissionUtils.requestPermission((AppCompatActivity) this,1, Manifest.permission.RECORD_AUDIO,true);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            PermissionUtils.requestPermission((AppCompatActivity) this,1, Manifest.permission.READ_CONTACTS,true);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            PermissionUtils.requestPermission((AppCompatActivity) this,1, Manifest.permission.CALL_PHONE,true);
        }
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
          //  return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendPersonList(ArrayList<Person> per) {

        //Contactlist list=new Contactlist();
        person=per;
        ItemFragment if1=new ItemFragment();
        //if1.setContact(per);
        ft=fb.beginTransaction();
        ft.replace(R.id.Rela,if1,"GII");
        ft.addToBackStack("AssistFrag");
        ft.commit();

    }

    @Override
    public void showDialog(Person per) {
        if(per.getPhoneNo().size()==1)
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ per.getPhoneNo().get(0)));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION","NOPE");
                PermissionUtils.requestPermission((AppCompatActivity) this,1, Manifest.permission.RECORD_AUDIO,true);
            }
            startActivity(callIntent);
            return;
        }
        else
        {
        DialogFrag frag=new DialogFrag();
        frag.setPhoneno(per.getPhoneNo());
        frag.show(getFragmentManager(),"CALL Dialog");
         }
    }


    @Override
    public void touched(Person per) {
        if(per.getPhoneNo().size()==1)
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+ per.getPhoneNo().get(0)));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION","NOPE");
                PermissionUtils.requestPermission((AppCompatActivity) this,1, Manifest.permission.RECORD_AUDIO,true);
            }
            startActivity(callIntent);
        }
        else {
            DialogFrag frag = new DialogFrag();
            frag.setPhoneno(per.getPhoneNo());
            frag.show(getFragmentManager(), "CALL Dialog");
        }
    }

    @Override
    public void onListFragmentInteraction(Person item) {
    }
    @Override
    public List<Person> getContact(){
        return person;
    }

}
