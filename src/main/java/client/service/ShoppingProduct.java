package client.service;

import java.io.*;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;


public class ShoppingProduct {
    private final String SERVER_ADDRESS = "localhost";//服务器的地址 即本地服务器
    private final int SERVER_PORT = 8080;//定义服务器的端口号

    public class oneProduct
    {
        String productID;//商品id
        String productName;//商品名称
        String productDetail;//商品属性（string）
        String productImage;//商品图片
        float productOriginalPrice;//商品原价
        float productCurrentPrice;//商品现价
        int productInventory;//商品库存
        String productAddress;//商品发货地址
        float productCommentRate;//商品好评率
        boolean productStatus;//商品状态
    }

    // 查看单个商品详细信息
    // 输入 商品id productID
    // 返回 一个商品对象
    private oneProduct getProductDetails(String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getDetails")
                    .put("productID", productID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONObject data = jsonResponse.getJSONObject("product");

            oneProduct theproduct = new oneProduct();

            theproduct.productID = productID;
            theproduct.productName = data.getString("productName");
            theproduct.productDetail = data.getString("productDetail");
            theproduct.productImage = data.getString("productImage");
            theproduct.productOriginalPrice = data.getFloat("productOriginalPrice");
            theproduct.productCurrentPrice = data.getFloat("productCurrentPrice");
            theproduct.productInventory = data.getInt("productInventory");
            theproduct.productAddress = data.getString("productAddress");
            theproduct.productCommentRate = data.getFloat("productCommentRate");
            theproduct.productStatus = data.getBoolean("productStatus");

            return theproduct;
        } catch (IOException e) {
            e.printStackTrace();
            return new oneProduct();
        }
    }

    // 查看所有商品详细信息
    // 输入 无
    // 返回 商品数组
    private oneProduct[] getAllProducts() throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getAll"));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("products");//获取JSON数组

            // 获取商品数量
            int numProducts = data.length();

            // 创建一个数组来存储所有商品信息
            oneProduct[] productsArray = new oneProduct[numProducts];

            for (int i = 0; i < numProducts; i++) {

                JSONObject course = data.getJSONObject(i);

                productsArray[i] = getProductDetails(course.getString("productID"));
                productsArray[i].productID = course.getString("productID");
                productsArray[i].productName = course.getString("productName");
                productsArray[i].productDetail = course.getString("productDetail");
                productsArray[i].productImage = course.getString("productImage");
                productsArray[i].productOriginalPrice = course.getFloat("productOriginalPrice");
                productsArray[i].productCurrentPrice = course.getFloat("productCurrentPrice");
                productsArray[i].productInventory = course.getInt("productInventory");
                productsArray[i].productAddress = course.getString("productAddress");
                productsArray[i].productCommentRate = course.getFloat("productCommentRate");
                productsArray[i].productStatus = course.getBoolean("productStatus");
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 添加商品
    // 输入 商品id productID；商品名称 productName；商品属性 productDetail；商品图片 productImage；商品原价 productOriginalPrice；商品现价 productCurrentPrice；商品库存 productInventory；商品发货地址 productAddress；商品好评率 productCommentRate；商品状态 productStatus
    // 返回 状态
    private boolean addProduct(String productID, String productName, String productDetail, String productImage, float productOriginalPrice, float productCurrentPrice, int productInventory, String productAddress, float productCommentRate, boolean productStatus) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "add")
                    .put("productID", productID)
                    .put("productName", productName)
                    .put("productDetail", productDetail)
                    .put("productImage", productImage)
                    .put("productOriginalPrice", productOriginalPrice)
                    .put("productCurrentPrice", productCurrentPrice)
                    .put("productInventory", productInventory)
                    .put("productAddress", productAddress)
                    .put("productCommentRate", productCommentRate)
                    .put("productStatus", productStatus));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除商品
    // 输入 商品id productID
    // 返回 状态
    private boolean deleteProduct(String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "delete")
                    .put("productID", productID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 上架/下架商品
    // 输入 商品id productID; 商品状态 productStatus
    // 返回 状态
    private boolean updateProductStatus(String productID,boolean status) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "updateStatus")
                    .put("productID", productID)
                    .put("status", status));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            return jsonResponse.getString("status").equals("success");//判断返回值，是否成功
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 查看同类商品详细信息
    // 输入 商品id productID
    // 返回 商品数组
    private oneProduct[] getSameCategoryProducts(String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getSameCategory"))
                    .put("productID", productID);

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("products");//获取JSON数组

            // 获取商品数量
            int numProducts = data.length();

            // 创建一个数组来存储所有商品信息
            oneProduct[] productsArray = new oneProduct[numProducts];

            for (int i = 0; i < numProducts; i++) {

                JSONObject course = data.getJSONObject(i);

                productsArray[i] = getProductDetails(course.getString("productID"));
                productsArray[i].productID = course.getString("productID");
                productsArray[i].productName = course.getString("productName");
                productsArray[i].productDetail = course.getString("productDetail");
                productsArray[i].productImage = course.getString("productImage");
                productsArray[i].productOriginalPrice = course.getFloat("productOriginalPrice");
                productsArray[i].productCurrentPrice = course.getFloat("productCurrentPrice");
                productsArray[i].productInventory = course.getInt("productInventory");
                productsArray[i].productAddress = course.getString("productAddress");
                productsArray[i].productCommentRate = course.getFloat("productCommentRate");
                productsArray[i].productStatus = course.getBoolean("productStatus");
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 检索商品
    // 输入 检索关键词 searchTerm
    // 返回 商品数组
    private oneProduct[] searchProducts(String searchTerm) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "search")
                    .put("searchTerm", searchTerm));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            JSONArray data = jsonResponse.getJSONArray("products");//获取JSON数组

            // 获取商品数量
            int numProducts = data.length();

            // 创建一个数组来存储所有商品信息
            oneProduct[] productsArray = new oneProduct[numProducts];

            for (int i = 0; i < numProducts; i++) {

                JSONObject course = data.getJSONObject(i);

                productsArray[i] = getProductDetails(course.getString("productID"));
                productsArray[i].productID = course.getString("productID");
                productsArray[i].productName = course.getString("productName");
                productsArray[i].productDetail = course.getString("productDetail");
                productsArray[i].productImage = course.getString("productImage");
                productsArray[i].productOriginalPrice = course.getFloat("productOriginalPrice");
                productsArray[i].productCurrentPrice = course.getFloat("productCurrentPrice");
                productsArray[i].productInventory = course.getInt("productInventory");
                productsArray[i].productAddress = course.getString("productAddress");
                productsArray[i].productCommentRate = course.getFloat("productCommentRate");
                productsArray[i].productStatus = course.getBoolean("productStatus");
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
