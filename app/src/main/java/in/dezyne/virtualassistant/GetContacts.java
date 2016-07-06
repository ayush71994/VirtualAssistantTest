package in.dezyne.virtualassistant;

import android.*;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import org.apache.commons.codec.*;
import org.apache.commons.codec.language.*;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ayush Agarwal on 6/27/2016.
 */
public class GetContacts {
    ContentResolver cr;
    Cursor cur;

    GetContacts(Context context) {
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            PermissionUtils.requestPermission((AppCompatActivity) context,1, android.Manifest.permission.CALL_PHONE,true);
        }
        cr = context.getContentResolver();
        cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
    }

    public ContentResolver getCr() {
        return cr;
    }

    public Cursor getCursor() {
        return cur;
    }

    public ArrayList<Person> checkIfContactPresent(String name) {
        cur.moveToFirst();
        if(name.equals(""))
            return null;
        boolean flag=true;
        ArrayList<Person> per=new ArrayList<Person>();

        if (cur.getCount() > 0)
        {
            DoubleMetaphone doubleMetaphone = new DoubleMetaphone();
            while(cur.moveToNext())
            {
                //String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                ArrayList<String> phoneno;
                String contacttemp = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //String[] cont=contacttemp.split("(\\W)+");
                ArrayList<String> cont=new ArrayList<String>(Arrays.asList(contacttemp.split("(\\W)+")));
                boolean fl=false;
                //System.out.println(cont);
                if(cont.size() >0)
                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        for(String contact:cont) {
                            //System.out.println("*"+contact);
                            if(doubleMetaphone.isDoubleMetaphoneEqual(name,contact))
                            {
                                Log.i("Calling:",contacttemp);
                                phoneno=getPhone(cur);
                                if(!(phoneno.size()==0)) {
                                    Person obj = new Person(contacttemp, phoneno);
                                    if(!per.contains(obj)) {
                                        per.add(obj);
                                        flag = false;
                                    }
                                }
                            }
                        /*if (contact.toUpperCase().equals(name.toUpperCase())) {
                            System.out.println("Calling " + contacttemp);
                            fl = true;
                            phoneno=getPhone(cur);
                            if(!phoneno.equals("")) {
                                Person obj = new Person(contacttemp, phoneno);
                                per.add(obj);
                            }

                            //return cur;
                        } else if (Soundex.soundex(contact).equals(Soundex.soundex(name))) {
                            System.out.println("Calling " + contacttemp);
                            fl = true;
                            phoneno=getPhone(cur);
                            if(!phoneno.equals("")) {
                                Person obj = new Person(contacttemp, phoneno);
                                per.add(obj);
                            }
                            //return cur;
                        } else {
                            String sub = CommonSubString.lcsdyn(name.toUpperCase(), contact.toUpperCase());
                            if (sub.length()>3)
                            {
                                System.out.println(sub);
                                if (Soundex.soundex(sub).equals(Soundex.soundex(name))&&Soundex.soundex(sub).equals(Soundex.soundex(contact))) {
                                    fl = true;
                                    System.out.println("sCalling " + contacttemp);
                                    phoneno=getPhone(cur);
                                    if(!phoneno.equals("")) {
                                        Person obj = new Person(contacttemp, phoneno);
                                        per.add(obj);
                                    }
                                    //return cur
                                }
                                }
                        }*/
                        }
                    }
                /*if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Toast.makeText(NativeContentProvider.this, "Name: " + name
                        //        + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                    }
                    pCur.close();*/
            }
        }
        if(flag)
            return null;
        else
            return per;
    }
    public ArrayList<String> getPhone(Cursor cur) {

        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id}, null);
        ArrayList<String> phoneNo=new ArrayList<String>();
        while(pCur.moveToNext()) {
            String no=pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("\\s","");
            if(!phoneNo.contains(no))
            {
                phoneNo.add(no);
            }

        }
        return phoneNo;
    }

}
