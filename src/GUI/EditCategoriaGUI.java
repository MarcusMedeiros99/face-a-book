package GUI;

import banco_dados.ConexaoBanco;
import content.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditCategoriaGUI extends JDialog implements ActionListener {

    private JComboBox cmbCategorias;

    private JButton btnCategoriaEditar, btnCategoriaAdd, btnCategoriaClear, btnCategoriaCancel;
    private JTextField txtCategoriaNome, txtCategoriaId;

    private void habilitaTexto(boolean enable) {
        cmbCategorias.setEnabled(!enable);
        btnCategoriaEditar.setEnabled(!enable);

        txtCategoriaId.setEnabled(false);
        txtCategoriaNome.setEnabled(enable);

        btnCategoriaAdd.setEnabled(enable);
        btnCategoriaClear.setEnabled(enable);
        btnCategoriaCancel.setEnabled(enable);
    }

    private void updateComboCategoria() {
        ConexaoBanco conexaoBanco = new ConexaoBanco();
        conexaoBanco.connect();
        ArrayList<Categoria> categorias = conexaoBanco.getCategorias();
        conexaoBanco.disconnect();
        cmbCategorias.removeAllItems();
        cmbCategorias.addItem("-- Adicionar --");
        for (Categoria categoria : categorias) {
            cmbCategorias.addItem(categoria);
        }
    }

    private void fillItems(Categoria categoria) {
        if (categoria == null) {
            txtCategoriaId.setText("");
            txtCategoriaNome.setText("");
        } else {
            txtCategoriaId.setText(categoria.getId() + "");
            txtCategoriaNome.setText(categoria.getNome());

        }
        habilitaTexto(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(btnCategoriaEditar)) {
            if (cmbCategorias.getSelectedIndex() == 0) {
                fillItems(null);
            } else {
                fillItems((Categoria) cmbCategorias.getSelectedItem());
            }
        } else if (actionEvent.getSource().equals(btnCategoriaClear)) {
            fillItems(null);
        } else if (actionEvent.getSource().equals(btnCategoriaCancel)) {
            fillItems(null);
            habilitaTexto(false);
        } else if (actionEvent.getSource().equals(btnCategoriaAdd)) {

            if (txtCategoriaNome.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Nome invalido!", "Falha", JOptionPane.WARNING_MESSAGE);
            } else {
                String nome = txtCategoriaNome.getText();
                String id = txtCategoriaId.getText();

                if (nome == null || nome.equals("")) {
                    JOptionPane.showMessageDialog(this, "Nome invalido!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (id == null || id.equals("")) {
                    id = "0";
                }

                Categoria categoria = new Categoria(Integer.parseInt(id), nome);

                ConexaoBanco conexaoBanco = new ConexaoBanco();
                conexaoBanco.connect();

                boolean ok = false;
                String message = "";

                if (cmbCategorias.getSelectedIndex() == 0) {
                    ok = conexaoBanco.insertCategoria(categoria);
                    message = "Inserido com sucesso!";
                } else {
                    ok = conexaoBanco.updateCategoria(categoria);
                    message = "Editado com sucesso!";
                }

                conexaoBanco.disconnect();

                if (ok) {
                    JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    this.setVisible(false);
                }
            }
        }
    }

    public EditCategoriaGUI() {
        this.setSize(920, 720);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setModal(true);

        JPanel panMain = new JPanel(null);

        cmbCategorias = new JComboBox();
        cmbCategorias.setBounds(10, 10, 200, 20);
        panMain.add(cmbCategorias);

        btnCategoriaEditar = new JButton("Editar");
        btnCategoriaEditar.setBounds(210, 10, 100, 20);
        btnCategoriaEditar.addActionListener(this);
        panMain.add(btnCategoriaEditar);

        txtCategoriaId = new JTextField();
        txtCategoriaId.setToolTipText("ID");
        txtCategoriaId.setBounds(10, 30, 200, 20);
        panMain.add(txtCategoriaId);

        txtCategoriaNome = new JTextField();
        txtCategoriaNome.setToolTipText("Nome");
        txtCategoriaNome.setBounds(10, 50, 200, 20);
        panMain.add(txtCategoriaNome);

        btnCategoriaAdd = new JButton("Confirmar");
        btnCategoriaAdd.addActionListener(this);
        btnCategoriaAdd.setBounds(10, 70, 200, 20);
        panMain.add(btnCategoriaAdd);

        btnCategoriaClear = new JButton("Limpar");
        btnCategoriaClear.addActionListener(this);
        btnCategoriaClear.setBounds(10, 90, 200, 20);
        panMain.add(btnCategoriaClear);

        btnCategoriaCancel = new JButton("Cancelar");
        btnCategoriaCancel.addActionListener(this);
        btnCategoriaCancel.setBounds(10, 110, 200, 20);
        panMain.add(btnCategoriaCancel);

        habilitaTexto(false);
        updateComboCategoria();

        this.add(panMain);
        this.setVisible(true);
    }
}
