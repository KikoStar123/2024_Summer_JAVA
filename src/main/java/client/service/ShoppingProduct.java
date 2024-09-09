package client.service;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;


public class ShoppingProduct {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;
    private static final FileService fileService = new FileService();
    public static class oneProduct
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
        String storeID;//商店id

        String storeName;

        public String getProductID() {
            return productID;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductDetail() {
            return productDetail;
        }

        public String getProductImage() {
            return productImage;
        }

        public float getProductOriginalPrice() {
            return productOriginalPrice;
        }

        public float getProductCurrentPrice() {
            return productCurrentPrice;
        }

        public int getProductInventory() {
            return productInventory;
        }

        public String getProductAddress() {
            return productAddress;
        }

        public float getProductCommentRate() {
            return productCommentRate;
        }

        public boolean isProductStatus() {
            return productStatus;
        }

        public String getStoreID() {
            return storeID;
        }

        public String getStoreName() {return storeName; }

    }

    // 查看单个商品详细信息
    // 输入 商品id productID
    // 返回 一个商品对象

    public static oneProduct getProductDetails(String productID) throws IOException
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
            theproduct.productImage = data.optString("productImage", "uploads/defaultproduct.jpg");
            theproduct.productOriginalPrice = data.getFloat("productOriginalPrice");
            theproduct.productCurrentPrice = data.getFloat("productCurrentPrice");
            theproduct.productInventory = data.getInt("productInventory");
            theproduct.productAddress = data.getString("productAddress");
            theproduct.productCommentRate = data.getFloat("productCommentRate");
            theproduct.productStatus = data.getBoolean("productStatus");
            theproduct.storeID = data.getString("storeID");
            theproduct.storeName = data.getString("storeName");

