package com.hktransfield;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.password4j.Hash;
import com.password4j.Password;

/** Represents user datastore
 *
 * Valid passwords and user IDs are stored here.
 *
 * https://happycoding.io/tutorials/java-server/secure-password-storage
 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
 */
public class UserDatastore {
	// declare class scope variables
    private static UserDatastore instance;
	private UserDatastore(){} // singleton class

	/**
	 * Create UserDatastore object if it does not exist.
	 *
	 * @return singleton instance of a UserDatastore
	 */
    public static UserDatastore getInstance(){
		if(instance == null) instance = new UserDatastore();
        return instance;
	}

	/**
	 * Prevents a new user from creating an account with an
	 * already existing username.
	 *
	 * @param username the chosen username to validate
	 * @return true if username already exists
	 */
    public boolean isUsernameTaken(String username) throws IOException{
		// ensure that it is case insensitive
		username = username.toLowerCase();

		// need a list of all usernames to check against
		List<String> usernames = new ArrayList<String>();

		// get the file from resources
		FileInputStream is = new FileInputStream("users.csv");
		InputStreamReader isr = new InputStreamReader(is);

		CSVParser csvParser = new CSVParserBuilder()
			.withSeparator(',')
			.build();

		CSVReader csvReader = new CSVReaderBuilder(isr)
			.withSkipLines(1)
			.withCSVParser(csvParser)
			.build();

		String[] nextLine;
		while ((nextLine = csvReader.readNext()) != null) {
			if (nextLine != null) {
				usernames.add(nextLine[0]);
			}
		}
		is.close();
		isr.close();
		return usernames.contains(username);
	}

	/**
	 * Stores valid credentials into the database.
	 * All passwords are hashed using Argon2 algorithm
	 * before they are stored.
	 *
	 * https://howtodoinjava.com/java/library/parse-read-write-csv-opencsv/
	 *
	 * @param username a valid username
	 * @param password a valid password
	 */
	public void registerUser(String username, char[] password) throws IOException {
		// ensure that it is case insensitive
		username = username.toLowerCase();

		// generate hash using Argon2
		Hash hash = Password.hash(new String(password)).addRandomSalt(32).addPepper("COMPX518").withArgon2();

		// get file from resources
		// URL fileUrl = UserDatastore.class.getClassLoader().getResource("users.csv");

		// open writer to file
		// CSVWriter writer = new CSVWriter(new FileWriter(fileUrl.getFile(), true));
		CSVWriter writer = new CSVWriter(new FileWriter("users.csv", true));

		// create new string array to append
		String[] credentials = new String[]{username, hash.getResult()};

		writer.writeNext(credentials, false);
		writer.close();
	}

	/**
	 * Verfies if a user's login information is correct by comparing the information contained in the database
	 *
	 * https://howtodoinjava.com/java/library/parse-read-write-csv-opencsv/
	 *
	 * @param username the user's login username
	 * @param password the user's login password
	 * @return true if credentials exist and are correctr
	 */
	public boolean isLoginCorrect(String username, char[] password) throws IOException {
		// ensure that it is case insensitive
		username = username.toLowerCase();

		// store results in a list
		Map<String, String> users = new HashMap<>();

		// setup IO
		FileInputStream is = new FileInputStream("users.csv");
		InputStreamReader isr = new InputStreamReader(is);

		CSVParser csvParser = new CSVParserBuilder()
			.withSeparator(',')
			.withIgnoreQuotations(false)
			.build();

		CSVReader csvReader = new CSVReaderBuilder(isr)
			.withSkipLines(1) // skip headings
			.withCSVParser(csvParser)
			.build();

		String[] nextLine;
		while ((nextLine = csvReader.readNext()) != null) {
			if (nextLine != null) {
				users.put(nextLine[0], nextLine[1]);
			}
		}

		// username isn't registered
		if(!users.containsKey(username)) return false;

		// check that hash is correct
		String storedHash = users.get(username);
		return Password.check(new String(password), storedHash).addPepper("COMPX518").withArgon2();
	}
}
