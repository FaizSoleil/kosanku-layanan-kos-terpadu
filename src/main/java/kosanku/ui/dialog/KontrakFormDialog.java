package kosanku.ui.dialog;

import java.awt.Frame;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import kosanku.dao.RoomDAO;
import kosanku.dao.TenantDAO;
import kosanku.model.Contract;
import kosanku.model.ContractStatus;
import kosanku.model.Room;
import kosanku.model.Tenant;

public class KontrakFormDialog extends javax.swing.JDialog {

    public enum KontrakDialogMode {
        ADD,
        EDIT
    }

    private boolean saved;
    private final KontrakDialogMode mode;
    private final Contract contractBeingEdited;

    public KontrakFormDialog(Frame owner, boolean modal) {
        this(owner, modal, KontrakDialogMode.ADD, null);
    }

    public KontrakFormDialog(Frame owner, boolean modal,
                             KontrakDialogMode mode) {
        this(owner, modal, mode, null);
    }

    public KontrakFormDialog(Frame owner, boolean modal,
                             KontrakDialogMode mode,
                             Contract contract) {
        super(owner, modal);
        this.mode = mode;
        this.contractBeingEdited = contract;
        initComponents();
        loadComboBoxes();

        labelTitle.setText(mode == KontrakDialogMode.ADD
            ? "TAMBAH KONTRAK"
            : "EDIT KONTRAK");
        btnMain.setText(mode == KontrakDialogMode.ADD ? "Tambah" : "Simpan");

        if (mode == KontrakDialogMode.EDIT && contract != null) {
            selectTenant(contract.getTenantId());
            selectRoom(contract.getRoomId());
            fieldTanggalMasuk.setText(formatDate(contract.getTanggalMasuk()));
            fieldTanggalKeluar.setText(formatDate(contract.getTanggalKeluar()));
            fieldTanggalJatuhTempo.setText(formatDate(contract.getTanggalJatuhTempo()));
            fieldDeposit.setText(BigDecimal.valueOf(contract.getDeposit()).toPlainString());
            cmbStatus.setSelectedItem(contract.getStatus());
        } else {
            fieldTanggalMasuk.setText(LocalDate.now().toString());
            cmbStatus.setSelectedItem(ContractStatus.AKTIF);
        }

        getRootPane().setDefaultButton(btnMain);
        setLocationRelativeTo(owner);
    }

