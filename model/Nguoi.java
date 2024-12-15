package QLDC_DoAn.model;

public class Nguoi {
    private String ten;
    private int tuoi;
    private String ngheNghiep;
    private String soCMND;
    private String soNha;

        public Nguoi(String ten, int tuoi, String ngheNghiep, String soCMND, String soNha) {
            this.ten = ten;
            this.tuoi = tuoi;
            this.ngheNghiep = ngheNghiep;
            this.soCMND = soCMND;
            this.soNha = soNha;
        }

        public String getTen() {
            return ten;
        }

        public void setTen(String ten) {
            this.ten = ten;
        }

        public int getTuoi() {
            return tuoi;
        }

        public void setTuoi(int tuoi) {
            this.tuoi = tuoi;
        }

        public String getNgheNghiep() {
            return ngheNghiep;
        }

        public void setNgheNghiep(String ngheNghiep) {
            this.ngheNghiep = ngheNghiep;
        }

        public String getSoCMND() {
            return soCMND;
        }

        public void setSoCMND(String soCMND) {
            this.soCMND = soCMND;
        }

        public String getSoNha() {
            return soNha;
        }

        public void setSoNha(String soNha) {
            this.soNha = soNha;
        }

        @Override
        public String toString() {
            return "Nguoi: " +
                    ten + " |\t " +
                    tuoi + " |\t " +
                    ngheNghiep + " |\t " +
                    soCMND + " |\t " +
                    soNha;
        }
}

