package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;

public class JanelaInicial extends JFrame {

    private JButton btnNovo = new JButton("Novo Jogo");
    private JButton btnContinuar = new JButton("Continuar");

    public JanelaInicial() {

        initComponents();
    }

    public void setAcaoNovoJogo(ActionListener ouvinte) {

        btnNovo.addActionListener(ouvinte);
    }

    public void setAcaoContinuar(ActionListener ouvinte) {

        btnContinuar.addActionListener(ouvinte);
    }

    private void initComponents() {

        setTitle("Clue - Início");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        Image img = null;

        try {

            img = ImageIO.read(
                getClass().getResource(
                    "/view/imagens/PlanosDeFundo/clue_inicio.png"
                )
            );

        } catch (IOException e) {

            System.err.println(
                "Erro: Não foi possível carregar a imagem."
            );
        }

        PainelDeFundo painelPrincipal =
                new PainelDeFundo(img);

        painelPrincipal.setLayout(null);

        btnNovo.setBounds(80, 190, 140, 30);
        btnContinuar.setBounds(80, 240, 140, 30);

        painelPrincipal.add(btnNovo);
        painelPrincipal.add(btnContinuar);

        add(painelPrincipal, BorderLayout.CENTER);
    }
}