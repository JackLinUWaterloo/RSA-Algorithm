import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Jack on 7/14/16.
 */
public class RSA {
    public static void main(String[] args) {
        String userMessage;
        String asciiMessage;

        Scanner reader = new Scanner(System.in);
        System.out.println("Enter your message: ");
        userMessage = reader.nextLine();
        asciiMessage = strToASCII(userMessage);
        System.out.println("Your ASCII message is: " + asciiMessage);

        BigInteger[] keys = generateKeys();
        System.out.println("Your public key is: " + keys[0].toString());
        System.out.println("Your private key is: " + keys[1].toString());
        System.out.println("Your modulus is: " + keys[2].toString());

        String cipherText = encryptMessage(asciiMessage, keys[0], keys[2]);
        System.out.println("Your encrypted message is: " + cipherText);
        String originalMessage = decryptMessage(cipherText, keys[1], keys[2]);
        System.out.println("Your decrypted message is: " + asciiToStr(originalMessage));
    }

    public static String strToASCII(String message) {
        StringBuilder asciiStr = new StringBuilder();
        for(int i = 0; i < message.length(); i++) {
            if((int)message.charAt(i) < 100) {
                asciiStr.append("0" + Integer.toString((int)message.charAt(i)));
            } else {
                asciiStr.append(Integer.toString((int)message.charAt(i)));
            }
        }
        return asciiStr.toString();
    }

    public static String asciiToStr(String asciiMessage) {
        StringBuilder returnMessage = new StringBuilder();
        if(asciiMessage.length() % 3 != 0) {
            int counter = asciiMessage.length() % 3;
            if(counter == 2) {
                asciiMessage = "0" + asciiMessage;
            } else {
                asciiMessage = "00" + asciiMessage;
            }
        }
        System.out.println(asciiMessage);
        for(int i = 0; i < asciiMessage.length(); i += 3){
            returnMessage.append((char) Integer.parseInt(asciiMessage.substring(i, i+3)));
        }
        return returnMessage.toString();
    }

    public static BigInteger[] generateKeys() {
        Random rnd = new Random();
        BigInteger p = BigInteger.probablePrime(1000, rnd);
        BigInteger q = BigInteger.probablePrime(1000, rnd);
        BigInteger n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = n.subtract(phiN);
        while(!phiN.gcd(e).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }
        BigInteger d = e.modPow(new BigInteger("-1"), phiN);
        BigInteger[] keys = {e, d, n};
        return keys;
    }

    public static String encryptMessage(String asciiMessage, BigInteger e, BigInteger n) {
        BigInteger M = new BigInteger(asciiMessage);
        BigInteger C = M.modPow(e, n);
        return C.toString();
    }

    public static String decryptMessage(String cipherText, BigInteger d, BigInteger n) {
        BigInteger C = new BigInteger(cipherText);
        BigInteger M = C.modPow(d, n);
        return M.toString();
    }
}