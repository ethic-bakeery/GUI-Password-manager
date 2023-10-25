/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

/**
 *
 * @author Nino
 */
public class KeyringException extends Exception {
    private String titleMsg; 
    private int typeMessage;
    
    public static final int ERROR_MESSAGE = 0;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int WARNING_MESSAGE = 2;
    public static final int QUESTION_MESSAGE = 3;
    
    /**
     * Creates a new instance of <code>KeyringException</code> without detail
     * message.
     */
    public KeyringException() {
    }

    /**
     * Constructs an instance of <code>KeyringException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @param titleMsg Titolo del messaggio
     * @param typeMessage 0 -> ERROR_MESSAGE, 1 -> INFORMATION_MESSAGE, 2 -> WARNING_MESSAGE, 3 -> QUESTION_MESSAGE
     */
    public KeyringException(String msg, String titleMsg, int typeMessage) {
        super(msg);
        this.typeMessage=typeMessage;
        this.titleMsg=titleMsg;   
    }
    
    public String getTitleMsg() {
        return titleMsg;
    }

    public int getTypeMessage() {
        return typeMessage;
    }
}
