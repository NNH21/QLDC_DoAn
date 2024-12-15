package QLDC_DoAn.cmd;
import QLDC_DoAn.model.HoGiaDinh;
import QLDC_DoAn.model.Nguoi;
import QLDC_DoAn.data.DataManager;

import java.util.*;

public class MainCMD {
    private Scanner scanner;
    public MainCMD() {
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("\n   ============= QUẢN LÝ CÁC HỘ DÂN CƯ ==============");
            System.out.println("1. Nhập n hộ dân, sau đó hiển thị ra màn hình. ");
            System.out.println("2. Thêm hộ gia đình, thêm thông tin các thành viên.");
            System.out.println("3. Tìm cá nhân theo số CMND.");
            System.out.println("4. Lưu thông tin từ file.");
            System.out.println("5. Đọc thông tin từ file.");
            System.out.println("6. Hiển thị thông tin.");
            System.out.println("7. Thoát chương trình. ");
            System.out.print("Chọn một tùy chọn: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    themHoDan();
                    break;
                case 2:
                    themHoGiaDinh();
                    break;
                case 3:
                    timCaNhanTheoCMND();
                    break;
                case 4:
                    luuThongTinRaFile();
                    break;
                case 5:
                    docThongTinTuFile();
                    break;
                case 6:
                    hienThiThongTinHoDan1();
                case 7:
                    System.out.println("Chương trình kết thúc.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
            }
        }
    }

    private void themHoDan() {
    System.out.print("Nhập số lượng hộ dân: ");
    int n = Integer.parseInt(scanner.nextLine());
    List<HoGiaDinh> hoGiaDinhs = new ArrayList<>();

    for (int i = 0; i < n; i++) {
        System.out.println("Nhập thông tin hộ gia đình thứ " + (i + 1) + ":");

        String soNha;
        while (true) {
            System.out.print("Nhập số nhà: ");
            soNha = scanner.nextLine();
            if (DataManager.kiemTraSoNhaTonTai(soNha)) {
                System.out.println("CẢNH BÁO: Số nhà \"" + soNha + "\" đã tồn tại. Vui lòng nhập số nhà khác.");
            } else {
                break;
            }
        }

        HoGiaDinh hoGiaDinh = new HoGiaDinh(soNha, new ArrayList<>());
        System.out.print("Nhập số lượng thành viên: ");
        int soThanhVien = Integer.parseInt(scanner.nextLine());
        for (int j = 0; j < soThanhVien; j++) {
            System.out.println("Nhập thông tin thành viên thứ " + (j + 1) + ":");
            System.out.print("Tên: ");
            String ten = scanner.nextLine();
            System.out.print("Tuổi: ");
            int tuoi = Integer.parseInt(scanner.nextLine());
            System.out.print("Nghề nghiệp: ");
            String ngheNghiep = scanner.nextLine();
            System.out.print("Số CMND: ");
            String soCMND = scanner.nextLine();
            soNha = hoGiaDinh.getSoNha();
            Nguoi nguoi = new Nguoi(ten, tuoi, ngheNghiep, soCMND, soNha);
            hoGiaDinh.getThanhVien().add(nguoi);
        }

        hoGiaDinhs.add(hoGiaDinh);
        DataManager.themHoGiaDinh(hoGiaDinh);
        for (Nguoi thanhVien : hoGiaDinh.getThanhVien()) {
            DataManager.themThanhVien(soNha, thanhVien);
        }
        System.out.println("Hộ gia đình được thêm thành công: " + hoGiaDinh);
    }
}

    private void themHoGiaDinh() {
        String soNha;
        while (true) {
            System.out.print("Nhập số nhà: ");
            soNha = scanner.nextLine();
            if (DataManager.kiemTraSoNhaTonTai(soNha)) {
                System.out.println("CẢNH BÁO: Số nhà " + soNha + " đã tồn tại. Vui lòng nhập số nhà khác.");
            } else {
                break; 
            }
        }

        HoGiaDinh hoGiaDinh = new HoGiaDinh(soNha, new ArrayList<>());
        System.out.print("Nhập số lượng thành viên: ");
        int soThanhVien = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < soThanhVien; i++) {
            System.out.println("Nhập thông tin thành viên thứ " + (i + 1) + ":");
            System.out.print("Tên: ");
            String ten = scanner.nextLine();
            System.out.print("Tuổi: ");
            int tuoi = Integer.parseInt(scanner.nextLine());
            System.out.print("Nghề nghiệp: ");
            String ngheNghiep = scanner.nextLine();
            System.out.print("Số CMND: ");
            String soCMND = scanner.nextLine();
            soNha = hoGiaDinh.getSoNha();

            Nguoi nguoi = new Nguoi(ten, tuoi, ngheNghiep, soCMND, soNha);
            hoGiaDinh.getThanhVien().add(nguoi);
        }

        try {
            DataManager.themHoGiaDinh(hoGiaDinh);
            for (Nguoi thanhVien : hoGiaDinh.getThanhVien()) {
                DataManager.themThanhVien(soNha, thanhVien);
            }
            System.out.println("Hộ gia đình với số nhà \"" + hoGiaDinh.getSoNha() + "\" đã được thêm thành công!");
        } catch (Exception e) {
            System.out.println("Thêm hộ gia đình thất bại do lỗi: " + e.getMessage());
        }
    }

    private void timCaNhanTheoCMND() {
        System.out.print("Nhập số CMND: ");
        String cmnd = scanner.nextLine();
        Nguoi nguoi = DataManager.timCaiNhanTheoCMND(cmnd);
        if (nguoi != null) {
            System.out.println("Thông tin cá nhân với số CMND \"" + cmnd + "\":");
            System.out.println(nguoi);
        } else {
            System.out.println("Không tìm thấy cá nhân với số CMND này.");
        }
    }

    private void luuThongTinRaFile() {
        DataManager.luuThongTinRaFile();
    }

    private void docThongTinTuFile() {
    List<HoGiaDinh> hoGiaDinhs = DataManager.docThongTinTuFile();
    for (HoGiaDinh hoGiaDinh : hoGiaDinhs) {
        System.out.println(hoGiaDinh);
    }
}

private void hienThiThongTinHoDan1() {
    List<HoGiaDinh> hoGiaDinhs = DataManager.layTatCaHoGiaDinh();
    for (HoGiaDinh hoGiaDinh : hoGiaDinhs) {
        if (!hoGiaDinh.getThanhVien().isEmpty()) {
            System.out.println(hoGiaDinh);
        }
    }
}
    public static void main(String[] args) {
        new MainCMD().run();
    }
}