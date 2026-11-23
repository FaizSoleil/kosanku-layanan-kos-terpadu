package kosanku.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import kosanku.model.Payment;

public class PaymentFormDialog extends JDialog {

    public enum PaymentDialogMode { ADD, EDIT }

    private final JTextField txtBillId = new JTextField(20);
    private final JTextField txtPaymentDate = new JTextField(20);
    private final JTextField txtJumlah = new JTextField(20);
    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnBatal = new JButton("Batal");

    private boolean saved = false;

    public PaymentFormDialog(Frame owner, boolean modal, PaymentDialogMode mode) {
        this(owner, modal, mode, null);
    }

    public PaymentFormDialog(Frame owner, boolean modal, PaymentDialogMode mode, Payment payment) {
        super(owner, modal);
        setTitle(mode == PaymentDialogMode.ADD ? "Tambah Pembayaran" : "Edit Pembayaran");
        buildUi();

        if (mode == PaymentDialogMode.EDIT && payment != null) {
            txtBillId.setText(String.valueOf(payment.getBillId()));
            txtPaymentDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(payment.getPaymentDate()));
            txtJumlah.setText(BigDecimal.valueOf(payment.getJumlah()).toPlainString());
        } else {
            txtPaymentDate.setText(LocalDate.now().toString());
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

        addRow(form, c, 0, "ID Tagihan", txtBillId);
        addRow(form, c, 1, "Tanggal Bayar (yyyy-MM-dd)", txtPaymentDate);
        addRow(form, c, 2, "Jumlah", txtJumlah);

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

    public String getSavedBillId() {
        return txtBillId.getText().trim();
    }

    public String getSavedPaymentDate() {
        return txtPaymentDate.getText().trim();
    }

    public String getSavedJumlah() {
        return txtJumlah.getText().trim();
    }
}