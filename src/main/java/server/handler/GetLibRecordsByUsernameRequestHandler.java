package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class GetLibRecordsByUsernameRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        JSONObject result = libraryService.getLibRecordsByUsername(parameters.getString("username"));

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success").put("data", result);
        return jsonResponse.toString();
    }
}
