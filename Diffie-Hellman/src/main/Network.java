package main;

import java.util.Random;
import java.util.Scanner;

/**
 * A simulation of a network over which a diffie-hellman key exchange is made
 * 
 * @author Artem Lavrov
 *
 */
public class Network {

    private static int upperBound = 31;
    private static int publicVar;

    /**
     * @param args
     */
    public static void main(String[] args) {
	Random rand = new Random();
	publicVar = rand.nextInt(2, upperBound - 1);

	// TODO Auto-generated method stub
	System.out.println("Hello! What kind of simulation would you like to run today?");
	System.out.println("Press 0 to exit the simulation");
	System.out.println("Press 1 for normal diffie-hellman simulation");
	System.out.println("Press 2 for simulated man in the middle attack on a diffie-hellman key exchange");
	Scanner simPicker = new Scanner(System.in);

	boolean retry = true;
	while (retry) {
	    retry = false;

	    String choice = simPicker.nextLine();
	    switch (choice.trim()) {
	    case "0":
		System.out.println("Have a nice day!");
		break;
	    case "1":
		runNormalSimulation();
		break;
	    case "2":
		simulateManInTheMiddle();
		break;
	    default:
		retry = true;
		System.out.println("Please input a valid choice");
		break;
	    }
	}
	simPicker.close();

    }

    /**
     * Logs a message that is visible to users on the network
     * 
     * @param message - message to log
     */
    private static void logMessage(String message) {
	System.out.println("What users on the network see: " + message);
    }

    /**
     * Simulates a diffie-hellman key change between two endpoints
     * 
     * @param e1 - end-point 1
     * @param e2 - end-point 2
     */
    private static void keyExchange(EndPoint e1, EndPoint e2) {
	e1.establishSharedSecret(e2.getOtherVar());
	logMessage(String.format("%d", e2.getOtherVar()));
	e2.establishSharedSecret(e1.getOtherVar());
	logMessage(String.format("%d", e1.getOtherVar()));
	System.out.println(String.format("Connections between %s and %s established!", e1.getName(), e2.getName()));
    }

    /**
     * Creates an encrypted connection between two endpoints using diffie-hellman
     * key exchange
     */
    private static void runNormalSimulation() {
	EndPoint alice = new EndPoint("Alice", publicVar, upperBound);
	EndPoint bob = new EndPoint("Bob", publicVar, upperBound);

	keyExchange(alice, bob);

	Scanner closer = new Scanner(System.in);
	while (true) {
	    String message1 = alice.sendLoggedMessage();
	    logMessage(message1);
	    bob.receiveLoggedMessage(message1);
	    String message2 = bob.sendLoggedMessage();
	    logMessage(message2);
	    alice.receiveLoggedMessage(message2);

	    System.out.print("Keep sending messages (y/n)?");
	    if (closer.nextLine().trim().equals("n")) {
		System.out.println("Thank you for running this simulation");
		closer.close();
		break;
	    }
	}
    }

    /**
     * Simulates a man in the middle attack on a diffie-hellman key exchange
     */
    private static void simulateManInTheMiddle() {
	EndPoint alice = new EndPoint("Alice", publicVar, upperBound);
	EndPoint bob = new EndPoint("Bob", publicVar, upperBound);

	ManInTheMiddle dylan = new ManInTheMiddle("Dylan", publicVar, upperBound);

	keyExchange(alice, dylan);
	keyExchange(dylan, bob);

	Scanner closer = new Scanner(System.in);
	while (true) {
	    String message1 = alice.sendLoggedMessage();
	    logMessage(message1);
	    String interceptedMessage1 = dylan.spyOnMessage(message1, true);
	    logMessage(interceptedMessage1);
	    bob.receiveLoggedMessage(interceptedMessage1);

	    String message2 = bob.sendLoggedMessage();
	    logMessage(message2);
	    String interceptedMessage2 = dylan.spyOnMessage(message2, false);
	    logMessage(interceptedMessage2);
	    alice.receiveLoggedMessage(interceptedMessage2);

	    System.out.print("Keep sending messages (y/n)?");
	    if (closer.nextLine().trim().equals("n")) {
		System.out.println("Thank you for running this simulation");
		closer.close();
		break;
	    }
	}
    }
}
