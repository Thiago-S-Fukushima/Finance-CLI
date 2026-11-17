package com.finance.ui;

import com.finance.entity.Lancamento;
import com.finance.service.LancamentoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

public class FinanceWindow extends JFrame {

    private static final Preferences PREFS = Preferences.userNodeForPackage(FinanceWindow.class);
    private static final String THEME_KEY = "theme";
    private static final Font FONT = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 13);

    private final LancamentoService service;
    private boolean darkMode = PREFS.getBoolean(THEME_KEY, false);
    private String currentPage = "overview";

    private Color background, surface, sidebar, text, muted, border, primary, primarySoft;
    private Color inputBackground, tableHeader, rowAlt, danger, success;

    private JPanel root, sidebarPanel, contentPanel;
    private JLabel pageTitle, pageSubtitle;
    private JTextField descricaoField, valorField, dataField;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public FinanceWindow(LancamentoService service) {
        this.service = service;
        setTitle("Finance | Controle financeiro");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 700));
        setSize(1240, 820);
        setLocationRelativeTo(null);
        applyPalette();
        buildShell();
        showPage("overview");
    }

    private void applyPalette() {
        if (darkMode) {
            background = new Color(17, 27, 31);
            surface = new Color(27, 41, 45);
            sidebar = new Color(20, 34, 38);
            text = new Color(232, 241, 238);
            muted = new Color(153, 174, 169);
            border = new Color(53, 72, 73);
            primary = new Color(105, 190, 157);
            primarySoft = new Color(38, 76, 65);
            inputBackground = new Color(20, 33, 37);
            tableHeader = new Color(34, 52, 54);
            rowAlt = new Color(31, 47, 49);
            danger = new Color(239, 135, 135);
            success = new Color(112, 205, 161);
        } else {
            background = new Color(237, 246, 246);
            surface = Color.WHITE;
            sidebar = new Color(246, 251, 250);
            text = new Color(28, 52, 52);
            muted = new Color(111, 135, 133);
            border = new Color(220, 233, 230);
            primary = new Color(27, 105, 86);
            primarySoft = new Color(222, 240, 233);
            inputBackground = Color.WHITE;
            tableHeader = new Color(239, 247, 245);
            rowAlt = new Color(248, 251, 250);
            danger = new Color(184, 72, 72);
            success = new Color(31, 133, 91);
        }
    }

    private void buildShell() {
        root = new JPanel(new BorderLayout());
        root.setBackground(background);
        setContentPane(root);

        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(218, 0));
        sidebarPanel.setBackground(sidebar);
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, border));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(new EmptyBorder(24, 16, 20, 16));

        JLabel brand = new JLabel("◈  FINANCE");
        brand.setFont(new Font("SansSerif", Font.BOLD, 20));
        brand.setForeground(text);
        brand.setBorder(new EmptyBorder(4, 8, 30, 0));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(brand);

        addNavButton("⌂", "Visão geral", "overview");
        addNavButton("▤", "Lançamentos", "transactions");
        addNavButton("⚙", "Configurações", "settings");

        sidebarPanel.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("CONTROLE FINANCEIRO");
        footer.setFont(new Font("SansSerif", Font.BOLD, 9));
        footer.setForeground(muted);
        footer.setBorder(new EmptyBorder(10, 8, 4, 0));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(footer);

        root.add(sidebarPanel, BorderLayout.WEST);

        JPanel main = new JPanel(new BorderLayout(0, 22));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(25, 30, 28, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        pageTitle = new JLabel("Visão geral");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 27));
        pageTitle.setForeground(text);
        pageSubtitle = new JLabel("Acompanhe sua vida financeira em um só lugar.");
        pageSubtitle.setFont(FONT);
        pageSubtitle.setForeground(muted);
        titles.add(pageTitle);
        titles.add(Box.createVerticalStrut(5));
        titles.add(pageSubtitle);
        header.add(titles, BorderLayout.WEST);

        JButton themeButton = button(darkMode ? "☀  Modo claro" : "☾  Modo escuro", false);
        themeButton.addActionListener(e -> toggleTheme());
        header.add(themeButton, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        main.add(contentPanel, BorderLayout.CENTER);
        root.add(main, BorderLayout.CENTER);
    }

    private void addNavButton(String icon, String label, String page) {
        JButton b = new JButton(icon + "   " + label);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(FONT_BOLD);
        b.setForeground(currentPage.equals(page) ? primary : muted);
        b.setBackground(currentPage.equals(page) ? primarySoft : sidebar);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 12, 12, 12));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> showPage(page));
        sidebarPanel.add(b);
        sidebarPanel.add(Box.createVerticalStrut(7));
    }

    private void showPage(String page) {
        currentPage = page;
        root.remove(sidebarPanel);
        buildShell();
        pageTitle.setText(switch (page) {
            case "transactions" -> "Lançamentos";
            case "settings" -> "Configurações";
            default -> "Visão geral";
        });
        pageSubtitle.setText(switch (page) {
            case "transactions" -> "Cadastre, consulte e organize suas movimentações.";
            case "settings" -> "Personalize a aparência do Finance.";
            default -> "Acompanhe sua vida financeira em um só lugar.";
        });

        contentPanel.removeAll();
        if ("transactions".equals(page)) contentPanel.add(buildTransactionsPage(), BorderLayout.CENTER);
        else if ("settings".equals(page)) contentPanel.add(buildSettingsPage(), BorderLayout.NORTH);
        else contentPanel.add(buildOverviewPage(), BorderLayout.CENTER);

        root.revalidate();
        root.repaint();
    }

    private JPanel buildOverviewPage() {
        JPanel page = new JPanel(new BorderLayout(0, 20));
        page.setOpaque(false);
        List<Lancamento> items = safeList();
        BigDecimal total = BigDecimal.ZERO;
        for (Lancamento item : items) total = total.add(item.getValor());
        BigDecimal media = items.isEmpty()
                ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(items.size()), 2, java.math.RoundingMode.HALF_UP);

        JPanel metrics = new JPanel(new GridLayout(1, 3, 14, 0));
        metrics.setOpaque(false);
        metrics.add(metricCard("TOTAL REGISTRADO", money(total), "Soma dos lançamentos", "◈"));
        metrics.add(metricCard("LANÇAMENTOS", String.valueOf(items.size()), "Registros cadastrados", "▤"));
        metrics.add(metricCard("VALOR MÉDIO", money(media), "Média por lançamento", "↗"));
        page.add(metrics, BorderLayout.NORTH);

        JPanel recent = card();
        recent.setLayout(new BorderLayout(0, 14));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel heading = heading("Movimentações recentes");
        top.add(heading, BorderLayout.WEST);
        JButton seeAll = button("Ver lançamentos  →", false);
        seeAll.addActionListener(e -> showPage("transactions"));
        top.add(seeAll, BorderLayout.EAST);
        recent.add(top, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "DESCRIÇÃO", "VALOR", "DATA"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int i = Math.max(0, items.size() - 8); i < items.size(); i++) {
            Lancamento item = items.get(i);
            model.addRow(new Object[]{item.getId(), item.getDescricao(), money(item.getValor()), item.getData()});
        }
        JTable recentTable = styledTable(model);
        recent.add(new JScrollPane(recentTable), BorderLayout.CENTER);
        page.add(recent, BorderLayout.CENTER);
        return page;
    }

    private JPanel metricCard(String label, String value, String detail, String symbol) {
        JPanel p = card();
        p.setLayout(new BorderLayout(0, 12));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel name = new JLabel(label);
        name.setFont(new Font("SansSerif", Font.BOLD, 10));
        name.setForeground(muted);
        JLabel icon = new JLabel(symbol);
        icon.setFont(new Font("SansSerif", Font.BOLD, 20));
        icon.setForeground(primary);
        top.add(name, BorderLayout.WEST);
        top.add(icon, BorderLayout.EAST);
        JLabel amount = new JLabel(value);
        amount.setFont(new Font("SansSerif", Font.BOLD, 23));
        amount.setForeground(text);
        JLabel hint = new JLabel(detail);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 11));
        hint.setForeground(muted);
        p.add(top, BorderLayout.NORTH);
        p.add(amount, BorderLayout.CENTER);
        p.add(hint, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildTransactionsPage() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);

        JPanel form = card();
        form.setLayout(new BorderLayout(0, 13));
        form.add(heading("Novo lançamento"), BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(1, 3, 12, 0));
        fields.setOpaque(false);
        descricaoField = new JTextField();
        valorField = new JTextField();
        dataField = new JTextField();
        ((AbstractDocument) dataField.getDocument())
        .setDocumentFilter(new DataMaskFilter(dataField));
        fields.add(field("Descrição", descricaoField));
        fields.add(field("Valor (ex.: 150,50)", valorField));
        fields.add(field("Data (DD/MM/AAAA)", dataField));
        form.add(fields, BorderLayout.CENTER);

        JPanel formActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        formActions.setOpaque(false);
        JButton clear = button("Limpar", false);
        clear.addActionListener(e -> limparCampos());
        JButton save = button("＋  Cadastrar lançamento", true);
        save.addActionListener(e -> cadastrarLancamento());
        formActions.add(clear);
        formActions.add(save);
        form.add(formActions, BorderLayout.SOUTH);
        page.add(form, BorderLayout.NORTH);

        JPanel listing = card();
        listing.setLayout(new BorderLayout(0, 13));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(heading("Todos os lançamentos"), BorderLayout.WEST);
        JButton refresh = button("Atualizar lista", false);
        refresh.addActionListener(e -> carregarLancamentos());
        top.add(refresh, BorderLayout.EAST);
        listing.add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "DESCRIÇÃO", "VALOR", "DATA"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        transactionTable = styledTable(tableModel);
        listing.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        JButton search = button("Buscar por ID", false);
        search.addActionListener(e -> buscarPorId());
        JButton edit = button("Editar selecionado", false);
        edit.addActionListener(e -> atualizarSelecionado());
        JButton delete = button("Excluir selecionado", false);
        delete.setForeground(danger);
        delete.addActionListener(e -> excluirSelecionado());
        actions.add(search);
        actions.add(edit);
        actions.add(delete);
        listing.add(actions, BorderLayout.SOUTH);
        page.add(listing, BorderLayout.CENTER);
        carregarLancamentos();
        return page;
    }

    private JPanel buildSettingsPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setOpaque(false);
        JPanel settings = card();
        settings.setLayout(new BorderLayout(0, 18));
        settings.add(heading("Aparência"), BorderLayout.NORTH);

        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setOpaque(false);
        JPanel descriptions = new JPanel();
        descriptions.setOpaque(false);
        descriptions.setLayout(new BoxLayout(descriptions, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Tema do aplicativo");
        title.setFont(FONT_BOLD);
        title.setForeground(text);
        JLabel desc = new JLabel("Escolha entre o modo claro e o modo escuro.");
        desc.setFont(FONT);
        desc.setForeground(muted);
        descriptions.add(title);
        descriptions.add(Box.createVerticalStrut(5));
        descriptions.add(desc);
        row.add(descriptions, BorderLayout.CENTER);

        JPanel choices = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        choices.setOpaque(false);
        JButton light = button("☀  Claro", !darkMode);
        light.addActionListener(e -> setTheme(false));
        JButton dark = button("☾  Escuro", darkMode);
        dark.addActionListener(e -> setTheme(true));
        choices.add(light);
        choices.add(dark);
        row.add(choices, BorderLayout.EAST);
        settings.add(row, BorderLayout.CENTER);
        page.add(settings, BorderLayout.NORTH);
        return page;
    }

    private JPanel field(String label, JTextField input) {
        JPanel p = new JPanel(new BorderLayout(0, 7));
        p.setOpaque(false);
        JLabel caption = new JLabel(label);
        caption.setFont(new Font("SansSerif", Font.BOLD, 11));
        caption.setForeground(muted);
        input.setFont(FONT);
        input.setForeground(text);
        input.setBackground(inputBackground);
        input.setCaretColor(text);
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border), new EmptyBorder(10, 11, 10, 11)));
        p.add(caption, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(37);
        t.setFont(FONT);
        t.setForeground(text);
        t.setBackground(surface);
        t.setGridColor(border);
        t.setSelectionBackground(primarySoft);
        t.setSelectionForeground(text);
        t.setFillsViewportHeight(true);
        t.setShowVerticalLines(false);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 10));
        t.getTableHeader().setForeground(muted);
        t.getTableHeader().setBackground(tableHeader);
        t.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, border));
        return t;
    }

    private JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(surface);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border), new EmptyBorder(18, 19, 18, 19)));
        return p;
    }

    private JLabel heading(String value) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(text);
        return label;
    }

    private JButton button(String label, boolean highlighted) {
        JButton b = new JButton(label);
        b.setFont(FONT_BOLD);
        b.setForeground(highlighted ? (darkMode ? new Color(14, 39, 31) : Color.WHITE) : text);
        b.setBackground(highlighted ? primary : primarySoft);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 14, 10, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private List<Lancamento> safeList() {
        try { return service.listarTodos(); }
        catch (RuntimeException ex) {
            showError("Não foi possível carregar os lançamentos. Verifique a conexão com o banco.");
            return List.of();
        }
    }

    private String money(BigDecimal value) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value);
    }

    private void toggleTheme() { setTheme(!darkMode); }

    private void setTheme(boolean dark) {
        darkMode = dark;
        PREFS.putBoolean(THEME_KEY, darkMode);
        applyPalette();
        buildShell();
        showPage(currentPage);
    }

    private void cadastrarLancamento() {
        try {
            Lancamento item = lerFormulario();
            service.salvar(item);
            JOptionPane.showMessageDialog(this, "Lançamento cadastrado com sucesso!", "Finance", JOptionPane.INFORMATION_MESSAGE);
            showPage("transactions");
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (RuntimeException ex) {
            showError("Não foi possível cadastrar. Verifique a conexão com o banco de dados.");
        }
    }

    private Lancamento lerFormulario() {
        String descricao = descricaoField.getText().trim();
        String valorTexto = valorField.getText().trim();
        if (valorTexto.contains(",")) valorTexto = valorTexto.replace(".", "").replace(",", ".");
        if (descricao.isBlank()) throw new IllegalArgumentException("Informe a descrição.");
        try {
            BigDecimal valor = new BigDecimal(valorTexto);
            if (valor.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("O valor precisa ser maior que zero.");
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");

LocalDate data = LocalDate.parse(
        dataField.getText().trim(),
        formato
);
            return new Lancamento(descricao, valor, data);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Valor inválido. Exemplo: 150,50.");
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Data inválida. Use o formato AAAA-MM-DD.");
        }
    }

    private void carregarLancamentos() {
        if (tableModel == null) return;
        try {
            tableModel.setRowCount(0);
            for (Lancamento item : service.listarTodos()) {
                tableModel.addRow(new Object[]{item.getId(), item.getDescricao(), money(item.getValor()), item.getData().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))});
            }
        } catch (RuntimeException ex) {
            showError("Não foi possível carregar os lançamentos. Verifique a conexão com o banco.");
        }
    }

    private void buscarPorId() {
        String value = JOptionPane.showInputDialog(this, "Informe o ID do lançamento:", "Buscar lançamento", JOptionPane.QUESTION_MESSAGE);
        if (value == null || value.isBlank()) return;
        try {
            long id = Long.parseLong(value.trim());
            var result = service.buscarPorId(id);
            if (result.isPresent()) {
                Lancamento item = result.get();
                descricaoField.setText(item.getDescricao());
                valorField.setText(item.getValor().toPlainString().replace(".", ","));
                dataField.setText(item.getData().toString());
                selecionarLinhaPorId(id);
            } else JOptionPane.showMessageDialog(this, "Nenhum lançamento encontrado com esse ID.");
        } catch (NumberFormatException ex) {
            showError("Digite um ID numérico válido.");
        }
    }

    private void atualizarSelecionado() {
        int row = transactionTable.getSelectedRow();
        if (row < 0) { showError("Selecione um lançamento na tabela para editar."); return; }
        try {
            Long id = Long.valueOf(tableModel.getValueAt(transactionTable.convertRowIndexToModel(row), 0).toString());
            Lancamento dados = lerFormulario();
            var result = service.atualizar(id, dados.getDescricao(), dados.getValor(), dados.getData());
            if (result.isPresent()) {
                JOptionPane.showMessageDialog(this, "Lançamento atualizado com sucesso!");
                showPage("transactions");
            } else showError("Lançamento não encontrado.");
        } catch (IllegalArgumentException ex) { showError(ex.getMessage()); }
        catch (RuntimeException ex) { showError("Não foi possível atualizar o lançamento."); }
    }

    private void excluirSelecionado() {
        int row = transactionTable.getSelectedRow();
        if (row < 0) { showError("Selecione um lançamento na tabela para excluir."); return; }
        int modelRow = transactionTable.convertRowIndexToModel(row);
        Long id = Long.valueOf(tableModel.getValueAt(modelRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o lançamento " + id + "?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.excluir(id);
            JOptionPane.showMessageDialog(this, "Lançamento excluído com sucesso!");
            showPage("transactions");
        } catch (RuntimeException ex) { showError("Não foi possível excluir o lançamento."); }
    }

    private void selecionarLinhaPorId(long id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (Long.parseLong(tableModel.getValueAt(i, 0).toString()) == id) {
                int viewRow = transactionTable.convertRowIndexToView(i);
                transactionTable.setRowSelectionInterval(viewRow, viewRow);
                transactionTable.scrollRectToVisible(transactionTable.getCellRect(viewRow, 0, true));
                break;
            }
        }
    }

    private void limparCampos() {
        if (descricaoField != null) descricaoField.setText("");
        if (valorField != null) valorField.setText("");
        if (dataField != null) dataField.setText("");
        if (transactionTable != null) transactionTable.clearSelection();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Finance", JOptionPane.WARNING_MESSAGE);
    }

    private static class DataMaskFilter extends DocumentFilter {

    private final JTextField field;

    public DataMaskFilter(JTextField field) {
        this.field = field;
    }

    @Override
    public void insertString(
            FilterBypass fb,
            int offset,
            String text,
            AttributeSet attributes
    ) throws BadLocationException {

        replace(fb, offset, 0, text, attributes);
    }

    @Override
    public void remove(
            FilterBypass fb,
            int offset,
            int length
    ) throws BadLocationException {

        replace(fb, offset, length, "", null);
    }

    @Override
    public void replace(
            FilterBypass fb,
            int offset,
            int length,
            String text,
            AttributeSet attributes
    ) throws BadLocationException {

        String current = fb.getDocument().getText(
                0, fb.getDocument().getLength()
        );

        String replacement = text == null ? "" : text;

        String candidate =
                current.substring(0, offset)
                + replacement
                + current.substring(offset + length);

        String digits = candidate.replaceAll("[^0-9]", "");

        if (digits.length() > 8) {
            digits = digits.substring(0, 8);
        }

        int cursorDigits = candidate
                .substring(0, Math.min(offset + replacement.length(), candidate.length()))
                .replaceAll("[^0-9]", "")
                .length();

        String formatted = formatDate(digits);

        fb.replace(
                0,
                fb.getDocument().getLength(),
                formatted,
                attributes
        );

        int cursorPosition = cursorDigits;

        if (cursorDigits > 2) cursorPosition++;
        if (cursorDigits > 4) cursorPosition++;

        final int finalPosition = Math.min(cursorPosition, formatted.length());

        SwingUtilities.invokeLater(() ->
                field.setCaretPosition(finalPosition)
        );
    }

    private String formatDate(String digits) {

        if (digits.length() <= 2) {
            return digits;
        }

        if (digits.length() <= 4) {
            return digits.substring(0, 2)
                    + "/"
                    + digits.substring(2);
        }

        return digits.substring(0, 2)
                + "/"
                + digits.substring(2, 4)
                + "/"
                + digits.substring(4);
    }
}
}