    private void loadComboBoxes() {
        List<Tenant> tenants = new TenantDAO().findAll();
        cmbTenant.setModel(new javax.swing.DefaultComboBoxModel<Tenant>(
            tenants.toArray(new Tenant[0])));

        List<Room> rooms = new RoomDAO().findAll();
        cmbRoom.setModel(new javax.swing.DefaultComboBoxModel<Room>(
            rooms.toArray(new Room[0])));
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<ContractStatus>(
            ContractStatus.values()));
    }

    private void selectTenant(int tenantId) {
        for (int i = 0; i < cmbTenant.getItemCount(); i++) {
            if (cmbTenant.getItemAt(i).getId() == tenantId) {
                cmbTenant.setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectRoom(int roomId) {
        for (int i = 0; i < cmbRoom.getItemCount(); i++) {
            if (cmbRoom.getItemAt(i).getId() == roomId) {
                cmbRoom.setSelectedIndex(i);
                break;
            }
        }
    }

    private String formatDate(java.util.Date date) {
        return date == null
            ? ""
            : new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void save() {
        if (cmbTenant.getSelectedItem() == null) {
            showValidationError("Penyewa wajib dipilih.");
            return;
        }
        if (cmbRoom.getSelectedItem() == null) {
            showValidationError("Kamar wajib dipilih.");
            return;
        }
        if (fieldTanggalMasuk.getText().trim().isEmpty()) {
            showValidationError("Tanggal masuk wajib diisi.");
            return;
        }
        if (fieldTanggalKeluar.getText().trim().isEmpty()) {
            showValidationError("Tanggal keluar wajib diisi.");
            return;
        }
        if (fieldDeposit.getText().trim().isEmpty()) {
            showValidationError("Deposit wajib diisi.");
            return;
        }
        if (fieldTanggalJatuhTempo.getText().trim().isEmpty()) {
            showValidationError("Tanggal jatuh tempo wajib diisi.");
            return;
        }
        if (cmbStatus.getSelectedItem() == null) {
            showValidationError("Status kontrak wajib dipilih.");
            return;
        }

        saved = true;
        dispose();
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Data belum lengkap",
            JOptionPane.WARNING_MESSAGE
        );
    }

    public boolean isSaved() {
        return saved;
    }

    public String getSavedTenantId() {
        Tenant tenant = (Tenant) cmbTenant.getSelectedItem();
        return tenant == null ? "" : String.valueOf(tenant.getId());
    }

    public String getSavedRoomId() {
        Room room = (Room) cmbRoom.getSelectedItem();
        return room == null ? "" : String.valueOf(room.getId());
    }

    public String getSavedTanggalMasuk() {
        return fieldTanggalMasuk.getText();
    }

    public String getSavedTanggalKeluar() {
        return fieldTanggalKeluar.getText();
    }

    public String getSavedDeposit() {
        return fieldDeposit.getText();
    }

    public String getSavedTanggalJatuhTempo() {
        return fieldTanggalJatuhTempo.getText();
    }

    public ContractStatus getSavedStatus() {
        return (ContractStatus) cmbStatus.getSelectedItem();
    }

    public Contract getContractBeingEdited() {
        return contractBeingEdited;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        labelTitle = new javax.swing.JLabel();
        labelTenant = new javax.swing.JLabel();
        labelRoom = new javax.swing.JLabel();
        labelTanggalMasuk = new javax.swing.JLabel();
        labelTanggalKeluar = new javax.swing.JLabel();
        labelDeposit = new javax.swing.JLabel();
        labelTanggalJatuhTempo = new javax.swing.JLabel();
        labelStatus = new javax.swing.JLabel();
        cmbTenant = new javax.swing.JComboBox<>();
        cmbRoom = new javax.swing.JComboBox<>();
        cmbStatus = new javax.swing.JComboBox<>();
        fieldTanggalMasuk = new javax.swing.JTextField();
        fieldTanggalKeluar = new javax.swing.JTextField();
        fieldDeposit = new javax.swing.JTextField();
        fieldTanggalJatuhTempo = new javax.swing.JTextField();
        btnMain = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelTitle.setFont(new java.awt.Font("Arial", 1, 18));
        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle.setText("TAMBAH KONTRAK");

        labelTenant.setFont(new java.awt.Font("Arial", 0, 14));
        labelTenant.setText("Penyewa");
        labelRoom.setFont(new java.awt.Font("Arial", 0, 14));
        labelRoom.setText("Kamar");
        labelTanggalMasuk.setFont(new java.awt.Font("Arial", 0, 14));
        labelTanggalMasuk.setText("Tanggal Masuk");
        labelTanggalKeluar.setFont(new java.awt.Font("Arial", 0, 14));
        labelTanggalKeluar.setText("Tanggal Keluar");
        labelDeposit.setFont(new java.awt.Font("Arial", 0, 14));
        labelDeposit.setText("Deposit");
        labelTanggalJatuhTempo.setFont(new java.awt.Font("Arial", 0, 14));
        labelTanggalJatuhTempo.setText("Jatuh Tempo");
        labelStatus.setFont(new java.awt.Font("Arial", 0, 14));
        labelStatus.setText("Status");

        btnMain.setFont(new java.awt.Font("Arial", 0, 14));
        btnMain.setText("Tambah");
        btnMain.addActionListener(this::btnMainActionPerformed);

        btnBack.setFont(new java.awt.Font("Arial", 0, 14));
        btnBack.setText("Kembali");
        btnBack.addActionListener(this::btnBackActionPerformed);

        javax.swing.GroupLayout layout =
            new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(labelTitle, javax.swing.GroupLayout.DEFAULT_SIZE,
                              javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnMain)
                            .addPreferredGap(
                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .addComponent(btnBack))
                        .addComponent(labelTenant)
                        .addComponent(cmbTenant,
                                      javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                      javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelRoom)
                        .addComponent(cmbRoom,
                                      javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                      javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelTanggalMasuk)
                        .addComponent(fieldTanggalMasuk,
                                      javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                      javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelTanggalKeluar)
                        .addComponent(fieldTanggalKeluar,
                                      javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                      javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelDeposit)
                        .addComponent(fieldDeposit,
                                      javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                      javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelTanggalJatuhTempo)
                        .addComponent(fieldTanggalJatuhTempo,
                                      javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                      javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(labelStatus)
                        .addComponent(cmbStatus,
                                      javax.swing.GroupLayout.PREFERRED_SIZE, 250,
                                      javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(30, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(labelTitle)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(labelTenant)
                    .addGap(4, 4, 4)
                    .addComponent(cmbTenant, javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(labelRoom)
                    .addGap(4, 4, 4)
                    .addComponent(cmbRoom, javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(labelTanggalMasuk)
                    .addGap(4, 4, 4)
                    .addComponent(fieldTanggalMasuk,
                                  javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(labelTanggalKeluar)
                    .addGap(4, 4, 4)
                    .addComponent(fieldTanggalKeluar,
                                  javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(labelDeposit)
                    .addGap(4, 4, 4)
                    .addComponent(fieldDeposit,
                                  javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(labelTanggalJatuhTempo)
                    .addGap(4, 4, 4)
                    .addComponent(fieldTanggalJatuhTempo,
                                  javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(8, 8, 8)
                    .addComponent(labelStatus)
                    .addGap(4, 4, 4)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE,
                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                  javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(
                        javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnMain)
                        .addComponent(btnBack))
                    .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMainActionPerformed(java.awt.event.ActionEvent event) {
        save();
    }

    private void btnBackActionPerformed(java.awt.event.ActionEvent event) {
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnMain;
    private javax.swing.JComboBox<Tenant> cmbTenant;
    private javax.swing.JComboBox<Room> cmbRoom;
    private javax.swing.JComboBox<ContractStatus> cmbStatus;
    private javax.swing.JTextField fieldTanggalMasuk;
    private javax.swing.JTextField fieldTanggalKeluar;
    private javax.swing.JTextField fieldDeposit;
    private javax.swing.JTextField fieldTanggalJatuhTempo;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JLabel labelTenant;
    private javax.swing.JLabel labelRoom;
    private javax.swing.JLabel labelTanggalMasuk;
    private javax.swing.JLabel labelTanggalKeluar;
    private javax.swing.JLabel labelDeposit;
    private javax.swing.JLabel labelTanggalJatuhTempo;
    private javax.swing.JLabel labelStatus;
    // End of variables declaration//GEN-END:variables
}
