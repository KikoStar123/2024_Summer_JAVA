package testhandler.student;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.*;
import java.net.Socket;
public class TestModifyStudentInfo {

    @Test
    public void testModifyStudentInfo() {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("requestType", "modifyStudentInfo");
            JSONObject parameters = new JSONObject();
            JSONObject newStudentInfo = new JSONObject();
            newStudentInfo.put("name", "杨锦波");
            newStudentInfo.put("id", "09022301");
            newStudentInfo.put("gender", "male");
            newStudentInfo.put("birthday", "2000-01-02");
            newStudentInfo.put("academy", "计算机科学与工程学院");
            newStudentInfo.put("origin", "xx省xx市");
            parameters.put("data", newStudentInfo);
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