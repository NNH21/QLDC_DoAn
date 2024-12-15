package QLDC_DoAn.model;

import java.util.List;

public class KhuPho {
    private List<HoGiaDinh> danhSachHoGD;

    public KhuPho(List<HoGiaDinh> danhSachHoGD) {
        this.danhSachHoGD = danhSachHoGD;
    }

    public List<HoGiaDinh> getDanhSachHoGD() {
        return danhSachHoGD;
    }

    public void setDanhSachHoGD(List<HoGiaDinh> danhSachHoGD) {
        this.danhSachHoGD = danhSachHoGD;
    }

    @Override
    public String toString() {
        return "KhuPho: " +
                danhSachHoGD;
    }

}