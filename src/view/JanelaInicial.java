package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class JanelaInicial extends JFrame {

    public JanelaInicial() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Clue - Início");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image img = null;

        try {
            img = ImageIO.read(getClass().getResource("/view/imagens/clue_inicio.png"));
        } catch (IOException e) {
            System.err.println("Erro: Não foi possível carregar a imagem.");
        }
        
        PainelDeFundo center = new PainelDeFundo(img);
        center.setLayout(null);

        JButton btnNovo = new JButton("Novo Jogo");
        JButton btnContinuar = new JButton("Continuar");

        btnNovo.setBounds(80, 190, 100, 25);
        btnContinuar.setBounds(80, 240, 100, 25);

        center.add(btnNovo);
        center.add(btnContinuar);

        add(center, BorderLayout.CENTER);

        btnNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JanelaJogo janela = new JanelaJogo(null);
                janela.setLocationRelativeTo(null);
                janela.setVisible(true);
                dispose();
            }
        });

        btnContinuar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JanelaJogo janela = new JanelaJogo(null);
                janela.setLocationRelativeTo(null);
                janela.setVisible(true);
                dispose();
            }
        });
    }

}
