package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class BookBorrowRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        String username = parameters.getString("username");
        String bookId = parameters.getString("bookId");
        JSONObject result = libraryService.borrowBook(username, bookId);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
