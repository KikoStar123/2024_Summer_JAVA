package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class GetBookDetailsByIdRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();

        JSONObject bookDetails = libraryService.getBookDetailsById(parameters.getString("bookId"));


        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", bookDetails.getString("status"));
        if (bookDetails.getString("status").equals("success")) {
            jsonResponse.put("book", bookDetails.getJSONObject("book"));
        } else {
            jsonResponse.put("message", bookDetails.getString("message"));
        }
        return jsonResponse.toString();
    }
}