            return theproduct;
        } catch (IOException e) {
            e.printStackTrace();
            return new oneProduct();
        }
    }

    // 查看所有商品详细信息
    // 输入 排序关键词 sortBy 取值为 price（按价格）或 rate（按好评率）
    //     排序顺序 sortOrder 取值为 ASC（升序） 或 DESC（降序）
    // 返回 商品数组
    public oneProduct[] getAllProducts(String sortBy, String sortOrder) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getAll")
                    .put("sortBy", sortBy)
                    .put("sortOrder", sortOrder));

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

                JSONObject theproduct = data.getJSONObject(i);

                productsArray[i] = new oneProduct();
                productsArray[i].productID = theproduct.getString("productID");
                productsArray[i].productName = theproduct.getString("productName");
                productsArray[i].productDetail = theproduct.getString("productDetail");
                productsArray[i].productImage = theproduct.optString("productImage", "uploads/defaultproduct.jpg");
                productsArray[i].productOriginalPrice = theproduct.getFloat("productOriginalPrice");
                productsArray[i].productCurrentPrice = theproduct.getFloat("productCurrentPrice");
                productsArray[i].productInventory = theproduct.getInt("productInventory");
                productsArray[i].productAddress = theproduct.getString("productAddress");
                productsArray[i].productCommentRate = theproduct.getFloat("productCommentRate");
                productsArray[i].productStatus = theproduct.getBoolean("productStatus");
                productsArray[i].storeID = theproduct.getString("storeID");
                productsArray[i].storeName = theproduct.getString("storeName");

                System.out.println(productsArray[i].productImage);
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 添加商品
    // 输入 商品id productID；商品名称 productName；商品属性 productDetail；商品图片 productImage；商品原价 productOriginalPrice；商品现价 productCurrentPrice；商品库存 productInventory；商品发货地址 productAddress；商品好评率 productCommentRate；商品状态 productStatus；商店id storeID
    // 返回 状态
    public static boolean addProduct(String productID, String productName, String productDetail, float productOriginalPrice, float productCurrentPrice, int productInventory, String productAddress, float productCommentRate, boolean productStatus, String storeID) throws IOException
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
                    .put("productOriginalPrice", productOriginalPrice)
                    .put("productCurrentPrice", productCurrentPrice)
                    .put("productInventory", productInventory)
                    .put("productAddress", productAddress)
                    .put("productCommentRate", productCommentRate)
                    .put("productStatus", productStatus)
                    .put("storeID", storeID));

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
    public boolean deleteProduct(String productID) throws IOException
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

    // 上架商品
    // 输入 商品id productID
    // 返回 状态
    public boolean enableProduct(String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "enableProduct")
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

    // 下架商品
    // 输入 商品id productID
    // 返回 状态
    public boolean disableProduct(String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "disableProduct")
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

    // 获取所有同类商品
    // 输入 商品id productID
    // 返回 商品数组
    public oneProduct[] getSameCategoryProducts(String productID) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getSameCategory")
                    .put("productID", productID));

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
                JSONObject theproduct = data.getJSONObject(i);

                productsArray[i] = new oneProduct();
                productsArray[i].productID = theproduct.getString("productID");
                productsArray[i].productName = theproduct.getString("productName");
                productsArray[i].productDetail = theproduct.getString("productDetail");
                productsArray[i].productImage = theproduct.optString("productImage", "uploads/defaultproduct.jpg");
                productsArray[i].productOriginalPrice = theproduct.getFloat("productOriginalPrice");
                productsArray[i].productCurrentPrice = theproduct.getFloat("productCurrentPrice");
                productsArray[i].productInventory = theproduct.getInt("productInventory");
                productsArray[i].productAddress = theproduct.getString("productAddress");
                productsArray[i].productCommentRate = theproduct.getFloat("productCommentRate");
                productsArray[i].productStatus = theproduct.getBoolean("productStatus");
                productsArray[i].storeID = theproduct.getString("storeID");
                productsArray[i].storeName = theproduct.getString("storeName");
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 带排序的搜索
    // 输入 检索关键词 searchTerm
    //     排序关键词 sortBy 取值为 price（按价格）或 rate（按好评率）
    //     排序顺序 sortOrder 取值为 ASC（升序） 或 DESC（降序）
    // 返回 商品数组
    public oneProduct[] searchProducts(String searchTerm,String sortBy, String sortOrder) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                            .put("action", "search")
                            .put("searchTerm", searchTerm))
                    .put("sortBy", sortBy)
                    .put("sortOrder", sortOrder);

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
                JSONObject theproduct = data.getJSONObject(i);

                productsArray[i] = new oneProduct();
                productsArray[i].productID = theproduct.getString("productID");
                productsArray[i].productName = theproduct.getString("productName");
                productsArray[i].productDetail = theproduct.getString("productDetail");
                productsArray[i].productImage = theproduct.optString("productImage", "uploads/defaultproduct.jpg");
                productsArray[i].productOriginalPrice = theproduct.getFloat("productOriginalPrice");
                productsArray[i].productCurrentPrice = theproduct.getFloat("productCurrentPrice");
                productsArray[i].productInventory = theproduct.getInt("productInventory");
                productsArray[i].productAddress = theproduct.getString("productAddress");
                productsArray[i].productCommentRate = theproduct.getFloat("productCommentRate");
                productsArray[i].productStatus = theproduct.getBoolean("productStatus");
                productsArray[i].storeID = theproduct.getString("storeID");
                productsArray[i].storeName = theproduct.getString("storeName");
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 更新商品原价
    // 输入 商品id productID； 商品原价更新为 newOriginalPrice
    // 返回 状态
    public boolean updateOriginalPrice(String productID,float newOriginalPrice) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "updateOriginalPrice")
                    .put("productID", productID)
                    .put("newOriginalPrice", newOriginalPrice));

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

    // 更新商品现价
    // 输入 商品id productID； 商品现价更新为 newCurrentPrice
    // 返回 状态
    public boolean updateCurrentPrice(String productID,float newCurrentPrice) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "updateCurrentPrice")
                    .put("productID", productID)
                    .put("newCurrentPrice", newCurrentPrice));

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

    // 增加库存
    // 输入 商品id productID； 商品库存增加 amount
    // 返回 状态
    public boolean increaseInventory(String productID,int amount) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "increaseInventory")
                    .put("productID", productID)
                    .put("amount", amount));

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


    // 减少库存
    // 输入 商品id productID； 商品库存减少 amount
    // 返回 状态
    public boolean decreaseInventory(String productID,int amount) throws IOException
    {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "decreaseInventory")
                    .put("productID", productID)
                    .put("amount", amount));

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

    public oneProduct[] getAllProductsByStore(String storeID) throws IOException{
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//创建一个Socket对象，并连接到指定的服务器地址和端口号
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 输入流，从服务器读取数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)){//创建一个PrintWriter对象，用于向网络连接的输出流写入数据

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getAllByStore")
                    .put("storeID", storeID));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);

            System.out.println(jsonResponse.toString());

            JSONArray data = jsonResponse.getJSONArray("products");//获取JSON数组

            // 获取商品数量
            int numProducts = data.length();

            // 创建一个数组来存储所有商品信息
            ShoppingProduct.oneProduct[] productsArray = new ShoppingProduct.oneProduct[numProducts];

            for (int i = 0; i < numProducts; i++) {

                JSONObject theproduct = data.getJSONObject(i);

                productsArray[i] = new ShoppingProduct.oneProduct();

                productsArray[i].productID = theproduct.getString("productID");
                productsArray[i].productName = theproduct.getString("productName");
                productsArray[i].productDetail = theproduct.getString("productDetail");
                productsArray[i].productImage = theproduct.optString("productImage", "uploads/defaultproduct.jpg");
                productsArray[i].productOriginalPrice = theproduct.getFloat("productOriginalPrice");
                productsArray[i].productCurrentPrice = theproduct.getFloat("productCurrentPrice");
                productsArray[i].productInventory = theproduct.getInt("productInventory");
                productsArray[i].productAddress = theproduct.getString("productAddress");
                productsArray[i].productCommentRate = theproduct.getFloat("productCommentRate");
                productsArray[i].productStatus = theproduct.getBoolean("productStatus");
                productsArray[i].storeID = theproduct.getString("storeID");
                productsArray[i].storeName = theproduct.getString("storeName");
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean uploadProductImage(File imageFile, String productID) {
        String fileName = productID + ".jpg";
        if (fileService.fileExists(fileName)) {
            fileName = productID + "_" + UUID.randomUUID().toString() + ".jpg";
        }

        if (fileService.uploadFile(imageFile, fileName)) {
            return updateProductImagePath(productID, "uploads/" + fileName);
        }
        return false;
    }
    public static boolean updateProductImagePath(String productID, String imagePath) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("productID", productID)
                    .put("imagePath", imagePath)
                    .put("action", "updateProductImagePath"));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request.toString());

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            System.out.println(jsonResponse.toString());
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
