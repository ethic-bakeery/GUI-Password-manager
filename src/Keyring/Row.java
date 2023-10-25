package Keyring;

import java.io.Serializable;

public class Row implements Serializable{
    public static final int ELEMENT_WEBSITE = 0;
    public static final int ELEMENT_USERNAME = 1;
    public static final int ELEMENT_EMAIL = 2;
    public static final int ELEMENT_PASSWORD = 3;
    public static final int ELEMENT_NOTE = 4;
    
    private static final long serialVersionUID = 1999L;
    private String webSite, username, email, password, note;

    public Row(String webSite, String username, String email, String password, String note) {
        this.webSite = webSite;
        this.username = username;
        this.email = email;
        this.password = password;
        this.note = note;
    }

    public String getElement(int element){
        switch (element){
            case 0:
                return getWebSite();
            case 1:
                return getUsername();
            case 2:
                return getEmail();
            case 3:
                return getPassword();
            case 4:
                return getNote();
            default:
                return null;
        }                
    }
    
    public String getWebSite() {
        return webSite;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNote() {
        return note;
    }     

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
