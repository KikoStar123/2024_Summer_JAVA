package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class SearchBooksByNameRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        JSONObject searchResult = libraryService.searchBooksByName(parameters.getString("bookName"));

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success").put("data", searchResult);
        return jsonResponse.toString();
    }
}
