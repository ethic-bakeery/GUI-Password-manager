package Keyring;


import Exceptions.KeyringException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.regex.Pattern;


public class Keyring {    
    public static final double version = 1.2;
    public static final String author = "bakeery";
    
    private static final String nameFile = "keyring";
        
    private String MasterKey;
    private LinkedList<Row> tableKeys;

   
    public static boolean checkFile() throws KeyringException{ 
        System.out.print("checking the existence of the file containing the passwords...");
        if(!new File(nameFile).exists()){
            System.out.println("Not found.");
            throw new KeyringException(
                "The file containing the passwords was not found.\nEnter a master key, store your passwords and save to create a new file.\n",
                "File not found.",
                KeyringException.INFORMATION_MESSAGE
            );
        }        
        System.out.println("Found.");
        return true;
    }
    
    public Keyring(String MasterKey) throws KeyringException{        
        setMasterKey(MasterKey);
        load();
    }
          
    private String verifyMasterKey(String masterKey) throws KeyringException{
        System.out.print("verifying the password is secure...   ");
        
        String regex1 = "^((?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!-.,;:@#$%^&*?_~])([A-Za-z\\d!-.,;:@#$%^&*?_~ ]\\2?(?!\\2)){10,200})$"; 
        if(Pattern.matches(regex1, masterKey)){
            System.out.println("A good password! Bravo.");
            return masterKey;
        }
        System.out.println("Password not secure!");
        throw new KeyringException(
            "Password entered: " + masterKey +
            "\n\nYour password must contain at least:  \n\n"+
            " - an uppercase character\n - a lowercase character\n - a numeric character\n - a special character\n- more than 10 characters\n\n" +
            "Must not contain consecutive identical characters. \n\n",
            "Attention!",
            KeyringException.INFORMATION_MESSAGE
        );
    }

    public void setMasterKey(String MasterKey) throws KeyringException {        
        this.MasterKey = verifyMasterKey(MasterKey);
    }
    
    public LinkedList<Row> getTableKeys() {
        return tableKeys;
    }
    
    
    public void addRow(String webSite, String username, String email, String password, String note) throws Exception{
        System.out.print("add a new line...   ");
        if (webSite.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()){
            System.out.println("Enter the Website, the email used, the username used and the password."); 
            throw new KeyringException("Enter the Website, the email used, the username used and the password.","Attention!",KeyringException.INFORMATION_MESSAGE);
        }
        Row r = new Row(webSite, username, email, password, note);
        tableKeys.add(r); 
        System.out.println("Completed.");
    }
    
    public void editRow(int currentIndex, String webSite, String username, String email, String password, String note) throws Exception{
        System.out.print("edit the line " + currentIndex + "...   ");
        if (currentIndex < 0 || currentIndex > tableKeys.size()-1){
            System.out.println("Select a table row."); 
            throw new KeyringException("Select a table row.","Attention",KeyringException.INFORMATION_MESSAGE);
        }
        if (webSite.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()){
            System.out.println("Enter the Website, the email used, the username used and the password."); 
            throw new KeyringException("Enter the Website, the email used, the username used and the password.","Attention!",KeyringException.INFORMATION_MESSAGE);
        }
        Row r = tableKeys.get(currentIndex); 
        r.setWebSite(webSite);
        r.setUsername(username);
        r.setEmail(email);
        r.setPassword(password);
        r.setNote(note);
        
        System.out.println("Completed.");
    }
    public void removeRow(int currentIndex) throws KeyringException{
        System.out.print("remove the line " + currentIndex + "...   ");        
        if (currentIndex < 0 || currentIndex > tableKeys.size()-1){
            System.out.println("Select a table row."); 
            throw new KeyringException("Select a table row.","Attention",KeyringException.INFORMATION_MESSAGE);
        } 
        tableKeys.remove(currentIndex);
        System.out.println("Completed.");
    }
    public void moveUpRow(int currentIndex) throws KeyringException{
        System.out.print("bring up the line " + currentIndex + "...   ");      
        if (currentIndex < 0 || currentIndex > tableKeys.size()-1){
            System.out.println("Select a table row."); 
            throw new KeyringException("Select a row of the table.","Attention!",KeyringException.INFORMATION_MESSAGE);
        } 
        if (currentIndex == 0){
            System.out.println("The line is at the top.");      
            throw new KeyringException("The line is at the top.","Attention!",KeyringException.INFORMATION_MESSAGE);
        }
        
        Row r = tableKeys.get(currentIndex);
        tableKeys.set(currentIndex, tableKeys.get(currentIndex-1));        
        tableKeys.set(currentIndex-1, r);
        System.out.println("Completed.");
    }
    public void moveDownRow(int currentIndex) throws KeyringException{
        System.out.print("bring the line down " + currentIndex + "...   ");      
        if (currentIndex < 0 || currentIndex > tableKeys.size()-1){
            System.out.println("Select a table row."); 
            throw new KeyringException("Select a table row.","Attention!",KeyringException.INFORMATION_MESSAGE);
        } 
        if (currentIndex == tableKeys.size()-1){
            System.out.println("The line is at the base.");                    
            throw new KeyringException("The line is at the base.","Attention!",KeyringException.INFORMATION_MESSAGE);
        }
        
        Row r = tableKeys.get(currentIndex);
        tableKeys.set(currentIndex, tableKeys.get(currentIndex+1));    
        tableKeys.set(currentIndex+1,r); 
        System.out.println("Completed");
    }
    public void copyToClipboard(int currentIndex, int rowElement) throws KeyringException{
        System.out.print("copy the cell (" + currentIndex + ", " + rowElement + ")...   ");      
        if (currentIndex < 0 || currentIndex > tableKeys.size()-1){
            System.out.println("Select a table row."); 
            throw new KeyringException("Select a table row.","Attention!",KeyringException.INFORMATION_MESSAGE);
        } 
        String element = tableKeys.get(currentIndex).getElement(rowElement);
        if(element == null){
            System.out.println("Invalid element choice."); 
            throw new KeyringException("Invalid element choice.","Error",KeyringException.ERROR_MESSAGE);
        }
        StringSelection stringSelection = new StringSelection(element);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        System.out.println("Copied.");
    }    
    public String save() throws KeyringException{ 
        System.out.println("Saving the file containing the passwords...");                
        CryptoUtils.encrypt(nameFile, MasterKey, tableKeys);         
        System.out.println("Saved Sucessfully.");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
        return sdf.format(System.currentTimeMillis());         
    }
    private void load() throws KeyringException{ 
        System.out.println("uploading the file containing the passwords...");
                
        FileInputStream fi = null;
        try {
            tableKeys = CryptoUtils.dencrypt(nameFile, MasterKey);  
            System.out.println("Loading successful.");
        } catch (KeyringException ex) {            
            tableKeys=new LinkedList();
            System.out.println("Error: "+ ex.getMessage() + "File upload failed.");
        } catch (StreamCorruptedException ex) {
            System.out.println("Error: "+ ex.getMessage() + "Invalid password.\nFile upload failed.");
            throw new KeyringException("Incorrect password. The file cannot be opened.", "The file cannot be opened.", KeyringException.WARNING_MESSAGE);
        }
    }
    
    public String getEditDateFile(){
        File file = new File(nameFile);	
        if (file.exists()) {            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");		
            return sdf.format(file.lastModified());
        }
        return "File was not saved.";
    }
}
