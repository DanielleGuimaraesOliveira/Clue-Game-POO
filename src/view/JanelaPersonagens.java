package view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.ControladorPartida;

public class JanelaPersonagens extends JFrame {

    private ControladorPartida controlador;

    // checkbox -> personagem
    private Map<JCheckBox, String> personagens;

    private JButton btnJogar;

    public JanelaPersonagens(ControladorPartida controlador) {

        this.controlador = controlador;

        initComponents();
    }

    private void initComponents() {

        setTitle("Clue - Personagens");
        setSize(860, 530);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        Image img = null;

        try {

            img = ImageIO.read(getClass().getResource("/assets/img/PlanosDeFundo/clue_novoJogo.jpg"));

        } catch (IOException e) {
            System.out.println(
                "Erro ao carregar imagem"
            );
        }

        ImagemDeFundo painel = new ImagemDeFundo(img);
        painel.setLayout(null);

        personagens = new LinkedHashMap<>();

        adicionarCheckBox(painel,"Srta. Scarlet", 650, 270);
        adicionarCheckBox(painel, "Coronel Mustard", 650, 300);
        adicionarCheckBox(painel,"Sra. White", 650, 330);
        adicionarCheckBox(painel, "Rev. Green", 650, 360);
        adicionarCheckBox(painel,"Sra. Peacock", 650,390);
        adicionarCheckBox(painel,"Prof. Plum", 650,420);

        btnJogar = new JButton("Jogar");
        btnJogar.setBounds(650, 460, 120, 30);
        btnJogar.addActionListener(e -> iniciarJogo());

        painel.add(btnJogar);

        add(painel, BorderLayout.CENTER);
    }

    private void adicionarCheckBox(ImagemDeFundo painel, String nome, int x, int y ) {
        JCheckBox check = new JCheckBox(nome);

        check.setBounds(x, y, 140, 25);

        personagens.put(check, nome);

        painel.add(check);
    }

    private void iniciarJogo() {

        int contador = 1;

        for (Map.Entry<JCheckBox, String> entry: personagens.entrySet()) {

            JCheckBox check = entry.getKey();
            String personagem = entry.getValue();

            if (check.isSelected()) {
                controlador.adicionarJogador("Jogador " + contador,personagem  );

                contador++;
            }
        }

        // impede iniciar vazio
        if (contador == 1) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos um personagem." );

            return;
        }

        controlador.iniciarPartida();

        dispose();

        new JanelaJogo(controlador).setVisible(true);
    }
}