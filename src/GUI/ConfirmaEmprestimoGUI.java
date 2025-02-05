package GUI;

import banco_dados.ConexaoBanco;
import content.Disponibilidade;
import content.Emprestimo;
import content.Livro;
import content.Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class ConfirmaEmprestimoGUI extends JDialog implements ActionListener {
    private JComboBox cmbEmprestimos;
    private JButton btnConfirmar;

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(btnConfirmar)) {
            Emprestimo emprestimo = (Emprestimo) cmbEmprestimos.getSelectedItem();
            if (emprestimo == null) {
                JOptionPane.showMessageDialog(this, "Selecione um emprestimo", "ERRO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ConexaoBanco conexaoBanco = new ConexaoBanco();
            conexaoBanco.connect();
            if (conexaoBanco.confirmarEmprestimo(emprestimo)) {
                JOptionPane.showMessageDialog(this, "Devolvido com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro na devolucao", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
            conexaoBanco.disconnect();
        }
    }

    public ConfirmaEmprestimoGUI() {
//        super("Confirmar emprestimo");
        this.setSize(920, 720);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setModal(true);

        JPanel panMain = new JPanel(null);

        cmbEmprestimos = new JComboBox();
        cmbEmprestimos.setBounds(10, 10, 200, 50);
        panMain.add(cmbEmprestimos);

        ConexaoBanco conexaoBanco = new ConexaoBanco();
        conexaoBanco.connect();
        ArrayList<Emprestimo> emprestimos = conexaoBanco.getEmprestimos();
        conexaoBanco.disconnect();

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getStatus() == 0) {
                cmbEmprestimos.addItem(emprestimo);
            }
        }

        btnConfirmar = new JButton("Confirma emprestimo");
        btnConfirmar.setBounds(20, 65, 200, 50);
        panMain.add(btnConfirmar);
        btnConfirmar.addActionListener(this);

        this.add(panMain);
        this.setVisible(true);
    }
}
