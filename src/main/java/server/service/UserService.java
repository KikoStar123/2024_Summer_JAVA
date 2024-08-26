package server.service;

public class UserService {
    public boolean login(String username, String password) {
        // 这里可以连接数据库进行验证
        return "admin".equals(username) && "admin".equals(password);
    }
}