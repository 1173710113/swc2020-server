package com.example.demo.domain;

public class TeacherCourse extends Course {

    private String code;

    /**
     * @param id
     * @param name
     * @param teacherId
     * @param teacherName
     * @param maxVol
     * @param destination
     * @param startTime
     * @param endTime
     * @param realVol
     */
    public TeacherCourse(String id, String name, String teacherId, String teacherName, int maxVol, String destination, String startTime, String endTime, int realVol) {
        super(id, name, teacherId, teacherName, maxVol, destination, startTime, endTime, realVol);
    }
    
    public TeacherCourse(Course course) {
    	super(course.getId(), course.getName(), course.getTeacherId(), course.getTeacherName(),course.getMaxVol(), course.getDestination(), course.getStartTime(), course.getEndTime(), course.getRealVol());
    	this.code = "";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
