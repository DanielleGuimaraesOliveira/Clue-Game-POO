package view;

import controller.ControladorPartida;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class JanelaBlocoNotas extends JDialog {

    private ControladorPartida controlador;
    private java.util.List<JCheckBox> checkboxes = new ArrayList<>();

    public JanelaBlocoNotas(JFrame pai, ControladorPartida controlador) {
        super(pai, "Bloco de Anotações", true);
        this.controlador = controlador;

        controlador.garantirBlocoParaJogadorAtual();

        java.util.List<String> nomes = controlador.getTodosNomesCartas();
        java.util.Map<String, Boolean> marcas = controlador.obterBlocoJogadorAtual();

        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(0,1));

        for (String nome : nomes) {
            JCheckBox cb = new JCheckBox(nome, marcas.getOrDefault(nome, false));
            checkboxes.add(cb);
            painel.add(cb);
        }

        JScrollPane scroll = new JScrollPane(painel);
        scroll.setPreferredSize(new Dimension(400, 400));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarMarcas();
                dispose();
            }
        });

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel();
        rodape.add(btnSalvar);
        rodape.add(btnFechar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(rodape, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(pai);
    }

    private void salvarMarcas() {
        java.util.List<String> nomes = controlador.getTodosNomesCartas();
        for (int i = 0; i < nomes.size() && i < checkboxes.size(); i++) {
            String nome = nomes.get(i);
            boolean marcado = checkboxes.get(i).isSelected();
            controlador.marcarCartaNoBlocoAtual(nome, marcado);
        }
    }
}
