import java.util.HashMap;
import java.util.Map;

/** Represents
 * https://happycoding.io/tutorials/java-server/secure-password-storage
 */
public class SecureStorage {

    private static SecureStorage instance = new SecureStorage();
    private Map<String, String> userMap = new HashMap<>();

    public static SecureStorage getInstance(){
        return instance;
	}

    private SecureStorage(){} // singleton class

    public boolean isUsernameTaken(String username){
		return userMap.containsKey(username);
	}

	public void registerUser(String username, String password){
		userMap.put(username, password);
	}

	public boolean isLoginCorrect(String username, String password) {

		// username isn't registered
		if(!userMap.containsKey(username)){
			return false;
		}

		String storedPassword = userMap.get(username);

		return password.equals(storedPassword);
	}

}
