package testhandler.library;

import static org.junit.Assert.assertEquals;
import java.io.*;
import java.net.Socket;
import org.json.JSONObject;
import org.junit.Test;

public class TestAddBook {

    @Test
    public void testAddBook() {
        String hostname = "localhost";
        int port = 8080;

        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("requestType", "addBook");
            JSONObject bookDetails = new JSONObject();
            bookDetails.put("bookID", "978-7-04-056814-1")
                    .put("name", "计算机系统结构")
                    .put("author", "张晨曦")
                    .put("publishHouse", "高等教育出版社")
                    .put("publicationYear", "2021")
                    .put("classification", "自动化技术、计算机技术")
                    .put("curNumber", 5)
                    .put("libNumber", 5)
                    .put("location", "四牌楼校区");
            request.put("parameters", bookDetails);

            out.println(request.toString());

            String responseString = in.readLine();
            System.out.println("Server response: " + responseString);

            JSONObject response = new JSONObject(responseString);

            assertEquals("success", response.getString("status"));
            assertEquals("Book added successfully.", response.getString("message"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
