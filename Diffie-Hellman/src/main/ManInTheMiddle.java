package main;

/**
 * A special end-point that can intercept and spy on messages between two other
 * end-points
 * 
 * @author Artem Lavrov
 *
 */
public class ManInTheMiddle extends EndPoint {

    /**
     * Creates a new man in the middle end-point with a name and two parameters for
     * a diffie-hellman key exchange
     * 
     * @param name       - name of this end-point
     * @param publicVar  - the number being chosen as the base
     * @param upperBound - the number which will be used for modulo
     */
    public ManInTheMiddle(String name, int publicVar, int upperBound) {
	super(name, publicVar, upperBound);
	// TODO Auto-generated constructor stub
    }

    /**
     * Intercepts and decrypts a message from end-point 1 to end-point 2, then
     * re-encrypts and forwards the message so everything looks normal
     * 
     * @param message - message to intercept
     * @param forward - whether the message is being send from endpoint 1 to 2 or
     *                the other way around
     * 
     * @return - encrypted version of the message being sent to the receiver
     */
    public String spyOnMessage(String message, boolean forward) {
	int sharedSecret1 = 1;
	int sharedSecret2 = 0;
	if (forward) {
	    sharedSecret1 = 0;
	    sharedSecret2 = 1;
	}

	String decodedMessage = receiveEncryptedMessage(message, sharedSecret1);
	System.out.println(String.format("%s sees: %s", this.getName(), decodedMessage));
	String fwdMessage = sendEncryptedMessage(decodedMessage, sharedSecret2);
	return fwdMessage;
    }
}