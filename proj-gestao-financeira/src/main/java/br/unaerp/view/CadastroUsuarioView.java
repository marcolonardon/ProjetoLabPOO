package br.unaerp.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class CadastroUsuarioView extends JFrame {
    private JTextField campoLogin;
    private JTextField campoNome;
    private JComboBox<String> campoTipo;
    private JFormattedTextField campoDocumento;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JButton botaoSalvar;
    private JButton botaoVoltar;
    private MaskFormatter maskCPF;
    private MaskFormatter maskCNPJ;

    public CadastroUsuarioView() {
        super("Cadastrar Usuário");
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            maskCPF = new MaskFormatter("###.###.###-##");
            maskCPF.setPlaceholderCharacter('_');

            maskCNPJ = new MaskFormatter("##.###.###/####-##");
            maskCNPJ.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            maskCPF = null;
            maskCNPJ = null;
        }

        JLabel lblTitulo = new JLabel("Cadastro de Usuário", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelLogin = new JLabel("Usuário:");
        campoLogin = new JTextField(20);
        campoLogin.setDocument(new LimitDocument(50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(labelLogin, gbc);
        gbc.gridx = 1;
        formPanel.add(campoLogin, gbc);

        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField(20);
        campoNome.setDocument(new LimitDocument(50));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(labelNome, gbc);
        gbc.gridx = 1;
        formPanel.add(campoNome, gbc);

        JLabel labelTipo = new JLabel("Tipo:");
        campoTipo = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(labelTipo, gbc);
        gbc.gridx = 1;
        formPanel.add(campoTipo, gbc);

        JLabel labelDocumento = new JLabel("CPF/CNPJ:");
        if (maskCPF != null) {
            campoDocumento = new JFormattedTextField(new DefaultFormatterFactory(maskCPF));
        } else {
            campoDocumento = new JFormattedTextField();
        }
        campoDocumento.setColumns(20);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(labelDocumento, gbc);
        gbc.gridx = 1;
        formPanel.add(campoDocumento, gbc);

        JLabel labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField(20);
        campoSenha.setDocument(new LimitDocument(50));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(labelSenha, gbc);
        gbc.gridx = 1;
        formPanel.add(campoSenha, gbc);

        JLabel labelConfirmarSenha = new JLabel("Confirmar Senha:");
        campoConfirmarSenha = new JPasswordField(20);
        campoConfirmarSenha.setDocument(new LimitDocument(50));
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(labelConfirmarSenha, gbc);
        gbc.gridx = 1;
        formPanel.add(campoConfirmarSenha, gbc);

        botaoSalvar = new JButton("Salvar");
        botaoVoltar = new JButton("Voltar");
        botaoSalvar.setPreferredSize(new Dimension(100, 30));
        botaoVoltar.setPreferredSize(new Dimension(100, 30));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.add(botaoSalvar);
        btnPanel.add(botaoVoltar);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        campoTipo.addActionListener(e -> {
            String tipoSelecionado = (String) campoTipo.getSelectedItem();
            if ("Pessoa Física".equals(tipoSelecionado)) {
                if (maskCPF != null) {
                    campoDocumento.setFormatterFactory(new DefaultFormatterFactory(maskCPF));
                }
            } else {
                if (maskCNPJ != null) {
                    campoDocumento.setFormatterFactory(new DefaultFormatterFactory(maskCNPJ));
                }
            }
            campoDocumento.setValue(null);
        });
    }

    public String getLogin() {
        return campoLogin.getText().trim();
    }

    public String getNome() {
        return campoNome.getText().trim();
    }

    public String getTipoUsuario() {
        return (String) campoTipo.getSelectedItem();
    }

    public String getDocumento() {
        return campoDocumento.getText().trim();
    }

    public String getSenha() {
        return new String(campoSenha.getPassword());
    }

    public String getConfirmarSenha() {
        return new String(campoConfirmarSenha.getPassword());
    }

    public void addSalvarListener(ActionListener listener) {
        botaoSalvar.addActionListener(e -> {
            String tipo = getTipoUsuario();
            String textoBruto = getDocumento();
            String apenasDigitos = textoBruto.replaceAll("\\D", "");

            if (apenasDigitos.isEmpty()) {
                JOptionPane.showMessageDialog(
                        CadastroUsuarioView.this,
                        "O campo de CPF/CNPJ é obrigatório.",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if ("Pessoa Física".equals(tipo)) {
                if (apenasDigitos.length() != 11) {
                    JOptionPane.showMessageDialog(
                            CadastroUsuarioView.this,
                            "CPF inválido. Certifique-se de ter 11 dígitos.",
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } else {
                if (apenasDigitos.length() != 14) {
                    JOptionPane.showMessageDialog(
                            CadastroUsuarioView.this,
                            "CNPJ inválido. Certifique-se de ter 14 dígitos.",
                            "Erro de Validação",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            String senha = getSenha();
            String confirma = getConfirmarSenha();

            if (senha.isEmpty() || confirma.isEmpty()) {
                JOptionPane.showMessageDialog(
                        CadastroUsuarioView.this,
                        "Preencha a senha e a confirmação de senha.",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (!senha.equals(confirma)) {
                JOptionPane.showMessageDialog(
                        CadastroUsuarioView.this,
                        "A senha e a confirmação não coincidem.",
                        "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            listener.actionPerformed(e);
        });
    }

    public void addVoltarListener(ActionListener listener) {
        botaoVoltar.addActionListener(listener);
    }

    /**
     * Documento que limita o número de caracteres inseridos.
     */
    private static class LimitDocument extends PlainDocument {
        private final int maxLength;

        public LimitDocument(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) {
                return;
            }
            int currentLength = getLength();
            int newLength = currentLength + str.length();
            if (newLength <= maxLength) {
                super.insertString(offs, str, a);
            } else {
                String truncated = str.substring(0, maxLength - currentLength);
                super.insertString(offs, truncated, a);
            }
        }
    }
}
