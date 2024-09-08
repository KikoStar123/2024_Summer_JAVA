package server.service;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
public class BankService {

    private static BankService instance;
    private final Map<String, PaymentInfo> paymentRequests = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    private BankService() {}

    public static synchronized BankService getInstance() {
        if (instance == null) {
            instance = new BankService();
        }
        return instance;
    }

    public class PaymentInfo {
        private double amount;
        private boolean processed;
        private JSONObject paymentResult;
        private int passwordErrorCount;

        public PaymentInfo(double amount) {
            this.amount = amount;
            this.processed = false;
            this.passwordErrorCount = 0;
        }

        public double getAmount() {
            return amount;
        }

        public boolean isProcessed() {
            return processed;
        }

        public void setProcessed(boolean processed) {
            this.processed = processed;
        }

        public JSONObject getPaymentResult() {
            return paymentResult;
        }

        public void setPaymentResult(JSONObject paymentResult) {
            this.paymentResult = paymentResult;
        }

        public int getPasswordErrorCount() {
            return passwordErrorCount;
        }

        public void incrementPasswordErrorCount() {
            this.passwordErrorCount++;
        }
    }




        public JSONObject processPayment(String orderID, String username, String bankpwd, double amount) {
            lock.lock();
            try {
                System.out.println("Processing payment for orderID: " + orderID);
                System.out.println("Current paymentRequests: " + paymentRequests);

                PaymentInfo paymentInfo = paymentRequests.get(orderID);
                if (paymentInfo != null && paymentInfo.getAmount() == amount) {
                    // 检查用户名和密码
                    DatabaseConnection dbConnection = new DatabaseConnection();
                    Connection conn = dbConnection.connect();

                    if (conn == null) {
                        JSONObject result = new JSONObject().put("status", "error").put("message", "Failed to connect to the database.");
                        synchronized (paymentInfo) {
                            paymentInfo.setPaymentResult(result);
                            paymentInfo.setProcessed(true);
                            paymentInfo.notifyAll();
                        }
                        return result;
                    }

                    try {
                        String checkUserQuery = "SELECT * FROM tblBankUser WHERE username = ? AND bankpwd = ?";
                        PreparedStatement checkUserStmt = conn.prepareStatement(checkUserQuery);
                        checkUserStmt.setString(1, username);
                        checkUserStmt.setString(2, bankpwd);
                        ResultSet userRs = checkUserStmt.executeQuery();

                        if (userRs.next()) {
                            double currentBalance = userRs.getDouble("balance");
                            if (currentBalance < amount) {
                                JSONObject result = new JSONObject().put("status", "error").put("message", "Insufficient balance");
                                synchronized (paymentInfo) {
                                    paymentInfo.setPaymentResult(result);
                                    paymentInfo.setProcessed(true);
                                    paymentInfo.notifyAll();
                                }
                                return result;
                            }

                            // 用户名和密码正确，更新余额
                            String updateBalanceQuery = "UPDATE tblBankUser SET balance = balance - ? WHERE username = ?";
                            PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery);
                            updateBalanceStmt.setDouble(1, amount);
                            updateBalanceStmt.setString(2, username);
                            updateBalanceStmt.executeUpdate();

                            // 插入收支记录
                            String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                            PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                            insertRecordStmt.setString(1, username);
                            insertRecordStmt.setDouble(2, -amount);
                            insertRecordStmt.setString(3, "支付");
                            insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                            insertRecordStmt.executeUpdate();


                            System.out.println("Payment matched and processed for orderID: " + orderID);
                            paymentInfo.setProcessed(true);
                            JSONObject result = new JSONObject().put("status", "success").put("message", "Payment processed successfully");
                            synchronized (paymentInfo) {
                                paymentInfo.setPaymentResult(result);
                                paymentInfo.setProcessed(true);
                                paymentInfo.notifyAll();
                            }
                            return result;
                        } else {
                            System.out.println("Invalid username or password for orderID: " + orderID);
                            paymentInfo.incrementPasswordErrorCount();
                            if (paymentInfo.getPasswordErrorCount() >= 3) {
                                JSONObject result = new JSONObject().put("status", "error").put("message", "Invalid username or password");
                                synchronized (paymentInfo) {
                                    paymentInfo.setPaymentResult(result);
                                    paymentInfo.setProcessed(true);
                                    paymentInfo.notifyAll();
                                }
                                return result;
                            } else {
                                return new JSONObject().put("status", "error").put("message", "Invalid username or password. Attempt " + paymentInfo.getPasswordErrorCount());
                            }
                        }
                    } catch (SQLException e) {
                        JSONObject result = new JSONObject().put("status", "error").put("message", e.getMessage());
                        synchronized (paymentInfo) {
                            paymentInfo.setPaymentResult(result);
                            paymentInfo.setProcessed(true);
                            paymentInfo.notifyAll();
                        }
                        return result;
                    } finally {
                        try {
                            if (conn != null) {
                                conn.close();
                            }
                        } catch (SQLException ex) {
                            JSONObject result = new JSONObject().put("status", "error").put("message", ex.getMessage());
                            synchronized (paymentInfo) {
                                paymentInfo.setPaymentResult(result);
                                paymentInfo.setProcessed(true);
                                paymentInfo.notifyAll();
                            }
                            return result;
                        }
                    }
                } else {
                    System.out.println("No matching wait request found for orderID: " + orderID);
                    return new JSONObject().put("status", "error").put("message", "No matching wait request found");
                }
            } finally {
                lock.unlock();
            }
        }



        public JSONObject waitForPayment(String orderID, double amount) {
            lock.lock();
            try {
                System.out.println("Waiting for payment for orderID: " + orderID);
                PaymentInfo paymentInfo = new PaymentInfo(amount);
                paymentRequests.put(orderID, paymentInfo);
                System.out.println("Added wait request for orderID: " + orderID);

                long startTime = System.currentTimeMillis();
                long timeout = 5 * 60 * 1000; // 5分钟

                synchronized (paymentInfo) {
                    try {
                        while (!paymentInfo.isProcessed()) {
                            long elapsedTime = System.currentTimeMillis() - startTime;
                            if (elapsedTime >= timeout) {
                                System.out.println("Timeout waiting for payment for orderID: " + orderID);
                                return new JSONObject().put("status", "error").put("message", "Timeout waiting for payment");
                            }
                            lock.unlock();
                            paymentInfo.wait(timeout - elapsedTime);
                            lock.lock();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread interrupted while waiting for payment for orderID: " + orderID);
                        return new JSONObject().put("status", "error").put("message", "Thread interrupted");
                    }
                }

                System.out.println("Payment received for orderID: " + orderID);
                return paymentInfo.getPaymentResult();
            } finally {
                paymentRequests.remove(orderID);
                System.out.println("Removed wait request for orderID: " + orderID);
                lock.unlock();
            }
        }




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
            String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
            PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
            insertRecordStmt.setString(1, username);
            insertRecordStmt.setDouble(2, amount);
            insertRecordStmt.setString(3, "存款");
            insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
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
                String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                insertRecordStmt.setString(1, username);
                insertRecordStmt.setDouble(2, -amount);
                insertRecordStmt.setString(3, "存款");
                insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
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

    public JSONObject getAllBankRecords(String username) {
        JSONObject response = new JSONObject();
        List<JSONObject> records = new ArrayList<>();
        DatabaseConnection dbConnection = new DatabaseConnection();
        Connection conn = dbConnection.connect();

        if (conn == null) {
            response.put("status", "error");
            response.put("message", "Failed to connect to the database.");
            return response;
        }

        lock.lock();

        try {
            String query = "SELECT username,balanceChange, balanceReason, curDate FROM tblBankRecord WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JSONObject record = new JSONObject();
                record.put("username",rs.getString("username"));
                record.put("balanceChange", rs.getFloat("balanceChange"));
                record.put("balanceReason", rs.getString("balanceReason"));
                record.put("curDate", rs.getString("curDate"));
                records.add(record);
            }

            response.put("status", "success");
            response.put("records", new JSONArray(records));
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

    public JSONObject updatePwd(String username, String oldPwd, String newPwd) {
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
            String query = "UPDATE tblBankUser SET bankpwd = ? WHERE username = ? AND bankpwd = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newPwd);
            stmt.setString(2, username);
            stmt.setString(3, oldPwd);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                response.put("status", "success");
                response.put("message", "Password updated successfully.");
            } else {
                response.put("status", "error");
                response.put("message", "Old password is incorrect.");
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

    public JSONObject searchByUsername(String username) {
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
            String query = "SELECT username, balance, bankpwd FROM tblBankUser WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JSONObject user = new JSONObject();
                user.put("username", rs.getString("username"));
                user.put("balance", rs.getFloat("balance"));
                user.put("bankpwd", rs.getString("bankpwd"));

                response.put("status", "success");
                response.put("data", user);
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

    public JSONObject getBankUser(String username, String bankpwd) {
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
            String query = "SELECT username, balance, bankpwd FROM tblBankUser WHERE username = ? AND bankpwd = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, bankpwd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JSONObject user = new JSONObject();
                user.put("username", rs.getString("username"));
                user.put("balance", rs.getFloat("balance"));
                user.put("bankpwd", rs.getString("bankpwd"));

                response.put("status", "success");
                response.put("data", user);
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

}
