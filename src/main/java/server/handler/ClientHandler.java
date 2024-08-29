package server.handler;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request = in.readLine();
            if (request == null || request.trim().isEmpty()) {
                System.out.println("Received empty request");
                return;
            }

            System.out.println("Received request: " + request);
            JSONObject jsonRequest = new JSONObject(request);

            String requestType = jsonRequest.getString("requestType");
            JSONObject parameters = jsonRequest.getJSONObject("parameters");

            // 获取路由映射表
            Map<String, RequestHandler> routeMap = initializeRoutes();

            // 获取对应的处理器
            RequestHandler handler = routeMap.getOrDefault(requestType, new UnknownRequestHandler());



            // 执行处理器逻辑
            String response = handler.handle(parameters);
            out.println(response);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private Map<String, RequestHandler> initializeRoutes() {
        Map<String, RequestHandler> routeMap = new HashMap<>();
        routeMap.put("login", new LoginRequestHandler());
        routeMap.put("checkStudentInfo", new CheckStudentInfoRequestHandler());
        routeMap.put("login_return", new LoginReturnRequestHandler());
        routeMap.put("viewStudentInfo", new ViewStudentInfoRequestHandler());
        routeMap.put("modifyStudentInfo", new ModifyStudentInfoRequestHandler());
        routeMap.put("register", new RegisterRequestHandler());
        routeMap.put("search_books_by_name", new SearchBooksByNameRequestHandler());
        routeMap.put("get_book_by_id", new GetBookDetailsByIdRequestHandler());
        return routeMap;
    }


}
