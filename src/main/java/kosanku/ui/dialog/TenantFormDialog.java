package kosanku.ui.dialog;

import java.awt.Frame;
import javax.swing.JOptionPane;
import kosanku.model.Tenant;

public class TenantFormDialog extends javax.swing.JDialog {
    private boolean saved;
    private final Tenant tenantBeingEdited;

    public TenantFormDialog(Frame parent, boolean modal) {
        this(parent, modal, null);
    }

    public TenantFormDialog(Frame parent, boolean modal, Tenant tenant) {
        super(parent, modal);
        tenantBeingEdited = tenant;
        initComponents();
        if (tenant != null) {
            fieldIdNumber.setText(tenant.getIdNumber());
            fieldName.setText(tenant.getName());
            fieldEmail.setText(tenant.getEmail() == null ? "" : tenant.getEmail());
            fieldPhoneNumber.setText(tenant.getPhoneNumber());
            labelTitle.setText("EDIT PENGHUNI");
            btnMain.setText("Simpan");
        }
        getRootPane().setDefaultButton(btnMain);
        setLocationRelativeTo(parent);
    }

    public boolean isSaved() {
        return saved;
    }

    public Tenant getTenant() {
        if (!saved) return null;
        Tenant tenant = new Tenant(
            fieldIdNumber.getText(),
            fieldName.getText(),
            fieldEmail.getText(),
            fieldPhoneNumber.getText()
        );
        if (tenantBeingEdited != null) {
            tenant.setId(tenantBeingEdited.getId());
        }
        return tenant;
    }

    private void save() {
        if (fieldIdNumber.getText().trim().isEmpty()) {
            showValidationError("NIK wajib diisi.");
            return;
        }
        if (fieldName.getText().trim().isEmpty()) {
            showValidationError("Nama wajib diisi.");
            return;
        }
        if (fieldPhoneNumber.getText().trim().isEmpty()) {
            showValidationError("Nomor telepon wajib diisi.");
            return;
        }
        saved = true;
        dispose();
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
            this, message, "Data belum lengkap", JOptionPane.WARNING_MESSAGE
        );
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        labelIdNumber = new javax.swing.JLabel();
        labelName = new javax.swing.JLabel();
        labelEmail = new javax.swing.JLabel();
        labelPhoneNumber = new javax.swing.JLabel();
        fieldIdNumber = new javax.swing.JTextField();
        fieldName = new javax.swing.JTextField();
        fieldEmail = new javax.swing.JTextField();
        fieldPhoneNumber = new javax.swing.JTextField();
        btnMain = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle.setText("TAMBAH PENGHUNI");
        labelTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        labelIdNumber.setText("NIK");
        labelIdNumber.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        labelName.setText("Nama");
        labelName.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        labelEmail.setText("Email");
        labelEmail.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        labelPhoneNumber.setText("No. Telepon");
        labelPhoneNumber.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        btnMain.setText("Tambah");
        btnMain.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnMain.addActionListener(this::btnMainActionPerformed);

        btnBack.setText("Kembali");
        btnBack.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        btnBack.addActionListener(this::btnBackActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack))
                    .addComponent(labelIdNumber)
                    .addComponent(fieldIdNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelName)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelEmail)
                    .addComponent(fieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPhoneNumber)
                    .addComponent(fieldPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(labelTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(labelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelIdNumber)
                .addGap(4, 4, 4)
                .addComponent(fieldIdNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelName)
                .addGap(4, 4, 4)
                .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelEmail)
                .addGap(4, 4, 4)
                .addComponent(fieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelPhoneNumber)
                .addGap(4, 4, 4)
                .addComponent(fieldPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMain)
                    .addComponent(btnBack))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainActionPerformed
        save();
    }//GEN-LAST:event_btnMainActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnMain;
    private javax.swing.JTextField fieldEmail;
    private javax.swing.JTextField fieldIdNumber;
    private javax.swing.JTextField fieldName;
    private javax.swing.JTextField fieldPhoneNumber;
    private javax.swing.JLabel labelEmail;
    private javax.swing.JLabel labelIdNumber;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelPhoneNumber;
    private javax.swing.JLabel labelTitle;
    // End of variables declaration//GEN-END:variables
}
