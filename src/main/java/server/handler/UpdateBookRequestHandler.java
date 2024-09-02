package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class UpdateBookRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        String bookID = parameters.getString("bookID");
        int finalLibNumber = parameters.getInt("finallibNumber");
        JSONObject result = libraryService.updateBook(bookID, finalLibNumber);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
