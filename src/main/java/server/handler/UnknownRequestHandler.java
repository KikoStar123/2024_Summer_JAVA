package server.handler;

import org.json.JSONObject;

public class UnknownRequestHandler implements RequestHandler {
    @Override
    public String handle(JSONObject parameters) {
        return new JSONObject().put("status", "error").put("message", "Unknown request").toString();
    }
}
