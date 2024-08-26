package client.service;

import client.service.StudentInformation;
import org.json.JSONObject;

public class StudentInformationTest {
    public static void main(String[] args) {
        StudentInformation studentInfoService = new StudentInformation();

        // 测试查看学生信息
        System.out.println("Testing viewStudentInfo method:");
        boolean viewResult = studentInfoService.viewStudentInfo(Role.student, "1");
        System.out.println("View Student Info Result: " + viewResult);


        // 测试查看所有学生信息与修改学生信息
        System.out.println("\nTesting modifyStudentInfo method:");
        JSONObject newStudentInfo = new JSONObject();
        newStudentInfo.put("id", "1");
        newStudentInfo.put("name", "Jane Doe");
        newStudentInfo.put("gender", "Female");
        newStudentInfo.put("origin", "California");
        newStudentInfo.put("birthday", "2002-05-15");
        newStudentInfo.put("academy", "Mathematics");

        boolean modifyResult = studentInfoService.modifyStudentInfo(Role.teacher, newStudentInfo.toString());
        System.out.println("Modify Student Info Result: " + modifyResult);
    }
}
