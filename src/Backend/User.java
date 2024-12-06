
package Backend;

/**
 *
 * @author User
 */
public class User {
     private String userid;
   private String email;
   private String username;
   private String password; //Hashed password
   private String dateOfBirth;
   private String status; //online or offline

    public User(String userid, String email, String username, String password, String dateOfBirth, String staus) {
        this.userid = userid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getStayus() {
        return status;
    }


    public void setStatus(String staues) {
        this.status = staues;
    }
    
   
}