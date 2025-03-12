package Controller;
import Model.Model_Students_Management;
import View.Thematic_Study;
import Repository.JDBC_Thematic_Study;
import Model.Model_Thematic_Study;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.net.URL;
import java.io.IOException;

public class Controller_Thematic_Study {
    private Thematic_Study view;
    private JDBC_Thematic_Study jdbc;
    private List<Model_Thematic_Study> thematicList;
    private String imagePath;


    public Controller_Thematic_Study(Thematic_Study view){
        this.view = view;
        jdbc = new JDBC_Thematic_Study();
        thematicList = new ArrayList<>();

        loadData();
        initControl();
    }

    private void initControl(){
        view.getAddButton().addActionListener(e -> setAdd());
        view.getNewButton().addActionListener(e -> setNew());
        updateImage();

        view.getTable1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Kiểm tra nếu số lần click là 2 (double-click)
                    tableRowDoubleClick();
                }
            }
        });
    }

    private void loadData(){
        thematicList = jdbc.getAllChuyenDe();
        view.getTableModel().setRowCount(0);
        for(Model_Thematic_Study thematic : thematicList){
            view.getTableModel().addRow(new Object[]{
                    thematic.getCode(),
                    thematic.getName(),
                    thematic.getTuitionFee(),
                    thematic.getTime(),
                    thematic.getPicture(),
                    thematic.getDescribe()
            });
        }
    }

    private void setNew(){
        clearText();
    }

    private void clearText(){
        view.getCodeField().setText("");
        view.getNameField().setText("");
        view.getTuitionFeeField().setText("");
        view.getTimeField().setText("");
        view.getDiscriptionArea().setText("");
        view.getPictureLbl().setIcon(null);
        view.getPictureLbl().setText("Choose Image:");
    }

    private void setAdd(){

        if(!setCheckErrors()){
            return;
        }

        String code = view.getCodeField().getText().trim();
        String name = view.getNameField().getText().trim();
        float tuitionFee = Float.parseFloat(view.getTuitionFeeField().getText().trim());
        int time = Integer.parseInt(view.getTimeField().getText().trim());
        String describe = view.getDiscriptionArea().getText().trim();

        Model_Thematic_Study thematic = new Model_Thematic_Study();
        thematic.setCode(code);
        thematic.setName(name);
        thematic.setTuitionFee(tuitionFee);
        thematic.setTime(time);
        thematic.setPicture(imagePath);
        thematic.setDescribe(describe);

        thematicList.add(thematic);
        jdbc.mergeThematicStudy(thematicList);
        JOptionPane.showMessageDialog(view.getFrame(),"Them du lieu thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);

        loadData();
        clearText();
    }

    public void updateImage() {
        view.getPictureLbl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String urlPath = JOptionPane.showInputDialog(null, "Nhập URL của hình ảnh:", "Nhập URL", JOptionPane.PLAIN_MESSAGE);

                if (urlPath != null && !urlPath.trim().isEmpty()) {
                    imagePath = urlPath; // Lưu đường dẫn URL

                    try {
                        // Tải ảnh từ URL
                        URL url = new URL(imagePath);
                        Image image = ImageIO.read(url);

                        // Thay đổi kích thước ảnh để phù hợp với JLabel
                        Image scaledImg = image.getScaledInstance(
                                view.getPictureLbl().getWidth(),
                                view.getPictureLbl().getHeight(),
                                Image.SCALE_SMOOTH
                        );

                        // Hiển thị ảnh trên JLabel
                        view.getPictureLbl().setIcon(new ImageIcon(scaledImg));
                        view.getPictureLbl().setText("");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi tải hình ảnh từ URL!", "Lỗi!", JOptionPane.ERROR_MESSAGE);
                        view.getPictureLbl().setIcon(null);
                        view.getPictureLbl().setText("Choose Image:");
                    }
                }
            }
        });
    }

    private boolean setCheckErrors() {
        String code = view.getCodeField().getText().trim();
        String name = view.getNameField().getText().trim();
        String fee = view.getTuitionFeeField().getText().trim();
        String time1 = view.getTimeField().getText().trim();
        String description = view.getDiscriptionArea().getText().trim();
        ImageIcon picture = (ImageIcon) view.getPictureLbl().getIcon();

        // Kiểm tra xem các trường có bị bỏ trống không
        if (code.isEmpty() || name.isEmpty() || fee.isEmpty() || time1.isEmpty() || description.isEmpty() || picture == null) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra độ dài mã chuyên đề và tên chuyên đề
        if (code.length() > 5) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Độ dài tối đa của mã chuyên đề là 5", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (name.length() > 50) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Độ dài tối đa của tên chuyên đề là 50", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra định dạng của fee (tuitionFee) và time1 (time)
        float tuitionFee;
        int time;

        try {
            tuitionFee = Float.parseFloat(fee);
            if (tuitionFee <= 0) {
                JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Học phí phải là số dương!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Học phí phải là số thực hợp lệ!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            time = Integer.parseInt(time1);
            if (time <= 0) {
                JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Thời gian học phải là số nguyên dương!", "Thông báo", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Lỗi! Thời gian học phải là số nguyên hợp lệ!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void tableRowDoubleClick() {
        int selectedRow = view.getTable1().getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        // Lấy chỉ số hàng trong model (tránh lỗi khi có sorter)
        int modelRow = view.getTable1().convertRowIndexToModel(selectedRow);

        // Lấy dữ liệu từ bảng
        String code = view.getTableModel().getValueAt(modelRow, 0).toString();
        String name = view.getTableModel().getValueAt(modelRow, 1).toString();
        String tuitionFeeText = view.getTableModel().getValueAt(modelRow, 2).toString();
        String timeText = view.getTableModel().getValueAt(modelRow, 3).toString();
        String picturePath = view.getTableModel().getValueAt(modelRow, 4) != null ? view.getTableModel().getValueAt(modelRow, 4).toString() : "";
        String description = view.getTableModel().getValueAt(modelRow, 5).toString();

        // Hiển thị dữ liệu vào các trường nhập liệu
        view.getCodeField().setText(code);
        view.getNameField().setText(name);
        view.getTuitionFeeField().setText(tuitionFeeText);
        view.getTimeField().setText(timeText);
        view.getDiscriptionArea().setText(description);

        // Xử lý hình ảnh nếu có
        if (!picturePath.isEmpty()) {
            try {
                URL url = new URL(picturePath);
                Image image = ImageIO.read(url);

                // Thay đổi kích thước ảnh để phù hợp với JLabel
                Image scaledImg = image.getScaledInstance(
                        view.getPictureLbl().getWidth(),
                        view.getPictureLbl().getHeight(),
                        Image.SCALE_SMOOTH
                );

                // Hiển thị ảnh trên JLabel
                view.getPictureLbl().setIcon(new ImageIcon(scaledImg));
                view.getPictureLbl().setText(""); // Xóa văn bản nếu có ảnh
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view.getFrame(), "Lỗi tải hình ảnh từ URL!", "Lỗi!", JOptionPane.ERROR_MESSAGE);
                view.getPictureLbl().setIcon(null);
                view.getPictureLbl().setText("Choose Image:");
            }
        } else {
            view.getPictureLbl().setIcon(null);
            view.getPictureLbl().setText("Choose Image:");
        }

        // Ngăn placeholder ghi đè lên dữ liệu thực tế
        if (!code.isEmpty()) {
            view.getCodeField().setForeground(Color.BLACK);
            view.getCodeField().setFont(view.getCodeField().getFont().deriveFont(Font.PLAIN));
        }

        if(!code.isEmpty()){
            view.getNameField().setForeground(Color.BLACK);
            view.getNameField().setFont(view.getNameField().getFont().deriveFont(Font.PLAIN));
        }

        if (!tuitionFeeText.isEmpty()) {
            view.getTuitionFeeField().setForeground(Color.BLACK);
            view.getTuitionFeeField().setFont(view.getTuitionFeeField().getFont().deriveFont(Font.PLAIN));
        }

        if (!timeText.isEmpty()) {
            view.getTimeField().setForeground(Color.BLACK);
            view.getTimeField().setFont(view.getTimeField().getFont().deriveFont(Font.PLAIN));
        }

        // Chuyển đến tab nhập liệu
        view.getTabbedPane1().setSelectedIndex(0);
    }


    public void addCodePlaceHolder(){
        JTextField code = view.getCodeField();
        String placeHolder = "Do dai toi da la 5 ky tu...";

        if(code.getText().trim().isEmpty() || code.getText().equals(placeHolder)){
            code.setText(placeHolder);
            code.setForeground(Color.GRAY);
            code.setFont(code.getFont().deriveFont(Font.ITALIC));
        }

        code.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(code.getText().equals(placeHolder)){
                    code.setText("");
                    code.setForeground(Color.BLACK);
                    code.setFont(code.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(code.getText().isEmpty()){
                    code.setText(placeHolder);
                    code.setForeground(Color.GRAY);
                    code.setFont(code.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }

    public void addNamePlaceHolder(){
        JTextField name = view.getNameField();
        String placeHolder = "Do dai toi da la: 50 ky tu...";

        if(name.getText().trim().isEmpty() || name.getText().equals(placeHolder)){
            name.setText(placeHolder);
            name.setForeground(Color.GRAY);
            name.setFont(name.getFont().deriveFont(Font.ITALIC));
        }

        name.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(name.getText().equals(placeHolder)){
                    name.setText("");
                    name.setForeground(Color.BLACK);
                    name.setFont(name.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (name.getText().isEmpty()){
                    name.setText(placeHolder);
                    name.setForeground(Color.GRAY);
                    name.setFont(name.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }

    public void addTimePlaceHolder(){
        JTextField time = view.getTimeField();
        String placeHolder = "Chi nhap ky tu la so...";

        if(time.getText().trim().isEmpty() || time.getText().equals(placeHolder)){
            time.setText(placeHolder);
            time.setForeground(Color.GRAY);
            time.setFont(time.getFont().deriveFont(Font.ITALIC));
        }

        time.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(time.getText().equals(placeHolder)){
                    time.setText("");
                    time.setForeground(Color.BLACK);
                    time.setFont(time.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(time.getText().isEmpty()){
                    time.setText(placeHolder);
                    time.setForeground(Color.GRAY);
                    time.setFont(time.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }

    public void addTuitionFeePlaceHolder(){
        JTextField tuitionFee = view.getTuitionFeeField();
        String placeHolder = "Chi nhap ky tu la so...";

        if(tuitionFee.getText().trim().isEmpty() || tuitionFee.getText().equals(placeHolder)){
            tuitionFee.setText(placeHolder);
            tuitionFee.setForeground(Color.GRAY);
            tuitionFee.setFont(tuitionFee.getFont().deriveFont(Font.ITALIC));
        }

        tuitionFee.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(tuitionFee.getText().equals(placeHolder)){
                    tuitionFee.setText("");
                    tuitionFee.setForeground(Color.BLACK);
                    tuitionFee.setFont(tuitionFee.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(tuitionFee.getText().isEmpty()){
                    tuitionFee.setText(placeHolder);
                    tuitionFee.setForeground(Color.GRAY);
                    tuitionFee.setFont(tuitionFee.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }
}
