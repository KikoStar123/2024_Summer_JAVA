package testhandler.user;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import java.net.Socket;

public class TestRegister {

    @Test
    public void testRegisterResponse() {

        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("requestType", "register");
            JSONObject parameters = new JSONObject();
            parameters.put("truename", "栗智勇");
            parameters.put("gender", "male");
            parameters.put("birthday", "2000-01-01");
            parameters.put("origin", "xx省xx市");
            parameters.put("pwd", "password123");
            parameters.put("academy", "计算机科学与工程学院");
            parameters.put("id", "09022119");
            parameters.put("age", 20);
            request.put("parameters", parameters);

            out.println(request.toString());

            String responseString = in.readLine();
            System.out.println("Server response: " + responseString);

            JSONObject response = new JSONObject(responseString);

            assertEquals("success", response.getString("status"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}