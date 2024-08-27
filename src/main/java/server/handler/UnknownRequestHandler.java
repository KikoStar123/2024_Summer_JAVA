package server.handler;

import org.json.JSONObject;

public class UnknownRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "error");
            response.put("message", "Unknown request type");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }
}