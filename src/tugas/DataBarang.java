package tugas;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataBarang {
    String kode, nama, namaVarian, hargaString;
    double harga, total, bayar, kembalian;
    
    //Array untuk menyimpan data
    List<BarangDto> savedItems = new ArrayList<>();
    
    //List data barang
    String monitor[][] = {
            {"ARM-22", "Armaggeddon Pixxel 22 inch Super FHD 75Hz", "1700000"},
            {"AIRO-27", "AIRO AIO271G 27 inch IPS 165Hz", "2500000"},
            {"MSI-24", "MSI OPTIX G2412 24 inch FHD 170Hz", "2750000"},
            {"SNC-32", "Viewsonic VX3218 32 inch VA 165Hz", "3300000"},
            {"SMG-32", "SAMSUNG S32AG550 32 inch VA 165Hz", "4870000"}};
    String keyboard[][] = {
            {"RXUS", "Rexus Daiva RX-D68SF", "689000"},
            {"RZER", "Razer BlackWidow V3 Tenkeyless", "1100000"},
            {"FAN", "Fantech MAXFIT81", "1495000"},
            {"LGTC", "Logitech G PRO X SUPERLIGHT", "2100000"},
            {"APEX", "Apex Pro TKL 2023", "3000000"}};
            
    String motherboard [][] = {
            {"M-VAR", "VARRO REVOLVER B250", "640000"},
            {"M-ASUS", "ASUS PRIME A320M-K", "825000"},
            {"M-ASR", "ASROCK B450", "1555000"},
            {"M-MSI", "MSI Z590", "2330000"},
            {"M-TUF","ASUS TUF GAMING B760-Plus", "3840000"}};
    String psu[][] = {
            {"MSTR","Cooler Master MWE 650W" , "850000"},
            {"CRSR","Corsair CV650 650W" , "953000"},
            {"PRIM","PRIM PREMIUM 850" , "1450000"},
            {"ADTA","ADATA XPG 750 W" , "1750000"},
            {"HYDR","FSP Hydro G Pro 750W" , "1835000"}
            };
    String mouse [][] = {
            {"COOL" , "Cooler Master MM711" , "250000"},
            {"NOIR" , "NOIR M1 Modular" , "795000"},
            {"RXUS" , "REXUS Daxa Air IV" , "869000"},
            {"RZR" , "Mouse RAZER NAGA PRO" , "1500000"},
            {"LGT" , "Logitech G502 X Plus" , "2210000"}};
    String casing[][] = {
            {"BYRN","CUBE GAMING BYRON " , "365000"},
            {"GAL","GALAX REVOLUTION-05" , "500000"},
            {"PRM","PRIME V[L] GREEN" , "1315000"},
            {"XPG","ADATA XPG" , "2500000"},
            {"LIA","LIANLI ODYSSEY X" , "7500000"}};
    String cpu[][] = {
            {"I9-13","INTEL CORE i9 13900K GEN 13" , "9600000"},
            {"I7-12","INTEL CORE i7 12700 GEN 12" , "4700000"},
            {"I5-11","INTEL CORE 11400 GEN 11" , "2590000"},
            {"AMD-9","AMD RYZEN 9 5950X" , "8600000"},
            {"AMD-7","AMD RYXZEN 7 5700G" , "3270000"}};
    String cooler[][] = {
            {"JONS", "JONSBO HX6200D" , "550000"},
            {"MAG","MSI MAG CORELIQUID 360R" , "1660000"},
            {"DARK","darkFlash TWISTER DX240" , "900000"},
            {"NZXT", "NZXT KRAKEN X53" , "2020000"},
            {"DEEP", "Deepcool LS520 SE", "1170000"}};
    String ram[][] = {
            {"IMP-8" , "IMPERION MAC DDR3L 8GB" , "330000"},
            {"KING-8" , "Kingston Fury Beast DDR5 8GB" , "680000"},
            {"SKILL-16" , "Gskill Trident DDR4 16GB" , "1015000"},
            {"ELITE-16" , "Team Elite Plus DDR5 16Gb" , "1110000"},
            {"TEAM-32" , "Team T-Create Classic DDR4 32Gb" , "1190000"}};
    String ssd [][] = { 
            {"WD-250", "WD Black SN770 250GB", "730000"},
            {"ADT-512", "ADATA XPG SX8200 PRO 512 GB", "554000"},
            {"KXA-500", "SSD KIOXIA 500GB", "1250000"},
            {"ADT-1000", "XPG SX8200 PRO 1TB", "929000"},
            {"MDS-2000", "MIDAS FORCE 2TB", "1180000"}};
    String hdd [][] = { 
            {"WD-1000", "WD Caviar Blue 1TB", "630000"},
            {"ADT-2" , "Hard Disk ADATA 2TB", "1150000"},
            {"TSB" , "WD Red Plus 8TB", "3410000"},
            {"LAC" , "Toshiba S300 Pro Surveillance 10TB", "3580000"},
            {"SEA" , "SEAGATE SKYHAWK 10TB", "4375000"}};
    String vga [][] = {
            {"VRRN" , "AMD VURRION R7 350", "1092000"},
            {"RX7" , "RX7 RX 6600", "2175000"},
            {"RDN" , "AMD Radeon RX 6600 XT", "2997000"},
            {"GFRC" , "NVIDIA Geforce RTX 3070", "4800000"},
            {"FORC" , "Colorful Geforce RTX 3060", "6800000"}};

    //Fungsi ini untuk memasukan data yang diminta setelah proses seleksi data
    public void setData(int i, String[][] product){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        
        namaVarian = product[i][1];
        harga = Double.parseDouble(product[i][2]);
        hargaString = formatRupiah.format(harga);
    }
    
    public String getHargaString() {
        return hargaString;
    }

    public void setHargaString(String hargaString) {
        this.hargaString = hargaString;
    }
    
    public String getNamaVarian() {
        return namaVarian;
    }

    public void setNamaVarian(String namaVarian) {
        this.namaVarian = namaVarian;
    }
    

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

    public double getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getBayar() {
        return bayar;
    }

    public void setBayar(int bayar) {
        this.bayar = bayar;
    }

    public double getKembalian() {
        kembalian = bayar - total;
        return kembalian;
    }

    public void setKembalian(int kembalian) {
        this.kembalian = kembalian;
    }
    
    //Fungsi ini melakukan seleksi data berdasarkan nama dan kode lalu memasukan data pada variabel di fungsi setData
    public void dataSelection(){
        if(nama=="Monitor"){
            for(int i=0; i<monitor.length; i++){
                if(kode==monitor[i][0]){
                    setData(i, monitor);
                }
            }
        } else if(nama=="Keyboard"){
            for(int i=0; i<keyboard.length; i++){
                if(kode==keyboard[i][0]){
                    setData(i, keyboard); 
                }
            }
        } else if(nama=="Motherboard"){
            for(int i=0; i<motherboard.length; i++){
                if(kode==motherboard[i][0]){
                    setData(i, motherboard);
                }
            }
        } else if(nama=="Casing"){
            for(int i=0; i<casing.length; i++){
                if(kode==casing[i][0]){
                    setData(i, casing);
                }
            }
        } else if(nama=="Mouse"){
            for(int i=0; i<mouse.length; i++){
                if(kode==mouse[i][0]){
                    setData(i, mouse);   
                } 
            }
        } else if(nama=="PSU"){
            for(int i=0; i<psu.length; i++){
                if(kode==psu[i][0]){
                    setData(i, psu);   
                } 
            }
        } else if(nama=="CPU"){
            for(int i=0; i<cpu.length; i++){
                if(kode==cpu[i][0]){
                    setData(i, cpu);
                } 
            }
        } else if(nama=="Cooler"){
            for(int i=0; i<cooler.length; i++){
                if(kode==cooler[i][0]){
                    setData(i, cooler);
                }
            }
        } else if(nama=="RAM"){
            for(int i=0; i<ram.length; i++){
                if(kode==ram[i][0]){
                    setData(i, ram);
                }
            }
        } else if(nama=="HDD"){
            for(int i=0; i<hdd.length; i++){
                if(kode==hdd[i][0]){
                    setData(i, hdd);
                }
            }
        } else if(nama=="SSD"){
            for(int i=0; i<ssd.length; i++){
                if(kode==ssd[i][0]){
                    setData(i, ssd);
                }
            }
        } else{
            for(int i=0; i<vga.length; i++){
                if(kode==vga[i][0]){
                    setData(i, vga);
                }
            }
        }        
    }

    public static void main(String[] args) {        

    }
    
    public static List<BarangDto> monitors(BarangDto monitor) {
        List<BarangDto> monitors = new ArrayList<>();
        monitors.add(monitor);
        return monitors;
    }
    
    
}
