package QLDC_DoAn.model;

import java.util.List;

public class HoGiaDinh {
    private String soNha;
    private List<Nguoi> thanhVien;


    public HoGiaDinh(String soNha, List<Nguoi> thanhVien) {
        this.soNha = soNha;
        this.thanhVien = thanhVien;
    }

    public String getSoNha() {
        return soNha;
    }

    public void setSoNha(String soNha) {
        this.soNha = soNha;
    }

    public List<Nguoi> getThanhVien() {
        return thanhVien;
    }

    public void setThanhVien(List<Nguoi> thanhVien) {
        this.thanhVien = thanhVien;
    }

    @Override
    public String toString() {
        return "HoGiaDinh: " +
                soNha + " |\t " +
                thanhVien;
    }

}
