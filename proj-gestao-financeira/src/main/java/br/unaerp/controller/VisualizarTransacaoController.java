package br.unaerp.controller;

import br.unaerp.model.Categoria;
import br.unaerp.model.Transacao;
import br.unaerp.model.Usuario;
import br.unaerp.model.DAO.CategoriaDAO;
import br.unaerp.model.DAO.CategoriaDAOImpl;
import br.unaerp.model.DAO.TransacaoDAO;
import br.unaerp.model.DAO.TransacaoDAOImpl;
import br.unaerp.view.VisualizarTransacaoView;
import br.unaerp.view.RegistrarTransacaoView;
import br.unaerp.view.MainView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class VisualizarTransacaoController {

    private final VisualizarTransacaoView view;
    private final TransacaoDAO transacaoDAO = new TransacaoDAOImpl();
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private final Usuario usuarioLogado;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private List<Transacao> todasTransacoes;

    private static final int COL_EDITAR = 5;
    private static final int COL_EXCLUIR = 6;

    public VisualizarTransacaoController(Usuario usuarioLogado, VisualizarTransacaoView view) {
        this.usuarioLogado = usuarioLogado;
        this.view = view;
        initController();
        carregarInicial();
    }

    private void carregarInicial() {
        todasTransacoes = transacaoDAO.buscarPorUsuario(usuarioLogado.getLogin());
        view.setListaTransacoes(todasTransacoes);

        List<Categoria> listaCats = categoriaDAO.buscarPorUsuario(usuarioLogado.getLogin());
        view.setListaCategoriasFiltro(listaCats);
    }

    private void initController() {
        view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTable().rowAtPoint(e.getPoint());
                int col = view.getTable().columnAtPoint(e.getPoint());
                if (row < 0 || col < 0) return;

                Transacao t = view.getTransacaoPorLinha(row);
                if (t == null) return;

                if (col == COL_EDITAR) {
                    abrirTelaEdicao(t);
                } else if (col == COL_EXCLUIR) {
                    excluirTransacaoConfirmacao(t);
                }
            }
        });

        view.addFiltrarListener(e -> {
            aplicarFiltro();
            view.selecionarAbaTransacoes();
        });

        view.addAtualizarListener(e -> {
            todasTransacoes = transacaoDAO.buscarPorUsuario(usuarioLogado.getLogin());
            view.setListaTransacoes(todasTransacoes);
            view.selecionarAbaTransacoes();
        });

        view.addVoltarFiltrosListener(e -> voltarParaMain());

        view.addVoltarTransacoesListener(e -> voltarParaMain());
    }

    private void voltarParaMain() {
        view.dispose();
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView(usuarioLogado);
            new MainController(usuarioLogado, mainView);
            mainView.setVisible(true);
        });
    }

    private void aplicarFiltro() {
        LocalDate inicio = null, fim = null;
        if (!view.isTodasDatasSelecionado()) {
            try {
                inicio = LocalDate.parse(view.getDataInicio(), fmt);
                fim    = LocalDate.parse(view.getDataFim(),    fmt);
                if (fim.isBefore(inicio)) {
                    JOptionPane.showMessageDialog(view,
                            "A data final não pode ser anterior à inicial.",
                            "Erro de Período", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(view,
                        "Formato de data inválido. Use dd/MM/yyyy.",
                        "Erro de Data", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        boolean filtrarReceita = view.isReceitaSelecionado();
        boolean filtrarDespesa = view.isDespesaSelecionado();
        if (!filtrarReceita && !filtrarDespesa) {
            JOptionPane.showMessageDialog(view,
                    "Selecione pelo menos uma classificação (Receita ou Despesa).",
                    "Erro de Classificação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean todasCats = view.isTodasCategoriasSelecionado();
        var selCats = todasCats
                ? List.of()
                : view.getCategoriasSelecionadas();
        if (!todasCats && selCats.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Selecione ao menos uma categoria ou marque 'Todas'.",
                    "Erro de Categoria", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate finalInicio = inicio;
        LocalDate finalFim = fim;
        List<Transacao> filtradas = todasTransacoes.stream()
                .filter(transacao -> {
                    if (finalInicio != null && finalFim != null) {
                        LocalDate d = transacao.getData();
                        return !(d.isBefore(finalInicio) || d.isAfter(finalFim));
                    }
                    return true;
                })
                .filter(transacao -> {
                    boolean isRec = transacao.getClassificacao().equalsIgnoreCase("Receita");
                    if (isRec && filtrarReceita) return true;
                    if (!isRec && filtrarDespesa) return true;
                    return false;
                })
                .filter(transacao -> {
                    if (todasCats) return true;
                    return selCats.contains(transacao.getCategoria().getNome());
                })
                .collect(Collectors.toList());

        view.setListaTransacoesFiltradas(filtradas, inicio, fim);
    }

    private void abrirTelaEdicao(Transacao transacao) {
        view.dispose();
        SwingUtilities.invokeLater(() -> {
            RegistrarTransacaoView cadastro = new RegistrarTransacaoView(usuarioLogado);
            RegistrarTransacaoController controller =
                    new RegistrarTransacaoController(usuarioLogado, cadastro);
            controller.carregarTransacaoParaEdicao(transacao.getId());
            cadastro.setVisible(true);
        });
    }

    private void excluirTransacaoConfirmacao(Transacao transacao) {
        int resp = JOptionPane.showConfirmDialog(
                view,
                "Deseja realmente excluir esta transação?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION
        );
        if (resp == JOptionPane.YES_OPTION) {
            transacaoDAO.deletar(transacao);
            todasTransacoes = transacaoDAO.buscarPorUsuario(usuarioLogado.getLogin());
            view.setListaTransacoes(todasTransacoes);
        }
    }
}
