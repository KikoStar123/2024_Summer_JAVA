package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

/**
 * 处理更新图书图片路径的请求。
 */
public class UpdateBookImagePathRequestHandler implements RequestHandler {
    /**
     * 处理更新图书图片路径的请求。
     *
     * @param parameters 请求参数的 JSON 对象，包含书籍 ID 和图片路径
     * @return 返回包含操作结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        String bookID = parameters.getString("bookID");
        String imagePath = parameters.getString("imagePath");

        LibraryService libraryService = new LibraryService();
        JSONObject response = libraryService.updateBookImagePath(bookID, imagePath);

        return response.toString();
    }
}
