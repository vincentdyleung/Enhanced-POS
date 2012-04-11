package core.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserList {
	private static UserList instance;
	private HashMap<String, String> users;
	private boolean initialized;
	
	private UserList(){
		this.users = new HashMap<String, String>();
		this.initialized = false;
	}
	
	public static UserList getInstance(){
		if(instance == null){
			instance = new UserList();
		}
		return instance;
	}
	
	public boolean loadUsers(String fullPath){
		try{
			if(this.initialized){
				Logger.getAnonymousLogger().log(Level.SEVERE, "ItemList already initialized!");
				return false;
			}
			int userCount = 0;
			BufferedReader bReader;
			bReader = new BufferedReader(new FileReader(fullPath));
			String line = bReader.readLine();
			while(line != null) {
				String[] temp = line.split(" ");
				// a line should be formatted like "userName password"
				if (temp.length == 2){
					String uId = temp[0];
					String password = temp[1];
					this.addUser(uId, password);
					userCount++;
				}else {
					Logger.getAnonymousLogger().log(Level.SEVERE, "The user-password map file is wrong formatted!");
					return false;
				}
				line = bReader.readLine();
			}
			bReader.close();
			if(userCount < 1){
				Logger.getAnonymousLogger().log(Level.SEVERE, "No user information found in the file!");
				return false;
			}else{
				initialized = true;
				return true;
			}
		}catch(Exception e){
			Logger.getAnonymousLogger().log(Level.SEVERE, "Load userList failed: " + e.getMessage());
			return false;
		}
	}
	
	private void addUser(String id, String password){
		if(id != null && password != null 
				&& id.length() != 0 && password.length() != 0){
			users.put(id, password);
		}
	}
	
	public boolean verifyUser(String id, String password){
		if(id != null && password != null 
				&& id.length() != 0 && password.length() != 0){
			String psw = users.get(id);
			if(psw != null){
				return psw.equals(password);
			}
		}
		return false;
	}
	
	public boolean isInitialized(){
		return this.initialized;
	}
}
