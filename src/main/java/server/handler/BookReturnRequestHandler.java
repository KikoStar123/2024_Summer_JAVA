package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class BookReturnRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        int bookId = parameters.getInt("borrowID");
        JSONObject result = libraryService.returnBook(bookId);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
