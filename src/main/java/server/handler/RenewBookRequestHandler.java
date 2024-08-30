package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class RenewBookRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        int borrowID = parameters.getInt("borrowID");
        JSONObject result = libraryService.renewBook(borrowID);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("message", result.getString("message"));
        return jsonResponse.toString();
    }
}
