package com.example.planetb.lists;

public class Categories {
    String categoryName;
    Integer courseIcon;

    public Categories(String categoryName, Integer courseIcon) {
        this.categoryName = categoryName;
        this.courseIcon = courseIcon;
    }

    public Categories() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getCourseIcon() {
        return courseIcon;
    }

    public void setCourseIcon(Integer courseIcon) {
        this.courseIcon = courseIcon;
    }
}
