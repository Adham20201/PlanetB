package com.example.planetb.lists;

import android.media.Image;

import java.net.URL;

public class Courses {
    String courseName , courseOrganizer, courseCategory ,courseLanguage, courseType, courseLevel, courseUrl , courseImageUrl , courseBrief;
    Integer courseImage;

    public String getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(String courseCategory) {
        this.courseCategory = courseCategory;
    }

    public Courses(String courseName, String courseOrganizer, String courseCategory, String courseLanguage, String courseType, String courseLevel, String courseImageUrl , String courseUrl, String courseBrief , Integer courseImage) {
        this.courseName = courseName;
        this.courseOrganizer = courseOrganizer;
        this.courseCategory = courseCategory;
        this.courseLanguage = courseLanguage;
        this.courseType = courseType;
        this.courseLevel = courseLevel;
        this.courseImageUrl = courseImageUrl;
        this.courseUrl = courseUrl;
        this.courseBrief = courseBrief;
        this.courseImage = courseImage;
    }

    public Courses(String courseName, String courseOrganizer, String courseCategory, String courseLanguage, String courseType, String courseLevel , String courseUrl, String courseBrief) {
        this.courseName = courseName;
        this.courseOrganizer = courseOrganizer;
        this.courseCategory = courseCategory;
        this.courseLanguage = courseLanguage;
        this.courseType = courseType;
        this.courseLevel = courseLevel;
        this.courseImageUrl = courseImageUrl;
        this.courseUrl = courseUrl;
        this.courseBrief = courseBrief;
        this.courseImage = courseImage;
    }

    public Courses(String courseName, String courseOrganizer, Integer courseImage) {
        this.courseName = courseName;
        this.courseOrganizer = courseOrganizer;
        this.courseImage = courseImage;
    }

    public Courses() {

    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseOrganizer() {
        return courseOrganizer;
    }

    public void setCourseOrganizer(String courseOrganizer) {
        this.courseOrganizer = courseOrganizer;
    }

    public Integer getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(Integer courseImage) {
        this.courseImage = courseImage;
    }

    public String getCourseLanguage() {
        return courseLanguage;
    }

    public void setCourseLanguage(String courseLanguage) {
        this.courseLanguage = courseLanguage;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public String getCourseUrl() {
        return courseUrl;
    }

    public void setCourseUrl(String courseUrl) {
        this.courseUrl = courseUrl;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }

    public void setCourseImageUrl(String courseImageUrl) {
        this.courseImageUrl = courseImageUrl;
    }

    public String getCourseBrief() {
        return courseBrief;
    }

    public void setCourseBrief(String courseBrief) {
        this.courseBrief = courseBrief;
    }
}
