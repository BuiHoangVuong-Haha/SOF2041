package Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Model_Course_Management {
    private Integer code;
    private String courseCode;
    private float fee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date openingDay;
    private String staffCode;
    private int duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dayOfCreation;
    private String description;

    public Model_Course_Management() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public Date getOpeningDay() {
        return openingDay;
    }

    public void setOpeningDay(Date openingDay) {
        this.openingDay = openingDay;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public Date getDayOfCreation() {
        return dayOfCreation;
    }

    public void setDayOfCreation(Date dayOfCreation) {
        this.dayOfCreation = dayOfCreation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
