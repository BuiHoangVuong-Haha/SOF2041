package Controller;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import Model.Model_Students_Management;
import Repository.JDBC_Students_Management;
import View.LoginScreen;
import View.Learner;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class Controller_Learner {
    private Learner view;
    private LoginScreen view1;
    private Controller_LoginScreen view1Controller;
    private List<Model_Students_Management> stuList;
    private JDBC_Students_Management jdbc;
    private TableRowSorter<TableModel> sorter;

    public Controller_Learner(Learner view){
        this.view = view;
        jdbc = new JDBC_Students_Management();
        stuList = new ArrayList<>();
        sorter = new TableRowSorter<>(view.getTableModel());
        view.getTable1().setRowSorter(sorter);

        this.view1Controller = Controller_LoginScreen.getInstance(null);
        this.view1 = this.view1Controller.getView();

        loadData();
        initControl();
    }

    private void initControl(){
        view.getAddButton().addActionListener(e -> setAdd());
        view.getNewButton().addActionListener(e -> setNew());
        view.getSearchButton().addActionListener(e -> setSearch());

        view.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setSearch();
            }
        });

        view.getTabbedPane1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Bảng được click, cập nhật sorter...");
                sorter.setRowFilter(null); // Đảm bảo dữ liệu hiển thị khi click vào tabbbedPane
            }
        });

        view.getTable1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Kiểm tra nếu số lần click là 2 (double-click)
                    tableRowDoubleClick();
                }
            }
        });
    }


    public void loadData(){
        stuList = jdbc.getAllStudents();
        view.getTableModel().setRowCount(0);
        for(Model_Students_Management stu : stuList){
            view.getTableModel().addRow(new Object[]{
                    stu.getStuCode(),
                    stu.getName(),
                    stu.getDate(),
                    stu.getGender(),
                    stu.getPhoneNum(),
                    stu.getEmail(),
                    stu.getStaffCode(),
                    stu.getRegisterDay()
            });
        }

        // Đảm bảo hiển thị toàn bộ dữ liệu khi load bảng
        sorter.setRowFilter(null);
        }


    private void setSearch() {
        String searchText = view.getSearchField().getText().trim().toLowerCase();

        // Kiểm tra nếu nội dung của ô tìm kiếm là placeholder thì không lọc
        if (searchText.equals("Tim kiem theo Code...")) {
            sorter.setRowFilter(null); // Không lọc nếu ô tìm kiếm chỉ chứa placeholder
            return;
        }

        if (sorter == null) {
            sorter = new TableRowSorter<>(view.getTableModel());
            view.getTable1().setRowSorter(sorter);
        }

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null); // Hiển thị toàn bộ dữ liệu
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i).*" + searchText + ".*", 0));
        }
    }



    private void setNew(){
        clearText();
    }


    private void setAdd(){
        if(!setCheckErros()){
            return;
        }

        String code = view.getCodeField().getText().trim();
        String name = view.getNameField().getText().trim();
        String gender = view.getMaleRbt().isSelected() ? "Nam" : view.getFemaleRbt().isSelected() ? "Nu" : null;
        String dateText = view.getBirthField().getText().trim();
        String phoneNum = view.getPhoneField().getText().trim();
        String email = view.getEmailField().getText().trim();
        String description = view.getDescriptionArea().getText().trim();

        //Lay username cua form dang nhap
        String staffCode = Controller_LoginScreen.getInstance(view1).getLoggedInUsername();

        // Chuyển đổi dateText (String) -> Date
        Date date = null;
        Date registerDay = new Date(); // Lấy ngày hiện tại làm ngày đăng ký

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng ngày
        try {
            if (!dateText.isEmpty()) {
                date = dateFormat.parse(dateText);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Ngày sinh không hợp lệ! Vui lòng nhập theo định dạng dd/MM/yyyy", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return; // Dừng thực hiện nếu lỗi
        }

        Model_Students_Management stu = new Model_Students_Management();
        stu.setStuCode(code);
        stu.setName(name);
        stu.setDate(date);
        stu.setGender(gender);
        stu.setPhoneNum(phoneNum);
        stu.setEmail(email);
        stu.setDescription(description);
        stu.setStaffCode(staffCode);
        stu.setRegisterDay(registerDay);

        stuList.add(stu);

        jdbc.mergeStudents(stuList);
        JOptionPane.showMessageDialog(view.getFrame(), "Them du lieu thanh cong!", "Thong bao", JOptionPane.INFORMATION_MESSAGE);
        loadData();
        clearText();
    }

    private boolean setCheckErros(){
        String code = view.getCodeField().getText().trim();
        String name = view.getNameField().getText().trim();
        String dateText = view.getBirthField().getText().trim();
        String phoneNum = view.getPhoneField().getText().trim();
        String email = view.getEmailField().getText().trim();
        String description = view.getDescriptionArea().getText().trim();

        if(code.isEmpty() || name.isEmpty() || dateText.isEmpty() || phoneNum.isEmpty() || email.isEmpty() || description.isEmpty() || !view.getMaleRbt().isSelected() && !view.getFemaleRbt().isSelected()){
            JOptionPane.showMessageDialog(view.getFrame(), "Xin vui long nhap day du thong tin", "Thong bao", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //Kiem tra dinh dang cua Code
        if(!code.matches("^PS+\\d{5}$")){
            JOptionPane.showMessageDialog(view.getFrame(), "Code khong hop le, Code phai co dang: PS cong voi 5 chu so");
            return false;
        }

        //Kiem tra dinh dang phoneNumber co dung khong?
        if(!phoneNum.matches("^0+\\d{8,10}$")){
            JOptionPane.showMessageDialog(view.getFrame(), "So dien thoai cua ban khong hop le!", "Thong bao", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        //Kiem tra dinh dang Email có dung khong?
        String expectedEmail = code + "@fpt.edu.vn";
        if(!email.equalsIgnoreCase(expectedEmail)){
            JOptionPane.showMessageDialog(view.getFrame(), "Email khong hop le, Email phai co dang: " + expectedEmail, "Thong bao", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearText(){
        view.getCodeField().setText("");
        view.getNameField().setText("");
        view.getButtonGroup1().clearSelection();
        view.getBirthField().setText("");
        view.getPhoneField().setText("");
        view.getEmailField().setText("");
        view.getDescriptionArea().setText("");
    }


    private void tableRowDoubleClick() {
        int selectedRow = view.getTable1().getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        // Lấy chỉ số hàng theo model, tránh lỗi khi có sorter
        int modelRow = view.getTable1().convertRowIndexToModel(selectedRow);

        // Lấy dữ liệu từ bảng
        String stuCode = view.getTableModel().getValueAt(modelRow, 0).toString();
        String name = view.getTableModel().getValueAt(modelRow, 1).toString();
        String dateText = view.getTableModel().getValueAt(modelRow, 2).toString();
        String gender = view.getTableModel().getValueAt(modelRow, 3).toString();
        String phoneNum = view.getTableModel().getValueAt(modelRow, 4).toString();
        String email = view.getTableModel().getValueAt(modelRow, 5).toString();

        // Hiển thị dữ liệu vào các trường nhập liệu
        view.getCodeField().setText(stuCode);
        view.getNameField().setText(name);
        view.getPhoneField().setText(phoneNum);
        view.getEmailField().setText(email);

        // Xử lý hiển thị ngày sinh
        if (dateText != null && !dateText.trim().isEmpty()) {
            try {
                // Chuyển từ String (yyyy-MM-dd) sang java.sql.Date
                java.sql.Date sqlDate = java.sql.Date.valueOf(dateText);

                // Chuyển từ java.sql.Date sang java.util.Date
                Date birthDate = new Date(sqlDate.getTime());

                // Định dạng lại và hiển thị trong JTextField
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                view.getBirthField().setText(dateFormat.format(birthDate));
            } catch (Exception e) {
                view.getBirthField().setText(""); // Nếu lỗi, để trống
            }
        } else {
            view.getBirthField().setText("");
        }

        // Chọn RadioButton tương ứng
        if ("Nam".equalsIgnoreCase(gender)) {
            view.getMaleRbt().setSelected(true);
        } else if ("Nu".equalsIgnoreCase(gender)) {
            view.getFemaleRbt().setSelected(true);
        } else {
            view.getButtonGroup1().clearSelection();
        }

        // Tìm và hiển thị mô tả của học viên từ danh sách
        String description = "";
        for (Model_Students_Management stu : stuList) {
            if (stu.getStuCode().equals(stuCode)) {
                description = stu.getDescription();
                break;
            }
        }
        view.getDescriptionArea().setText(description);

        // Ngăn placeholder ghi đè lên dữ liệu thực tế
        if (!stuCode.isEmpty()) {
            view.getCodeField().setForeground(Color.BLACK);
            view.getCodeField().setFont(view.getCodeField().getFont().deriveFont(Font.PLAIN));
        }

        if(!dateText.isEmpty()) {
            view.getBirthField().setForeground(Color.BLACK);
            view.getBirthField().setFont(view.getBirthField().getFont().deriveFont(Font.PLAIN));
        }

        if (!phoneNum.isEmpty()) {
            view.getPhoneField().setForeground(Color.BLACK);
            view.getPhoneField().setFont(view.getPhoneField().getFont().deriveFont(Font.PLAIN));
        }

        if (!email.isEmpty()) {
            view.getEmailField().setForeground(Color.BLACK);
            view.getEmailField().setFont(view.getEmailField().getFont().deriveFont(Font.PLAIN));
        }

        // Chuyển đến tab nhập liệu
        view.getTabbedPane1().setSelectedIndex(0);
    }



    public void addCodePlaceHolder(){
        JTextField code = view.getCodeField();
        String placeHolder = "Dinh dang theo: PS + 5 chu so. Vi du: PS00001...";

        // Chỉ đặt placeholder nếu JTextField đang rỗng
        if (code.getText().trim().isEmpty() || code.getText().equals(placeHolder)) {
            code.setText(placeHolder);
            code.setForeground(Color.GRAY);
            code.setFont(code.getFont().deriveFont(Font.ITALIC));
        }

        code.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(code.getText().equals(placeHolder)){
                    code.setText("");
                    code.setForeground(Color.BLACK);
                    code.setFont(code.getFont().deriveFont(Font.PLAIN));
                }
            }
        });

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


    public void addDatePlaceHolder(){
        JTextField dateText = view.getBirthField();
        String placeHolder = "Dinh dang theo dd/MM/yyyy...";

        if(dateText.getText().trim().isEmpty() || dateText.getText().equals(placeHolder)){
            dateText.setText(placeHolder);
            dateText.setForeground(Color.GRAY);
            dateText.setFont(dateText.getFont().deriveFont(Font.ITALIC));
        }

        dateText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(dateText.getText().equals(placeHolder)){
                    dateText.setText("");
                    dateText.setForeground(Color.BLACK);
                    dateText.setFont(dateText.getFont().deriveFont(Font.PLAIN));
                }
            }
        });

        dateText.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(dateText.getText().equals(placeHolder)){
                    dateText.setText("");
                    dateText.setForeground(Color.BLACK);
                    dateText.setFont(dateText.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(dateText.getText().isEmpty()){
                    dateText.setText(placeHolder);
                    dateText.setForeground(Color.GRAY);
                    dateText.setFont(dateText.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }

    public void addPhoneNumPlaceHolder(){
        JTextField phoneNum = view.getPhoneField();
        String placeHolder = "Gom tu 8 den 10 so...";

        if(phoneNum.getText().trim().isEmpty() || phoneNum.getText().equals(placeHolder)){
            phoneNum.setText(placeHolder);
            phoneNum.setForeground(Color.GRAY);
            phoneNum.setFont(phoneNum.getFont().deriveFont(Font.ITALIC));
        }

        phoneNum.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(phoneNum.getText().equals(placeHolder)){
                    phoneNum.setText("");
                    phoneNum.setForeground(Color.BLACK);
                    phoneNum.setFont(phoneNum.getFont().deriveFont(Font.PLAIN));
                }
            }
        });

        phoneNum.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(phoneNum.getText().equals(placeHolder)){
                    phoneNum.setText("");
                    phoneNum.setForeground(Color.BLACK);
                    phoneNum.setFont(phoneNum.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(phoneNum.getText().isEmpty()){
                    phoneNum.setText(placeHolder);
                    phoneNum.setForeground(Color.GRAY);
                    phoneNum.setFont(phoneNum.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }

    public void addEmailPlaceHolder(){
        JTextField email = view.getEmailField();
        String placeHolder = "Dinh dang theo: Code + @fpt.edu.vn...";

        if(email.getText().trim().isEmpty() || email.getText().equals(placeHolder)){
            email.setText(placeHolder);
            email.setForeground(Color.GRAY);
            email.setFont(email.getFont().deriveFont(Font.ITALIC));
        }

        email.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(email.getText().equals(placeHolder)){
                    email.setText("");
                    email.setForeground(Color.BLACK);
                    email.setFont(email.getFont().deriveFont(Font.PLAIN));
                }
            }
        });

        email.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(email.getText().equals(placeHolder)){
                    email.setText("");
                    email.setForeground(Color.BLACK);
                    email.setFont(email.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(email.getText().isEmpty()){
                    email.setText(placeHolder);
                    email.setForeground(Color.GRAY);
                    email.setFont(email.getFont().deriveFont(Font.ITALIC));
                }
            }
        });
    }

    public void addSearchPlaceHolder(){
        JTextField search = view.getSearchField();
        String placeHolder = "Tim kiem theo Code...";

        if(search.getText().trim().isEmpty() || search.getText().equals(placeHolder)){
            search.setText(placeHolder);
            search.setForeground(Color.GRAY);
            search.setFont(search.getFont().deriveFont(Font.ITALIC));
        }

        search.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(search.getText().equals(placeHolder)){
                    search.setText("");
                    search.setForeground(Color.BLACK);
                    search.setFont(search.getFont().deriveFont(Font.PLAIN));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(search.getText().isEmpty()){
                    search.setText(placeHolder);
                    search.setForeground(Color.GRAY);
                    search.setFont(search.getFont().deriveFont(Font.ITALIC));
                    sorter.setRowFilter(null); // Đảm bảo hiển thị tất cả dữ liệu khi placeholder xuất hiện lại
                }
            }
        });
    }
}
