package testclient.course;

import client.service.CourseSelection;

public class CourseSelectionTest {

    public static void main(String[] args) {
        CourseSelection courseSelection = new CourseSelection();

        // 测试获取所有课程
        System.out.println("测试获取所有课程：");
        CourseSelection.oneCourseinfo[] courses = courseSelection.GetAllCourses();
        if (courses != null) {
            for (CourseSelection.oneCourseinfo course : courses) {
                System.out.println("课程ID: " + course.getCourseID() + ", 课程名称: " + course.getCourseName());
            }
        } else {
            System.out.println("获取课程失败");
        }

        // 测试选课功能
        System.out.println("\n测试选课功能：");
        String username = "200000001";
        String courseID = "CS101";
        boolean enrollSuccess = courseSelection.enrollInCourse(username, courseID);
        System.out.println("选课 " + courseID + " 结果: " + (enrollSuccess ? "成功" : "失败"));



        // 测试查看已选课程
        System.out.println("\n测试查看已选课程：");
        CourseSelection.oneCourseinfo[] enrolledCourses = courseSelection.viewEnrolledCourses(username);
        if (enrolledCourses != null) {
            for (CourseSelection.oneCourseinfo course : enrolledCourses) {
                System.out.println("已选课程ID: " + course.getCourseID() + ", 课程名称: " + course.getCourseName());
            }
        } else {
            System.out.println("查看已选课程失败");
        }

        // 测试查看单个课程的详细信息
        System.out.println("\n测试查看单个课程的详细信息：");
        CourseSelection.oneCourseinfo courseInfo = courseSelection.viewCourseInfo(courseID);
        if (courseInfo != null) {
            System.out.println("课程ID: " + courseInfo.getCourseID() + ", 课程名称: " + courseInfo.getCourseName()
                    + ", 教师: " + courseInfo.getCourseTeacher() + ", 学分: " + courseInfo.getCourseCredits());
        } else {
            System.out.println("查看课程详细信息失败");
        }

        // 测试添加课程
        System.out.println("\n测试添加课程：");
        String newCourseID = "CS102";
        String newCourseName = "Data Structures";
        String newCourseTeacher = "Prof. Smith";
        int newCourseCredits = 4;
        String newCourseTime = "1-16|1|1-2;1-16|3|3-4";
        int newCourseCapacity = 100;
        String newcourseRoom="J1-206";
        String newcourseType="required";
        boolean addCourseSuccess = courseSelection.addCourse(newCourseID, newCourseName, newCourseTeacher,
                newCourseCredits, newCourseTime, newCourseCapacity, newcourseRoom, newcourseType);
        System.out.println("添加课程 " + newCourseID + " 结果: " + (addCourseSuccess ? "成功" : "失败"));

        // 测试搜索课程
        System.out.println("\n测试搜索课程：");
        CourseSelection.oneCourseinfo[] searchResults = courseSelection.searchCourses(newCourseName, "");
        if (searchResults != null) {
            for (CourseSelection.oneCourseinfo course : searchResults) {
                System.out.println("搜索到的课程ID: " + course.getCourseID() + ", 课程名称: " + course.getCourseName());
            }
        } else {
            System.out.println("搜索课程失败");
        }

        // 测试退课功能
        System.out.println("\n测试退课功能：");
        boolean dropSuccess = courseSelection.dropCourse(username, courseID);
        System.out.println("退课 " + courseID + " 结果: " + (dropSuccess ? "成功" : "失败"));
    }
}
