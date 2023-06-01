package tugas.uas;

public class DataBarang {
    String kode, nama;
    int harga, total, bayar, kembalian;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getBayar() {
        return bayar;
    }

    public void setBayar(int bayar) {
        this.bayar = bayar;
    }

    public int getKembalian() {
        kembalian = bayar - total;
        return kembalian;
    }

    public void setKembalian(int kembalian) {
        this.kembalian = kembalian;
    }
    
    
    // Meliputi seleksi kondisi seluruh data komponen komputer
    public void nameSelection(){
        if(nama=="Monitor"){
            monitorSelection();
        } else if(nama=="Keyboard"){
            keyboardSelection();
        } else if(nama=="Motherboard"){
            motherboardSelection();
        } else if(nama=="Casing"){
            casingSelection();
        }
    }
    
    public void monitorSelection(){
        switch(kode){
            case "SMG-27":
                nama = "Monitor SAMSUNG 27 inch";
                harga = 2_025_000;
                break;
            case "AOC-24":
                nama = "Monitor AOC 24 inch";
                harga = 2_095_000;
                break;
            case "ACR-19":
                nama = "Monitor ACER 19 inch";
                harga = 1_080_000;
                break;
            case "ASUS-25":
                nama = "Monitor ASUS 25 inch";
                harga = 4_300_000;
                break;
            case "LG-20":
                nama = "Monitor LG 25 inch";
                harga = 1_150_000;
        }
    }
    
    public void keyboardSelection(){
    
    }
    
    public void motherboardSelection(){
    
    }
    
    public void casingSelection(){
    
    }
    
    public void mouseSelection(){
    
    }
    
    public void psuSelection(){
    
    }
    
    public void cpuSelection(){
    
    }
    
    public void coolerSelection(){
    
    }
    
    public void ramSelection(){
    
    }
    
    public void hddSelection(){
        
        
    }
    
    public void ssdSelection(){
    
    }
    
    public void vgaSelection(){
    
    
    }
    
    
}
