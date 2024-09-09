package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class UpdateBookImagePathRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        String bookID = parameters.getString("bookID");
        String imagePath = parameters.getString("imagePath");

        LibraryService libraryService = new LibraryService();
        JSONObject response = libraryService.updateBookImagePath(bookID, imagePath);

        return response.toString();
    }
}
