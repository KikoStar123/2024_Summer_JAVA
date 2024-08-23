package client.service;

public class User {

    private String id;//用户登录名
    private String username;//用户姓名
    private String role;//用户角色：老师/学生/管理员
    private String pwd;//password
    private int age;

    public String getId() {
        return id;
    }
    // 构造函数（可选，根据需要实现）
    public User(String id, String username, String role, String pwd, int age) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.pwd = pwd;
        this.age = age;
    }

    // Getter 和 Setter 方法

    // 设置用户登录名
    public void setId(String id) {
        this.id = id;
    }

    // 获取用户姓名
    public String getUsername() {
        return username;
    }

    // 设置用户姓名
    public void setUsername(String username) {
        this.username = username;
    }

    // 获取用户角色
    public String getRole() {
        return role;
    }

    // 设置用户角色
    public void setRole(String role) {
        this.role = role;
    }

    // 获取密码
    public String getPwd() {
        return pwd;
    }

    // 设置密码
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    // 获取年龄
    public int getAge() {
        return age;
    }

    // 设置年龄
    public void setAge(int age) {
        this.age = age;
    }
}
