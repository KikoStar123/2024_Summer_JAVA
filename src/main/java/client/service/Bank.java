package client.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONObject; // 如果你使用的是 org.json 库

public class Bank {
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8080;
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
    public boolean payment(String username, String bankpwd, String orderID) {
        boolean isSuccess = false;
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "payment");
            request.put("parameters", new JSONObject()
                    .put("username", username)
                    .put("bankpwd", bankpwd)
                    .put("orderID", orderID));

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
}
