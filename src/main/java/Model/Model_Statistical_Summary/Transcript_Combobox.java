package Model.Model_Statistical_Summary;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transcript_Combobox {
    private String thematicCode;
    private Date openingDay;

    public Transcript_Combobox() {
    }

    public String getThematicCode() {
        return thematicCode;
    }

    public void setThematicCode(String thematicCode) {
        this.thematicCode = thematicCode;
    }

    public Date getOpeningDay() {
        return openingDay;
    }

    public void setOpeningDay(Date openingDay) {
        this.openingDay = openingDay;
    }

    // Override toString() để hiển thị đúng định dạng trong JComboBox
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return thematicCode + " (" + dateFormat.format(openingDay) + ")";
    }
}
