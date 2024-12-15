package QLDC_DoAn.data;

import QLDC_DoAn.model.HoGiaDinh;
import QLDC_DoAn.model.Nguoi;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public static void themHoGiaDinh(HoGiaDinh hoGiaDinh) {
        if (hoGiaDinh == null || hoGiaDinh.getSoNha() == null || hoGiaDinh.getSoNha().trim().isEmpty()) {
            throw new IllegalArgumentException("Số nhà không được để trống.");
        }

        try (Connection connection = DataConnect.connectToDatabase()) {
            if (kiemTraSoNhaTonTai(hoGiaDinh.getSoNha())) {
                System.err.println("Số nhà đã tồn tại: " + hoGiaDinh.getSoNha() + ". Không thể thêm trùng lặp.");
            }
            String query = "INSERT INTO HoGiaDinh (soNha) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hoGiaDinh.getSoNha());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void themThanhVien(String soNha, Nguoi thanhVien) {
        if (soNha == null || soNha.trim().isEmpty()) {
            throw new IllegalArgumentException("Số nhà không được để trống.");
        }

        try (Connection connection = DataConnect.connectToDatabase()) {
            if (!kiemTraSoNhaTonTai(soNha)) {
                System.err.println("Số nhà không tồn tại: " + soNha + ". Không thể thêm thành viên.");
            }
            String query = "INSERT INTO Nguoi (hoTen, tuoi, ngheNghiep, cmnd, soNha) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, thanhVien.getTen());
            preparedStatement.setInt(2, thanhVien.getTuoi());
            preparedStatement.setString(3, thanhVien.getNgheNghiep());
            preparedStatement.setString(4, thanhVien.getSoCMND());
            preparedStatement.setString(5, soNha);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Nguoi timCaiNhanTheoCMND(String cmnd) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            conn = DataConnect.connectToDatabase();
            String query = "SELECT * FROM Nguoi WHERE cmnd = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, cmnd);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String ten = resultSet.getString("hoTen");
                int tuoi = resultSet.getInt("tuoi");
                String ngheNghiep = resultSet.getString("ngheNghiep");
                String cmnd1 = resultSet.getString("cmnd");
                String soNha = resultSet.getString("soNha");
                return new Nguoi(ten, tuoi, ngheNghiep, cmnd1, soNha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void luuThongTinRaFile() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DataConnect.connectToDatabase();
            String query = "SELECT * FROM Nguoi";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/QuanLyDanCu2/QLDC_DoAn.assets/DS_HoGiaDinh.txt"));
            while (rs.next()) {
                String hoTen = rs.getString("hoTen");
                int tuoi = rs.getInt("tuoi");
                String ngheNghiep = rs.getString("ngheNghiep");
                String cmnd = rs.getString("cmnd");
                String soNha = rs.getString("soNha");

                writer.write("Thanh vien: "+hoTen + " | " + tuoi + " | " + ngheNghiep +" | "+cmnd +" | " + soNha);
                writer.newLine();
            }
            writer.close();
            System.out.println("Lưu thông tin thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public static List<HoGiaDinh> docThongTinTuFile() {
    List<HoGiaDinh> hoGiaDinhs = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader("src/QLDC_DoAn.assets/DS_HoGiaDinh.txt"))) {
        String line;
        HoGiaDinh hoGiaDinh = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Thanh vien: ")) {
                String[] parts = line.substring(12).split(" \\| ");
                String ten = parts[0];
                int tuoi = Integer.parseInt(parts[1]);
                String ngheNghiep = parts[2];
                String cmnd = parts[3];
                String soNha = parts[4];
                Nguoi thanhVien = new Nguoi(ten, tuoi, ngheNghiep, cmnd, soNha);
                if (hoGiaDinh == null || !hoGiaDinh.getSoNha().equals(soNha)) {
                    if (hoGiaDinh != null) {
                        hoGiaDinhs.add(hoGiaDinh);
                    }
                    hoGiaDinh = new HoGiaDinh(soNha, new ArrayList<>());
                }
                hoGiaDinh.getThanhVien().add(thanhVien);
            }
        }
        if (hoGiaDinh != null) {
            hoGiaDinhs.add(hoGiaDinh);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return hoGiaDinhs;
}

    public static List<HoGiaDinh> layTatCaHoGiaDinh() {
        List<HoGiaDinh> hoGiaDinhs = new ArrayList<>();
        try (Connection connection = DataConnect.connectToDatabase();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM HoGiaDinh")) {
            while (resultSet.next()) {
                String soNha = resultSet.getString("soNha");
                HoGiaDinh hoGiaDinh = new HoGiaDinh(soNha, new ArrayList<>());
                hoGiaDinh.setThanhVien(layThanhVienTheoSoNha(soNha));
                hoGiaDinhs.add(hoGiaDinh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hoGiaDinhs;
    }

    private static List<Nguoi> layThanhVienTheoSoNha(String soNha) {
        List<Nguoi> thanhVien = new ArrayList<>();
        try (Connection connection = DataConnect.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Nguoi WHERE soNha = ?")) {
            preparedStatement.setString(1, soNha);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String ten = resultSet.getString("hoTen");
                int tuoi = resultSet.getInt("tuoi");
                String ngheNghiep = resultSet.getString("ngheNghiep");
                String cmnd = resultSet.getString("cmnd");
                String soNha1 = resultSet.getString("soNha");
                thanhVien.add(new Nguoi(ten, tuoi, ngheNghiep, cmnd, soNha1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thanhVien;
    }

    public static boolean kiemTraSoNhaTonTai(String soNha) {
        try (Connection connection = DataConnect.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM HoGiaDinh WHERE soNha = ?")) {
            preparedStatement.setString(1, soNha);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean kiemTraCMNDTonTai(String cmnd) {
        try (Connection connection = DataConnect.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Nguoi WHERE cmnd = ?")) {
            preparedStatement.setString(1, cmnd);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
