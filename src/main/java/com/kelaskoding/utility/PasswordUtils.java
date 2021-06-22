/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kelaskoding.utility;

import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author jarvis
 */
public class PasswordUtils {
    
    public static String digestPassword(String plainText){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plainText.getBytes("UTF-8"));
            byte[] passwordDigest = md.digest();
            return new String(Base64.getEncoder().encode(passwordDigest));
        }catch(Exception ex){
            throw new RuntimeException("Exception encoding password");
        }
    }
    
}
