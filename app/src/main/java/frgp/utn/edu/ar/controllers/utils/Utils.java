package frgp.utn.edu.ar.controllers.utils;

public class Utils {
    public static final  String EMAIL = "comunidad.activa.utn@gmail.com";
    public static final String MAIL_TOKEN = "zfat ccgi pvpc yhto";

    public static String generateToken() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }



}
