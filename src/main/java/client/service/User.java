package client.service;

/**
 * 用户类，用于表示用户的基本信息，包括真实姓名、账号、角色、密码、年龄、性别和邮箱地址。
 */
public class User {
    private String truename; // 用户真实姓名
    private String username; // 登录名
    private Role role; // 用户角色：老师/学生/管理员
    private String pwd; // 用户密码
    private int age; // 用户年龄
    private Gender gender; // 用户性别
    private String EmailAddress; // 用户邮箱地址

    /**
     * 带参数的构造函数，初始化用户的部分信息。
     *
     * @param userName 登录名
     * @param role     用户角色
     * @param age      用户年龄
     * @param gender   用户性别
     * @param pwd      用户密码
     */
    public User(String userName, Role role, int age, Gender gender, String pwd) {
        this.username = userName;
        this.role = role;
        this.age = age;
        this.gender = gender;
        this.pwd = pwd;
    }

    /**
     * 无参构造函数，设置默认角色为学生，默认性别为男。
     */
    public User() {
        // 例如，可以设置默认角色或性别
        this.role = Role.student; // 假设默认角色为学生
        this.gender = Gender.male; // 假设默认性别为男
        // 其他字段可以根据需要设置默认值或者保持为null
    }

    /**
     * 带全部参数的构造函数，初始化用户的所有信息。
     *
     * @param truename  用户真实姓名
     * @param username  登录名
     * @param role      用户角色
     * @param pwd       用户密码
     * @param age       用户年龄
     * @param gender    用户性别
     */
    public User(String truename, String username, Role role, String pwd, int age, Gender gender) {
        this.truename = truename;
        this.username = username;
        this.role = role;
        this.pwd = pwd;
        this.age = age;
        this.gender = gender;
    }

    // Getter 和 Setter 方法

    /**
     * 获取用户真实姓名。
     *
     * @return 用户真实姓名
     */
    public String getTruename() {
        return truename;
    }

    /**
     * 设置用户真实姓名。
     *
     * @param truename 用户真实姓名
     */
    public void setTruename(String truename) {
        this.truename = truename;
    }

    /**
     * 获取用户登录名。
     *
     * @return 用户登录名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户登录名。
     *
     * @param username 用户登录名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取用户角色。
     *
     * @return 用户角色
     */
    public Role getRole() {
        return role;
    }

    /**
     * 设置用户角色。
     *
     * @param role 用户角色
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * 获取用户性别。
     *
     * @return 用户性别
     */
    public Gender getgender() {
        return gender;
    }

    /**
     * 设置用户性别。
     *
     * @param gender 用户性别
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * 获取用户密码。
     *
     * @return 用户密码
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置用户密码。
     *
     * @param pwd 用户密码
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 获取用户年龄。
     *
     * @return 用户年龄
     */
    public int getAge() {
        return age;
    }

    /**
     * 设置用户年龄。
     *
     * @param age 用户年龄
     */
    public void setAge(int age) {
        this.age = age;
    }
}
