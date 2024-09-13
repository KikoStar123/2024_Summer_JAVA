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
import java.sql.Date;

import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
/**
 * 银行服务类，提供处理银行相关操作的服务，例如支付、存款、取款等。
 */
public class BankService {

    private static BankService instance;
    private final Map<String, PaymentInfo> paymentRequests = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    private BankService() {}
    /**
     * 获取 BankService 的单例实例。
     * @return BankService 的唯一实例。
     */
    public static synchronized BankService getInstance() {
        if (instance == null) {
            instance = new BankService();
        }
        return instance;
    }
    /**
     * 根据存款类型获取利率。
     * @param type 存款类型（如活期或定期）。
     * @return 包含利率的 JSON 响应。
     */
    public JSONObject getInterestRate(String type) {
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
            String query = "SELECT * FROM tblInterestRate WHERE type = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                response.put("status", "success");
                response.put("rate", rs.getDouble("rate"));
                response.put("message", "Get rate successfully");
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

    /**
     * 内部类 PaymentInfo，用于存储支付信息。
     */
    public class PaymentInfo {
        private double amount;
        private boolean processed;
        private JSONObject paymentResult;
        private int passwordErrorCount;
        public PaymentInfo(double amount) {
            this.amount = amount;
            this.processed = false;
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

    }


    /**
     * 处理支付请求。
     *
     * @param orderID 订单ID
     * @param username 用户名
     * @param bankpwd 银行密码
     * @param amount 支付金额
     * @return 支付结果的JSONObject
     */
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
                        double currentBalance = userRs.getDouble("currentBalance");
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
                        String updateBalanceQuery = "UPDATE tblBankUser SET currentBalance = currentBalance - ?, balance = balance - ? WHERE username = ?";
                        PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery);
                        updateBalanceStmt.setDouble(1, amount);
                        updateBalanceStmt.setDouble(2, amount);
                        updateBalanceStmt.setString(3, username);
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
                            JSONObject result = new JSONObject().put("status", "error").put("message", "Invalid username or password");
                            synchronized (paymentInfo) {
                                paymentInfo.setPaymentResult(result);
                                paymentInfo.setProcessed(true);
                                paymentInfo.notifyAll();
                            }
                            return result;
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

    /**
     * 等待支付的结果，设置超时。
     *
     * @param orderID 订单ID
     * @param amount 支付金额
     * @return 支付结果的JSONObject
     */
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


    /**
     * 处理存款请求。
     * @param username 用户名。
     * @param amount 存款金额。
     * @param depositType 存款类型（活期或定期）。
     * @param term 存款期限（仅定期）。
     * @return 包含存款结果的 JSON 响应。
     */
    public JSONObject deposit(String username, double amount, String depositType, int term) {
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
            if (depositType.equals("活期")) {
                // 更新活期余额和总余额
                String updateCurrentBalanceQuery = "UPDATE tblBankUser SET currentBalance = currentBalance + ?, balance = balance + ? WHERE username = ?";
                PreparedStatement updateCurrentBalanceStmt = conn.prepareStatement(updateCurrentBalanceQuery);
                updateCurrentBalanceStmt.setDouble(1, amount);
                updateCurrentBalanceStmt.setDouble(2, amount);
                updateCurrentBalanceStmt.setString(3, username);
                updateCurrentBalanceStmt.executeUpdate();

                // 插入收支记录
                String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                insertRecordStmt.setString(1, username);
                insertRecordStmt.setDouble(2, amount);
                insertRecordStmt.setString(3, "活期存款");
                insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                insertRecordStmt.executeUpdate();
            } else if (depositType.equals("定期")) {
                // 插入定期存款记录
                String insertFixedDepositQuery = "INSERT INTO tblFixedDeposit (username, amount, term, startDate, endDate) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertFixedDepositStmt = conn.prepareStatement(insertFixedDepositQuery);
                insertFixedDepositStmt.setString(1, username);
                insertFixedDepositStmt.setDouble(2, amount);
                insertFixedDepositStmt.setInt(3, term);
                insertFixedDepositStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                insertFixedDepositStmt.setDate(5, new java.sql.Date(System.currentTimeMillis() + term * 30L * 24 * 60 * 60 * 1000));
                insertFixedDepositStmt.executeUpdate();

                // 更新总余额
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
                insertRecordStmt.setString(3, "定期存款");
                insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                insertRecordStmt.executeUpdate();
            }

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

    /**
     * 处理取款请求。
     * @param username 用户名。
     * @param amount 取款金额。
     * @return 包含取款结果的 JSON 响应。
     */
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
            // 检查活期余额
            String checkQuery = "SELECT currentBalance FROM tblBankUser WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("currentBalance");
                if (currentBalance < amount) {
                    response.put("status", "error");
                    response.put("message", "Insufficient balance.");
                    return response;
                }

                // 更新活期余额和总余额
                String updateBalanceQuery = "UPDATE tblBankUser SET currentBalance = currentBalance - ?, balance = balance - ? WHERE username = ?";
                PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery);
                updateBalanceStmt.setDouble(1, amount);
                updateBalanceStmt.setDouble(2, amount);
                updateBalanceStmt.setString(3, username);
                updateBalanceStmt.executeUpdate();

                // 插入收支记录
                String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                insertRecordStmt.setString(1, username);
                insertRecordStmt.setDouble(2, -amount);
                insertRecordStmt.setString(3, "取款");
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
    /**
     * 处理银行登录请求。
     * @param username 用户名。
     * @param bankpwd 银行密码。
     * @return 包含登录结果的 JSON 响应。
     */
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

    /**
     * 处理银行注册请求。
     * @param username 用户名。
     * @param bankpwd 银行密码。
     * @return 包含注册结果的 JSON 响应。
     */
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
    /**
     * 获取用户的银行交易记录。
     * @param username 用户名。
     * @return 包含交易记录的 JSON 响应。
     */
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
    /**
     * 更新用户的银行密码。
     * @param username 用户名。
     * @param oldPwd 旧密码。
     * @param newPwd 新密码。
     * @return 包含更新结果的 JSON 响应。
     */
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
    /**
     * 根据用户名搜索用户信息。
     * @param username 用户名。
     * @return 包含用户信息的 JSON 响应。
     */
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
            String query = "SELECT * FROM tblBankUser WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JSONObject user = new JSONObject();
                user.put("username", rs.getString("username"));
                user.put("balance", rs.getDouble("balance"));
                user.put("bankpwd", rs.getString("bankpwd"));
                user.put("currentBalance", rs.getDouble("currentBalance"));

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
    /**
     * 获取银行用户信息。
     * @param username 用户名。
     * @param bankpwd 银行密码。
     * @return 包含用户信息的 JSON 响应。
     */
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
            String query = "SELECT * FROM tblBankUser WHERE username = ? AND bankpwd = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, bankpwd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JSONObject user = new JSONObject();
                user.put("username", rs.getString("username"));
                user.put("balance", rs.getFloat("balance"));
                user.put("bankpwd", rs.getString("bankpwd"));
                user.put("currentBalance", rs.getDouble("currentBalance"));

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
    /**
     * 更新存款利率。
     * @param type 存款类型（如活期或定期）。
     * @param newRate 新的利率。
     * @return 包含更新结果的 JSON 响应。
     */
    public JSONObject updateInterestRate(String type, double newRate) {
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
            String updateRateQuery = "UPDATE tblInterestRate SET rate = ? WHERE type = ?";
            PreparedStatement updateRateStmt = conn.prepareStatement(updateRateQuery);
            updateRateStmt.setDouble(1, newRate);
            updateRateStmt.setString(2, type);
            int rowsUpdated = updateRateStmt.executeUpdate();

            if (rowsUpdated > 0) {
                response.put("status", "success");
                response.put("message", "Interest rate updated successfully.");
            } else {
                response.put("status", "error");
                response.put("message", "Failed to update interest rate.");
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
    /**
     * 模拟月末结算。
     * 计算用户的存款利息并更新余额。
     * @return 包含模拟结果的 JSON 响应。
     */
    public JSONObject simulateMonthEnd() {
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
            // 计算活期利息
            String query = "SELECT username, currentBalance FROM tblBankUser";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                double currentBalance = rs.getDouble("currentBalance");

                // 获取活期月利率
                String interestRateQuery = "SELECT rate FROM tblInterestRate WHERE type = '活期'";
                PreparedStatement interestRateStmt = conn.prepareStatement(interestRateQuery);
                ResultSet interestRateRs = interestRateStmt.executeQuery();

                if (interestRateRs.next()) {
                    double interestRate = interestRateRs.getDouble("rate") / 12; // 月利率
                    double interest = currentBalance * interestRate / 100;

                    // 更新活期余额
                    String updateBalanceQuery = "UPDATE tblBankUser SET currentBalance = currentBalance + ? WHERE username = ?";
                    PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery);
                    updateBalanceStmt.setDouble(1, interest);
                    updateBalanceStmt.setString(2, username);
                    updateBalanceStmt.executeUpdate();

                    // 插入收支记录
                    String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                    insertRecordStmt.setString(1, username);
                    insertRecordStmt.setDouble(2, interest);
                    insertRecordStmt.setString(3, "活期利息");
                    insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                    insertRecordStmt.executeUpdate();
                }
            }

            // 计算定期利息并更新定期存款数额和剩余月数
            String fixedDepositQuery = "SELECT username, amount, term, startDate FROM tblFixedDeposit";
            PreparedStatement fixedDepositStmt = conn.prepareStatement(fixedDepositQuery);
            ResultSet fixedDepositRs = fixedDepositStmt.executeQuery();

            while (fixedDepositRs.next()) {
                String username = fixedDepositRs.getString("username");
                double amount = fixedDepositRs.getDouble("amount");
                int term = fixedDepositRs.getInt("term");
                Date startDate = fixedDepositRs.getDate("startDate");

                // 获取定期月利率
                String interestRateQuery = "SELECT rate FROM tblInterestRate WHERE type = '定期'";
                PreparedStatement interestRateStmt = conn.prepareStatement(interestRateQuery);
                ResultSet interestRateRs = interestRateStmt.executeQuery();

                if (interestRateRs.next()) {
                    double interestRate = interestRateRs.getDouble("rate") / 12; // 月利率
                    double interest = amount * interestRate / 100;

                    // 更新定期存款数额
                    amount += interest;

                    // 更新定期存款记录
                    String updateFixedDepositQuery = "UPDATE tblFixedDeposit SET amount = ?, term = ? WHERE username = ? AND startDate = ?";
                    PreparedStatement updateFixedDepositStmt = conn.prepareStatement(updateFixedDepositQuery);
                    updateFixedDepositStmt.setDouble(1, amount);
                    updateFixedDepositStmt.setInt(2, term - 1); // 减少1个月
                    updateFixedDepositStmt.setString(3, username);
                    updateFixedDepositStmt.setDate(4, startDate);
                    updateFixedDepositStmt.executeUpdate();

                    // 插入收支记录
                    String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                    insertRecordStmt.setString(1, username);
                    insertRecordStmt.setDouble(2, interest);
                    insertRecordStmt.setString(3, "定期利息");
                    insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                    insertRecordStmt.executeUpdate();

                    // 检查定期存款是否到期
                    if (term - 1 <= 0) {
                        // 更新活期余额
                        String updateCurrentBalanceQuery = "UPDATE tblBankUser SET currentBalance = currentBalance + ? WHERE username = ?";
                        PreparedStatement updateCurrentBalanceStmt = conn.prepareStatement(updateCurrentBalanceQuery);
                        updateCurrentBalanceStmt.setDouble(1, amount);
                        updateCurrentBalanceStmt.setString(2, username);
                        updateCurrentBalanceStmt.executeUpdate();

                        // 插入到期记录
                        String insertMaturityRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                        PreparedStatement insertMaturityRecordStmt = conn.prepareStatement(insertMaturityRecordQuery);
                        insertMaturityRecordStmt.setString(1, username);
                        insertMaturityRecordStmt.setDouble(2, amount);
                        insertMaturityRecordStmt.setString(3, "定期到期");
                        insertMaturityRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                        insertMaturityRecordStmt.executeUpdate();

                        // 删除到期的定期存款记录
                        String deleteFixedDepositQuery = "DELETE FROM tblFixedDeposit WHERE username = ? AND startDate = ?";
                        PreparedStatement deleteFixedDepositStmt = conn.prepareStatement(deleteFixedDepositQuery);
                        deleteFixedDepositStmt.setString(1, username);
                        deleteFixedDepositStmt.setDate(2, startDate);
                        deleteFixedDepositStmt.executeUpdate();
                    }
                }
            }

            // 更新所有用户的总余额
            String updateTotalBalanceQuery = "UPDATE tblBankUser SET balance = currentBalance + (SELECT COALESCE(SUM(amount), 0) FROM tblFixedDeposit WHERE tblFixedDeposit.username = tblBankUser.username)";
            PreparedStatement updateTotalBalanceStmt = conn.prepareStatement(updateTotalBalanceQuery);
            updateTotalBalanceStmt.executeUpdate();

            response.put("status", "success");
            response.put("message", "Month-end simulation successful.");
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


    /**
     * 模拟年末结算。
     * 计算用户的存款利息并更新余额。
     * @return 包含模拟结果的 JSON 响应。
     */
    public JSONObject simulateYearEnd() {
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
            // 计算活期利息
            String query = "SELECT username, currentBalance FROM tblBankUser";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                double currentBalance = rs.getDouble("currentBalance");

                // 获取活期年利率
                String interestRateQuery = "SELECT rate FROM tblInterestRate WHERE type = '活期'";
                PreparedStatement interestRateStmt = conn.prepareStatement(interestRateQuery);
                ResultSet interestRateRs = interestRateStmt.executeQuery();

                if (interestRateRs.next()) {
                    double interestRate = interestRateRs.getDouble("rate"); // 年利率
                    double interest = currentBalance * interestRate / 100;

                    // 更新活期余额
                    String updateBalanceQuery = "UPDATE tblBankUser SET currentBalance = currentBalance + ? WHERE username = ?";
                    PreparedStatement updateBalanceStmt = conn.prepareStatement(updateBalanceQuery);
                    updateBalanceStmt.setDouble(1, interest);
                    updateBalanceStmt.setString(2, username);
                    updateBalanceStmt.executeUpdate();

                    // 插入收支记录
                    String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                    insertRecordStmt.setString(1, username);
                    insertRecordStmt.setDouble(2, interest);
                    insertRecordStmt.setString(3, "活期利息");
                    insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                    insertRecordStmt.executeUpdate();
                }
            }

            // 计算定期利息并更新定期存款数额和剩余月数
            String fixedDepositQuery = "SELECT username, amount, term, startDate FROM tblFixedDeposit";
            PreparedStatement fixedDepositStmt = conn.prepareStatement(fixedDepositQuery);
            ResultSet fixedDepositRs = fixedDepositStmt.executeQuery();

            while (fixedDepositRs.next()) {
                String username = fixedDepositRs.getString("username");
                double amount = fixedDepositRs.getDouble("amount");
                int term = fixedDepositRs.getInt("term");
                Date startDate = fixedDepositRs.getDate("startDate");

                // 获取定期年利率
                String interestRateQuery = "SELECT rate FROM tblInterestRate WHERE type = '定期'";
                PreparedStatement interestRateStmt = conn.prepareStatement(interestRateQuery);
                ResultSet interestRateRs = interestRateStmt.executeQuery();

                if (interestRateRs.next()) {
                    double interestRate = interestRateRs.getDouble("rate"); // 年利率
                    double interest = amount * interestRate / 100;

                    // 更新定期存款数额
                    amount += interest;

                    // 更新定期存款记录
                    String updateFixedDepositQuery = "UPDATE tblFixedDeposit SET amount = ?, term = ? WHERE username = ? AND startDate = ?";
                    PreparedStatement updateFixedDepositStmt = conn.prepareStatement(updateFixedDepositQuery);
                    updateFixedDepositStmt.setDouble(1, amount);
                    updateFixedDepositStmt.setInt(2, term - 12); // 减少12个月
                    updateFixedDepositStmt.setString(3, username);
                    updateFixedDepositStmt.setDate(4, startDate);
                    updateFixedDepositStmt.executeUpdate();

                    // 插入收支记录
                    String insertRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertRecordStmt = conn.prepareStatement(insertRecordQuery);
                    insertRecordStmt.setString(1, username);
                    insertRecordStmt.setDouble(2, interest);
                    insertRecordStmt.setString(3, "定期利息");
                    insertRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                    insertRecordStmt.executeUpdate();

                    // 检查定期存款是否到期
                    if (term - 12 <= 0) {
                        // 更新活期余额
                        String updateCurrentBalanceQuery = "UPDATE tblBankUser SET currentBalance = currentBalance + ? WHERE username = ?";
                        PreparedStatement updateCurrentBalanceStmt = conn.prepareStatement(updateCurrentBalanceQuery);
                        updateCurrentBalanceStmt.setDouble(1, amount);
                        updateCurrentBalanceStmt.setString(2, username);
                        updateCurrentBalanceStmt.executeUpdate();

                        // 插入到期记录
                        String insertMaturityRecordQuery = "INSERT INTO tblBankRecord (username, balanceChange, balanceReason, curDate) VALUES (?, ?, ?, ?)";
                        PreparedStatement insertMaturityRecordStmt = conn.prepareStatement(insertMaturityRecordQuery);
                        insertMaturityRecordStmt.setString(1, username);
                        insertMaturityRecordStmt.setDouble(2, amount);
                        insertMaturityRecordStmt.setString(3, "定期到期");
                        insertMaturityRecordStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
                        insertMaturityRecordStmt.executeUpdate();

                        // 删除到期的定期存款记录
                        String deleteFixedDepositQuery = "DELETE FROM tblFixedDeposit WHERE username = ? AND startDate = ?";
                        PreparedStatement deleteFixedDepositStmt = conn.prepareStatement(deleteFixedDepositQuery);
                        deleteFixedDepositStmt.setString(1, username);
                        deleteFixedDepositStmt.setDate(2, startDate);
                        deleteFixedDepositStmt.executeUpdate();
                    }
                }
            }

            // 更新所有用户的总余额
            String updateTotalBalanceQuery = "UPDATE tblBankUser SET balance = currentBalance + (SELECT COALESCE(SUM(amount), 0) FROM tblFixedDeposit WHERE tblFixedDeposit.username = tblBankUser.username)";
            PreparedStatement updateTotalBalanceStmt = conn.prepareStatement(updateTotalBalanceQuery);
            updateTotalBalanceStmt.executeUpdate();

            response.put("status", "success");
            response.put("message", "Year-end simulation successful.");
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



