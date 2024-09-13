package client.service;

/**
 * 角色枚举类，定义系统中不同用户的角色类型。
 */
public enum Role {
    /**
     * 学生角色，普通用户身份。
     */
    student,         // 001学生--普通身份

    /**
     * 银行管理员角色，负责管理银行系统。
     */
    BankManager,     // 银行管理员

    /**
     * 图书馆管理员角色，负责管理图书馆系统。
     */
    Librarian,       // 图书馆管理员

    /**
     * 商店管理员角色，商家身份，负责管理商店系统。
     */
    ShopAssistant,   // 商店管理员--商家

    /**
     * 选课管理员角色，负责管理选课系统。
     */
    CourseManager,   // 选课管理员

    /**
     * 学籍管理员角色，负责管理学籍系统。
     */
    StuInfoManager   // 学籍管理员
}
