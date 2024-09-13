package client.service;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 购物商品服务类，提供商品的相关操作，包括查看、添加、删除、更新商品信息等功能。
 */
public class ShoppingProduct {
    private static final String SERVER_ADDRESS = IpConfig.SERVER_ADDRESS;
    private static final int SERVER_PORT = IpConfig.SERVER_PORT;
    private static final FileService fileService = new FileService();

    /**
     * 内部类，用于表示商品信息
     */
    public static class oneProduct {
        String productID;              // 商品ID
        String productName;            // 商品名称
        String productDetail;          // 商品详情
        String productImage;           // 商品图片路径
        float productOriginalPrice;    // 商品原价
        float productCurrentPrice;     // 商品现价
        int productInventory;          // 商品库存
        String productAddress;         // 商品发货地址
        float productCommentRate;      // 商品好评率
        boolean productStatus;         // 商品状态，是否上架
        String storeID;                // 商店ID
        String storeName;              // 商店名称

        /**
         * 默认构造函数
         */
        public oneProduct() {
            // 默认构造函数
        }

        // Getters
        public String getProductID() { return productID; }
        public String getProductName() { return productName; }
        public String getProductDetail() { return productDetail; }
        public String getProductImage() { return productImage; }
        public float getProductOriginalPrice() { return productOriginalPrice; }
        public float getProductCurrentPrice() { return productCurrentPrice; }
        public int getProductInventory() { return productInventory; }
        public String getProductAddress() { return productAddress; }
        public float getProductCommentRate() { return productCommentRate; }
        public boolean isProductStatus() { return productStatus; }
        public String getStoreID() { return storeID; }
        public String getStoreName() { return storeName; }
    }

    /**
     * 查看单个商品的详细信息
     *
     * @param productID 商品ID
     * @return 一个商品对象
     * @throws IOException 如果发生IO异常
     */
    public static oneProduct getProductDetails(String productID) throws IOException {
        oneProduct product = new oneProduct();  // 创建新的商品对象

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getDetails")
                    .put("productID", productID));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject data = jsonResponse.getJSONObject("product");

