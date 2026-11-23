package kosanku.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kosanku.model.Bill;

public class BillFormDialog extends JDialog {

    public enum BillDialogMode { ADD, EDIT }

    private final JTextField txtContractId = new JTextField(20);
    private final JTextField txtBulanTagihan = new JTextField(20);
    private final JTextField txtTotalTagihan = new JTextField(20);
    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnBatal = new JButton("Batal");

    private boolean saved = false;

    public BillFormDialog(Frame owner, boolean modal, BillDialogMode mode) {
        this(owner, modal, mode, null);
    }

    public BillFormDialog(Frame owner, boolean modal, BillDialogMode mode, Bill bill) {
        super(owner, modal);
        setTitle(mode == BillDialogMode.ADD ? "Tambah Tagihan" : "Edit Tagihan");
        buildUi();

        if (mode == BillDialogMode.EDIT && bill != null) {
            txtContractId.setText(String.valueOf(bill.getContractId()));
            txtBulanTagihan.setText(new SimpleDateFormat("yyyy-MM").format(bill.getBulanTagihan()));
            txtTotalTagihan.setText(BigDecimal.valueOf(bill.getTotalTagihan()).toPlainString());
        } else {
            txtBulanTagihan.setText(YearMonth.now().toString());
        }

        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    private void buildUi() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.WEST;

        addRow(form, c, 0, "ID Kontrak", txtContractId);
        addRow(form, c, 1, "Bulan Tagihan (yyyy-MM)", txtBulanTagihan);
        addRow(form, c, 2, "Total Tagihan", txtTotalTagihan);

        JPanel buttons = new JPanel();
        buttons.add(btnSimpan);
        buttons.add(btnBatal);

        btnSimpan.addActionListener(e -> {
            saved = true;
            dispose();
        });
        btnBatal.addActionListener(e -> {
            saved = false;
            dispose();
        });
        getRootPane().setDefaultButton(btnSimpan);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private void addRow(JPanel form, GridBagConstraints c, int row, String label, JTextField field) {
        c.gridy = row;
        c.gridx = 0;
        c.fill = GridBagConstraints.NONE;
        form.add(new JLabel(label), c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, c);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getSavedContractId() {
        return txtContractId.getText().trim();
    }

    public String getSavedBulanTagihan() {
        return txtBulanTagihan.getText().trim();
    }

    public String getSavedTotalTagihan() {
        return txtTotalTagihan.getText().trim();
    }
}