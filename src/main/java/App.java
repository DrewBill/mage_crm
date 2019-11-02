import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class App {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String s="123456";
        MessageDigest messageDigest = MessageDigest.getInstance("md5");
        byte[] digest = messageDigest.digest(s.getBytes());
        System.out.println(Base64.encodeBase64String(digest));
    }
}
