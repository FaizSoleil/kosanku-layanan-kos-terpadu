package kosanku.ui.panel;

import java.awt.Frame;
import java.util.List;
import javax.swing.JOptionPane;
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

    private final ContractController contractController;

    public KontrakPanel() {
        this(new ContractController(new ContractService(new ContractDAO())));
    }

    public KontrakPanel(ContractController contractController) {
        this.contractController = contractController;
        initComponents();

        tableContract.setModel(new DefaultTableModel(
            new Object[]{"ID", "ID Penyewa", "ID Kamar", "Masuk",
                         "Keluar", "Deposit", "Jatuh Tempo", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        renderTable();
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
                "ID", "ID Penyewa", "ID Kamar", "Masuk", "Keluar",
                "Deposit", "Jatuh Tempo", "Status"
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(31, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(208, 208, 208)
                            .addComponent(btnHapus)))
                    .addContainerGap(31, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(210, 210, 210)
                    .addComponent(labelTitle)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(27, 27, 27)
                    .addComponent(labelTitle)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah)
                        .addComponent(btnEdit)
                        .addComponent(btnHapus))
                    .addContainerGap(25, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent event) {//GEN-FIRST:event_btnTambahActionPerformed
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        KontrakFormDialog dialog =
            new KontrakFormDialog(owner, true, KontrakDialogMode.ADD);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                contractController.create(
                    dialog.getSavedTenantId(),
                    dialog.getSavedRoomId(),
                    dialog.getSavedTanggalMasuk(),
                    dialog.getSavedTanggalKeluar(),
                    dialog.getSavedDeposit(),
                    dialog.getSavedTanggalJatuhTempo(),
                    dialog.getSavedStatus()
                );
                renderTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent event) {//GEN-FIRST:event_btnEditActionPerformed
        int row = tableContract.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kontrak dulu.");
            return;
        }

        Contract selectedContract = selectedRowToContract(row);
        Frame owner = (Frame) SwingUtilities.getWindowAncestor(this);
        KontrakFormDialog dialog =
            new KontrakFormDialog(owner, true, KontrakDialogMode.EDIT, selectedContract);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            try {
                contractController.edit(
                    selectedContract.getId(),
                    dialog.getSavedTenantId(),
                    dialog.getSavedRoomId(),
                    dialog.getSavedTanggalMasuk(),
                    dialog.getSavedTanggalKeluar(),
                    dialog.getSavedDeposit(),
                    dialog.getSavedTanggalJatuhTempo(),
                    dialog.getSavedStatus()
                );
                renderTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent event) {//GEN-FIRST:event_btnHapusActionPerformed
        int row = tableContract.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kontrak dulu.");
            return;
        }//GEN-LAST:event_btnHapusActionPerformed

        int choice = JOptionPane.showConfirmDialog(
            this,
            "Hapus kontrak ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int modelRow = tableContract.convertRowIndexToModel(row);
            int id = ((Number) tableContract.getModel().getValueAt(modelRow, 0)).intValue();
            contractController.delete(id);
            renderTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private Contract selectedRowToContract(int selectedRow) {
        int modelRow = tableContract.convertRowIndexToModel(selectedRow);
        DefaultTableModel model = (DefaultTableModel) tableContract.getModel();

        Contract contract = new Contract();
        contract.setId(((Number) model.getValueAt(modelRow, 0)).intValue());
        contract.setTenantId(((Number) model.getValueAt(modelRow, 1)).intValue());
        contract.setRoomId(((Number) model.getValueAt(modelRow, 2)).intValue());
        contract.setTanggalMasuk((java.util.Date) model.getValueAt(modelRow, 3));
        contract.setTanggalKeluar((java.util.Date) model.getValueAt(modelRow, 4));
        contract.setDeposit(((Number) model.getValueAt(modelRow, 5)).doubleValue());
        contract.setTanggalJatuhTempo((java.util.Date) model.getValueAt(modelRow, 6));
        contract.setStatus((ContractStatus) model.getValueAt(modelRow, 7));
        return contract;
    }

    private void renderTable() {
        try {
            List<Contract> contracts = contractController.loadAll();
            DefaultTableModel model = (DefaultTableModel) tableContract.getModel();
            model.setRowCount(0);

            for (Contract contract : contracts) {
                model.addRow(new Object[]{
                    contract.getId(),
                    contract.getTenantId(),
                    contract.getRoomId(),
                    contract.getTanggalMasuk(),
                    contract.getTanggalKeluar(),
                    contract.getDeposit(),
                    contract.getTanggalJatuhTempo(),
                    contract.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Terjadi kesalahan",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTable tableContract;
    // End of variables declaration//GEN-END:variables
}
