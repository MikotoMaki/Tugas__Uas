/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tugas;

import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class Login extends javax.swing.JFrame {

    int xx,xy;
    private String realUsername = "komputer";
    private String realPassword = "123";
    private String usernameTextField;
    private String passwordTextField;
    
    public Login() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpanel = new javax.swing.JPanel();
        panel1 = new java.awt.Panel();
        PasswordTextField = new javax.swing.JPasswordField();
        UsernameTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        login = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        disable = new javax.swing.JLabel();
        show = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setPreferredSize(new java.awt.Dimension(830, 430));

        jpanel.setBackground(new java.awt.Color(255, 255, 255));
        jpanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jpanelMouseDragged(evt);
            }
        });
        jpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpanelMousePressed(evt);
            }
        });
        jpanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel1.setBackground(new java.awt.Color(204, 204, 255));
        panel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panel1MousePressed(evt);
            }
        });
        panel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panel1MouseDragged(evt);
            }
        });
        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PasswordTextField.setBackground(new java.awt.Color(204, 204, 255));
        PasswordTextField.setText("Password");
        PasswordTextField.setBorder(null);
        PasswordTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PasswordTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PasswordTextFieldFocusLost(evt);
            }
        });
        panel1.add(PasswordTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 240, 30));

        UsernameTextField.setBackground(new java.awt.Color(204, 204, 255));
        UsernameTextField.setText("Username");
        UsernameTextField.setBorder(null);
        UsernameTextField.setCaretColor(new java.awt.Color(255, 255, 255));
        UsernameTextField.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        UsernameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UsernameTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                UsernameTextFieldFocusLost(evt);
            }
        });
        panel1.add(UsernameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 240, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 32)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Login");
        panel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 400, 41));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("________________________");
        panel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, 30));

        login.setBackground(new java.awt.Color(204, 204, 255));
        login.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        login.setText("LOGIN");
        login.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        panel1.add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 270, 40));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel10.setText("________________________");
        panel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 220, -1, 30));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_Male_User_35px_1.png"))); // NOI18N
        panel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 40, 40));

        disable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        disable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_invisible_20px_2.png"))); // NOI18N
        disable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        disable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                disableMouseClicked(evt);
            }
        });
        panel1.add(disable, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 40, 40));

        show.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        show.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/icons8_eye_20px_1.png"))); // NOI18N
        show.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        show.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMouseClicked(evt);
            }
        });
        panel1.add(show, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 40, 40));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Selamat Datang");
        panel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 400, 20));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("X");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        panel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 30, 30));

        jpanel.add(panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 400, 430));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Computer Login.png"))); // NOI18N
        jpanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 360, 380));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void PasswordTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PasswordTextFieldFocusGained
        String pass = PasswordTextField.getText();
        if (pass.equals("Password")){
            PasswordTextField.setText("");
        }
    }//GEN-LAST:event_PasswordTextFieldFocusGained

    private void PasswordTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PasswordTextFieldFocusLost
        String pass = PasswordTextField.getText();
        if (pass.equals("")||pass.equals("Password")){
            PasswordTextField.setText("Password");
        }
    }//GEN-LAST:event_PasswordTextFieldFocusLost

    private void UsernameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UsernameTextFieldFocusGained
        String user = UsernameTextField.getText();
        if (user.equals("Username")){
            UsernameTextField.setText("");
        }
    }//GEN-LAST:event_UsernameTextFieldFocusGained

    private void UsernameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UsernameTextFieldFocusLost
        String user = UsernameTextField.getText();
        if (user.equals("")||user.equals("Username")){
            UsernameTextField.setText("Username");
        }
    }//GEN-LAST:event_UsernameTextFieldFocusLost

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed
        usernameTextField = UsernameTextField.getText();
        passwordTextField = PasswordTextField.getText();

        if(usernameTextField.equals(realUsername) && passwordTextField.equals(realPassword)){
            new main().setVisible(true);
            this.setVisible(false);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Username atau password salah", "Notification", JOptionPane.OK_OPTION);
        }
    }//GEN-LAST:event_loginActionPerformed

    private void disableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_disableMouseClicked
        PasswordTextField.setEchoChar((char)0);
        disable.setVisible(false);
        disable.setEnabled(false);
        show.setEnabled(true);
        show.setEnabled(true);
    }//GEN-LAST:event_disableMouseClicked

    private void showMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showMouseClicked
        PasswordTextField.setEchoChar((char)8226);
        disable.setVisible(true);
        disable.setEnabled(true);
        show.setEnabled(false);
        show.setEnabled(false);
    }//GEN-LAST:event_showMouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        System.exit(0);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void panel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel1MousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_panel1MousePressed

    private void panel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panel1MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_panel1MouseDragged

    private void jpanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpanelMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x - xx, y - xy);
    }//GEN-LAST:event_jpanelMouseDragged

    private void jpanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpanelMousePressed
        xx = evt.getX();
        xy = evt.getY();
    }//GEN-LAST:event_jpanelMousePressed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField PasswordTextField;
    private javax.swing.JTextField UsernameTextField;
    private javax.swing.JLabel disable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jpanel;
    private javax.swing.JButton login;
    private java.awt.Panel panel1;
    private javax.swing.JLabel show;
    // End of variables declaration//GEN-END:variables
}
