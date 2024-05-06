package Service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import Model.UserModel;

@Stateless
public class UserService {
    

	public static String email;
	public static String password;
	public static String username;
	
	
    String emailPattern = "@gmail.com";
    @PersistenceContext(unitName = "hello")
    private EntityManager EM;
    
    public String register(UserModel account) {
        
        String email = account.getEmail();
        String userName = account.getUserName();
        String password = account.getPassword();
        
        try {
            account.setEmail(account.getEmail());
            if (!account.getEmail().contains(emailPattern)) {
                return "invalid email!";
            }
            if (email.isEmpty() || userName.isEmpty()) {
                return "username or email can not be empty!";
            }
            account.setUserName(userName);
            
            if (existedByUserName(userName) || existedByEmail(email)) {
                return "username or email already existed!";
            }
            if (!validPassword(password)) {
                return "Invalid password, the password must have at least one uppercase letter and at least 8 characters";
            }
            account.setPassword(password);
            if (password.isEmpty()) {
                return "Password can not be empty!";
            }
            
            
            UserService.email = email;
            UserService.username = userName;
            UserService.password = password;
            
            EM.persist(account);
            
            return "added!";
        } catch (Exception e) {
            return "Error occurred!";
        }
    }
    
    

    
    public boolean existedByUserName(String userName) {
        List<UserModel> similarUserNames = EM.createQuery("SELECT a FROM UserModel a WHERE a.userName = :userName", UserModel.class)
                                           .setParameter("userName", userName).getResultList();
        return !similarUserNames.isEmpty();
    }
    
    
    
    
    public boolean existedByEmail(String email) {
        List<UserModel> similarEmails = EM.createQuery("SELECT a FROM UserModel a WHERE a.email = :email", UserModel.class)
                                        .setParameter("email", email).getResultList();
        return !similarEmails.isEmpty();
    }
    
    
    
    
    public List<UserModel> getAllAccounts() {
        return EM.createQuery("SELECT a FROM UserModel a", UserModel.class).getResultList();
    }
    
    
    
    
    public String loginWithEmail(String email, String password) {
        try {
        	if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return "email or password cannot be empty";}
        	UserModel account = EM.createQuery("SELECT a FROM UserModel a WHERE a.email = :email", UserModel.class)
                                .setParameter("email", email)
                                .getSingleResult();
            
            if (account != null && account.getEmail().equals(email) && account.getPassword().equals(password)) {
                
                UserService.email = email;
                UserService.username = account.getUserName();
                UserService.password = password;
                return "Valid login!";
            } else {
                return "Invalid email or password";
            }
        } catch (NoResultException e) {
            return "Not found, Invalid email or password";
        }
    }
    
    
    
    
    public String loginWithUserName(String userName, String password) {
        try {
        	
        	if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
                return "Username or password cannot be empty";}
        	UserModel account = EM.createQuery("SELECT a FROM UserModel a WHERE a.userName = :userName", UserModel.class)
                                .setParameter("userName", userName)
                                .getSingleResult();
            
            if (account != null && account.getUserName().equals(userName) && account.getPassword().equals(password)) {
                UserService.email = account.getEmail();
                UserService.username = userName;
                UserService.password = password;
                return "Valid login!";
            } else {
                return "Invalid username or password";
            }
        } catch (NoResultException e) {
            return "Not found ,Invalid username or password";
        }
    }
    
    
    
    public String updateEmail(String email, String updatedEmail, String password) {
        try {
        	
        	if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return "email or password cannot be empty";}
        	UserModel account = EM.createQuery("SELECT a FROM UserModel a WHERE a.email = :email", UserModel.class)
                                .setParameter("email", email)
                                .getSingleResult();
            if (account != null && account.getEmail().equals(email) && account.getPassword().equals(password)) {
                account.setEmail(updatedEmail);
                if (!updatedEmail.contains(emailPattern) || updatedEmail == null || updatedEmail.isEmpty()) {
                    return "Valid login, updated email is Invalid, Try entering another email..";
                }
                if (existedByEmail(email)) {
                    return "Valid login, updated email is already existed, Try entering another email..";
                }
                UserService.email = account.getEmail();
                return "Valid login, Your email has been updated to : " + updatedEmail;
            } else {
                return "Invalid email or password, email can't be changed";
            }
        } catch (NoResultException e) {
            return "Account not found";
        }
    }
    
    
    
    public String updateUserName(String userName, String updatedUserName, String password) {
        try {
        	if(updatedUserName == null || updatedUserName.isEmpty() )
        		return "updated username can not be null";
    
        	if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
                return "userName or password cannot be empty";}
        	UserModel account = EM.createQuery("SELECT a FROM UserModel a WHERE a.userName = :userName", UserModel.class)
                                .setParameter("userName", userName)
                                .getSingleResult();

            if (account != null && account.getUserName().equals(userName) && account.getPassword().equals(password)) {
                if (existedByUserName(updatedUserName)) {
                    return "Valid login, updated username is already existed, Try entering another username..";
                }
                account.setUserName(updatedUserName);
                UserService.username = account.getUserName();
                return "Valid login, your username is updated to : " + updatedUserName;
            } else {
                return "Invalid username or password , username can't be changed";
            }
        } catch (NoResultException e) {
            return "Account not found";
        }
    }

    
    
    
    public String updatePassword(String email, String password, String newPassword) {
        try {
        	
        	if(newPassword == null || newPassword.isEmpty())
        		return "new password can not be empty";
        	if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return "email or password cannot be empty";}
        	
        	UserModel account = EM.createQuery("SELECT a FROM UserModel a WHERE a.email = :email", UserModel.class)
                                .setParameter("email", email)
                                .getSingleResult();

            if (account != null && account.getEmail().equals(email) && account.getPassword().equals(password)) {
                if (!validPassword(newPassword)) {
                    return "Valid login! The new password is invalid , Try entering another password..";
                }
                account.setPassword(newPassword);
                UserService.password = account.getPassword();
                return "Valid login, your new password is :" + newPassword;
            } else {
                return "Invalid email or password";
            }
        } catch (NoResultException e) {
            return "Account not found";
        }
    }

    
    private boolean validPassword(String password) {
        boolean flag = false;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                flag = true;
                break;
            }
            continue;
        }
        return password.length() >= 8 && flag;
    }
    
    
    public String deleteAccount(String email, String password) {
        try {
        	
        	if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return "email or password cannot be empty";}
            UserModel account = EM.createQuery("SELECT a FROM UserModel a WHERE a.email = :email", UserModel.class)
                                .setParameter("email", email)
                                .getSingleResult();
            
            if (account != null && account.getEmail().equals(email) && account.getPassword().equals(password)) {
                EM.remove(account);
                return "Account deleted successfully.";
            } else {
                return "Invalid email or password.";
            }
        } catch (NoResultException e) {
            return "Account not found.";
        }
    }
}