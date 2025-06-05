package br.unaerp.controller;

import br.unaerp.model.Categoria;
import br.unaerp.model.Transacao;
import br.unaerp.model.Usuario;
import br.unaerp.model.DAO.CategoriaDAO;
import br.unaerp.model.DAO.CategoriaDAOImpl;
import br.unaerp.model.DAO.TransacaoDAO;
import br.unaerp.model.DAO.TransacaoDAOImpl;
import br.unaerp.view.RegistrarTransacaoView;
import br.unaerp.view.MainView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RegistrarTransacaoController {

    private final RegistrarTransacaoView view;
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private final TransacaoDAO transacaoDAO = new TransacaoDAOImpl();
    private final Usuario usuarioLogado;

    private Transacao transacaoEmEdicao = null;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RegistrarTransacaoController(Usuario usuarioLogado, RegistrarTransacaoView view) {
        this.usuarioLogado = usuarioLogado;
        this.view = view;
        initController();
        carregarCategorias();
    }

    private void initController() {
        view.getBtnRegistrar().addActionListener(e -> salvarOuAtualizarTransacao());
        view.getBtnVoltar().addActionListener(e -> voltarParaMain());
    }

    private void carregarCategorias() {
        List<Categoria> listaCategorias = categoriaDAO.buscarPorUsuario(usuarioLogado.getLogin());
        view.setListaCategorias(listaCategorias);
    }

    public void carregarTransacaoParaEdicao(Integer idTransacao) {
        Transacao transacao = transacaoDAO.buscarPorId(idTransacao);
        if (transacao == null) {
            JOptionPane.showMessageDialog(view,
                    "Transação não encontrada no banco.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            voltarParaMain();
            return;
        }
        this.transacaoEmEdicao = transacao;
        view.clearCampos();
        view.setValor(String.format("%.2f", transacao.getValor()));
        view.setData(transacao.getData().format(fmt));
        view.setDescricao(transacao.getDescricao());
        view.setClassificacao(transacao.getClassificacao());
        view.setCategoriaSelecionada(transacao.getCategoria());
        view.getBtnRegistrar().setText("Salvar Alteração");
    }

    private void salvarOuAtualizarTransacao() {
        String valorStr     = view.getValor();
        String dataStr      = view.getData();
        String descricao    = view.getDescricao();
        Categoria categoria = view.getCategoriaSelecionada();
        String classificacao= view.getClassificacao();

        StringBuilder erros = new StringBuilder();
        if (valorStr.isEmpty())                       erros.append("- Valor é obrigatório\n");
        if (dataStr.isEmpty() || dataStr.contains("_"))erros.append("- Data é obrigatória (dd/MM/yyyy)\n");
        if (descricao.isEmpty())                      erros.append("- Descrição é obrigatória\n");
        if (categoria == null)                        erros.append("- Selecione uma categoria\n");

        if (erros.length() > 0) {
            JOptionPane.showMessageDialog(
                    view,
                    "Por favor, verifique os seguintes campos:\n" + erros.toString(),
                    "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            float valor = Float.parseFloat(valorStr.replace(',', '.'));
            LocalDate data = LocalDate.parse(dataStr, fmt);

            if (transacaoEmEdicao == null) {
                Transacao transacao = new Transacao();
                transacao.setValor(valor);
                transacao.setClassificacao(classificacao);
                transacao.setDescricao(descricao);
                transacao.setData(data);
                transacao.setUsuario(usuarioLogado);
                transacao.setCategoria(categoria);
                transacaoDAO.salvar(transacao);
                JOptionPane.showMessageDialog(
                        view,
                        "Transação registrada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                transacaoEmEdicao.setValor(valor);
                transacaoEmEdicao.setData(data);
                transacaoEmEdicao.setClassificacao(classificacao);
                transacaoEmEdicao.setDescricao(descricao);
                transacaoEmEdicao.setCategoria(categoria);
                transacaoDAO.atualizar(transacaoEmEdicao);
                JOptionPane.showMessageDialog(
                        view,
                        "Transação atualizada com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE
                );
            }

            voltarParaMain();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Valor inválido. Use apenas números.",
                    "Erro", JOptionPane.ERROR_MESSAGE
            );
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    view,
                    "Data inválida. Use dd/MM/yyyy.",
                    "Erro", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void voltarParaMain() {
        view.dispose();
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView(usuarioLogado);
            new MainController(usuarioLogado, mainView);
            mainView.setVisible(true);
        });
    }
}
