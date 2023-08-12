package com.hanindya.ag5s.Model;

public class HistoryRekapCashIn {
    String tanggal;
    int totalTransaksi,totalSetoran;

    public HistoryRekapCashIn() {
    }

    public HistoryRekapCashIn(String tanggal, int totalTransaksi, int totalSetoran) {
        this.tanggal = tanggal;
        this.totalTransaksi = totalTransaksi;
        this.totalSetoran = totalSetoran;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(int totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }

    public int getTotalSetoran() {
        return totalSetoran;
    }

    public void setTotalSetoran(int totalSetoran) {
        this.totalSetoran = totalSetoran;
    }
}
