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
        routeMap.put("updateUserPwd", new UpdateUserPwdRequestHandler());//名称相同***

        //图书馆相关请求
        routeMap.put("searchBooksByName", new SearchBooksByNameRequestHandler());
        routeMap.put("getBookDetailsById", new GetBookDetailsByIdRequestHandler());
        routeMap.put("getLibRecordsByUsername", new GetLibRecordsByUsernameRequestHandler());
        routeMap.put("bookReturn", new BookReturnRequestHandler());
        routeMap.put("bookBorrow", new BookBorrowRequestHandler());
        routeMap.put("updateBook", new UpdateBookRequestHandler());
        routeMap.put("addBook", new AddBookRequestHandler());
        routeMap.put("renewBook", new RenewBookRequestHandler());
        routeMap.put("getAllLibRecords", new GetAllLibRecordsRequestHandler());
        routeMap.put("updateBookImagePath", new UpdateBookImagePathRequestHandler());
        routeMap.put("updateBookPDFPath", new UpdateBookPDFPathRequestHandler());



        // 课程相关请求
        routeMap.put("enrollInCourse", new EnrollInCourseRequestHandler());
        routeMap.put("dropCourse", new DropCourseRequestHandler());
        routeMap.put("viewEnrolledCourses", new ViewEnrolledCoursesRequestHandler());
        routeMap.put("addCourse", new AddCourseRequestHandler()); // 新增课程
        routeMap.put("searchCourses", new SearchCoursesRequestHandler());//检索课程
        routeMap.put("viewCourseInfo", new ViewCourseInfoRequestHandler());  // 课程信息查看处理
        routeMap.put("getAllCourses", new GetAllCoursesRequestHandler());//查看所有课程信息

        // 商店相关请求 - 通过相应的处理器根据 action字段 处理
        /*
        * e.g.
        * const request = {
            requestType: "product",
            parameters: {
                action: "add",//<-此处action字段规定请求类型
                productID: "12345",
                productName: "New Product",
                productDetail: "This is a new product",
                productImage: "base64encodedImageString",
                productOriginalPrice: 99.99,
                productCurrentPrice: 89.99,
                productInventory: 100,
                productAddress: "Warehouse A",
                productCommentRate: 4.5,
                productStatus: true
        }
        };
        * */
        routeMap.put("product", new ProductRequestHandler());  // 处理与商品相关的请求
        routeMap.put("cart", new CartRequestHandler());        // 处理与购物车相关的请求
        routeMap.put("order", new OrderRequestHandler());      // 处理与订单相关的请求
        routeMap.put("store", new StoreRequestHandler());      // 处理店铺相关请求
        routeMap.put("user", new ShoppingUserRequestHandler());  // 处理与商店用户相关的请求
        routeMap.put("map", new ShoppingMapRequestHandler());  // 处理与商店用户相关的请求

        // 银行
        routeMap.put("deposit", new DepositRequestHandler());
        routeMap.put("withdraw", new WithdrawRequestHandler());
        routeMap.put("bankLogin", new BankLoginRequestHandler());
        routeMap.put("bankRegister", new BankRegisterRequestHandler());
        routeMap.put("payment", new PaymentRequestHandler());
        routeMap.put("wait", new WaitRequestHandler());
        routeMap.put("getAllBankRecords", new GetAllBankRecordsRequestHandler());
        routeMap.put("updatePwd", new UpdatePwdRequestHandler());
        routeMap.put("searchUser", new SearchUserRequestHandler());
        routeMap.put("getBankUser", new GetBankUserRequestHandler());
        routeMap.put("simulateMonthEnd", new SimulateMonthEndRequestHandler());
        routeMap.put("simulateYearEnd", new SimulateYearEndRequestHandler());
        routeMap.put("updateInterestRate", new UpdateInterestRateRequestHandler());
        routeMap.put("getInterestRate", new GetInterestRateRequestHandler());




        return routeMap;
    }


}