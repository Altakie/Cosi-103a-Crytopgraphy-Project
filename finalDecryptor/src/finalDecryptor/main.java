package finalDecryptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class main {
	
	public static void main(String[] args) throws FileNotFoundException {
		HashMap<String, Boolean> map = constructMap();
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to the basic decryptor. Please input the text that you would like to attempt to decrypt.");
		String decode = scan.nextLine();
		boolean found = caesarCipher(decode, map);
		if (!found) {
			vigenereCipherRealWordKey(decode, map);
			vigenereCipherRandomKey(decode, map);
		}
	}
	
	public static HashMap<String, Boolean> constructMap() throws FileNotFoundException{
		Scanner file = new Scanner(new File("EnglishWords"));
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		while (file.hasNext()) {				
			map.put(file.next(), true);
		}
		return map;
	}
	
	public static boolean caesarCipher(String decode, HashMap<String, Boolean> map) {
		boolean found = false;
		for (int mod = 0; mod < 26; mod++) {
			String input = decode;
			input = input.toUpperCase(); //Modify message to all caps, otherwise ascii codes could be different
			for (int i = 0; i<input.length(); i++) {//Go through the whole message, one character at a time
				int ascii = input.charAt(i); //Store the ascii code of the character being looked at
				if (ascii > 48 && ascii < 91) { //If statement to avoid all characters other than letters
					while (ascii + mod > 90) { //Wrap around so that no matter how big the encoding key, will repeat until the character becomes a valid letter
						ascii -= 26;
					}
					while (ascii + mod < 65) {
						ascii += 26;
					}
					//Rewrites the string, replacing the current character with the replaced letter
					input = input.substring(0, i) + (char) (ascii + mod) + input.substring(i+1, input.length());
				}
			}
			if (checkString(input, map)) {
				System.out.println("The code could possibly be broken with a caesar cipher with a key of " + mod 
						+ " and a final decrypted message of " + input);
				found = true;
			}
		}
		if (!found) {
			System.out.println("It doesn't look like the sequence was encrypted with a caesar cipher.");
		}
		return found;
	}
	
	public static boolean vigenereCipherRealWordKey(String decode, HashMap<String, Boolean> map) throws FileNotFoundException {
		boolean found = false;
		Scanner file = new Scanner(new File("EnglishWords"));
		while (file.hasNext()) {				
			String key = file.next().toUpperCase();
			String input = decode;
			int indexTracker = 0;
			input = input.toUpperCase(); //Modify message to all caps, otherwise ascii codes could be different
			for (int i = 0; i<input.length(); i++) {//Go through the whole message, one character at a time
				int mod = key.charAt(indexTracker % key.length()) - 65;
				int ascii = input.charAt(i); //Store the ascii code of the character being looked at
				if (ascii > 64 && ascii < 91) { //If statement to avoid all characters other than letters
					while (ascii - mod > 90) { //Wrap around so that no matter how big the encoding key, will repeat until the character becomes a valid letter
						ascii -= 26;
					}
					while (ascii - mod < 65) {
						ascii += 26;
					}
					input = input.substring(0, i) + (char) (ascii - mod) + input.substring(i+1, input.length());
					indexTracker++;
				}
			}
			if (checkString(input, map)) {
				System.out.println("The code could possibly be broken with a Vigenere cipher with a key of " + key 
						+ " and a final decrypted message of " + input);
				found = true;
			}
		}
		if (!found) {
			System.out.println("It doesn't look like the sequence was encrypted with a Vigenere cipher using an English word as a key.");
		}
		return found;
		
	}
	
	public static boolean vigenereCipherRandomKey(String decode, HashMap<String, Boolean> map) throws FileNotFoundException {
		boolean found = false;
		createRandomKeysFile();
		Scanner file = new Scanner(new File("RandomKeysFile.txt"));
		while (file.hasNext()) {				
			String key = file.next().toUpperCase();
			String input = decode;
			int indexTracker = 0;
			input = input.toUpperCase(); //Modify message to all caps, otherwise ascii codes could be different
			for (int i = 0; i<input.length(); i++) {//Go through the whole message, one character at a time
				int mod = key.charAt(indexTracker % key.length()) - 65;
				int ascii = input.charAt(i); //Store the ascii code of the character being looked at
				if (ascii > 64 && ascii < 91) { //If statement to avoid all characters other than letters
					while (ascii - mod > 90) { //Wrap around so that no matter how big the encoding key, will repeat until the character becomes a valid letter
						ascii -= 26;
					}
					while (ascii - mod < 65) {
						ascii += 26;
					}
					input = input.substring(0, i) + (char) (ascii - mod) + input.substring(i+1, input.length());
					indexTracker++;
				}
			}
			if (checkString(input, map)) {
				System.out.println("The code could possibly be broken with a Vigenere cipher with a key of " + key 
						+ " and a final decrypted message of " + input);
				found = true;
			}
		}
		if (!found) {
			System.out.println("It doesn't look like the sequence was encrypted with a Vigenere cipher using a key of length 5 or less.");
		}
		return found;
		
	}
	
	public static void createRandomKeysFile() {
		File f = new File("RandomKeysFile.txt");
		FileWriter myWriter;
		
		try {
			myWriter = new FileWriter("RandomKeysFile.txt");

			for (int a = 0; a < 26; a++) {
				for (int b = 0; b < 26; b++) {
					myWriter.write((char) (a + 65) + "" + (char) (b + 65) + "\n");
				}
			}
		      
			for (int a = 0; a < 26; a++) {
				for (int b = 0; b < 26; b++) {
					for (int c = 0; c < 26; c++) {
						myWriter.write((char) (a + 65) + "" + (char) (b + 65) + (char) (c + 65) +  "\n");
					}
				}
			}
			
			for (int a = 0; a < 26; a++) {
				for (int b = 0; b < 26; b++) {
					for (int c = 0; c < 26; c++) {
						for (int d = 0; d < 26; d++) {
							myWriter.write((char) (a + 65) + "" + (char) (b + 65) + (char) (c + 65) + (char) (d + 65) +  "\n");
						}
					}
				}
			}
			
			for (int a = 0; a < 26; a++) {
				for (int b = 0; b < 26; b++) {
					for (int c = 0; c < 26; c++) {
						for (int d = 0; d < 26; d++) {
							for (int e = 0; e < 26; e++) {
								myWriter.write((char) (a + 65) + "" + (char) (b + 65) + (char) (c + 65) + (char) (d + 65) + (char) (e + 65) + "\n");
							}
						}
					}
				}
			}
			
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkString(String input, HashMap<String, Boolean> map) {
		String[] words = input.split(" ");
		int validWords = 0;
		for (String w : words) {
			w = w.replaceAll("[^a-zA-Z]", "");
			if (map.get(w.toLowerCase()) != null) {
				validWords++;
			}
		}
		if ((double) validWords / (double) words.length > 0.75) {
			return true;
		}
		return false;
	}
}