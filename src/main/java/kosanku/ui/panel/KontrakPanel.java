package kosanku.ui.panel;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import kosanku.controller.ContractController;
import kosanku.dao.ContractDAO;
import kosanku.model.Contract;
import kosanku.model.ContractStatus;
import kosanku.service.ContractService;
import kosanku.ui.dialog.KontrakFormDialog;
import kosanku.ui.dialog.KontrakFormDialog.KontrakDialogMode;

public class KontrakPanel extends javax.swing.JPanel {

    private static final String[] COLUMNS = {
        "ID", "ID Penyewa", "ID Kamar", "Masuk", "Keluar", "Deposit", "Jatuh Tempo", "Status"
    };

    private final ContractController contractController;

    // Row i of the table <-> contracts.get(i). Always updated together in renderTable().
    private List<Contract> contracts = new ArrayList<>();

    public KontrakPanel() {
        this(new ContractController(new ContractService(new ContractDAO())));
    }

    public KontrakPanel(ContractController contractController) {
        this.contractController = contractController;
        initComponents();
        setupTable();
        setupEndButton();
        renderTable();
    }

    // ---------- setup ----------
    private void setupTable() {
        tableContract.setModel(new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tableContract.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupEndButton() {
        btnEnd.setEnabled(false);
        tableContract.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateEndButtonState();
        });
    }

    private void updateEndButtonState() {
        Contract selected = getSelectedContract();
        btnEnd.setEnabled(selected != null && selected.getStatus() == ContractStatus.AKTIF);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableContract = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        labelTitle = new javax.swing.JLabel();
        btnEnd = new javax.swing.JButton();

        setBackground(new java.awt.Color(153, 153, 153));
        setPreferredSize(new java.awt.Dimension(540, 380));

        tableContract.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "ID Penyewa", "ID Kamar", "Masuk", "Keluar", "Deposit", "Jatuh Tempo", "Status"
            }
        ));
        jScrollPane1.setViewportView(tableContract);

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(this::btnTambahActionPerformed);

        btnEdit.setText("Edit");
        btnEdit.addActionListener(this::btnEditActionPerformed);

        btnHapus.setBackground(new java.awt.Color(210, 0, 0));
        btnHapus.setForeground(new java.awt.Color(220, 220, 220));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(this::btnHapusActionPerformed);

        labelTitle.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        labelTitle.setText("KONTRAK");

        btnEnd.setText("Akhiri");
        btnEnd.addActionListener(this::btnEndActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEnd)
                        .addGap(181, 181, 181)
                        .addComponent(btnHapus)))
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelTitle)
                .addGap(200, 200, 200))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(labelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus)
                    .addComponent(btnEnd))
                .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent event) {//GEN-FIRST:event_btnTambahActionPerformed
        onAdd();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent event) {//GEN-FIRST:event_btnEditActionPerformed
        onEdit();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent event) {//GEN-FIRST:event_btnHapusActionPerformed
        onDelete();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndActionPerformed
        onEnd();
    }//GEN-LAST:event_btnEndActionPerformed

    // ---------- actions ----------
    private void onAdd() {
        KontrakFormDialog dialog = new KontrakFormDialog(owner(), true, KontrakDialogMode.ADD);
        dialog.setVisible(true);
        if (!dialog.isSaved()) return;

        runAndRefresh(() -> contractController.create(
            dialog.getSavedTenantId(),
            dialog.getSavedRoomId(),
            dialog.getSavedTanggalMasuk(),
            dialog.getSavedTanggalKeluar(),
            dialog.getSavedDeposit(),
            dialog.getSavedTanggalJatuhTempo(),
            dialog.getSavedStatus()
        ));
    }

    private void onEdit() {
        Contract selected = requireSelectedContract();
        if (selected == null) return;

        KontrakFormDialog dialog = new KontrakFormDialog(owner(), true, KontrakDialogMode.EDIT, selected);
        dialog.setVisible(true);
        if (!dialog.isSaved()) return;

        runAndRefresh(() -> contractController.edit(
            selected.getId(),
            dialog.getSavedTenantId(),
            dialog.getSavedRoomId(),
            dialog.getSavedTanggalMasuk(),
            dialog.getSavedTanggalKeluar(),
            dialog.getSavedDeposit(),
            dialog.getSavedTanggalJatuhTempo(),
            dialog.getSavedStatus()
        ));
    }

    private void onDelete() {
        Contract selected = requireSelectedContract();
        if (selected == null) return;

        int choice = JOptionPane.showConfirmDialog(this, "Hapus kontrak ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) return;

        runAndRefresh(() -> contractController.delete(selected.getId()));
    }

    private void onEnd() {
        Contract selected = requireSelectedContract();
        if (selected == null) return;

        Object[] options = {"Selesai", "Dibatalkan", "Batal"};
        int choice = JOptionPane.showOptionDialog(this,
            "Akhiri kontrak #" + selected.getId() + " dengan status apa?\nKamar akan dikosongkan.",
            "Akhiri Kontrak",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);
        if (choice != 0 && choice != 1) return; // "Batal" or dialog closed

        ContractStatus finalStatus =
            (choice == 0) ? ContractStatus.SELESAI : ContractStatus.DIBATALKAN;
        runAndRefresh(() -> contractController.end(selected.getId(), finalStatus));
    }

    // ---------- helpers ----------
    private Frame owner() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    /** The contract in the selected row, or null if nothing is selected. */
    private Contract getSelectedContract() {
        int row = tableContract.getSelectedRow();
        if (row < 0) return null;
        int modelRow = tableContract.convertRowIndexToModel(row);
        return modelRow < contracts.size() ? contracts.get(modelRow) : null;
    }

    /** Like getSelectedContract(), but tells the user when nothing is selected. */
    private Contract requireSelectedContract() {
        Contract selected = getSelectedContract();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Pilih kontrak dulu.");
        }
        return selected;
    }

    /** Runs a controller call, reloads the table, and reports errors in one place. */
    private void runAndRefresh(Runnable action) {
        try {
            action.run();
            renderTable();
        } catch (IllegalArgumentException | IllegalStateException e) {
            // rule violations from the service, e.g. "Kamar sedang terisi."
            JOptionPane.showMessageDialog(this, e.getMessage(), "Gagal", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan tak terduga.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderTable() {
        try {
            List<Contract> loaded = contractController.loadAll();
            DefaultTableModel model = (DefaultTableModel) tableContract.getModel();
            model.setRowCount(0);
            contracts = loaded; // right after the clear, so rows and list stay aligned

            for (Contract c : loaded) {
                model.addRow(new Object[]{
                    c.getId(), c.getTenantId(), c.getRoomId(),
                    c.getTanggalMasuk(), c.getTanggalKeluar(),
                    c.getDeposit(), c.getTanggalJatuhTempo(), c.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Terjadi kesalahan", JOptionPane.ERROR_MESSAGE);
        }
        updateEndButtonState(); // don't rely on the selection event firing after a reload
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEnd;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTable tableContract;
    // End of variables declaration//GEN-END:variables
}
