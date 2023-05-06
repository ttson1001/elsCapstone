package capstone.elsv2.sercurity;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Random;


public class Utilities {
    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
    private static Random generator = new Random();

    public static String calculateHMac(String data, String algorithm, String key) throws Exception {
        Mac Hmac = Mac.getInstance(algorithm);
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);
        Hmac.init(secret_key);
        return byteArrayToHex(Hmac.doFinal(data.getBytes("UTF-8")));
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static String randomAlphaNumeric(int numberOfChart) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfChart; i++) {
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return "ELS"+sb.toString();
    }

    private static int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }


}
