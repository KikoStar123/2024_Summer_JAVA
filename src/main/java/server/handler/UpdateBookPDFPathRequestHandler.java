package server.handler;

import server.service.LibraryService;
import org.json.JSONObject;

public class UpdateBookPDFPathRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        String bookID = parameters.getString("bookID");
        String pdfPath = parameters.getString("pdfPath");

        LibraryService libraryService = new LibraryService();
        JSONObject response = libraryService.updateBookPDFPath(bookID, pdfPath);

        return response.toString();
    }
}