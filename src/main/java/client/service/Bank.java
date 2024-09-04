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

import client.service.BankUser;

public class Bank {
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;
    //利率实现
    private  double InterestRate;
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
//存钱
    public boolean deposit(String username, float amount) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "deposit");
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
                System.out.println("存款成功！");
            } else {
                System.out.println("存款失败：" + jsonResponse.getString("message"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
    //取钱
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
    //获取所有收支记录//管理员和学生都可以使用
    public List<BankUser.BankRecord> getAllBankRecords(String username) {
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
    //登录
    public boolean bankLogin(String username, String bankpwd) {
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
    //注册账号
    public boolean bankRegister(String username, String bankpwd) {
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
    public boolean updatePwd(String username, String oldPwd, String newPwd) {
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
    //支付功能
    public boolean payment(String username, String bankpwd, String orderID, double amount) {
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
                            userJson.getFloat("balance"),
                            userJson.getString("bankpwd")
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
}
