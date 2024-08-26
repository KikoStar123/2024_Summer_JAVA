package server.handler;

import org.json.JSONObject;

public interface RequestHandler {
    String handle(JSONObject parameters);
}
