package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class GetAllLibRecordsRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        LibraryService libraryService = new LibraryService();
        JSONObject result = libraryService.getAllLibRecords();

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", result.getString("status"));
        jsonResponse.put("libRecords", result.getJSONArray("libRecords"));
        return jsonResponse.toString();
    }
}
