package com.hktransfield;
import java.util.HashMap;
import java.util.Map;

import com.password4j.Hash;
import com.password4j.Password;

/** Represents
 * https://happycoding.io/tutorials/java-server/secure-password-storage
 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
 */
public class UserDatabase {
	// declare class scope variables
    private static UserDatabase instance;
    private Map<String, Hash> userDB = new HashMap<>();
	private UserDatabase(){} // singleton class

	/**
	 * Create UserDatabase object if it does not exist.
	 *
	 * @return
	 */
    public static UserDatabase getInstance(){
		if(instance == null) instance = new UserDatabase();
        return instance;
	}

	/**
	 * Prevents a new user from creating an account with an
	 * already existing username.
	 *
	 * @param username the chosen username to validate
	 * @return true if username already exists
	 */
    public boolean isUsernameTaken(String username){
		return userDB.containsKey(username);
	}

	/**
	 *
	 * @param username
	 * @param password
	 */
	public void registerUser(String username, char[] password){
		Hash hash = Password.hash(new String(password)).addRandomSalt(42).addPepper("COMPX518").withArgon2();
		userDB.put(username, hash);
	}

	/**
	 *
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean isLoginCorrect(String username, char[] password) {

		// username isn't registered
		if(!userDB.containsKey(username)){
			System.out.println("Can't find username");
			return false;
		}

		Hash storedPassword = userDB.get(username);
		boolean verified = Password.check(new String(password), storedPassword);

		return verified;
	}

}
