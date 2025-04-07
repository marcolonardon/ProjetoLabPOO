package br.unaerp.view;

import br.unaerp.model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class VisualizarTransacaoView extends JFrame {

    private Usuario usuario;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JTextField campoDiaInicio, campoMesInicio, campoAnoInicio;
    private JTextField campoDiaFim, campoMesFim, campoAnoFim;
    private JCheckBox chkTodasTransacoes;
    private JComboBox<String> comboFiltro;
    private JComboBox<String> comboFiltroExtra;
    private JButton btnFiltrar, btnAtualizar, btnVoltar;

    public VisualizarTransacaoView(Usuario usuario) {
        super("Visualizar Transações");
        this.usuario = usuario;
        initComponents();
        atualizarInformacoes();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelFiltros = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 6;
        chkTodasTransacoes = new JCheckBox("Buscar todas as transações");
        panelFiltros.add(chkTodasTransacoes, gbc);
        gbc.gridwidth = 1;

        chkTodasTransacoes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean selecionado = (e.getStateChange() == ItemEvent.SELECTED);
                campoDiaInicio.setEnabled(!selecionado);
                campoMesInicio.setEnabled(!selecionado);
                campoAnoInicio.setEnabled(!selecionado);
                campoDiaFim.setEnabled(!selecionado);
                campoMesFim.setEnabled(!selecionado);
                campoAnoFim.setEnabled(!selecionado);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFiltros.add(new JLabel("Dia Início:"), gbc);
        campoDiaInicio = new JTextField(2);
        gbc.gridx = 1;
        panelFiltros.add(campoDiaInicio, gbc);

        gbc.gridx = 2;
        panelFiltros.add(new JLabel("Mês Início:"), gbc);
        campoMesInicio = new JTextField(2);
        gbc.gridx = 3;
        panelFiltros.add(campoMesInicio, gbc);

        gbc.gridx = 4;
        panelFiltros.add(new JLabel("Ano Início:"), gbc);
        campoAnoInicio = new JTextField(4);
        gbc.gridx = 5;
        panelFiltros.add(campoAnoInicio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelFiltros.add(new JLabel("Dia Fim:"), gbc);
        campoDiaFim = new JTextField(2);
        gbc.gridx = 1;
        panelFiltros.add(campoDiaFim, gbc);

        gbc.gridx = 2;
        panelFiltros.add(new JLabel("Mês Fim:"), gbc);
        campoMesFim = new JTextField(2);
        gbc.gridx = 3;
        panelFiltros.add(campoMesFim, gbc);

        gbc.gridx = 4;
        panelFiltros.add(new JLabel("Ano Fim:"), gbc);
        campoAnoFim = new JTextField(4);
        gbc.gridx = 5;
        panelFiltros.add(campoAnoFim, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panelFiltros.add(new JLabel("Filtro:"), gbc);
        comboFiltro = new JComboBox<>(new String[]{"Todas", "Por Classificação", "Por Categoria"});
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panelFiltros.add(comboFiltro, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 3;
        panelFiltros.add(new JLabel("Filtro Extra:"), gbc);
        comboFiltroExtra = new JComboBox<>();
        comboFiltroExtra.setEnabled(false);
        gbc.gridx = 4;
        gbc.gridwidth = 2;
        panelFiltros.add(comboFiltroExtra, gbc);
        gbc.gridwidth = 1;

        comboFiltro.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    String selecionado = (String) comboFiltro.getSelectedItem();
                    if(selecionado.equalsIgnoreCase("Por Categoria")){
                        comboFiltroExtra.setModel(new DefaultComboBoxModel<>(usuario.getCategorias().toArray(new String[0])));
                        comboFiltroExtra.setEnabled(true);
                    } else if(selecionado.equalsIgnoreCase("Por Classificação")){
                        comboFiltroExtra.setModel(new DefaultComboBoxModel<>(new String[]{"Receita", "Despesa"}));
                        comboFiltroExtra.setEnabled(true);
                    } else {
                        comboFiltroExtra.setModel(new DefaultComboBoxModel<>(new String[]{}));
                        comboFiltroExtra.setEnabled(false);
                    }
                }
            }
        });

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFiltrar = new JButton("Filtrar");
        btnAtualizar = new JButton("Atualizar");
        btnVoltar = new JButton("Voltar");
        panelBotoes.add(btnFiltrar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnVoltar);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 6;
        panelFiltros.add(panelBotoes, gbc);
        gbc.gridwidth = 1;

        add(panelFiltros, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltro();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarInformacoes();
            }
        });

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainView(usuario).setVisible(true);
                dispose();
            }
        });
    }

    private void atualizarInformacoes(){
        textArea.setText(usuario.getInformacoesUsuario());
    }

    private void aplicarFiltro(){
        try {
            int diaInicio, mesInicio, anoInicio, diaFim, mesFim, anoFim;
            if(chkTodasTransacoes.isSelected()){
                diaInicio = 1; mesInicio = 0; anoInicio = 1;
                diaFim = 31; mesFim = 12; anoFim = 9999;
            } else {
                diaInicio = Integer.parseInt(campoDiaInicio.getText());
                mesInicio = Integer.parseInt(campoMesInicio.getText());
                anoInicio = Integer.parseInt(campoAnoInicio.getText());
                diaFim = Integer.parseInt(campoDiaFim.getText());
                mesFim = Integer.parseInt(campoMesFim.getText());
                anoFim = Integer.parseInt(campoAnoFim.getText());
            }

            int filtro;
            String filtroExtra = "";

            String filtroSelecionado = (String) comboFiltro.getSelectedItem();
            if(filtroSelecionado.equalsIgnoreCase("Todas")){
                filtro = 1;
            } else if(filtroSelecionado.equalsIgnoreCase("Por Classificação")){
                filtro = 2;
                filtroExtra = (String) comboFiltroExtra.getSelectedItem();
            } else if(filtroSelecionado.equalsIgnoreCase("Por Categoria")){
                filtro = 3;
                filtroExtra = (String) comboFiltroExtra.getSelectedItem();
            } else {
                filtro = 1;
            }

            String resultado = usuario.filtrarTransacoes(filtro, diaInicio, mesInicio, anoInicio,
                    diaFim, mesFim, anoFim, filtroExtra);
            textArea.setText(resultado);
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira datas válidas.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch(IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
