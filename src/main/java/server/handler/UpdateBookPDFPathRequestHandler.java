package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * 处理更新图书 PDF 路径的请求。
 */
public class UpdateBookPDFPathRequestHandler implements RequestHandler {
    /**
     * 处理更新图书 PDF 路径的请求。
     *
     * @param parameters 请求参数的 JSON 对象，包含书籍 ID 和 PDF 路径
     * @return 返回包含操作结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        String bookID = parameters.getString("bookID");
        String pdfPath = parameters.getString("pdfPath");

        LibraryService libraryService = new LibraryService();
        JSONObject response = libraryService.updateBookPDFPath(bookID, pdfPath);

        return response.toString();
    }
}