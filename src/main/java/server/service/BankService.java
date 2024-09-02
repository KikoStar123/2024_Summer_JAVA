package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BankService {

    private final Lock lock = new ReentrantLock();

    public JSONObject deposit(String username, double amount) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();
        try {
            // 更新余额
            String updateBalanceQuery = "UPDATE tblBankUser SET balance = balance + ? WHERE username = ?";
            PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery);
            updateBalanceStmt.setDouble(1, amount);
            updateBalanceStmt.setString(2, username);
            updateBalanceStmt.executeUpdate();

            // 插入收支记录
            String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason) VALUES (?, ?, ?)";
            PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
            insertRecordStmt.setString(1, username);
            insertRecordStmt.setDouble(2, amount);
            insertRecordStmt.setString(3, "存款");
            insertRecordStmt.executeUpdate();

            response.put("status", "success");
            response.put("message", "Deposit successful.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }

    public JSONObject withdraw(String username, double amount) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();
        try {
            // 检查用户余额
            String checkQuery = "SELECT balance FROM tblBankUser WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance < amount) {
                    response.put("status", "error");
                    response.put("message", "Insufficient balance.");
                    return response;
                }

                // 更新余额
                String updateBalanceQuery = "UPDATE tblBankUser SET balance = balance - ? WHERE username = ?";
                PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery);
                updateBalanceStmt.setDouble(1, amount);
                updateBalanceStmt.setString(2, username);
                updateBalanceStmt.executeUpdate();

                // 插入收支记录
                String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason) VALUES (?, ?, ?)";
                PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                insertRecordStmt.setString(1, username);
                insertRecordStmt.setDouble(2, -amount);
                insertRecordStmt.setString(3, "取款");
                insertRecordStmt.executeUpdate();

                response.put("status", "success");
                response.put("message", "Withdrawal successful.");
            } else {
                response.put("status", "error");
                response.put("message", "User not found.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }

    public JSONObject bankLogin(String username, String bankpwd) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();
        try {
            // 检查用户名和密码
            String checkQuery = "SELECT * FROM tblBankUser WHERE username = ? AND bankpwd = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            checkStmt.setString(2, bankpwd);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                response.put("status", "success");
                response.put("message", "Login successful.");
            } else {
                response.put("status", "error");
                response.put("message", "Invalid username or password.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }

    public JSONObject bankRegister(String username, String bankpwd) {
        JSONObject response = new JSONObject();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();
        try {
            // 检查用户是否存在
            String checkQuery = "SELECT * FROM tblBankUser WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // 用户存在，更新密码
                String updatePwdQuery = "UPDATE tblBankUser SET bankpwd = ? WHERE username = ?";
                PreparedStatement updatePwdStmt = conn.prepareStatement(updatePwdQuery);
                updatePwdStmt.setString(1, bankpwd);
                updatePwdStmt.setString(2, username);
                updatePwdStmt.executeUpdate();

                response.put("status", "success");
                response.put("message", "Password updated successfully.");
            } else {
                // 用户不存在，插入新用户
                String insertUserQuery = "INSERT INTO tblBankUser (username, balance, bankpwd) VALUES (?, 0, ?)";
                PreparedStatement insertUserStmt = conn.prepareStatement(insertUserQuery);
                insertUserStmt.setString(1, username);
                insertUserStmt.setString(2, bankpwd);
                insertUserStmt.executeUpdate();

                response.put("status", "success");
                response.put("message", "Registration successful.");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                response.put("status", "error");
                response.put("message", ex.getMessage());
            }
            lock.unlock();
        }

        return response;
    }

}
