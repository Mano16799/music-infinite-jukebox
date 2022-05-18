
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {

    public boolean isPhoneNoValid(String phone) {
        Pattern patternObj = Pattern.compile("^[789]\\d{9}$");
        Matcher matcherObj = patternObj.matcher(phone);
        return matcherObj.find();
    }

    //email id validator
    public boolean emailValidation(String email) {
        Pattern patternObj = Pattern.compile("^\\w+@+[a-z]+\\.[a-z]+");
        Matcher matcherObj = patternObj.matcher(email);
        return matcherObj.find();
    }

    public boolean isUserIdAvailable(String userId) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            Statement ps = con.createStatement();
            ResultSet r = ps.executeQuery("select * from user");
            while (r.next()) {
                if (r.getString(1).equals(userId)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyUserIdPassword(String userId, String password) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_infinite", "root", "Password@123")) {
            PreparedStatement ps = con.prepareStatement("select * from user where user_id = ?");
            ps.setString(1, userId);
            ResultSet r = ps.executeQuery();
            while (r.next()) {
                if (r.getString(7).equals(password)) {
                    System.out.println("Welcome " + r.getString(2) + " " + r.getString(3));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String userIdGenerator() {
        long num = (long) (Math.random() * (999999L - 200000L + 1) + 200000L);
        return "ID" + num;
    }


    public boolean isNameValid(String name){
        Pattern patternObj = Pattern.compile("^[a-z]+\\w{4}$");
        Matcher matcherObj = patternObj.matcher(name);
        return matcherObj.find();
    }
}
