package testclient.shop;

import client.service.ShoppingProduct;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ShoppingProductTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ShoppingProduct shoppingProduct = new ShoppingProduct();

        while (true) {
            System.out.println("请选择操作:");
            System.out.println("1. 查看单个商品详细信息");
            System.out.println("2. 查看所有商品");
            System.out.println("3. 添加商品");
            System.out.println("4. 删除商品");
            System.out.println("5. 上架商品");
            System.out.println("6. 下架商品");
            System.out.println("7. 增加商品库存");
            System.out.println("8. 减少商品库存");
            System.out.println("9. 检索同类商品");
            System.out.println("10. 搜索商品");
            System.out.println("11. 更新商品原价");
            System.out.println("12. 更新商品现价");
            System.out.println("13. 查看商店内所有商品");
            System.out.println("14. 退出");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 处理换行符

            try {
                switch (choice) {
                    case 1:
                        System.out.println("请输入商品ID:");
                        String productID = scanner.nextLine();
                        ShoppingProduct.oneProduct product = shoppingProduct.getProductDetails(productID);
                        System.out.println("商品详情:");
                        System.out.println("商品ID: " + product.getProductID());
                        System.out.println("商品名称: " + product.getProductName());
                        System.out.println("商品详情: " + product.getProductDetail());
                        System.out.println("商品原价: " + product.getProductOriginalPrice());
                        System.out.println("商品现价: " + product.getProductCurrentPrice());
                        System.out.println("商品库存: " + product.getProductInventory());
                        System.out.println("商品发货地址: " + product.getProductAddress());
                        System.out.println("商品好评率: " + product.getProductCommentRate());
                        System.out.println("商品状态: " + (product.isProductStatus() ? "上架" : "下架"));
                        System.out.println("商店ID：" + product.getStoreID());
                        System.out.println("商店名称：" + product.getStoreName());
                        break;
                    case 2:
                        System.out.println("请输入排序方式(price/rate):");
                        String sortBy = scanner.nextLine();
                        System.out.println("请输入排序顺序(ASC/DESC):");
                        String sortOrder = scanner.nextLine();
                        ShoppingProduct.oneProduct[] allProducts = shoppingProduct.getAllProducts(sortBy, sortOrder);
                        if (allProducts != null) {
                            for (ShoppingProduct.oneProduct p : allProducts) {
                                System.out.println("商品ID: " + p.getProductID() + ", 商品名称: " + p.getProductName());
                            }
                        }
                        break;
                    case 3:
                        System.out.println("请输入商品ID:");
                        String addProductID = scanner.nextLine();
                        System.out.println("请输入商品名称:");
                        String productName = scanner.nextLine();
                        System.out.println("请输入商品详情:");
                        String productDetail = scanner.nextLine();
                        System.out.println("请输入商品图片(路径或URL):");
                        String productImage = scanner.nextLine();
                        System.out.println("请输入商品原价:");
                        float productOriginalPrice = scanner.nextFloat();
                        System.out.println("请输入商品现价:");
                        float productCurrentPrice = scanner.nextFloat();
                        System.out.println("请输入商品库存:");
                        int productInventory = scanner.nextInt();
                        scanner.nextLine(); // 处理换行符
                        System.out.println("请输入商品发货地址:");
                        String productAddress = scanner.nextLine();
                        System.out.println("请输入商店ID:");
                        String storeID = scanner.nextLine();
                        System.out.println("请输入商品好评率(0-5):");
                        float productCommentRate = scanner.nextFloat();
                        System.out.println("请输入商品状态(1:上架, 0:下架):");
                        boolean productStatus = scanner.nextInt() == 1;

                        boolean addProductSuccess = shoppingProduct.addProduct(addProductID, productName, productDetail,
                                productOriginalPrice, productCurrentPrice, productInventory, productAddress, productCommentRate, productStatus, storeID);
                        System.out.println("添加商品" + (addProductSuccess ? "成功" : "失败"));
                        break;
                    case 4:
                        System.out.println("请输入要删除的商品ID:");
                        String deleteProductID = scanner.nextLine();
                        boolean deleteProductSuccess = shoppingProduct.deleteProduct(deleteProductID);
                        System.out.println("删除商品" + (deleteProductSuccess ? "成功" : "失败"));
                        break;
                    case 5:
                        System.out.println("请输入要上架的商品ID:");
                        String enableProductID = scanner.nextLine();
                        boolean enableProductSuccess = shoppingProduct.enableProduct(enableProductID);
                        System.out.println("上架商品" + (enableProductSuccess ? "成功" : "失败"));
                        break;
                    case 6:
                        System.out.println("请输入要下架的商品ID:");
                        String disableProductID = scanner.nextLine();
                        boolean disableProductSuccess = shoppingProduct.disableProduct(disableProductID);
                        System.out.println("下架商品" + (disableProductSuccess ? "成功" : "失败"));
                        break;
                    case 7:
                        System.out.println("请输入商品ID:");
                        String increaseInventoryID = scanner.nextLine();
                        System.out.println("请输入增加的库存数量:");
                        int increaseAmount = scanner.nextInt();
                        boolean increaseInventorySuccess = shoppingProduct.increaseInventory(increaseInventoryID, increaseAmount);
                        System.out.println("增加库存" + (increaseInventorySuccess ? "成功" : "失败"));
                        break;
                    case 8:
                        System.out.println("请输入商品ID:");
                        String decreaseInventoryID = scanner.nextLine();
                        System.out.println("请输入减少的库存数量:");
                        int decreaseAmount = scanner.nextInt();
                        boolean decreaseInventorySuccess = shoppingProduct.decreaseInventory(decreaseInventoryID, decreaseAmount);
                        System.out.println("减少库存" + (decreaseInventorySuccess ? "成功" : "失败"));
                        break;
                    case 9:
                        System.out.println("请输入商品ID以查看同类商品:");
                        String sameCategoryProductID = scanner.nextLine();
                        ShoppingProduct.oneProduct[] sameCategoryProducts = shoppingProduct.getSameCategoryProducts(sameCategoryProductID);
                        if (sameCategoryProducts != null) {
                            for (ShoppingProduct.oneProduct p : sameCategoryProducts) {
                                System.out.println("商品ID: " + p.getProductID() + ", 商品名称: " + p.getProductName());
                            }
                        }
                        break;
                    case 10:
                        System.out.println("请输入检索关键词:");
                        String searchTerm = scanner.nextLine();
                        System.out.println("请输入排序方式(price/rate):");
                        String searchSortBy = scanner.nextLine();
                        System.out.println("请输入排序顺序(ASC/DESC):");
                        String searchSortOrder = scanner.nextLine();
                        ShoppingProduct.oneProduct[] searchResults = shoppingProduct.searchProducts(searchTerm, searchSortBy, searchSortOrder);
                        if (searchResults != null) {
                            for (ShoppingProduct.oneProduct p : searchResults) {
                                System.out.println("商品ID: " + p.getProductID() + ", 商品名称: " + p.getProductName());
                            }
                        }
                        break;
                    case 11:
                        System.out.println("请输入商品ID:");
                        String updateOriginalPriceID = scanner.nextLine();
                        System.out.println("请输入新的原价:");
                        float newOriginalPrice = scanner.nextFloat();
                        boolean updateOriginalPriceSuccess = shoppingProduct.updateOriginalPrice(updateOriginalPriceID, newOriginalPrice);
                        System.out.println("更新商品原价" + (updateOriginalPriceSuccess ? "成功" : "失败"));
                        break;
                    case 12:
                        System.out.println("请输入商品ID:");
                        String updateCurrentPriceID = scanner.nextLine();
                        System.out.println("请输入新的现价:");
                        float newCurrentPrice = scanner.nextFloat();
                        boolean updateCurrentPriceSuccess = shoppingProduct.updateCurrentPrice(updateCurrentPriceID, newCurrentPrice);
                        System.out.println("更新商品现价" + (updateCurrentPriceSuccess ? "成功" : "失败"));
                        break;
                    case 13:
                        System.out.println("请输入商店ID:");
                        storeID = scanner.nextLine();
                        ShoppingProduct.oneProduct[] allProductsInStore = shoppingProduct.getAllProductsByStore(storeID);
                        if (allProductsInStore != null) {
                            for (ShoppingProduct.oneProduct p : allProductsInStore) {
                                System.out.println("商品ID: " + p.getProductID() + ", 商品名称: " + p.getProductName());
                            }
                        }
                        break;
                    case 14:
                        System.out.println("退出程序");
                        System.exit(0);
                    default:
                        System.out.println("无效的选择，请重新输入");
                }
            } catch (IOException e) {
                System.out.println("操作失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
