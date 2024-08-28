package client.service;

public class User {
    private String id;//用户登录名
    private String username;//用户姓名
    private Role role;//用户角色：老师/学生/管理员
    private String pwd;//password
    private int age;
    private Gender gender;

    public User(String userName, Role role, int age, Gender gender,String pwd) {
        this.username = userName;
        this.role = role;
        this.age = age;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }
    // 构造函数（可选，根据需要实现）
    public User(String id, String username, Role role, String pwd, int age,Gender gender) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.pwd = pwd;
        this.age = age;
        this.gender = gender;
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
    public Role getRole() {
        return role;
    }

    // 设置用户角色
    public void setRole(Role role) {
        this.role = role;
    }

    // 获取用户角色
    public Gender getgender() {
        return gender;
    }

    // 设置用户角色
    public void setGender(Gender gender) {
        this.gender = gender;
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
