package client.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject; // 如果你使用的是 org.json 库

import java.util.ArrayList;
import java.util.List;

/**
 * 提供银行服务，包括存款、取款、利率更新以及账户操作。
 */
public class Bank {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;

    /**
     * 存钱到用户账户。
     *
     * @param username     用户名
     * @param amount       存款金额
     * @param depositType  存款类型（如定期、活期）
     * @param term         存款期限（适用于定期存款）
     * @return 如果存款成功返回 true，否则返回 false
     */
    public boolean deposit(String username, float amount, String depositType, int term) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "deposit");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("amount", amount)
                    .put("depositType", depositType)
                    .put("term", term));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("存款成功！");
            } else {
                System.out.println("存款失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 从用户账户取钱。
     *
     * @param username 用户名
     * @param amount   取款金额
     * @return 如果取款成功返回 true，否则返回 false
     */
    public boolean withdraw(String username, float amount) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "withdraw");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("amount", amount));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("取款成功！");
            } else {
                System.out.println("取款失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 模拟月末结算操作。
     *
     * @return 如果操作成功返回 true，否则返回 false
     */
    public boolean simulateMonthEnd() {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "simulateMonthEnd");
            request.put("parameters", new JSONObject());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("模拟月末结算成功！");
            } else {
                System.out.println("模拟月末结算失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 模拟年末结算操作。
     *
     * @return 如果操作成功返回 true，否则返回 false
     */
    public boolean simulateYearEnd() {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "simulateYearEnd");
            request.put("parameters", new JSONObject());

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("模拟年末结算成功！");
            } else {
                System.out.println("模拟年末结算失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 修改账户利率。
     *
     * @param type    利率类型（如活期、定期）
     * @param newRate 新利率
     * @return 如果操作成功返回 true，否则返回 false
     */
    public boolean updateInterestRate(String type, double newRate) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updateInterestRate");
            request.put("parameters", new JSONObject()
                    .put("type", type)
                    .put("newRate", newRate));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("利率修改成功！");
            } else {
                System.out.println("利率修改失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 获取指定用户的所有收支记录。
     *
     * @param username 用户名
     * @return 包含用户所有收支记录的列表
     */
    public static List<BankUser.BankRecord> getAllBankRecords(String username) {
        List<BankUser.BankRecord> bankRecords = new ArrayList<>();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getAllBankRecords");
            request.put("parameters", new JSONObject()
                    .put("username", username));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            if (jsonResponse.getString("status").equals("success")) {
                JSONArray recordsArray = jsonResponse.getJSONArray("records");
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject recordJson = recordsArray.getJSONObject(i);
                    BankUser.BankRecord record = new BankUser.BankRecord(
                            recordJson.getString("username"),
                            recordJson.getFloat("balanceChange"),
                            recordJson.getString("balanceReason"),
                            recordJson.getString("curDate")
                    );
                    bankRecords.add(record);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bankRecords;
    }

    /**
     * 用户登录银行系统。
     *
     * @param username 用户名
     * @param bankpwd  银行密码
     * @return 如果登录成功返回 true，否则返回 false
     */
    public static boolean bankLogin(String username, String bankpwd) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "bankLogin");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bankpwd", bankpwd));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据用户名和密码获取 BankUser 对象。
     *
     * @param username 用户名
     * @param bankpwd  银行密码
     * @return BankUser 对象，如果查找失败则返回 null
     */
    public static BankUser getBankUser(String username, String bankpwd) {
        BankUser user = null;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "getBankUser");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bankpwd", bankpwd));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            System.out.println(jsonResponse.toString()); // 测试函数结果

            if (jsonResponse.getString("status").equals("success")) {
                JSONObject userJson = jsonResponse.getJSONObject("data");
                String retrievedUsername = userJson.getString("username");
                double retrievedBalance = userJson.getDouble("balance");
                String retrievedBankpwd = userJson.getString("bankpwd");
                double retrievedCurrentBalance = userJson.getDouble("currentBalance");

                user = new BankUser(retrievedUsername, retrievedBalance, retrievedBankpwd, retrievedCurrentBalance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 注册新银行账户。
     *
     * @param username 用户名
     * @param bankpwd  银行密码
     * @return 如果注册成功返回 true，否则返回 false
     */
    public static boolean bankRegister(String username, String bankpwd) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "bankRegister");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bankpwd", bankpwd));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 更新用户密码。
     *
     * @param username 用户名
     * @param oldPwd   旧密码
     * @param newPwd   新密码
     * @return 如果密码更新成功返回 true，否则返回 false
     */
    public static boolean updatePwd(String username, String oldPwd, String newPwd) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "updatePwd");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("oldPwd", oldPwd)
                    .put("newPwd", newPwd));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("密码修改成功！");
            } else {
                System.out.println("密码修改失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 支付功能，与商店协同。
     *
     * @param username 用户名
     * @param bankpwd  银行密码
     * @param orderID  订单编号
     * @param amount   支付金额
     * @return 如果支付成功返回 true，否则返回 false
     */
    public static boolean payment(String username, String bankpwd, String orderID, float amount) {
        System.out.println("payment: " + orderID);
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "payment");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bankpwd", bankpwd)
                    .put("orderID", orderID)
                    .put("amount", amount));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                System.out.println("支付成功！");
            } else {
                System.out.println("支付失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 根据用户名查找银行用户信息。
     *
     * @param username 用户名
     * @return 查找到的 BankUser 对象，如果未找到则返回 null
     */
    public BankUser searchByUsername(String username) {
        boolean isSuccess = false;
        BankUser foundUser = null;
        try (Socket socket = new Socket("localhost", 8080)) { // 假设服务器地址和端口
            JSONObject request = new JSONObject();
            request.put("requestType", "searchUser");
            request.put("parameters", new JSONObject()
                    .put("username", username));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                JSONObject userJson = jsonResponse.getJSONObject("data");
                foundUser = new BankUser(
                        userJson.getString("username"),
                        userJson.getDouble("balance"),
                        userJson.getString("bankpwd"),
                        userJson.getDouble("currentBalance")
                );
                System.out.println("用户查找成功：" + foundUser.getUsername());
            } else {
                System.out.println("用户查找失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundUser;
    }

    /**
     * 获取指定类型的利率。
     *
     * @param type 利率类型
     * @return 对应的利率值
     */
    public static Double getInterestRate(String type) {
        double ret = 0.0;
        try (Socket socket = new Socket("localhost", 8080)) { // 假设服务器地址和端口
            JSONObject request = new JSONObject();
            request.put("requestType", "getInterestRate");
            request.put("parameters", new JSONObject()
                    .put("type", type));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            Boolean isSuccess = jsonResponse.getString("status").equals("success");
            if (isSuccess) {
                ret = jsonResponse.getDouble("rate");
            } else {
                System.out.println("利率获取失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
