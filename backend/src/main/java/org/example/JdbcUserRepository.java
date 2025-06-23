package org.example;

import java.sql.*;
import java.util.*;

public class JdbcUserRepository {
    private final Connection connection;

    public JdbcUserRepository(Connection connection){
        this.connection = connection;
    }

    //Get existing user information from the userid
    public User getUserData(int userId){
        String sql = "SELECT * FROM users WHERE id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, userId);
            ResultSet rs= stmt.executeQuery();

            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String passwordHash = rs.getString("password_hash");

                Array interestsArray = rs.getArray("interests");
                Set<String> userInterests = new HashSet<>();
                if (interestsArray != null) {
                    String[] interests = (String[]) interestsArray.getArray();
                    for (String interest : interests) {
                        userInterests.add(interest.toLowerCase());
                    }
                }
                return new User(userId, name, email, passwordHash, userInterests);
            }
        } catch (Exception e) {
            Logger.addLog("Error while fetching user details from DB.", "JDBCUserRepository Class");
            System.out.println("Error in JDBCUserRepository Class at :: " + e.getStackTrace() + " error:: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
    //method is used to login user if user is registered in db
    public int loginUser(String email, String password) {
        String sql = "SELECT id, password_hash FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                String storedHash = rs.getString("password_hash");
                return PasswordUtils.verifyPassword(password, storedHash) ? userId : 0;
            } else {
                // Email doesn't exist
                return 0;
            }
        } catch (Exception e) {
            Logger.addLog("Error during login attempt.", "JDBCUserRepository, loginUser");
            e.printStackTrace();
            throw new RuntimeException("Login failed.", e);
        }
    }

    //Check is user already exists based on email uniqueness
    public boolean userExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            Logger.addLog("Error while checking if user exists.", "JDBCUserRepository, userExists");
            e.printStackTrace();
            throw new RuntimeException("Failed to check user existence", e);
        }
    }

    // Create new User
    public int createUser(String name, String email, String password) {
        if (name == null || email == null || password == null) {
            Logger.addLog("Name, email, or password cannot be null while creating user.", "JDBCUserRepository, createUser");
            throw new IllegalArgumentException("Name, email, or password cannot be null.");
        }
        if(userExists(email)){
            Logger.addLog("User already exists with email :: " + email, "JDBCUserRepository, createUser");
            throw new IllegalArgumentException("Email already registered.");
        }

        String sqlInsert = "INSERT INTO users (name, email, password_hash) VALUES (?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sqlInsert)) {
            stmt.setString(1, name);
            stmt.setString(2, email);

            // Hash the password securely
            String passkeyHash = PasswordUtils.hashPassword(password);
            stmt.setString(3, passkeyHash);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("User created successfully.");
                return rs.getInt("id");
            }
            return 0;
        } catch (Exception e) {
            Logger.addLog("Error while inserting user into DB.", "JdbcUserRepository Class, createUser method");
            System.out.println("Error in createUser: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //update interests for user
    public boolean updateInterests(int userId, List<String> interests){
        if(userId <= 0 || interests == null || interests.isEmpty()){
            Logger.addLog("UserId or Interests cannot be null while updating interests.", "JDBCUserRepository, updateInterests");
            throw new IllegalArgumentException("UserId or Interests cannot be null while updating interests.");
        }

        String sqlUpdate = "UPDATE users SET interests = ? WHERE id = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sqlUpdate)){
            Array intrestArray = connection.createArrayOf("text", interests.toArray());
            stmt.setArray(1, intrestArray);
            stmt.setInt(2, userId);

            int rowsExecuted = stmt.executeUpdate();

            return rowsExecuted > 0;
        } catch (Exception e) {
            Logger.addLog("Error while updating interests on userid : " + userId, "JdbcUserRepository Class, updateInterests method");
            System.out.println("Error in updateInterests: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]){
        try(Connection conn = DatabaseManager.getConnection()){
            JdbcUserRepository repo = new JdbcUserRepository(conn);
            int userId = repo.loginUser("ram@gmail.com", "Abcde12345");
            System.out.println("userId : " + userId);
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
