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

public class Bank {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;
    // 利率实现
    private double InterestRate;

    // 获取利率
    public double getInterestRate() {
        return InterestRate;
    }

    // 设置利率
    public void setInterestRate(double InterestRate) {
        this.InterestRate = InterestRate;
    }

    // 计算利息的方法
    public double calculateInterest(double principal, int years) {
        // 这里使用简单利息计算公式：利息 = 本金 * 利率 * 时间
        return principal * InterestRate * years / 100;
    }

    // 存钱
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

    // 取钱
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

    // 模拟过月结算
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
                System.out.println("模拟过月结算成功！");
            } else {
                System.out.println("模拟过月结算失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    // 模拟过年结算
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
                System.out.println("模拟过年结算成功！");
            } else {
                System.out.println("模拟过年结算失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    // 修改利率
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

    //获取所有收支记录//管理员和学生都可以使用
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
    //登录返回bool值
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
//登陆同时返回user类
// 新函数，返回BankUser对象
public static BankUser getBankUser(String username,String bankpwd) {
    BankUser user = null;
    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
        JSONObject request = new JSONObject();
        request.put("requestType", "getBankUser");
        request.put("parameters", new JSONObject()
                .put("username", username)
                .put("bankpwd", bankpwd)
        );

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(request.toString());

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        JSONObject jsonResponse = new JSONObject(response);

        System.out.println(jsonResponse.toString());//test the outcome of the function

        if (jsonResponse.getString("status").equals("success")) {
            JSONObject userJson = jsonResponse.getJSONObject("data");
            String retrievedUsername = userJson.getString("username");
            double retrievedBalance = userJson.getDouble("balance");
            String retrievedBankpwd = userJson.getString("bankpwd");
            double retrievedCurrentBalance = userJson.getDouble("currentBalance");        ;

            user = new BankUser(retrievedUsername, retrievedBalance, retrievedBankpwd, retrievedCurrentBalance);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return user;
}
    //注册账号
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
    //修改密码
    // 修改密码功能
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
    //支付功能--与商店协同
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
    //
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
                    // 假设服务器返回的用户信息是JSON格式
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
