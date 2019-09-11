package com.tony.automationserverweb.helper;

import java.util.List;
import java.util.Optional;

import com.tony.automationserverweb.model.Application;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Helper {
    private static final String NUMERIC_STRING = "0123456789";

    public static final String generateOTP() {
        StringBuilder builder = new StringBuilder();
        int count = 5;
        while (count-- != 0) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static final String generateToken() {
        StringBuilder builder = new StringBuilder();
        int count = 15;
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static String Encode(String pass) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        return encoder.encode(pass);
        // try{
        // MessageDigest md = MessageDigest.getInstance("MD5");
        // md.update(pass.getBytes());
        // byte[] digest = md.digest();
        // return DatatypeConverter.printHexBinary(digest).toUpperCase();
        // }catch(Exception ex){
        // return null;
        // }
    }

    public static boolean EncodingMatches(String password, String encodedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        return encoder.matches(password, encodedPassword);
    }

    public static Application getAppFromList(List<Application> apps, final Long id){
        Optional<Application> application = apps.stream().filter(app -> app.getId().equals(id)).findFirst();
        if(application.isPresent())
            return application.get();
        return null;
    }

}