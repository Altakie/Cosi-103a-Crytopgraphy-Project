package main;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * An endpoint that is capable of performing a diffie-hellman key exchange
 * without authentication
 * 
 * @author Artem Lavrov
 *
 */
public class EndPoint {

    protected int publicVar, upperBound;
    private int privateKey;
    private ArrayList<Integer> sharedSecrets;

    private String name;

    /**
     * Creates a new end-point with a name and two parameters for a diffie-hellman
     * key exchange
     * 
     * @param name       - name of the end-point
     * @param publicVar  - the number being chosen as the base
     * @param upperBound - the number which will be used for modulo
     */
    public EndPoint(String name, int publicVar, int upperBound) {
	this.name = name;
	this.publicVar = publicVar;
	this.upperBound = upperBound;

	sharedSecrets = new ArrayList<Integer>();

	Random rand = new Random();

	// Generates a random number between 2 and 2 less than the upper bound
	// (inclusive)
	privateKey = rand.nextInt(upperBound - 3) + 2;
    }

    /**
     * Gets the name of this end-point
     * 
     * @return - the name of this end-point
     */
    public String getName() {
	return name;
    }

    /**
     * Creates a variable that can be shared with the other side of the exchange
     * without leaking this server's private variable
     * 
     * @return - the variable to be shared with the other side of the exchange
     */
    public int getOtherVar() {
	return publicVar ^ privateKey % upperBound;
    }

    /**
     * Establishes a shared secret number using the variable shared by the other
     * side of the exchange
     * 
     * @param OtherVar - the variable shared by the other side of the exchange
     */
    public void establishSharedSecret(int OtherVar) {
	sharedSecrets.add(((OtherVar ^ privateKey) % upperBound) % 26);
    }

    /**
     * Takes in a string and encrypts it with a caesar cipher using the shared
     * secret key
     * 
     * @param message      - message to encrypt
     * @param sharedSecret - sharedSecret
     * @return - encrypted message
     */
    protected String sendEncryptedMessage(String message, int sharedSecret) {
	return rotN(sharedSecrets.get(sharedSecret), message);
    }

    /**
     * Receives an encrypted message and decodes it using the shared secret
     * 
     * @param message      - encrypted message received
     * @param sharedSecret - sharedSecret
     * @return - decrypted message
     */
    protected String receiveEncryptedMessage(String message, int sharedSecret) {
	return rotN(26 - sharedSecrets.get(sharedSecret), message);
    }

    /**
     * Allows the user to send a message as this endpoint and logs it for
     * demonstration purposes
     */
    public String sendLoggedMessage() {
	System.out.print(String.format("%s: ", name));
	Scanner uI = new Scanner(System.in);
	String result = sendEncryptedMessage(uI.nextLine(), 0);
	return result;
    }

    /**
     * Receives an encrypted message as the current endpoint and logs it
     * 
     * @param message - encrypted message that is received by this endpoint
     */
    public void receiveLoggedMessage(String message) {
	System.out.println(String.format("%s sees: %s", name, receiveEncryptedMessage(message, 0)));
    }

    /**
     * Encrypts the message using a rotation cipher
     * 
     * @param rotCount - amount of rotations the message will be encrypted with
     * @param message  - message to encrypt
     * @return - encrypted message
     */
    private String rotN(int rotCount, String message) {
	if (rotCount == 0) {
	    rotCount = 13;
	}
	String result = "";
	for (int i = 0; i < message.length(); i++) {
	    char currChr = message.charAt(i);

	    if (Character.isLowerCase(currChr)) {
		currChr += rotCount;
		if (currChr > 122) {
		    currChr = (char) (currChr % 123 + 97);
		}
	    } else if (Character.isUpperCase(currChr)) {
		currChr += rotCount;
		if (currChr > 90) {
		    currChr = (char) (currChr % 91 + 65);
		}
	    }

	    result += currChr;
	}

	return result;
    }

}
