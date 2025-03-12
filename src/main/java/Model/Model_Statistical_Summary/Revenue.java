package Model.Model_Statistical_Summary;

public class Revenue {
    private String thematic;
    private int courseNumber;
    private int stuNumber;
    private float feeTotal;
    private float feeHighest;
    private float feeLowest;
    private float feeAvg;

    public Revenue() {
    }

    public String getThematic() {
        return thematic;
    }

    public void setThematic(String thematic) {
        this.thematic = thematic;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public int getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(int stuNumber) {
        this.stuNumber = stuNumber;
    }

    public float getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(float feeTotal) {
        this.feeTotal = feeTotal;
    }

    public float getFeeHighest() {
        return feeHighest;
    }

    public void setFeeHighest(float feeHighest) {
        this.feeHighest = feeHighest;
    }

    public float getFeeLowest() {
        return feeLowest;
    }

    public void setFeeLowest(float feeLowest) {
        this.feeLowest = feeLowest;
    }

    public float getFeeAvg() {
        return feeAvg;
    }

    public void setFeeAvg(float feeAvg) {
        this.feeAvg = feeAvg;
    }
}
