package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;
/**
 * 处理按书名搜索图书的请求。
 * 该类负责从客户端接收书名查询请求，并调用 LibraryService 进行搜索。
 */
public class SearchBooksByNameRequestHandler implements RequestHandler {
    /**
     * 处理按书名搜索图书的请求。
     *
     * @param parameters 包含搜索条件（书名）的 JSON 对象
     * @return 返回包含搜索结果的 JSON 字符串
     */
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        JSONObject searchResult = libraryService.searchBooksByName(parameters.getString("bookName"));

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success").put("data", searchResult);
        return jsonResponse.toString();
    }
}
