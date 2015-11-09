package com.asap.messenger.bo;
import java.lang.*;

/**
 * The Contact Java Class is used to store the information related to Contact information of a Person - either Sender or Receiver
 * @author  Deepa Raveendran , Umadevi Samudrala
 * @version 1.0
 * @since 10/15/2015
 */
public class Contact {

    /*Attributes to store the contact information*/
    private String contactName;
    private String phoneNumber;
    private String email;

    /*Getters and Setters*/
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
