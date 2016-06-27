package name.domain.com.virtualassistanttest;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import org.apache.commons.codec.*;
import org.apache.commons.codec.language.*;
import android.provider.ContactsContract;
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
                                if(!phoneno.equals("")) {
                                    Person obj = new Person(contacttemp, phoneno);
                                    per.add(obj);
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
            phoneNo.add(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

        }
        return phoneNo;
    }

}
