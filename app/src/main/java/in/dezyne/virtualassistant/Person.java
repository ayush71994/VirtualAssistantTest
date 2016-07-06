package in.dezyne.virtualassistant;

import java.util.ArrayList;

/**
 * Created by Ayush Agarwal on 6/27/2016.
 */
public class Person {
        private String name;
        private ArrayList<String> phoneNo;
        Person()
        {
            name="";
            phoneNo=new ArrayList<>();
        }
        Person(String n,ArrayList<String> p)
        {
            name=n;
            phoneNo=p;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<String> getPhoneNo() {
            return phoneNo;
        }

        public void addPhoneNo(String phoneNo) {
            this.phoneNo.add(phoneNo);
        }


 }

