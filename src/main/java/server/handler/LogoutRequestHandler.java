package server.handler;

import org.json.JSONObject;
import server.service.UserService;

public class LogoutRequestHandler implements RequestHandler {
    private final UserService userService;

    public LogoutRequestHandler() {
        this.userService = new UserService();
    }

    @Override
    public String handle(JSONObject parameters) {
        return userService.logout(parameters);
    }
}