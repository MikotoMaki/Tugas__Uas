package tugas;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;


public class main extends javax.swing.JFrame {
    LoadingScreen ls = new LoadingScreen();
    Dialog dialog = new Dialog();
    private Timer timer;
    private int progress;
    private boolean inProgress = false;
    
    List<BarangDto> savedData = new ArrayList<>();
    boolean cleanData=false;
    DataBarang data = new DataBarang();
    String[][] monitor = data.monitor;
    String[][] keyboard = data.keyboard;
    String[][] mouse = data.mouse;
    String[][] motherboard = data.motherboard;
    String[][] casing = data.casing;
    String[][] psu = data.psu;
    String[][] cpu = data.cpu;
    String[][] cooler = data.cooler;
    String[][] hdd = data.hdd;
    String[][] ssd = data.ssd;
    String[][] ram = data.ram;
    String[][] vga = data.vga;
    
    double monitorPrice, keyboardPrice, mousePrice, motherboardPrice, casingPrice,
            psuPrice, cpuPrice, coolerPrice, hddPrice, ssdPrice, ramPrice, vgaPrice;
    
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    LocalDateTime now = LocalDateTime.now();
    
    Font sunny;
    Font nature;
    Font mouldy;
    
    public main() {
        initComponents();
        
        try{
            sunny = Font.createFont(Font.TRUETYPE_FONT, new File("SunnyspellsRegular-MV9ze.ttf"));
            nature = Font.createFont(Font.TRUETYPE_FONT, new File("NatureBeautyPersonalUse-9Y2DK.ttf")).deriveFont(50f);
            mouldy = Font.createFont(Font.TRUETYPE_FONT, new File("MouldyCheeseRegular-WyMWG.ttf")).deriveFont(50f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("SunnyspellsRegular-MV9ze.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("NatureBeautyPersonalUse-9Y2DK.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("MouldyCheeseRegular-WyMWG.ttf")));
        }catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        
        topLabel.setFont(sunny.deriveFont(55f));
        welcomeText.setFont(sunny.deriveFont(75f));
        afterWelcomeText.setFont(sunny.deriveFont(35f));
        rakitLabel.setFont(sunny.deriveFont(50f));
        
        setVisiblePanel(true, false, false, false);
        uneditableTextField();
        dateTextField.setText(date.format(now).toString());
        
        tableDetailProduct.setFont(new Font("Tahoma", Font.PLAIN, 13));
        tableDetailProduct.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableDetailProduct.getTableHeader().setOpaque(false);
        tableDetailProduct.getTableHeader().setBackground(new Color(216,216,245));
        tableDetailProduct.getTableHeader().setForeground(new Color(255,255,255));
        
        timer = new Timer(80, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (progress == 100) {
                    timer.stop();
                    ls.setVisible(false);
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    setDialog();
                    savedData.clear();
                    fCancelButton.setText("Back");
                    
                    // Perform actions after progress completes
                } else {
                    progress++;
                    ls.jProgressBar.setValue(progress);
                }
            }
        
        });

    }
    
    public void setDialog(){
        String id = UUID.randomUUID().toString();
        dialog.transactionID.setText(id);
        dialog.transactionTime.setText(dateTime.format(now).toString());
        totalPayment(dialog.amount);
        
    }
    
    public void backToHome(){
        if(savedData.size()!=0){
            int response = JOptionPane.showConfirmDialog(null, "Data barang yang telah ditambahkan akan direset", "WARNING", JOptionPane.OK_CANCEL_OPTION);
            if(response==JOptionPane.OK_OPTION){
                savedData.clear();
                setVisiblePanel(true, false, false, false);
            }
        } else{
            setVisiblePanel(true, false, false, false);
        }
    }
    
    public void formatDateTime(){
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
    }
    
    public void startProgress() {
        progress = 0;
        ls.jProgressBar.setValue(progress);
        timer.start();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    public void setPrice(JSpinner product, JTextField price, double harga){
        int val = Integer.parseInt(product.getValue().toString());
        double total = val * harga;
        price.setText(formatRupiah.format(total));
    }
    
    public String convertPrice(String rupiahFormat){
        String replace1 = rupiahFormat.replace("Rp", "");
        String replace2 = replace1.replace(",00", "");
        String replace3 = replace2.replace(".", "");
        return replace3;
    }
    
    //Mengembalikan nilai pada spinner yang berubah
    public void setTextAfterChanged(JSpinner product, JComboBox varian, String name, JTextField price){
        String code = varian.getSelectedItem().toString();
        data.setNama(name);
        data.setKode(code);
        data.dataSelection();
        
        //Seleksi data untuk handle bug
        if(name=="Monitor"){
            monitorPrice = data.getHarga();
            setPrice(product, price, monitorPrice);
        } else if(name=="Keyboard"){
            keyboardPrice=data.getHarga();
            setPrice(product, price, keyboardPrice);
        } else if(name=="Mouse"){
            mousePrice = data.getHarga();
            setPrice(product, price, mousePrice);
        } else if(name=="Motherboard"){
            motherboardPrice = data.getHarga();
            setPrice(product, price, motherboardPrice);
        } else if(name=="Casing"){
            casingPrice = data.getHarga();
            setPrice(product, price, casingPrice);
        } else if(name=="RAM"){
            ramPrice = data.getHarga();
            setPrice(product, price, ramPrice);
        } else if(name=="SSD"){
            ssdPrice = data.getHarga();
            setPrice(product, price, ssdPrice);
        } else if(name=="HDD"){
            hddPrice = data.getHarga();
            setPrice(product, price, hddPrice);
        } else if(name=="Cooler"){
            coolerPrice = data.getHarga();
            setPrice(product, price, coolerPrice);
        } else if(name=="CPU"){
            cpuPrice = data.getHarga();
            setPrice(product, price, cpuPrice);
        } else if(name=="PSU"){
            psuPrice = data.getHarga();
            setPrice(product, price, psuPrice);
        } else{
            vgaPrice = data.getHarga();
            setPrice(product, price, vgaPrice);
        }  
    }
    
    //Menyimpan data barang yang telah ditambahkan
    public void saveData(String name, String code, String varian, String price){
        BarangDto item = new BarangDto(name, code, varian, price);
        savedData.add(item);
    }
    
    //Fungsi ini menambahkan data yang disimpan ke dalam tabel
    public void addDataTable(){
        DefaultTableModel model = (DefaultTableModel)tableDetailProduct.getModel();
        model.setRowCount(0);
        for(BarangDto item: savedData){
            String[] row = new String[4];
                row[0] = item.getName();
                row[1] = item.getCode();
                row[2] = item.getProduct();
                row[3] = item.getPrice();

            model.addRow(row);
        }
        tableDetailProduct.setRowHeight(25);
        tableDetailProduct.getColumnModel().getColumn(0).setPreferredWidth(65);
        tableDetailProduct.getColumnModel().getColumn(1).setPreferredWidth(65);
        tableDetailProduct.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableDetailProduct.getColumnModel().getColumn(3).setPreferredWidth(100);
    }
    
    public void totalPayment(JTextField textField){
        int total = 0;
        int price;
        for(BarangDto item:savedData){
            price = Integer.parseInt(convertPrice(item.getPrice()));
            total += price;
        }
        textField.setText(formatRupiah.format(total));
    }
    

    
    public void setData(JTextField name, JTextField price){
        name.setText(data.getNamaVarian());
        price.setText(data.getHargaString());
            
    }
    
    //Menampilkan data pada text field
    public void setTextField(JComboBox product, JTextField name, JTextField price){
        if(product.getSelectedItem().toString()=="PILIH"){
            clean(name, price);
        } else {
            data.setKode(product.getSelectedItem().toString());
            data.dataSelection();
            setData(name, price);        
        }
    }
    
    //Bug?
    public void setTextFieldSpinner(JComboBox product, JTextField name, JTextField price, int val){
        if(product.getSelectedItem().toString()=="PILIH"){
            clean(name, price);
        } else {
            data.setKode(product.getSelectedItem().toString());
            data.dataSelection();
            setDataSpinner(name, price, val);        
        }
    }
    
    public void setDataSpinner(JTextField name, JTextField price, int val){
        name.setText(data.getNamaVarian());
        double total = data.getHarga() * val;
        price.setText(formatRupiah.format(total));
    }
    
    //Bug?
    public void setChangedSpinner(JSpinner product, JComboBox productBox, JTextField name, JTextField price){
        clean(name, price);
        int val = Integer.parseInt(product.getValue().toString());
        setTextFieldSpinner(productBox, name, price, val);
    }
    
    public void addChoice(){   
        String name = data.getNama();
        cbVarian.addItem("PILIH");
        
        if(name=="Monitor"){
            addVarianProduct(monitor);
        } else if(name=="Keyboard"){
            addVarianProduct(keyboard);
        } else if(name=="Mouse"){
            addVarianProduct(mouse);
        } else if(name=="Motherboard"){
            addVarianProduct(motherboard);
        } else if(name=="Casing"){
            addVarianProduct(casing);
        } else if(name=="RAM"){
            addVarianProduct(ram);
        } else if(name=="SSD"){
            addVarianProduct(ssd);
        } else if(name=="HDD"){
            addVarianProduct(hdd);
        } else if(name=="Cooler"){
            addVarianProduct(cooler);
        } else if(name=="CPU"){
            addVarianProduct(cpu);
        } else if(name=="PSU"){
            addVarianProduct(psu);
        } else{
            addVarianProduct(vga);
        }   
    }
    
    //Menambahkan item pada combo box
    public void addVarianProduct(String[][] product){
        for(int i=0; i<product.length; i++){
            cbVarian.addItem(product[i][0]);
        }
    }
    
    //Menambahkan item pada seluruh combo box
    public void addChoice2(){
        jCasingBox.addItem("PILIH");
        jMonitorBox.addItem("PILIH");
        jKeyboardBox.addItem("PILIH");
        jMouseBox.addItem("PILIH");
        jProcessorBox.addItem("PILIH");
        jMotherboardBox.addItem("PILIH");
        jRAMBox.addItem("PILIH");
        jSSDBox.addItem("PILIH");
        jHDDBox.addItem("PILIH");
        jCoolerBox.addItem("PILIH");
        jPSUBox.addItem("PILIH");
        jVGABox.addItem("PILIH");
        
        for(int i=0; i<5; i++){
            jMonitorBox.addItem(monitor[i][0]);
            jKeyboardBox.addItem(keyboard[i][0]);
            jCasingBox.addItem(casing[i][0]);
            jMouseBox.addItem(mouse[i][0]);
            jProcessorBox.addItem(cpu[i][0]);
            jMotherboardBox.addItem(motherboard[i][0]);
            jRAMBox.addItem(ram[i][0]);
            jSSDBox.addItem(ssd[i][0]);
            jHDDBox.addItem(hdd[i][0]);
            jCoolerBox.addItem(cooler[i][0]);
            jPSUBox.addItem(psu[i][0]);
            jVGABox.addItem(vga[i][0]);
        }       
    }
    
    //Mereset text field
    public void clean(JTextField name, JTextField price){
        name.setText("");
        price.setText("");
    }
    
    //Menonaktifkan radio button
    
public void rbEnable(boolean choice){
        rbCPU.setEnabled(choice);
        rbCooler.setEnabled(choice);
        rbCasing.setEnabled(choice);
        rbKeyboard.setEnabled(choice);
        rbHDD.setEnabled(choice);
        rbSSD.setEnabled(choice);
        rbMonitor.setEnabled(choice);
        rbMouse.setEnabled(choice);
        rbMotherboard.setEnabled(choice);
        rbPSU.setEnabled(choice);
        rbRAM.setEnabled(choice);
        rbVGA.setEnabled(choice);
    }
    
    public void uneditableTextField(){
        cbVarian.setEditable(false);
        dateTextField.setEditable(false);
        totalBayar.setEnabled(false);
        namaBarang.setEditable(false);
        hargaBarang.setEditable(false);
        namaMonitor.setEditable(false);
        hargaMonitor.setEditable(false);
        namaKeyboard.setEditable(false);
        hargaKeyboard.setEditable(false);
        namaMouse.setEditable(false);
        hargaMouse.setEditable(false);
        namaCasing.setEditable(false);
        hargaCasing.setEditable(false);
        namaMotherboard.setEditable(false);
        hargaMotherboard.setEditable(false);
        namaProcessor.setEditable(false);
        hargaProcessor.setEditable(false);
        namaRAM.setEditable(false);
        hargaRAM.setEditable(false);
        namaSSD.setEditable(false);
        hargaSSD.setEditable(false);
        namaHDD.setEditable(false);
        hargaHDD.setEditable(false);
        namaCooler.setEditable(false);
        hargaCooler.setEditable(false);
        namaVGA.setEditable(false);
        hargaVGA.setEditable(false);
        namaPSU.setEditable(false);
        hargaPSU.setEditable(false);   
    }
    
    public void setVisiblePanel(boolean home, boolean belanja, boolean rakit, boolean payment){
        pnlBeranda.setVisible(home);
        pnlBelanja.setVisible(belanja);
        pnlRakit.setVisible(rakit);
        pnlPembayaran.setVisible(payment);    
    }
        
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Home = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        topLabel = new javax.swing.JLabel();
        jClose2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jBeranda = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        pnlBelanja = new javax.swing.JPanel();
        rbMonitor = new javax.swing.JRadioButton();
        rbKeyboard = new javax.swing.JRadioButton();
        rbMotherboard = new javax.swing.JRadioButton();
        rbCasing = new javax.swing.JRadioButton();
        rbMouse = new javax.swing.JRadioButton();
        rbPSU = new javax.swing.JRadioButton();
        rbCPU = new javax.swing.JRadioButton();
        rbCooler = new javax.swing.JRadioButton();
        rbRAM = new javax.swing.JRadioButton();
        rbHDD = new javax.swing.JRadioButton();
        rbSSD = new javax.swing.JRadioButton();
        rbVGA = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        namaBarang = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        hargaBarang = new javax.swing.JTextField();
        cbVarian = new javax.swing.JComboBox<>();
        backButton2 = new tugas.FButton();
        fAddButton = new tugas.FButton();
        fPayButton = new tugas.FButton();
        fButton5 = new tugas.FButton();
        pnlRakit = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jProcessorBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jMotherboardBox = new javax.swing.JComboBox<>();
        jSpinnerProcessor = new javax.swing.JSpinner();
        jSpinnerMotherboard = new javax.swing.JSpinner();
        namaCasing = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jRAMBox = new javax.swing.JComboBox<>();
        jSpinnerRAM = new javax.swing.JSpinner();
        namaMotherboard = new javax.swing.JTextField();
        namaVGA = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jCasingBox = new javax.swing.JComboBox<>();
        namaProcessor = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jMonitorBox = new javax.swing.JComboBox<>();
        namaMonitor = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jKeyboardBox = new javax.swing.JComboBox<>();
        namaKeyboard = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jMouseBox = new javax.swing.JComboBox<>();
        namaMouse = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jVGABox = new javax.swing.JComboBox<>();
        jSSDBox = new javax.swing.JComboBox<>();
        jHDDBox = new javax.swing.JComboBox<>();
        jPSUBox = new javax.swing.JComboBox<>();
        namaSSD = new javax.swing.JTextField();
        namaHDD = new javax.swing.JTextField();
        namaPSU = new javax.swing.JTextField();
        namaRAM = new javax.swing.JTextField();
        jSpinnerSSD = new javax.swing.JSpinner();
        jSpinnerHDD = new javax.swing.JSpinner();
        jLabel26 = new javax.swing.JLabel();
        jCoolerBox = new javax.swing.JComboBox<>();
        namaCooler = new javax.swing.JTextField();
        jSpinnerCooler = new javax.swing.JSpinner();
        hargaCasing = new javax.swing.JTextField();
        hargaMonitor = new javax.swing.JTextField();
        hargaKeyboard = new javax.swing.JTextField();
        hargaMouse = new javax.swing.JTextField();
        hargaProcessor = new javax.swing.JTextField();
        hargaMotherboard = new javax.swing.JTextField();
        hargaRAM = new javax.swing.JTextField();
        hargaSSD = new javax.swing.JTextField();
        hargaHDD = new javax.swing.JTextField();
        hargaCooler = new javax.swing.JTextField();
        hargaPSU = new javax.swing.JTextField();
        hargaVGA = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        rakitLabel = new javax.swing.JLabel();
        fBackButton = new tugas.FButton();
        fNextButton = new tugas.FButton();
        pnlPembayaran = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDetailProduct = new javax.swing.JTable();
        totalBayar = new javax.swing.JTextField();
        fButton2 = new tugas.FButton();
        fCancelButton = new tugas.FButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        dateTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        pnlBeranda = new javax.swing.JPanel();
        jRakitButton = new javax.swing.JButton();
        jBelanjaButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        afterWelcomeText = new javax.swing.JLabel();
        welcomeText = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        Home.setBackground(new java.awt.Color(204, 204, 255));
        Home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        topLabel.setForeground(new java.awt.Color(51, 51, 51));
        topLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/computer(1).png"))); // NOI18N
        topLabel.setText("  TOKO KOMPUTER");
        jPanel5.add(topLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, -1, 80));

        jClose2.setForeground(new java.awt.Color(255, 255, 255));
        jClose2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jClose2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_close_window_30px.png"))); // NOI18N
        jClose2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jClose2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jClose2MouseClicked(evt);
            }
        });
        jPanel5.add(jClose2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 80, 80));

        Home.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 0, 940, 80));

        jPanel3.setBackground(new java.awt.Color(41, 54, 63));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jBeranda.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        jBeranda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jBerandaMousePressed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/house.png"))); // NOI18N
        jLabel29.setText("  Home");

        javax.swing.GroupLayout jBerandaLayout = new javax.swing.GroupLayout(jBeranda);
        jBeranda.setLayout(jBerandaLayout);
        jBerandaLayout.setHorizontalGroup(
            jBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
        );
        jBerandaLayout.setVerticalGroup(
            jBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
        );

        jPanel3.add(jBeranda, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 60));

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/emergency-exit.png"))); // NOI18N
        jLabel27.setText("Exit");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 510, 220, 60));

        Home.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 217, 570));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setPreferredSize(new java.awt.Dimension(723, 596));
        jPanel4.setLayout(new java.awt.CardLayout());

        pnlBelanja.setBackground(new java.awt.Color(255, 255, 255));

        buttonGroup1.add(rbMonitor);
        rbMonitor.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbMonitor.setText("Monitor");
        rbMonitor.setBorder(null);
        rbMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMonitorActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbKeyboard);
        rbKeyboard.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbKeyboard.setText("Keyboard");
        rbKeyboard.setBorder(null);
        rbKeyboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbKeyboardActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbMotherboard);
        rbMotherboard.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbMotherboard.setText("Motherboard");
        rbMotherboard.setBorder(null);
        rbMotherboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMotherboardActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbCasing);
        rbCasing.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbCasing.setText("Casing");
        rbCasing.setBorder(null);
        rbCasing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCasingActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbMouse);
        rbMouse.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbMouse.setText("Mouse");
        rbMouse.setBorder(null);
        rbMouse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbMouseActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbPSU);
        rbPSU.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbPSU.setText("PSU");
        rbPSU.setBorder(null);
        rbPSU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPSUActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbCPU);
        rbCPU.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbCPU.setText("CPU");
        rbCPU.setBorder(null);
        rbCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCPUActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbCooler);
        rbCooler.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbCooler.setText("Cooler FAN");
        rbCooler.setBorder(null);

        buttonGroup1.add(rbRAM);
        rbRAM.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbRAM.setText("RAM");
        rbRAM.setBorder(null);
        rbRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRAMActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbHDD);
        rbHDD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbHDD.setText("HDD");
        rbHDD.setBorder(null);
        rbHDD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbHDDActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbSSD);
        rbSSD.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbSSD.setText("SSD");
        rbSSD.setBorder(null);
        rbSSD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSSDActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbVGA);
        rbVGA.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rbVGA.setText("VGA");
        rbVGA.setBorder(null);
        rbVGA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbVGAActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setText("Nama Barang        :");

        jLabel11.setText("-------------------------------------------------------------------------------------------------------------------------------------------------------------");

        namaBarang.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel13.setText("Kode Barang         :");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel28.setText("Harga Barang       :");

        hargaBarang.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        hargaBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargaBarangActionPerformed(evt);
            }
        });

        cbVarian.setBackground(new java.awt.Color(41, 54, 63));
        cbVarian.setEditable(true);
        cbVarian.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        cbVarian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbVarianActionPerformed(evt);
            }
        });

        backButton2.setBackground(new java.awt.Color(155, 155, 255));
        backButton2.setText("Back");
        backButton2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        backButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButton2ActionPerformed(evt);
            }
        });

        fAddButton.setText("Add");
        fAddButton.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        fAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fAddButtonActionPerformed(evt);
            }
        });

        fPayButton.setText("Pay");
        fPayButton.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        fPayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fPayButtonActionPerformed(evt);
            }
        });

        fButton5.setBackground(new java.awt.Color(155, 155, 255));
        fButton5.setText("Next");
        fButton5.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        fButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBelanjaLayout = new javax.swing.GroupLayout(pnlBelanja);
        pnlBelanja.setLayout(pnlBelanjaLayout);
        pnlBelanjaLayout.setHorizontalGroup(
            pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBelanjaLayout.createSequentialGroup()
                        .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbMonitor)
                            .addComponent(rbMouse)
                            .addComponent(rbRAM))
                        .addGap(108, 108, 108)
                        .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbPSU)
                            .addComponent(rbKeyboard)
                            .addComponent(rbHDD))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBelanjaLayout.createSequentialGroup()
                                .addComponent(rbMotherboard)
                                .addGap(102, 102, 102))
                            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rbCPU)
                                    .addComponent(rbSSD))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbCooler)
                            .addComponent(rbCasing)
                            .addComponent(rbVGA))
                        .addContainerGap())
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlBelanjaLayout.createSequentialGroup()
                        .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                                .addComponent(hargaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(fPayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43))
                            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBelanjaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        pnlBelanjaLayout.setVerticalGroup(
            pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbMonitor)
                    .addComponent(rbKeyboard)
                    .addComponent(rbMotherboard)
                    .addComponent(rbCasing))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbMouse)
                    .addComponent(rbPSU)
                    .addComponent(rbCPU)
                    .addComponent(rbCooler))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbRAM)
                    .addComponent(rbHDD)
                    .addComponent(rbSSD)
                    .addComponent(rbVGA))
                .addGap(30, 30, 30)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28)
                    .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hargaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fPayButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(fAddButton, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)))
                .addGap(614, 614, 614))
        );

        jPanel4.add(pnlBelanja, "card4");

        pnlRakit.setPreferredSize(new java.awt.Dimension(719, 550));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Pilih Processor        :");

        jProcessorBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jProcessorBoxActionPerformed(evt);
            }
        });

        jLabel7.setText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Pilih Motherboard  :");

        jMotherboardBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMotherboardBoxActionPerformed(evt);
            }
        });

        jSpinnerProcessor.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerProcessor.setValue(1);
        jSpinnerProcessor.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerProcessorStateChanged(evt);
            }
        });

        jSpinnerMotherboard.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerMotherboard.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMotherboardStateChanged(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Pilih RAM                :");

        jRAMBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRAMBoxActionPerformed(evt);
            }
        });

        jSpinnerRAM.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerRAM.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerRAMStateChanged(evt);
            }
        });

        jLabel16.setText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        jLabel17.setText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setText("Pilih Casing             :");
        jLabel21.setPreferredSize(new java.awt.Dimension(102, 14));

        jCasingBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCasingBoxActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setText("Pilih Monitor           :");
        jLabel23.setPreferredSize(new java.awt.Dimension(102, 14));

        jMonitorBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMonitorBoxActionPerformed(evt);
            }
        });

        namaMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaMonitorActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setText("Pilih Keyboard       :");
        jLabel24.setPreferredSize(new java.awt.Dimension(102, 14));

        jKeyboardBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jKeyboardBoxActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel25.setText("Pilih Mouse             :");
        jLabel25.setMaximumSize(new java.awt.Dimension(102, 14));
        jLabel25.setMinimumSize(new java.awt.Dimension(102, 14));
        jLabel25.setPreferredSize(new java.awt.Dimension(102, 14));

        jMouseBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMouseBoxActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setText("Pilih VGA                   :");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Pilih SSD                   :");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setText("Pilih HDD                  :");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setText("Pilih PSU                   :");

        jVGABox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jVGABoxActionPerformed(evt);
            }
        });

        jSSDBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSSDBoxActionPerformed(evt);
            }
        });

        jHDDBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHDDBoxActionPerformed(evt);
            }
        });

        jPSUBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPSUBoxActionPerformed(evt);
            }
        });

        namaHDD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaHDDActionPerformed(evt);
            }
        });

        namaPSU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaPSUActionPerformed(evt);
            }
        });

        jSpinnerSSD.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerSSD.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerSSDStateChanged(evt);
            }
        });

        jSpinnerHDD.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerHDD.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerHDDStateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("Pilih Cooler FAN      :");

        jCoolerBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCoolerBoxActionPerformed(evt);
            }
        });

        jSpinnerCooler.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerCooler.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerCoolerStateChanged(evt);
            }
        });

        hargaCasing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargaCasingActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(357, 80));

        rakitLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        rakitLabel.setText("Komponen Rakit PC");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rakitLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rakitLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );

        fBackButton.setText("Back");
        fBackButton.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        fBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fBackButtonActionPerformed(evt);
            }
        });

        fNextButton.setText("Next");
        fNextButton.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        fNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fNextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlRakitLayout = new javax.swing.GroupLayout(pnlRakit);
        pnlRakit.setLayout(pnlRakitLayout);
        pnlRakitLayout.setHorizontalGroup(
            pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRakitLayout.createSequentialGroup()
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRakitLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(pnlRakitLayout.createSequentialGroup()
                                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlRakitLayout.createSequentialGroup()
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(4, 4, 4)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pnlRakitLayout.createSequentialGroup()
                                                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jPSUBox, 0, 144, Short.MAX_VALUE)
                                                    .addComponent(jVGABox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(pnlRakitLayout.createSequentialGroup()
                                                        .addGap(225, 225, 225)
                                                        .addComponent(fBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(10, 10, 10)
                                                        .addComponent(fNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRakitLayout.createSequentialGroup()
                                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(namaVGA)
                                                            .addComponent(namaPSU))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(hargaVGA, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(hargaPSU, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                            .addGroup(pnlRakitLayout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addComponent(jCoolerBox, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jSpinnerCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(8, 8, 8)
                                                .addComponent(namaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(hargaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(pnlRakitLayout.createSequentialGroup()
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(6, 6, 6)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jSSDBox, 0, 143, Short.MAX_VALUE)
                                            .addComponent(jHDDBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jSpinnerSSD)
                                            .addComponent(jSpinnerHDD))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(namaHDD, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                            .addComponent(namaSSD))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(hargaSSD, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                                            .addComponent(hargaHDD)))
                                    .addGroup(pnlRakitLayout.createSequentialGroup()
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(8, 8, 8)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(pnlRakitLayout.createSequentialGroup()
                                                .addComponent(jRAMBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jSpinnerRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(namaRAM))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRakitLayout.createSequentialGroup()
                                                .addComponent(jMotherboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jSpinnerMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(namaMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRakitLayout.createSequentialGroup()
                                                .addComponent(jProcessorBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jSpinnerProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(namaProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(12, 12, 12)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(hargaMotherboard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                            .addComponent(hargaProcessor, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(hargaRAM)))
                                    .addGroup(pnlRakitLayout.createSequentialGroup()
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(pnlRakitLayout.createSequentialGroup()
                                                .addGap(9, 9, 9)
                                                .addComponent(jMouseBox, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(pnlRakitLayout.createSequentialGroup()
                                                    .addGap(9, 9, 9)
                                                    .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jMonitorBox, 0, 141, Short.MAX_VALUE)
                                                        .addComponent(jCasingBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                .addGroup(pnlRakitLayout.createSequentialGroup()
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jKeyboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(18, 18, 18)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(namaMouse, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                                            .addComponent(namaKeyboard)
                                            .addComponent(namaCasing)
                                            .addComponent(namaMonitor))
                                        .addGap(12, 12, 12)
                                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(hargaKeyboard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                                            .addComponent(hargaMonitor, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(hargaCasing, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(hargaMouse)))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlRakitLayout.setVerticalGroup(
            pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRakitLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCasingBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(hargaCasing, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(namaCasing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMonitorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jKeyboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaKeyboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaKeyboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMouseBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaMouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaMouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jProcessorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jMotherboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jRAMBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jSSDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerSSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaSSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaSSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(namaHDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaHDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerHDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jHDDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(hargaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(namaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSpinnerCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(jCoolerBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jPSUBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaPSU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaPSU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jVGABox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaVGA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaVGA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel4.add(pnlRakit, "card3");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setText("Total Bayar   :");

        tableDetailProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Barang", "Kode Barang", "Varian", "Harga"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableDetailProduct.setIntercellSpacing(new java.awt.Dimension(0, 0));
        tableDetailProduct.setRowSelectionAllowed(false);
        tableDetailProduct.setShowVerticalLines(false);
        jScrollPane2.setViewportView(tableDetailProduct);

        totalBayar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        totalBayar.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        totalBayar.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        totalBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalBayarActionPerformed(evt);
            }
        });

        fButton2.setText("Process");
        fButton2.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        fButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fButton2ActionPerformed(evt);
            }
        });

        fCancelButton.setText("Cancel");
        fCancelButton.setFont(new java.awt.Font("Tahoma", 0, 17)); // NOI18N
        fCancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fCancelButtonActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Detail Transaksi");

        jLabel14.setText("Tanggal :");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel14))
        );

        javax.swing.GroupLayout pnlPembayaranLayout = new javax.swing.GroupLayout(pnlPembayaran);
        pnlPembayaran.setLayout(pnlPembayaranLayout);
        pnlPembayaranLayout.setHorizontalGroup(
            pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlPembayaranLayout.createSequentialGroup()
                .addGroup(pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPembayaranLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPembayaranLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPembayaranLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPembayaranLayout.createSequentialGroup()
                                .addComponent(fButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        pnlPembayaranLayout.setVerticalGroup(
            pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPembayaranLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(totalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addGroup(pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36))
        );

        jPanel4.add(pnlPembayaran, "card3");

        pnlBeranda.setBackground(new java.awt.Color(255, 255, 255));

        jRakitButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jRakitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/mainboard.png"))); // NOI18N
        jRakitButton.setText("Rakit Komputer");
        jRakitButton.setBorder(null);
        jRakitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jRakitButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRakitButton.setInheritsPopupMenu(true);
        jRakitButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRakitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRakitButtonActionPerformed(evt);
            }
        });

        jBelanjaButton.setBackground(new java.awt.Color(204, 255, 255));
        jBelanjaButton.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jBelanjaButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/pc-tower.png"))); // NOI18N
        jBelanjaButton.setText("Belanja Komponen");
        jBelanjaButton.setBorder(null);
        jBelanjaButton.setBorderPainted(false);
        jBelanjaButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jBelanjaButton.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jBelanjaButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBelanjaButton.setInheritsPopupMenu(true);
        jBelanjaButton.setPreferredSize(new java.awt.Dimension(145, 160));
        jBelanjaButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBelanjaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBelanjaButtonActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(41, 54, 63));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 75, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        afterWelcomeText.setBackground(new java.awt.Color(204, 204, 204));
        afterWelcomeText.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        afterWelcomeText.setForeground(new java.awt.Color(51, 51, 51));
        afterWelcomeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        afterWelcomeText.setText("Silahkan pilih menu di bawah ini");

        welcomeText.setBackground(new java.awt.Color(204, 204, 204));
        welcomeText.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        welcomeText.setForeground(new java.awt.Color(51, 51, 51));
        welcomeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        welcomeText.setText("Welcome!");

        javax.swing.GroupLayout pnlBerandaLayout = new javax.swing.GroupLayout(pnlBeranda);
        pnlBeranda.setLayout(pnlBerandaLayout);
        pnlBerandaLayout.setHorizontalGroup(
            pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBerandaLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addComponent(welcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlBerandaLayout.createSequentialGroup()
                                .addComponent(jBelanjaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(97, 97, 97)
                                .addComponent(jRakitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(afterWelcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlBerandaLayout.setVerticalGroup(
            pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBerandaLayout.createSequentialGroup()
                .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 386, Short.MAX_VALUE))
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(welcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(afterWelcomeText)
                        .addGap(37, 37, 37)
                        .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBelanjaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRakitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(98, 98, 98)))
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.add(pnlBeranda, "card3");

        Home.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 86, 723, 570));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rbMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMonitorActionPerformed
        data.setNama("Monitor");
    }//GEN-LAST:event_rbMonitorActionPerformed

    private void rbKeyboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbKeyboardActionPerformed
        data.setNama("Keyboard");
    }//GEN-LAST:event_rbKeyboardActionPerformed

    private void rbMotherboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMotherboardActionPerformed
        data.setNama("Motherboard");
    }//GEN-LAST:event_rbMotherboardActionPerformed

    private void rbMouseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbMouseActionPerformed
        data.setNama("Mouse");
    }//GEN-LAST:event_rbMouseActionPerformed

    private void rbCPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCPUActionPerformed
        data.setNama("CPU");
    }//GEN-LAST:event_rbCPUActionPerformed

    private void rbRAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRAMActionPerformed
        data.setNama("RAM");
    }//GEN-LAST:event_rbRAMActionPerformed

    private void rbHDDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbHDDActionPerformed
        data.setNama("HDD");
    }//GEN-LAST:event_rbHDDActionPerformed

    private void rbVGAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbVGAActionPerformed
        data.setNama("VGA");
    }//GEN-LAST:event_rbVGAActionPerformed

    private void rbSSDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSSDActionPerformed
        data.setNama("SSD");
    }//GEN-LAST:event_rbSSDActionPerformed

    private void rbCasingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCasingActionPerformed
        data.setNama("Casing");
    }//GEN-LAST:event_rbCasingActionPerformed

    private void rbPSUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPSUActionPerformed
        data.setNama("PSU");
        
    }//GEN-LAST:event_rbPSUActionPerformed

    private void cbVarianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbVarianActionPerformed
        if(cleanData!=true){
            setTextField(cbVarian, namaBarang, hargaBarang);
        }  
    }//GEN-LAST:event_cbVarianActionPerformed

    private void hargaBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaBarangActionPerformed

    private void totalBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalBayarActionPerformed

    private void jClose2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jClose2MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jClose2MouseClicked

    private void jBerandaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBerandaMousePressed
        backToHome();
        fButton2.setEnabled(true);
        
    }//GEN-LAST:event_jBerandaMousePressed

    private void jSpinnerCoolerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerCoolerStateChanged
        setTextAfterChanged(jSpinnerCooler, jCoolerBox, "Cooler", hargaCooler);
    }//GEN-LAST:event_jSpinnerCoolerStateChanged

    private void jCoolerBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCoolerBoxActionPerformed
        data.setNama("Cooler");
        setChangedSpinner(jSpinnerCooler, jCoolerBox, namaCooler, hargaCooler);
    }//GEN-LAST:event_jCoolerBoxActionPerformed

    private void jSpinnerHDDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerHDDStateChanged
        setTextAfterChanged(jSpinnerHDD, jHDDBox, "HDD", hargaHDD);
    }//GEN-LAST:event_jSpinnerHDDStateChanged

    private void jSpinnerSSDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerSSDStateChanged
        setTextAfterChanged(jSpinnerSSD, jSSDBox, "SSD", hargaSSD);
    }//GEN-LAST:event_jSpinnerSSDStateChanged

    private void namaPSUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaPSUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaPSUActionPerformed

    private void namaHDDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaHDDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaHDDActionPerformed

    private void jPSUBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPSUBoxActionPerformed
        data.setNama("PSU");
        setTextField(jPSUBox, namaPSU, hargaPSU);
    }//GEN-LAST:event_jPSUBoxActionPerformed

    private void jHDDBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHDDBoxActionPerformed
        data.setNama("HDD");
        setChangedSpinner(jSpinnerHDD, jHDDBox, namaHDD, hargaHDD);
    }//GEN-LAST:event_jHDDBoxActionPerformed

    private void jSSDBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSSDBoxActionPerformed
        data.setNama("SSD");
        setChangedSpinner(jSpinnerSSD, jSSDBox, namaSSD, hargaSSD);
    }//GEN-LAST:event_jSSDBoxActionPerformed

    private void jVGABoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jVGABoxActionPerformed
        data.setNama("VGA");
        setTextField(jVGABox, namaVGA, hargaVGA);
    }//GEN-LAST:event_jVGABoxActionPerformed

    private void jMouseBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMouseBoxActionPerformed
        data.setNama("Mouse");
        setTextField(jMouseBox, namaMouse, hargaMouse);
    }//GEN-LAST:event_jMouseBoxActionPerformed

    private void jKeyboardBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jKeyboardBoxActionPerformed
        data.setNama("Keyboard");
        setTextField(jKeyboardBox, namaKeyboard, hargaKeyboard);
    }//GEN-LAST:event_jKeyboardBoxActionPerformed

    private void namaMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaMonitorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaMonitorActionPerformed

    private void jMonitorBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMonitorBoxActionPerformed
        data.setNama("Monitor");
        setTextField(jMonitorBox, namaMonitor, hargaMonitor);
    }//GEN-LAST:event_jMonitorBoxActionPerformed

    private void jCasingBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCasingBoxActionPerformed
        data.setNama("Casing");
        setTextField(jCasingBox, namaCasing, hargaCasing);
    }//GEN-LAST:event_jCasingBoxActionPerformed

    private void jSpinnerRAMStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerRAMStateChanged
        setTextAfterChanged(jSpinnerRAM, jRAMBox, "RAM", hargaRAM);
    }//GEN-LAST:event_jSpinnerRAMStateChanged

    private void jRAMBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRAMBoxActionPerformed
        data.setNama("RAM");
        setChangedSpinner(jSpinnerRAM, jRAMBox, namaRAM, hargaRAM);
    }//GEN-LAST:event_jRAMBoxActionPerformed

    private void jSpinnerMotherboardStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMotherboardStateChanged
        setTextAfterChanged(jSpinnerMotherboard, jMotherboardBox, "Motherboard", hargaMotherboard);
    }//GEN-LAST:event_jSpinnerMotherboardStateChanged

    private void jSpinnerProcessorStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerProcessorStateChanged
        setTextAfterChanged(jSpinnerProcessor, jProcessorBox, "CPU", hargaProcessor);
    }//GEN-LAST:event_jSpinnerProcessorStateChanged

    private void jMotherboardBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMotherboardBoxActionPerformed
        data.setNama("Motherboard");
        setChangedSpinner(jSpinnerMotherboard, jMotherboardBox, namaMotherboard, hargaMotherboard);

    }//GEN-LAST:event_jMotherboardBoxActionPerformed

    private void jProcessorBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jProcessorBoxActionPerformed
        data.setNama("CPU");
        setChangedSpinner(jSpinnerProcessor, jProcessorBox, namaProcessor, hargaProcessor);
    }//GEN-LAST:event_jProcessorBoxActionPerformed

    private void jBelanjaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBelanjaButtonActionPerformed
        setVisiblePanel(false,true,false,false);
        fAddButton.setEnabled(false);
        fPayButton.setEnabled(false);
    }//GEN-LAST:event_jBelanjaButtonActionPerformed

    private void jRakitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRakitButtonActionPerformed
        setVisiblePanel(false,false,true,false);
        addChoice2();
    }//GEN-LAST:event_jRakitButtonActionPerformed

    private void hargaCasingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaCasingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaCasingActionPerformed

    private void backButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButton2ActionPerformed
        backToHome();
        rbEnable(true);
        buttonGroup1.clearSelection();
    }//GEN-LAST:event_backButton2ActionPerformed

    private void fPayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fPayButtonActionPerformed
        if(savedData.size()==0){
            JOptionPane.showMessageDialog(null, "Anda belum menambahkan barang", "WARNING", JOptionPane.OK_OPTION);
        } else{
            setVisiblePanel(false, false, false, true);
            totalPayment(totalBayar);
            addDataTable();
        }
    }//GEN-LAST:event_fPayButtonActionPerformed

    private void fAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fAddButtonActionPerformed
        if(cbVarian.getSelectedItem().toString()=="PILIH"){
            JOptionPane.showMessageDialog(null, "Varian Belum Dipilih", "WARNING", JOptionPane.OK_OPTION);
        } else{
            saveData(data.getNama(), cbVarian.getSelectedItem().toString(), namaBarang.getText().toString(), hargaBarang.getText().toString());
            cleanData = true;
            cbVarian.removeAllItems();
            clean(hargaBarang, namaBarang);
            rbEnable(true);
            
            fAddButton.setEnabled(false);
        }
    }//GEN-LAST:event_fAddButtonActionPerformed

    private void fBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fBackButtonActionPerformed
        backToHome();
    }//GEN-LAST:event_fBackButtonActionPerformed

    private void fNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fNextButtonActionPerformed
        String casingSelected = jCasingBox.getSelectedItem().toString();
        String monitorSelected = jMonitorBox.getSelectedItem().toString();
        String keyboardSelected = jKeyboardBox.getSelectedItem().toString();
        String mouseSelected = jMouseBox.getSelectedItem().toString();
        String cpuSelected = jProcessorBox.getSelectedItem().toString();
        String motherboardSelected = jMotherboardBox.getSelectedItem().toString();
        String ramSelected = jRAMBox.getSelectedItem().toString();
        String ssdSelected = jSSDBox.getSelectedItem().toString();
        String hddSelected = jHDDBox.getSelectedItem().toString();
        String coolerSelected = jCoolerBox.getSelectedItem().toString();
        String psuSelected = jPSUBox.getSelectedItem().toString();
        String vgaSelected = jVGABox.getSelectedItem().toString();
        if(casingSelected=="PILIH" || monitorSelected=="PILIH" || keyboardSelected=="PILIH" ||
                mouseSelected=="PILIH" || cpuSelected=="PILIH" || motherboardSelected=="PILIH" ||
                ramSelected=="PILIH" || ssdSelected=="PILIH" || hddSelected=="PILIH" ||
                coolerSelected=="PILIH" || psuSelected=="PILIH" || vgaSelected=="PILIH"){
            JOptionPane.showMessageDialog(null, "Data masih kosong", "WARNING", JOptionPane.OK_OPTION);
        } else {
            saveData("Casing", casingSelected, namaCasing.getText(), hargaCasing.getText());
            saveData("Monitor", monitorSelected, namaMonitor.getText(), hargaMonitor.getText());
            saveData("Keyboard", keyboardSelected, namaKeyboard.getText(), hargaKeyboard.getText());
            saveData("Mouse", mouseSelected, namaMouse.getText(), hargaMouse.getText());
            saveData("Processor", cpuSelected, namaProcessor.getText(), hargaProcessor.getText());
            saveData("Motherboard", motherboardSelected, namaMotherboard.getText(), hargaMotherboard.getText());
            saveData("RAM", ramSelected, namaRAM.getText(), hargaRAM.getText());
            saveData("SSD", ssdSelected, namaSSD.getText(), hargaSSD.getText());
            saveData("HDD", hddSelected, namaHDD.getText(), hargaHDD.getText());
            saveData("Cooler", coolerSelected, namaCooler.getText(), hargaCooler.getText());
            saveData("PSU", psuSelected, namaPSU.getText(), hargaPSU.getText());
            saveData("VGA", vgaSelected, namaVGA.getText(), hargaVGA.getText());
            
            addDataTable();
            totalPayment(totalBayar);
            setVisiblePanel(false, false, false, true);            
        }
    }//GEN-LAST:event_fNextButtonActionPerformed

    private void fButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fButton2ActionPerformed
        ls.setLocationRelativeTo(null);
        ls.setVisible(true);
        startProgress();
        fButton2.setEnabled(false);
    }//GEN-LAST:event_fButton2ActionPerformed

    private void fCancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fCancelButtonActionPerformed
        backToHome();
        fButton2.setEnabled(true);
        fCancelButton.setText("Cancel");
    }//GEN-LAST:event_fCancelButtonActionPerformed

    private void fButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fButton5ActionPerformed
        if(buttonGroup1.getSelection()==null){
            JOptionPane.showMessageDialog(null, "Anda Belum Memilih Komponen", "WARNING", JOptionPane.OK_OPTION);
        } else{
            buttonGroup1.clearSelection();
            cleanData=false;
            addChoice();
            rbEnable(false);
            fAddButton.setEnabled(true);
            fPayButton.setEnabled(true);           
        }
    }//GEN-LAST:event_fButton5ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jPanel1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(UnsupportedLookAndFeelException ignore) {
            Toolkit.getDefaultToolkit().beep();
        } catch(ClassNotFoundException | InstantiationException | IllegalAccessException ex){
            ex.printStackTrace();
            return;
        }
        
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Home;
    private javax.swing.JLabel afterWelcomeText;
    private tugas.FButton backButton2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbVarian;
    private javax.swing.JTextField dateTextField;
    private tugas.FButton fAddButton;
    private tugas.FButton fBackButton;
    private tugas.FButton fButton2;
    private tugas.FButton fButton5;
    private tugas.FButton fCancelButton;
    private tugas.FButton fNextButton;
    private tugas.FButton fPayButton;
    private javax.swing.JTextField hargaBarang;
    private javax.swing.JTextField hargaCasing;
    private javax.swing.JTextField hargaCooler;
    private javax.swing.JTextField hargaHDD;
    private javax.swing.JTextField hargaKeyboard;
    private javax.swing.JTextField hargaMonitor;
    private javax.swing.JTextField hargaMotherboard;
    private javax.swing.JTextField hargaMouse;
    private javax.swing.JTextField hargaPSU;
    private javax.swing.JTextField hargaProcessor;
    private javax.swing.JTextField hargaRAM;
    private javax.swing.JTextField hargaSSD;
    private javax.swing.JTextField hargaVGA;
    private javax.swing.JButton jBelanjaButton;
    javax.swing.JPanel jBeranda;
    private javax.swing.JComboBox<String> jCasingBox;
    javax.swing.JLabel jClose2;
    private javax.swing.JComboBox<String> jCoolerBox;
    private javax.swing.JComboBox<String> jHDDBox;
    private javax.swing.JComboBox<String> jKeyboardBox;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox<String> jMonitorBox;
    private javax.swing.JComboBox<String> jMotherboardBox;
    private javax.swing.JComboBox<String> jMouseBox;
    private javax.swing.JComboBox<String> jPSUBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JComboBox<String> jProcessorBox;
    private javax.swing.JComboBox<String> jRAMBox;
    private javax.swing.JButton jRakitButton;
    private javax.swing.JComboBox<String> jSSDBox;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSpinner jSpinnerCooler;
    private javax.swing.JSpinner jSpinnerHDD;
    private javax.swing.JSpinner jSpinnerMotherboard;
    private javax.swing.JSpinner jSpinnerProcessor;
    private javax.swing.JSpinner jSpinnerRAM;
    private javax.swing.JSpinner jSpinnerSSD;
    private javax.swing.JComboBox<String> jVGABox;
    private javax.swing.JTextField namaBarang;
    private javax.swing.JTextField namaCasing;
    private javax.swing.JTextField namaCooler;
    private javax.swing.JTextField namaHDD;
    private javax.swing.JTextField namaKeyboard;
    private javax.swing.JTextField namaMonitor;
    private javax.swing.JTextField namaMotherboard;
    private javax.swing.JTextField namaMouse;
    private javax.swing.JTextField namaPSU;
    private javax.swing.JTextField namaProcessor;
    private javax.swing.JTextField namaRAM;
    private javax.swing.JTextField namaSSD;
    private javax.swing.JTextField namaVGA;
    private javax.swing.JPanel pnlBelanja;
    private javax.swing.JPanel pnlBeranda;
    private javax.swing.JPanel pnlPembayaran;
    private javax.swing.JPanel pnlRakit;
    private javax.swing.JLabel rakitLabel;
    private javax.swing.JRadioButton rbCPU;
    private javax.swing.JRadioButton rbCasing;
    private javax.swing.JRadioButton rbCooler;
    private javax.swing.JRadioButton rbHDD;
    private javax.swing.JRadioButton rbKeyboard;
    private javax.swing.JRadioButton rbMonitor;
    private javax.swing.JRadioButton rbMotherboard;
    private javax.swing.JRadioButton rbMouse;
    private javax.swing.JRadioButton rbPSU;
    private javax.swing.JRadioButton rbRAM;
    private javax.swing.JRadioButton rbSSD;
    private javax.swing.JRadioButton rbVGA;
    private javax.swing.JTable tableDetailProduct;
    private javax.swing.JLabel topLabel;
    private javax.swing.JTextField totalBayar;
    private javax.swing.JLabel welcomeText;
    // End of variables declaration//GEN-END:variables
}
