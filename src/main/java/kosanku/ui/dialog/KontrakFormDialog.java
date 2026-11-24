package kosanku.ui.dialog;

import java.awt.Frame;
import com.github.lgooddatepicker.components.DatePicker;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    public KontrakFormDialog(Frame owner, boolean modal, KontrakDialogMode mode, Contract contract) {
        super(owner, modal);
        this.mode = mode;
        this.contractBeingEdited = contract;
        initComponents();
        loadComboBoxes();
        setTitle(mode == KontrakDialogMode.ADD ? "Tambah Kontrak" : "Edit Kontrak");
        btnMain.setText(mode == KontrakDialogMode.ADD ? "Tambah" : "Simpan");
        labelTitle.setText(mode == KontrakDialogMode.ADD ? "TAMBAH KONTRAK" : "EDIT KONTRAK");
        btnMain.setText(mode == KontrakDialogMode.ADD ? "Tambah" : "Simpan");

        if (mode == KontrakDialogMode.EDIT && contract != null) {
            selectTenant(contract.getTenantId());
            selectRoom(contract.getRoomId());
            dateTanggalMasuk.setDate(toLocalDate(contract.getTanggalMasuk()));
            dateTanggalKeluar.setDate(toLocalDate(contract.getTanggalKeluar()));
            dateTanggalJatuhTempo.setDate(toLocalDate(contract.getTanggalJatuhTempo()));
            fieldDeposit.setText(BigDecimal.valueOf(contract.getDeposit()).toPlainString());
            cmbStatus.setSelectedItem(contract.getStatus());
        } else {
            dateTanggalMasuk.setDate(LocalDate.now());
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

    private LocalDate toLocalDate(java.util.Date date) {
        return date == null ? null : new java.sql.Date(date.getTime()).toLocalDate();
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
        if (dateTanggalMasuk.getDate() == null) {
            showValidationError("Tanggal masuk wajib diisi.");
            return;
        }
        if (dateTanggalKeluar.getDate() == null) {
            showValidationError("Tanggal keluar wajib diisi.");
            return;
        }
        if (fieldDeposit.getText().trim().isEmpty()) {
            showValidationError("Deposit wajib diisi.");
            return;
        }
        if (dateTanggalJatuhTempo.getDate() == null) {
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
        return dateTanggalMasuk.getDate() == null
            ? "" : dateTanggalMasuk.getDate().toString();
    }

    public String getSavedTanggalKeluar() {
        return dateTanggalKeluar.getDate() == null
            ? "" : dateTanggalKeluar.getDate().toString();
    }

    public String getSavedDeposit() {
        return fieldDeposit.getText();
    }

    public String getSavedTanggalJatuhTempo() {
        return dateTanggalJatuhTempo.getDate() == null
            ? "" : dateTanggalJatuhTempo.getDate().toString();
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
        cmbTenant = new javax.swing.JComboBox<>();
        labelRoom = new javax.swing.JLabel();
        cmbRoom = new javax.swing.JComboBox<>();
        labelTanggalMasuk = new javax.swing.JLabel();
        dateTanggalMasuk = new com.github.lgooddatepicker.components.DatePicker();
        labelTanggalKeluar = new javax.swing.JLabel();
        dateTanggalKeluar = new com.github.lgooddatepicker.components.DatePicker();
        labelDeposit = new javax.swing.JLabel();
        fieldDeposit = new javax.swing.JTextField();
        labelTanggalJatuhTempo = new javax.swing.JLabel();
        dateTanggalJatuhTempo = new com.github.lgooddatepicker.components.DatePicker();
        labelStatus = new javax.swing.JLabel();
        cmbStatus = new javax.swing.JComboBox<>();
        btnMain = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labelTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitle.setText("TAMBAH KONTRAK");
        labelTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        labelTenant.setText("Penyewa");

        labelRoom.setText("Kamar");

        labelTanggalMasuk.setText("Tanggal Masuk");

        labelTanggalKeluar.setText("Tanggal Keluar");

        labelDeposit.setText("Deposit");

        labelTanggalJatuhTempo.setText("Jatuh Tempo");

        labelStatus.setText("Status");

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
            .addComponent(labelTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbRoom, 0, 250, Short.MAX_VALUE)
                    .addComponent(dateTanggalMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(dateTanggalKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(fieldDeposit, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(dateTanggalJatuhTempo, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(cmbStatus, 0, 250, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelTenant)
                            .addComponent(labelRoom)
                            .addComponent(labelTanggalMasuk)
                            .addComponent(labelTanggalKeluar)
                            .addComponent(labelDeposit)
                            .addComponent(labelTanggalJatuhTempo)
                            .addComponent(labelStatus))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack))
                    .addComponent(cmbTenant, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(labelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelTenant)
                .addGap(4, 4, 4)
                .addComponent(cmbTenant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelRoom)
                .addGap(4, 4, 4)
                .addComponent(cmbRoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelTanggalMasuk)
                .addGap(4, 4, 4)
                .addComponent(dateTanggalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelTanggalKeluar)
                .addGap(4, 4, 4)
                .addComponent(dateTanggalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelDeposit)
                .addGap(4, 4, 4)
                .addComponent(fieldDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelTanggalJatuhTempo)
                .addGap(4, 4, 4)
                .addComponent(dateTanggalJatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(labelStatus)
                .addGap(4, 4, 4)
                .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMain)
                    .addComponent(btnBack))
                .addGap(30, 30, 30))
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
    private javax.swing.JComboBox<Room> cmbRoom;
    private javax.swing.JComboBox<ContractStatus> cmbStatus;
    private javax.swing.JComboBox<Tenant> cmbTenant;
    private com.github.lgooddatepicker.components.DatePicker dateTanggalJatuhTempo;
    private com.github.lgooddatepicker.components.DatePicker dateTanggalKeluar;
    private com.github.lgooddatepicker.components.DatePicker dateTanggalMasuk;
    private javax.swing.JTextField fieldDeposit;
    private javax.swing.JLabel labelDeposit;
    private javax.swing.JLabel labelRoom;
    private javax.swing.JLabel labelStatus;
    private javax.swing.JLabel labelTanggalJatuhTempo;
    private javax.swing.JLabel labelTanggalKeluar;
    private javax.swing.JLabel labelTanggalMasuk;
    private javax.swing.JLabel labelTenant;
    private javax.swing.JLabel labelTitle;
    // End of variables declaration//GEN-END:variables
}
