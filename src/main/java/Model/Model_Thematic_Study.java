package Model;

public class Model_Thematic_Study {
    private String code;
    private String name;
    private float tuitionFee;
    private int time;
    private String picture;
    private String describe;

    public Model_Thematic_Study() {
    }

    public Object getCode() {
        return code;
    }

    public Object getName() {
        return name;
    }

    public Object getTuitionFee() {
        return tuitionFee;
    }

    public Object getTime() {
        return time;
    }

    public Object getPicture() {
        return picture;
    }

    public Object getDescribe() {
        return describe;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTuitionFee(float tuitionFee) {
        this.tuitionFee = tuitionFee;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
