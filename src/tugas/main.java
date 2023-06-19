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
    private boolean inBelanja = false;
    private boolean inRakit = false;
    int xx,xy;
    
    List<BarangDto> savedData = new ArrayList<>();
    boolean cleanData = false;
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

    public main() {
        initComponents();

        //Creating font
        try {
            sunny = Font.createFont(Font.TRUETYPE_FONT, new File("SunnyspellsRegular-MV9ze.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("SunnyspellsRegular-MV9ze.ttf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        //Applying font
        topLabel.setFont(sunny.deriveFont(55f));
        welcomeText.setFont(sunny.deriveFont(75f));
        afterWelcomeText.setFont(sunny.deriveFont(35f));
        rakitLabel.setFont(sunny.deriveFont(50f));
        detailTransaksiText.setFont(sunny.deriveFont(50f));

        setVisiblePanel(true, false, false, false);
        uneditableTextField();
        dateTextField.setText(date.format(now).toString());

        //Modif design tabel
        tableDetailProduct.setFont(new Font("Tahoma", Font.PLAIN, 13));
        tableDetailProduct.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableDetailProduct.getTableHeader().setOpaque(false);
        tableDetailProduct.getTableHeader().setBackground(new Color(216, 216, 245));
        tableDetailProduct.getTableHeader().setForeground(new Color(255, 255, 255));

        timer = new Timer(80, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (progress == 100) {
                    timer.stop();
                    ls.setVisible(false);
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                    setDialog();
                    savedData.clear();
                    fCancelButton.setEnabled(true);
                    fCancelButton.setText("Back");
                } else {
                    progress++;
                    ls.jProgressBar.setValue(progress);
                }
            }
        });
    }

    //Set data pada dialog transaksi berhasil
    public void setDialog() {
        String id = UUID.randomUUID().toString();
        dialog.transactionID.setText(id);
        dialog.transactionTime.setText(dateTime.format(now).toString());
        totalPayment(dialog.amount);

    }

    public void resetBelanjaPanel() {
        cbVarian.setEnabled(false);
        clean(cbVarian, namaBarang, hargaBarang);
        jumlahBarangSpinner.setValue(1);
        jumlahBarangSpinner.setEnabled(false);
    }

    public void resetRakitPanel() {
        clean(jCasingBox, namaCasing, hargaCasing);
        clean(jMonitorBox, namaMonitor, hargaMonitor);
        clean(jKeyboardBox, namaKeyboard, hargaKeyboard);
        clean(jMouseBox, namaMouse, hargaMouse);
        clean(jProcessorBox, namaProcessor, hargaProcessor);
        clean(jRAMBox, namaRAM, hargaRAM);
        clean(jMotherboardBox, namaMotherboard, hargaMotherboard);
        clean(jSSDBox, namaSSD, hargaSSD);
        clean(jHDDBox, namaHDD, hargaHDD);
        clean(jPSUBox, namaPSU, hargaPSU);
        clean(jCoolerBox, namaCooler, hargaCooler);
        clean(jVGABox, namaVGA, hargaVGA);

        jSpinnerProcessor.setValue(1);
        jSpinnerMotherboard.setValue(1);
        jSpinnerSSD.setValue(1);
        jSpinnerHDD.setValue(1);
        jSpinnerCooler.setValue(1);
        jSpinnerRAM.setValue(1);
    }

    public void backToHome() {
        if (savedData.size() != 0) {
            int response = JOptionPane.showConfirmDialog(null, "Data barang yang telah ditambahkan akan direset", "WARNING", JOptionPane.OK_CANCEL_OPTION);
            if (response == JOptionPane.OK_OPTION) {
                savedData.clear();
                setVisiblePanel(true, false, false, false);
            }
        } else {
            setVisiblePanel(true, false, false, false);
        }
    }

    public void formatDateTime() {
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
    //Mengubah angka biasa ke format rupiah
    public void setPrice(JSpinner product, JTextField price, double harga) {
        int val = Integer.parseInt(product.getValue().toString());
        double total = val * harga;
        price.setText(formatRupiah.format(total));
    }

    //Mengubah format rupiah ke format angka biasa
    public String convertPrice(String rupiahFormat) {
        String replace1 = rupiahFormat.replace("Rp", "");
        String replace2 = replace1.replace(",00", "");
        String replace3 = replace2.replace(".", "");
        return replace3;
    }

    //Menyimpan data barang yang telah ditambahkan
    public void saveData(String name, String code, String varian, String price, String amount) {
        BarangDto item = new BarangDto(name, code, varian, price, amount);
        savedData.add(item);
    }

    //Menambahkan data yang disimpan ke dalam tabel
    public void addDataTable() {
        DefaultTableModel model = (DefaultTableModel) tableDetailProduct.getModel();
        model.setRowCount(0);
        for (BarangDto item : savedData) {
            String[] row = new String[5];
            row[0] = item.getName();
            row[1] = item.getCode();
            row[2] = item.getProduct();
            row[3] = item.getPrice();
            row[4] = item.getAmount();

            model.addRow(row);
        }
        tableDetailProduct.setRowHeight(25);
        tableDetailProduct.getColumnModel().getColumn(0).setPreferredWidth(65);
        tableDetailProduct.getColumnModel().getColumn(1).setPreferredWidth(65);
        tableDetailProduct.getColumnModel().getColumn(2).setPreferredWidth(200);
        tableDetailProduct.getColumnModel().getColumn(3).setPreferredWidth(85);
        tableDetailProduct.getColumnModel().getColumn(4).setPreferredWidth(50);
    }

    //Menghitung total bayar
    public void totalPayment(JTextField textField) {
        int total = 0;
        int price;
        for (BarangDto item : savedData) {
            price = Integer.parseInt(convertPrice(item.getPrice())) * Integer.parseInt(item.getAmount());
            total += price;
        }
        textField.setText(formatRupiah.format(total));
    }

    //Set text di text field
    public void setData(JTextField name, JTextField price) {
        name.setText(data.getNamaVarian());
        price.setText(data.getHargaString());

    }

    public void cleanItem(JTextField name, JTextField price) {
        name.setText("");
        price.setText("");
    }

    //Menampilkan data pada text field
    public void setTextField(JComboBox product, JTextField name, JTextField price) {
        if (cleanData != true) {
            if (product.getSelectedItem().toString() == "PILIH") {
                cleanItem(name, price);
            } else {
                data.setKode(product.getSelectedItem().toString());
                data.dataSelection();
                setData(name, price);
            }
        }

    }

    //Bug?
    public void setTextFieldSpinner(JComboBox product, JTextField name, JTextField price, int val) {
        if (cleanData != true) {
            if (product.getSelectedItem().toString() == "PILIH") {
                cleanItem(name, price);
            } else {
                data.setKode(product.getSelectedItem().toString());
                data.dataSelection();
                setDataSpinner(name, price, val);
            }
        }

    }

    public void setDataSpinner(JTextField name, JTextField price, int val) {
        name.setText(data.getNamaVarian());
        double total = data.getHarga() * val;
        price.setText(formatRupiah.format(total));
    }

    //Bug?
    public void setChangedSpinner(JSpinner product, JComboBox productBox, JTextField name, JTextField price) {
        cleanItem(name, price);
        int val = Integer.parseInt(product.getValue().toString());
        setTextFieldSpinner(productBox, name, price, val);
    }

    public void addChoice() {
        String name = data.getNama();
        cbVarian.addItem("PILIH");

        if (name == "Monitor") {
            addVarianProduct(monitor);
        } else if (name == "Keyboard") {
            addVarianProduct(keyboard);
        } else if (name == "Mouse") {
            addVarianProduct(mouse);
        } else if (name == "Motherboard") {
            addVarianProduct(motherboard);
        } else if (name == "Casing") {
            addVarianProduct(casing);
        } else if (name == "RAM") {
            addVarianProduct(ram);
        } else if (name == "SSD") {
            addVarianProduct(ssd);
        } else if (name == "HDD") {
            addVarianProduct(hdd);
        } else if (name == "Cooler") {
            addVarianProduct(cooler);
        } else if (name == "CPU") {
            addVarianProduct(cpu);
        } else if (name == "PSU") {
            addVarianProduct(psu);
        } else {
            addVarianProduct(vga);
        }
    }

    //Menambahkan item pada combo box
    public void addVarianProduct(String[][] product) {
        for (int i = 0; i < product.length; i++) {
            cbVarian.addItem(product[i][0]);
        }
    }

    //Menambahkan item pada seluruh combo box
    public void addChoice2() {
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

        for (int i = 0; i < 5; i++) {
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
    public void clean(JComboBox box, JTextField name, JTextField price) {
        box.removeAllItems();
        name.setText("");
        price.setText("");
    }

    //Menonaktifkan atau mengaktifkan radio button   
    public void rbEnable(boolean choice) {
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

    //Membuat text field tidak dapat diedit
    public void uneditableTextField() {
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

    public void setVisiblePanel(boolean home, boolean belanja, boolean rakit, boolean payment) {
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
        jPanel3 = new javax.swing.JPanel();
        jBeranda = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        pnlBeranda = new javax.swing.JPanel();
        jRakitButton = new javax.swing.JButton();
        jBelanjaButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        afterWelcomeText = new javax.swing.JLabel();
        welcomeText = new javax.swing.JLabel();
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
        jLabel1 = new javax.swing.JLabel();
        jumlahBarangSpinner = new javax.swing.JSpinner();
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
        detailTransaksiText = new javax.swing.JLabel();
        dateTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        Home.setBackground(new java.awt.Color(204, 204, 255));
        Home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jPanel5.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel5MouseDragged(evt);
            }
        });
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel5MousePressed(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        topLabel.setForeground(new java.awt.Color(51, 51, 51));
        topLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Computer Menu.png"))); // NOI18N
        topLabel.setText(" DIGITAL TECH SOLUTIONS");
        jPanel5.add(topLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 910, 90));

        Home.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-1, 0, 940, 90));

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
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 230, 60));

        Home.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 220, 550));

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setPreferredSize(new java.awt.Dimension(723, 596));
        jPanel4.setLayout(new java.awt.CardLayout());

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
            .addGap(0, 62, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
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
            .addGroup(pnlBerandaLayout.createSequentialGroup()
                .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBerandaLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 358, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBerandaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(welcomeText, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(afterWelcomeText)
                        .addGap(37, 37, 37)
                        .addGroup(pnlBerandaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBelanjaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRakitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(90, 90, 90)))
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.add(pnlBeranda, "card3");

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Jumlah Barang     :");

        jumlahBarangSpinner.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jumlahBarangSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 100, 1));

        javax.swing.GroupLayout pnlBelanjaLayout = new javax.swing.GroupLayout(pnlBelanja);
        pnlBelanja.setLayout(pnlBelanjaLayout);
        pnlBelanjaLayout.setHorizontalGroup(
            pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(rbMonitor)
                .addGap(108, 108, 108)
                .addComponent(rbKeyboard)
                .addGap(99, 99, 99)
                .addComponent(rbMotherboard)
                .addGap(102, 102, 102)
                .addComponent(rbCasing))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(rbMouse)
                .addGap(116, 116, 116)
                .addComponent(rbPSU)
                .addGap(141, 141, 141)
                .addComponent(rbCPU)
                .addGap(168, 168, 168)
                .addComponent(rbCooler))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(rbRAM)
                .addGap(132, 132, 132)
                .addComponent(rbHDD)
                .addGap(137, 137, 137)
                .addComponent(rbSSD)
                .addGap(170, 170, 170)
                .addComponent(rbVGA))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(540, 540, 540)
                .addComponent(backButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(fButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 709, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cbVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addGap(10, 10, 10)
                .addComponent(namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(hargaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jumlahBarangSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(520, 520, 520)
                .addComponent(fAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(fPayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlBelanjaLayout.setVerticalGroup(
            pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBelanjaLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbMonitor)
                    .addComponent(rbKeyboard)
                    .addComponent(rbMotherboard)
                    .addComponent(rbCasing))
                .addGap(11, 11, 11)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbMouse)
                    .addComponent(rbPSU)
                    .addComponent(rbCPU)
                    .addComponent(rbCooler))
                .addGap(6, 6, 6)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbRAM)
                    .addComponent(rbHDD)
                    .addComponent(rbSSD)
                    .addComponent(rbVGA))
                .addGap(27, 27, 27)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jLabel11)
                .addGap(6, 6, 6)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBelanjaLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel13))
                    .addComponent(cbVarian, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBelanjaLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel3))
                    .addComponent(namaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBelanjaLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel28))
                    .addComponent(hargaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBelanjaLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addComponent(jumlahBarangSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(137, 137, 137)
                .addGroup(pnlBelanjaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fPayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.add(pnlBelanja, "card4");

        pnlRakit.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlRakit.setPreferredSize(new java.awt.Dimension(719, 550));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Pilih Processor        :");
        jLabel6.setMaximumSize(new java.awt.Dimension(132, 16));
        jLabel6.setMinimumSize(new java.awt.Dimension(132, 16));
        jLabel6.setPreferredSize(new java.awt.Dimension(102, 14));

        jProcessorBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jProcessorBoxActionPerformed(evt);
            }
        });

        jLabel7.setText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Pilih Motherboard   :");
        jLabel8.setMaximumSize(new java.awt.Dimension(132, 16));
        jLabel8.setMinimumSize(new java.awt.Dimension(132, 16));
        jLabel8.setPreferredSize(new java.awt.Dimension(102, 14));

        jMotherboardBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMotherboardBoxActionPerformed(evt);
            }
        });

        jSpinnerProcessor.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));
        jSpinnerProcessor.setValue(1);

        jSpinnerMotherboard.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        namaCasing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaCasingActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Pilih RAM                 :");
        jLabel9.setPreferredSize(new java.awt.Dimension(102, 14));

        jRAMBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRAMBoxActionPerformed(evt);
            }
        });

        jSpinnerRAM.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        namaMotherboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaMotherboardActionPerformed(evt);
            }
        });

        jLabel16.setText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        jLabel17.setText("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setText("Pilih Casing              :");
        jLabel21.setPreferredSize(new java.awt.Dimension(102, 14));

        jCasingBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCasingBoxActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setText("Pilih Monitor            :");
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
        jLabel24.setText("Pilih Keyboard         :");
        jLabel24.setPreferredSize(new java.awt.Dimension(102, 14));

        jKeyboardBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jKeyboardBoxActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel25.setText("Pilih Mouse              :");
        jLabel25.setPreferredSize(new java.awt.Dimension(102, 14));

        jMouseBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMouseBoxActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setText("Pilih VGA                 :");
        jLabel15.setMaximumSize(new java.awt.Dimension(132, 16));
        jLabel15.setMinimumSize(new java.awt.Dimension(132, 16));
        jLabel15.setPreferredSize(new java.awt.Dimension(102, 14));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Pilih SSD                  :");
        jLabel18.setMaximumSize(new java.awt.Dimension(132, 16));
        jLabel18.setMinimumSize(new java.awt.Dimension(132, 16));
        jLabel18.setPreferredSize(new java.awt.Dimension(102, 14));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setText("Pilih HDD                 :");
        jLabel19.setMaximumSize(new java.awt.Dimension(132, 16));
        jLabel19.setMinimumSize(new java.awt.Dimension(132, 16));
        jLabel19.setPreferredSize(new java.awt.Dimension(102, 14));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setText("Pilih PSU                  :");
        jLabel20.setMaximumSize(new java.awt.Dimension(132, 16));
        jLabel20.setMinimumSize(new java.awt.Dimension(132, 16));
        jLabel20.setPreferredSize(new java.awt.Dimension(102, 14));

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

        namaRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaRAMActionPerformed(evt);
            }
        });

        jSpinnerSSD.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        jSpinnerHDD.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("Pilih Cooler FAN      :");
        jLabel26.setMaximumSize(new java.awt.Dimension(132, 16));
        jLabel26.setMinimumSize(new java.awt.Dimension(132, 16));
        jLabel26.setPreferredSize(new java.awt.Dimension(102, 14));

        jCoolerBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCoolerBoxActionPerformed(evt);
            }
        });

        jSpinnerCooler.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        hargaCasing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargaCasingActionPerformed(evt);
            }
        });

        hargaMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargaMonitorActionPerformed(evt);
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
            .addComponent(rakitLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
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
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1102, Short.MAX_VALUE)
            .addGroup(pnlRakitLayout.createSequentialGroup()
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 1092, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jCasingBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaCasing, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(hargaCasing, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jMonitorBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(hargaMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jKeyboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaKeyboard, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(hargaKeyboard, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jMouseBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaMouse, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(hargaMouse, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jProcessorBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namaProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hargaProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jMotherboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namaMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hargaRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRAMBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(namaRAM, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hargaMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jSSDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSpinnerSSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaSSD, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(hargaSSD, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jHDDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jSpinnerHDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaHDD, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(hargaHDD, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jPSUBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaPSU, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(hargaPSU, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jVGABox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(namaVGA, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(hargaVGA, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlRakitLayout.createSequentialGroup()
                                .addComponent(fBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlRakitLayout.createSequentialGroup()
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jCoolerBox, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jSpinnerCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(namaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hargaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlRakitLayout.setVerticalGroup(
            pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRakitLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCasingBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(namaCasing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(hargaCasing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jMonitorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(namaMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(hargaMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jKeyboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaKeyboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaKeyboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jMouseBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(namaMouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(hargaMouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jProcessorBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(namaProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hargaProcessor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jMotherboardBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(namaMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hargaRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jSpinnerRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jRAMBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(namaRAM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(hargaMotherboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSSDBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jSpinnerSSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(namaSSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(hargaSSD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jHDDBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerHDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaHDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hargaHDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCoolerBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinnerCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(namaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hargaCooler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(6, 6, 6)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPSUBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(namaPSU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(hargaPSU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jVGABox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(namaVGA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlRakitLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(hargaVGA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(pnlRakitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fBackButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        jPanel4.add(pnlRakit, "card3");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setText("Total Bayar   :");

        tableDetailProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Barang", "Kode Barang", "Varian", "Harga", "Jumlah"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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

        detailTransaksiText.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        detailTransaksiText.setText("Detail Transaksi");

        dateTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Tanggal :");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(detailTransaksiText, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(detailTransaksiText, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
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
                        .addComponent(jScrollPane2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPembayaranLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPembayaranLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPembayaranLayout.createSequentialGroup()
                                .addComponent(fButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addGroup(pnlPembayaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fCancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.add(pnlPembayaran, "card3");

        Home.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 723, 550));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Home, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        if (cleanData != true) {
            jumlahBarangSpinner.setValue(1);
            setTextField(cbVarian, namaBarang, hargaBarang);
        }
    }//GEN-LAST:event_cbVarianActionPerformed

    private void jBerandaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jBerandaMousePressed
        backToHome();
        cleanData = true;
        fButton2.setEnabled(true);
        if (inBelanja == true) {
            cleanData = true;
            clean(cbVarian, hargaBarang, namaBarang);
            rbEnable(true);
            jumlahBarangSpinner.setValue(1);
            inBelanja = false;
        }
        if (inRakit == true) {
            resetRakitPanel();
            inRakit = false;
        }
    }//GEN-LAST:event_jBerandaMousePressed

    private void jCoolerBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCoolerBoxActionPerformed
        data.setNama("Cooler");
        setChangedSpinner(jSpinnerCooler, jCoolerBox, namaCooler, hargaCooler);
    }//GEN-LAST:event_jCoolerBoxActionPerformed

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

    private void jRAMBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRAMBoxActionPerformed
        data.setNama("RAM");
        setChangedSpinner(jSpinnerRAM, jRAMBox, namaRAM, hargaRAM);
    }//GEN-LAST:event_jRAMBoxActionPerformed

    private void jMotherboardBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMotherboardBoxActionPerformed
        data.setNama("Motherboard");
        setChangedSpinner(jSpinnerMotherboard, jMotherboardBox, namaMotherboard, hargaMotherboard);

    }//GEN-LAST:event_jMotherboardBoxActionPerformed

    private void jProcessorBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jProcessorBoxActionPerformed
        data.setNama("CPU");
        setChangedSpinner(jSpinnerProcessor, jProcessorBox, namaProcessor, hargaProcessor);
    }//GEN-LAST:event_jProcessorBoxActionPerformed

    private void jBelanjaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBelanjaButtonActionPerformed
        setVisiblePanel(false, true, false, false);
        jumlahBarangSpinner.setEnabled(false);
        cbVarian.setEnabled(false);
        fAddButton.setEnabled(false);
        fPayButton.setEnabled(false);
    }//GEN-LAST:event_jBelanjaButtonActionPerformed

    private void jRakitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRakitButtonActionPerformed
        setVisiblePanel(false, false, true, false);
        addChoice2();
        inRakit = true;
        cleanData = false;
    }//GEN-LAST:event_jRakitButtonActionPerformed

    private void backButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButton2ActionPerformed
        backToHome();
        cleanData = true;
        clean(cbVarian, hargaBarang, namaBarang);
        rbEnable(true);
        jumlahBarangSpinner.setValue(1);

        fAddButton.setEnabled(false);
        buttonGroup1.clearSelection();
    }//GEN-LAST:event_backButton2ActionPerformed

    private void fPayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fPayButtonActionPerformed
        if (savedData.size() == 0) {
            JOptionPane.showMessageDialog(null, "Anda belum menambahkan barang", "WARNING", JOptionPane.OK_OPTION);
        } else {
            setVisiblePanel(false, false, false, true);
            totalPayment(totalBayar);
            addDataTable();
        }
    }//GEN-LAST:event_fPayButtonActionPerformed

    private void fAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fAddButtonActionPerformed
        if (cbVarian.getSelectedItem().toString() == "PILIH") {
            JOptionPane.showMessageDialog(null, "Varian Belum Dipilih", "WARNING", JOptionPane.OK_OPTION);
        } else {
            saveData(data.getNama(), cbVarian.getSelectedItem().toString(), namaBarang.getText().toString(), hargaBarang.getText().toString(), jumlahBarangSpinner.getValue().toString());
            cleanData = true;
            clean(cbVarian, hargaBarang, namaBarang);
            rbEnable(true);
            jumlahBarangSpinner.setValue(1);
            jumlahBarangSpinner.setEnabled(false);
            cbVarian.setEnabled(false);

            fAddButton.setEnabled(false);
        }
    }//GEN-LAST:event_fAddButtonActionPerformed

    private void fBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fBackButtonActionPerformed
        backToHome();
        cleanData = true;
        if (inRakit == true) {
            resetRakitPanel();
        }
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
        if (casingSelected == "PILIH" || monitorSelected == "PILIH" || keyboardSelected == "PILIH"
                || mouseSelected == "PILIH" || cpuSelected == "PILIH" || motherboardSelected == "PILIH"
                || ramSelected == "PILIH" || ssdSelected == "PILIH" || hddSelected == "PILIH"
                || coolerSelected == "PILIH" || psuSelected == "PILIH" || vgaSelected == "PILIH") {
            JOptionPane.showMessageDialog(null, "Data masih kosong", "WARNING", JOptionPane.OK_OPTION);
        } else {
            saveData("Casing", casingSelected, namaCasing.getText(), hargaCasing.getText(), "1");
            saveData("Monitor", monitorSelected, namaMonitor.getText(), hargaMonitor.getText(), "1");
            saveData("Keyboard", keyboardSelected, namaKeyboard.getText(), hargaKeyboard.getText(), "1");
            saveData("Mouse", mouseSelected, namaMouse.getText(), hargaMouse.getText(), "1");
            saveData("Processor", cpuSelected, namaProcessor.getText(), hargaProcessor.getText(), jSpinnerProcessor.getValue().toString());
            saveData("Motherboard", motherboardSelected, namaMotherboard.getText(), hargaMotherboard.getText(), jSpinnerMotherboard.getValue().toString());
            saveData("RAM", ramSelected, namaRAM.getText(), hargaRAM.getText(), jSpinnerProcessor.getValue().toString());
            saveData("SSD", ssdSelected, namaSSD.getText(), hargaSSD.getText(), jSpinnerSSD.getValue().toString());
            saveData("HDD", hddSelected, namaHDD.getText(), hargaHDD.getText(), jSpinnerHDD.getValue().toString());
            saveData("Cooler", coolerSelected, namaCooler.getText(), hargaCooler.getText(), jSpinnerCooler.getValue().toString());
            saveData("PSU", psuSelected, namaPSU.getText(), hargaPSU.getText(), "1");
            saveData("VGA", vgaSelected, namaVGA.getText(), hargaVGA.getText(), "1");

            addDataTable();
            totalPayment(totalBayar);
            cleanData = true;
            resetRakitPanel();
            setVisiblePanel(false, false, false, true);
        }
    }//GEN-LAST:event_fNextButtonActionPerformed

    private void fButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fButton2ActionPerformed
        fCancelButton.setEnabled(false);
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
        if (buttonGroup1.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Anda Belum Memilih Komponen", "WARNING", JOptionPane.OK_OPTION);
        } else {
            inBelanja = true;
            buttonGroup1.clearSelection();
            cleanData = false;
            addChoice();
            rbEnable(false);
            cbVarian.setEnabled(true);
            jumlahBarangSpinner.setEnabled(true);
            fAddButton.setEnabled(true);
            fPayButton.setEnabled(true);
        }
    }//GEN-LAST:event_fButton5ActionPerformed

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jPanel1MouseClicked

    private void namaCasingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaCasingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaCasingActionPerformed

    private void hargaCasingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaCasingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaCasingActionPerformed

    private void hargaMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargaMonitorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargaMonitorActionPerformed

    private void namaRAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaRAMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaRAMActionPerformed

    private void namaMotherboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaMotherboardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaMotherboardActionPerformed

    private void jPanel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jPanel5MousePressed

    private void jPanel5MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jPanel5MouseDragged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException ignore) {
            Toolkit.getDefaultToolkit().beep();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
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
    private javax.swing.JLabel detailTransaksiText;
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
    private javax.swing.JComboBox<String> jCoolerBox;
    private javax.swing.JComboBox<String> jHDDBox;
    private javax.swing.JComboBox<String> jKeyboardBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JSpinner jumlahBarangSpinner;
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