            // 填充商品信息
            product.productID = productID;
            product.productName = data.getString("productName");
            product.productDetail = data.getString("productDetail");
            product.productImage = data.optString("productImage", "uploads/defaultproduct.jpg");
            product.productOriginalPrice = data.getFloat("productOriginalPrice");
            product.productCurrentPrice = data.getFloat("productCurrentPrice");
            product.productInventory = data.getInt("productInventory");
            product.productAddress = data.getString("productAddress");
            product.productCommentRate = data.getFloat("productCommentRate");
            product.productStatus = data.getBoolean("productStatus");
            product.storeID = data.getString("storeID");
            product.storeName = data.getString("storeName");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return product;  // 返回新创建的商品对象
    }

    /**
     * 查看所有商品的详细信息
     *
     * @param sortBy    排序关键词，取值为 price（按价格）或 rate（按好评率）
     * @param sortOrder 排序顺序，取值为 ASC（升序）或 DESC（降序）
     * @return 商品数组
     * @throws IOException 如果发生IO异常
     */
    public oneProduct[] getAllProducts(String sortBy, String sortOrder) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "getAll")
                    .put("sortBy", sortBy)
                    .put("sortOrder", sortOrder));

            // 发送请求
            out.println(request);

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("products");

            // 创建一个数组来存储所有商品信息
            int numProducts = data.length();
            oneProduct[] productsArray = new oneProduct[numProducts];

            for (int i = 0; i < numProducts; i++) {
                JSONObject productData = data.getJSONObject(i);
                oneProduct product = new oneProduct();

                product.productID = productData.getString("productID");
                product.productName = productData.getString("productName");
                product.productDetail = productData.getString("productDetail");
                product.productImage = productData.optString("productImage", "uploads/defaultproduct.jpg");
                product.productOriginalPrice = productData.getFloat("productOriginalPrice");
                product.productCurrentPrice = productData.getFloat("productCurrentPrice");
                product.productInventory = productData.getInt("productInventory");
                product.productAddress = productData.getString("productAddress");
                product.productCommentRate = productData.getFloat("productCommentRate");
                product.productStatus = productData.getBoolean("productStatus");
                product.storeID = productData.getString("storeID");
                product.storeName = productData.getString("storeName");

                productsArray[i] = product;
            }

            return productsArray;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 添加商品
     *
     * @param productID            商品ID
     * @param productName          商品名称
     * @param productDetail        商品详情
     * @param productOriginalPrice 商品原价
     * @param productCurrentPrice  商品现价
     * @param productInventory     商品库存
     * @param productAddress       商品发货地址
     * @param productStatus        商品状态
     * @param storeID              商店ID
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public static boolean addProduct(String productID, String productName, String productDetail, float productOriginalPrice, float productCurrentPrice, int productInventory, String productAddress, boolean productStatus, String storeID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 删除商品
     *
     * @param productID 商品ID
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean deleteProduct(String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 上架商品
     *
     * @param productID 商品ID
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean enableProduct(String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 下架商品
     *
     * @param productID 商品ID
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean disableProduct(String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 获取同类商品信息
     *
     * @param productID 商品ID
     * @return 同类商品数组
     * @throws IOException 如果发生IO异常
     */
    public oneProduct[] getSameCategoryProducts(String productID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

            JSONArray data = jsonResponse.getJSONArray("products");

            int numProducts = data.length();
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

    /**
     * 带排序的搜索商品
     *
     * @param searchTerm 检索关键词
     * @param sortBy     排序关键词，取值为 price（按价格）或 rate（按好评率）
     * @param sortOrder  排序顺序，取值为 ASC（升序）或 DESC（降序）
     * @return 商品数组
     * @throws IOException 如果发生IO异常
     */
    public oneProduct[] searchProducts(String searchTerm, String sortBy, String sortOrder) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("action", "search")
                    .put("searchTerm", searchTerm)
                    .put("sortBy", sortBy)
                    .put("sortOrder", sortOrder));

            // 发送请求
            out.println(request);

            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("products");

            int numProducts = data.length();
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

    /**
     * 更新商品原价
     *
     * @param productID        商品ID
     * @param newOriginalPrice 新的原价
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean updateOriginalPrice(String productID, float newOriginalPrice) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 更新商品现价
     *
     * @param productID       商品ID
     * @param newCurrentPrice 新的现价
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean updateCurrentPrice(String productID, float newCurrentPrice) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 增加库存
     *
     * @param productID 商品ID
     * @param amount    增加的数量
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean increaseInventory(String productID, int amount) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 减少库存
     *
     * @param productID 商品ID
     * @param amount    减少的数量
     * @return 是否成功
     * @throws IOException 如果发生IO异常
     */
    public boolean decreaseInventory(String productID, int amount) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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

    /**
     * 根据商店ID获取所有商品
     *
     * @param storeID 商店ID
     * @return 商品数组
     * @throws IOException 如果发生IO异常
     */
    public oneProduct[] getAllProductsByStore(String storeID) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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
            JSONArray data = jsonResponse.getJSONArray("products");

            int numProducts = data.length();
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
            return null;
        }
    }

    /**
     * 上传商品图片
     *
     * @param imageFile 商品图片文件
     * @param productID 商品ID
     * @return 是否成功
     */
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

    /**
     * 更新商品图片路径
     *
     * @param productID 商品ID
     * @param imagePath 新的图片路径
     * @return 是否成功
     */
    public static boolean updateProductImagePath(String productID, String imagePath) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // 构建请求
            JSONObject request = new JSONObject();
            request.put("requestType", "product");
            request.put("parameters", new JSONObject()
                    .put("productID", productID)
                    .put("imagePath", imagePath)
                    .put("action", "updateProductImagePath"));

            // 发送请求
            out.println(request.toString());

            // 读取响应
            String response = in.readLine();
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getString("status").equals("success");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
