package QLDC_DoAn.ui;

import QLDC_DoAn.data.DataManager;
import QLDC_DoAn.model.HoGiaDinh;
import QLDC_DoAn.model.Nguoi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable dataTable;

    public MainUI() {
        setTitle("Quản Lý Hộ Dân Cư");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.decode("#dcdcdc"));
        add(createTitleLabel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("QUẢN LÝ DÂN CƯ");
        // Màu nền và màu chữ
        titleLabel.setBackground(Color.BLUE);
        titleLabel.setForeground(Color.RED);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        return titleLabel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 30, 30));
        buttonPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);

        CustomButton addMultipleHouseholdButton = new CustomButton("Thêm N Hộ Dân");
        CustomButton addHouseholdButton = new CustomButton("Thêm Hộ Gia Đình");
        CustomButton searchButton = new CustomButton("Tìm Theo CMND");
        CustomButton saveButton = new CustomButton("Lưu Dữ Liệu");
        CustomButton loadButton = new CustomButton("Đọc Dữ Liệu");
        CustomButton showAllButton = new CustomButton("Hiển Thị Thông tin");
        CustomButton exitButton = createExitButton();

        addMultipleHouseholdButton.addActionListener(new AddMultipleHouseholdsAction());
        addHouseholdButton.addActionListener(new AddHouseholdAction());
        searchButton.addActionListener(new SearchAction());
        saveButton.addActionListener(e -> saveDataToFile());
        loadButton.addActionListener(e -> loadDataFromFile());
        showAllButton.addActionListener(e -> displayAllData());

        buttonPanel.add(addMultipleHouseholdButton);
        buttonPanel.add(addHouseholdButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(showAllButton);
        buttonPanel.add(exitButton);

        tableModel = new DefaultTableModel(new String[]{"Số Nhà", "Tên", "Tuổi", "Nghề Nghiệp", "CMND"}, 0);
        dataTable = new JTable(tableModel);
        dataTable.setBackground(Color.decode("#dcdcdc"));
        dataTable.setForeground(Color.BLACK);
        dataTable.getTableHeader().setBackground(Color.GRAY);
        dataTable.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(dataTable);

        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }


    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JLabel footerLabel = new JLabel("Quản Lý Hộ Dân Cư - Đồ Án Java ");
        footerPanel.add(footerLabel);
        return footerPanel;
    }

    private class AddMultipleHouseholdsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = JOptionPane.showInputDialog(MainUI.this,
                    "Nhập số lượng hộ dân:", "Thêm n Hộ Dân", JOptionPane.QUESTION_MESSAGE);
            if (input == null || input.trim().isEmpty()) {
                return;
            }

            int n;
            try {
                n = Integer.parseInt(input.trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainUI.this, "Giá trị không hợp lệ! Nhập một số nguyên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<HoGiaDinh> hoGiaDinhs = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                JTextField soNhaField = new JTextField(20);
                JTextField soThanhVienField = new JTextField(10);

                inputPanel.add(new JLabel("Nhập số nhà:"));
                inputPanel.add(soNhaField);
                inputPanel.add(new JLabel("Nhập số lượng thành viên:"));
                inputPanel.add(soThanhVienField);

                int result = JOptionPane.showConfirmDialog(MainUI.this, inputPanel, "Nhập thông tin hộ gia đình thứ " + (i + 1) + ":", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                if (result == JOptionPane.OK_OPTION) {
                    String soNha = soNhaField.getText().trim();
                    String soThanhVienText = soThanhVienField.getText().trim();

                    if (soNha.isEmpty() || soThanhVienText.isEmpty()) {
                        JOptionPane.showMessageDialog(MainUI.this, "Tất cả các trường thông tin phải được nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        i--;
                        continue;
                    }

                    if (DataManager.kiemTraSoNhaTonTai(soNha)) {
                        JOptionPane.showMessageDialog(MainUI.this, "CẢNH BÁO: Số nhà \"" + soNha + "\" đã tồn tại. Vui lòng nhập số nhà khác.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        i--;
                        continue;
                    }

                    int soThanhVien;
                    try {
                        soThanhVien = Integer.parseInt(soThanhVienText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainUI.this, "Giá trị không hợp lệ! Nhập một số nguyên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        i--;
                        continue;
                    }

                    HoGiaDinh hoGiaDinh = new HoGiaDinh(soNha, new ArrayList<>());

                    for (int j = 0; j < soThanhVien; j++) {
                        while (true) {
                            JPanel memberInputPanel = new JPanel(new GridLayout(4, 2));
                            JTextField tenField = new JTextField(20);
                            JTextField tuoiField = new JTextField(10);
                            JTextField ngheNghiepField = new JTextField(20);
                            JTextField cmndField = new JTextField(20);

                            memberInputPanel.add(new JLabel("Tên:"));
                            memberInputPanel.add(tenField);
                            memberInputPanel.add(new JLabel("Tuổi:"));
                            memberInputPanel.add(tuoiField);
                            memberInputPanel.add(new JLabel("Nghề nghiệp:"));
                            memberInputPanel.add(ngheNghiepField);
                            memberInputPanel.add(new JLabel("Số CMND:"));
                            memberInputPanel.add(cmndField);

                            int memberResult = JOptionPane.showConfirmDialog(MainUI.this, memberInputPanel, "Nhập thông tin thành viên thứ " + (j + 1) + ":", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (memberResult == JOptionPane.CANCEL_OPTION || memberResult == JOptionPane.CLOSED_OPTION) {
                                return;
                            }
                            if (memberResult == JOptionPane.OK_OPTION) {
                                String ten = tenField.getText().trim();
                                String tuoiText = tuoiField.getText().trim();
                                String ngheNghiep = ngheNghiepField.getText().trim();
                                String soCMND = cmndField.getText().trim();

                                if (ten.isEmpty() || tuoiText.isEmpty() || ngheNghiep.isEmpty() || soCMND.isEmpty()) {
                                    JOptionPane.showMessageDialog(MainUI.this, "Tất cả các trường thông tin phải được nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                } else if (DataManager.kiemTraCMNDTonTai(soCMND)) {
                                    JOptionPane.showMessageDialog(MainUI.this, "CẢNH BÁO: Số CMND \"" + soCMND + "\" đã tồn tại. Vui lòng nhập số CMND khác.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                                    tenField.setText(ten);
                                    tuoiField.setText(tuoiText);
                                    ngheNghiepField.setText(ngheNghiep);
                                    cmndField.setText("");
                                } else {
                                    int tuoi;
                                    try {
                                        tuoi = Integer.parseInt(tuoiText);
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(MainUI.this, "Tuổi phải là số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                        continue;
                                    }
                                    Nguoi nguoi = new Nguoi(ten, tuoi, ngheNghiep, soCMND, soNha);
                                    hoGiaDinh.getThanhVien().add(nguoi);
                                    break;
                                }
                            }
                        }
                    }

                    hoGiaDinhs.add(hoGiaDinh);
                    DataManager.themHoGiaDinh(hoGiaDinh);
                    for (Nguoi thanhVien : hoGiaDinh.getThanhVien()) {
                        DataManager.themThanhVien(soNha, thanhVien);
                    }
                }
            }

            for (HoGiaDinh hoGiaDinh : hoGiaDinhs) {
                for (Nguoi nguoi : hoGiaDinh.getThanhVien()) {
                    tableModel.addRow(new Object[]{
                            nguoi.getSoNha(),
                            nguoi.getTen(),
                            nguoi.getTuoi(),
                            nguoi.getNgheNghiep(),
                            nguoi.getSoCMND()
                    });
                }
            }
            JOptionPane.showMessageDialog(MainUI.this, "Đã thêm thành công " + n + " hộ dân.");
        }
    }

    private class AddHouseholdAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            while (true) {
                JPanel inputPanel = new JPanel(new GridLayout(2, 2));
                JTextField soNhaField = new JTextField(20);
                JTextField soThanhVienField = new JTextField(10);

                inputPanel.add(new JLabel("Nhập số nhà:"));
                inputPanel.add(soNhaField);
                inputPanel.add(new JLabel("Nhập số lượng thành viên:"));
                inputPanel.add(soThanhVienField);

                int result = JOptionPane.showConfirmDialog(MainUI.this, inputPanel, "Nhập thông tin hộ gia đình:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                if (result == JOptionPane.OK_OPTION) {
                    String soNha = soNhaField.getText().trim();
                    String soThanhVienText = soThanhVienField.getText().trim();

                    if (soNha.isEmpty() || soThanhVienText.isEmpty()) {
                        JOptionPane.showMessageDialog(MainUI.this, "Tất cả các trường thông tin phải được nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    if (DataManager.kiemTraSoNhaTonTai(soNha)) {
                        JOptionPane.showMessageDialog(MainUI.this, "CẢNH BÁO: Số nhà \"" + soNha + "\" đã tồn tại. Vui lòng nhập số nhà khác.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }

                    int soThanhVien;
                    try {
                        soThanhVien = Integer.parseInt(soThanhVienText);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(MainUI.this, "Giá trị không hợp lệ! Nhập một số nguyên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    HoGiaDinh hoGiaDinh = new HoGiaDinh(soNha, new ArrayList<>());

                    for (int i = 0; i < soThanhVien; i++) {
                        while (true) {
                            JPanel memberInputPanel = new JPanel(new GridLayout(4, 2));
                            JTextField tenField = new JTextField(20);
                            JTextField tuoiField = new JTextField(10);
                            JTextField ngheNghiepField = new JTextField(20);
                            JTextField cmndField = new JTextField(20);

                            memberInputPanel.add(new JLabel("Tên:"));
                            memberInputPanel.add(tenField);
                            memberInputPanel.add(new JLabel("Tuổi:"));
                            memberInputPanel.add(tuoiField);
                            memberInputPanel.add(new JLabel("Nghề nghiệp:"));
                            memberInputPanel.add(ngheNghiepField);
                            memberInputPanel.add(new JLabel("Số CMND:"));
                            memberInputPanel.add(cmndField);

                            int memberResult = JOptionPane.showConfirmDialog(MainUI.this, memberInputPanel, "Nhập thông tin thành viên thứ " + (i + 1) + ":", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                            if (memberResult == JOptionPane.CANCEL_OPTION || memberResult == JOptionPane.CLOSED_OPTION) {
                                return;
                            }
                            if (memberResult == JOptionPane.OK_OPTION) {
                                String ten = tenField.getText().trim();
                                String tuoiText = tuoiField.getText().trim();
                                String ngheNghiep = ngheNghiepField.getText().trim();
                                String soCMND = cmndField.getText().trim();

                                if (ten.isEmpty() || tuoiText.isEmpty() || ngheNghiep.isEmpty() || soCMND.isEmpty()) {
                                    JOptionPane.showMessageDialog(MainUI.this, "Tất cả các trường thông tin phải được nhập.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                } else if (DataManager.kiemTraCMNDTonTai(soCMND)) {
                                    JOptionPane.showMessageDialog(MainUI.this, "CẢNH BÁO: Số CMND \"" + soCMND + "\" đã tồn tại. Vui lòng nhập số CMND khác.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                                    tenField.setText(ten);
                                    tuoiField.setText(tuoiText);
                                    ngheNghiepField.setText(ngheNghiep);
                                    cmndField.setText("");
                                } else {
                                    int tuoi;
                                    try {
                                        tuoi = Integer.parseInt(tuoiText);
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(MainUI.this, "Tuổi phải là số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                        continue;
                                    }
                                    Nguoi nguoi = new Nguoi(ten, tuoi, ngheNghiep, soCMND, soNha);
                                    hoGiaDinh.getThanhVien().add(nguoi);
                                    break;
                                }
                            }
                        }
                    }

                    try {
                        DataManager.themHoGiaDinh(hoGiaDinh);
                        for (Nguoi thanhVien : hoGiaDinh.getThanhVien()) {
                            DataManager.themThanhVien(soNha, thanhVien);
                        }
                        JOptionPane.showMessageDialog(MainUI.this, "Hộ gia đình với số nhà \"" + hoGiaDinh.getSoNha() + "\" đã được thêm thành công!");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MainUI.this, "Thêm hộ gia đình thất bại do lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        }
    }

    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchCMND = JOptionPane.showInputDialog(MainUI.this,
                    "Nhập Số CMND để tìm kiếm:", "Tìm Theo CMND", JOptionPane.QUESTION_MESSAGE);

            if (searchCMND != null) {
                boolean found = false;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (tableModel.getValueAt(i, 4).equals(searchCMND)) {
                        found = true;
                        JOptionPane.showMessageDialog(MainUI.this,
                                "Thông Tin Cá Nhân:\n" +
                                        "Số Nhà: " + tableModel.getValueAt(i, 0) + "\n" +
                                        "Tên: " + tableModel.getValueAt(i, 1) + "\n" +
                                        "Tuổi: " + tableModel.getValueAt(i, 2) + "\n" +
                                        "Nghề Nghiệp: " + tableModel.getValueAt(i, 3) + "\n" +
                                        "CMND: " + tableModel.getValueAt(i, 4),
                                "Kết Quả Tìm Kiếm", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showMessageDialog(MainUI.this,
                            "Không tìm thấy cá nhân với số CMND: " + searchCMND, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void saveDataToFile() {
        DataManager.luuThongTinRaFile();
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được lưu thành công!", "Lưu Dữ Liệu", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadDataFromFile() {
        List<HoGiaDinh> hoGiaDinhs = DataManager.docThongTinTuFile();
        tableModel.setRowCount(0);
        for (HoGiaDinh hoGiaDinh : hoGiaDinhs) {
            for (Nguoi nguoi : hoGiaDinh.getThanhVien()) {
                tableModel.addRow(new Object[]{
                        nguoi.getSoNha(),
                        nguoi.getTen(),
                        nguoi.getTuoi(),
                        nguoi.getNgheNghiep(),
                        nguoi.getSoCMND()
                });
            }
        }
        JOptionPane.showMessageDialog(this, "Dữ liệu đã được tải thành công!", "Đọc Dữ Liệu", JOptionPane.INFORMATION_MESSAGE);
    }

    private void displayAllData() {
        List<HoGiaDinh> hoGiaDinhs = DataManager.layTatCaHoGiaDinh();
        tableModel.setRowCount(0);
        for (HoGiaDinh hoGiaDinh : hoGiaDinhs) {
            for (Nguoi nguoi : hoGiaDinh.getThanhVien()) {
                tableModel.addRow(new Object[]{
                        nguoi.getSoNha(),
                        nguoi.getTen(),
                        nguoi.getTuoi(),
                        nguoi.getNgheNghiep(),
                        nguoi.getSoCMND()
                });
            }
        }
        JOptionPane.showMessageDialog(this, "Hiển thị tất cả thông tin hộ dân!", "Hiển Thị Tất Cả", JOptionPane.INFORMATION_MESSAGE);
    }

    private CustomButton createExitButton() {
        CustomButton exitButton = new CustomButton("Thoát");
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(MainUI.this,
                    "Bạn có chắc chắn muốn thoát?", "Xác nhận thoát",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        return exitButton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI app = new MainUI();
            app.setVisible(true);
        });
    }
}