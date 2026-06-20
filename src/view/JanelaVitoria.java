package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.ControladorPartida;

public class JanelaVitoria extends JDialog {

    public JanelaVitoria(
        JFrame janelaPai,
        String vencedor,
        String assassino,
        String arma,
        String local,
        ControladorPartida controlador
    ) {

        super(janelaPai, "Fim de Jogo", true);

        setSize(500, 300);
        setLocationRelativeTo(janelaPai);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel(
            "🎉 PARABÉNS! 🎉",
            SwingConstants.CENTER
        );

        titulo.setFont(
            new Font("Arial", Font.BOLD, 24)
        );

        JLabel resultado = new JLabel(
            "<html><center>" +
            vencedor + " resolveu o crime!<br><br>" +
            "<b>Assassino:</b> " + assassino + "<br>" +
            "<b>Arma:</b> " + arma + "<br>" +
            "<b>Local:</b> " + local +
            "</center></html>",
            SwingConstants.CENTER
        );
        
        JButton btnNovaPartida = new JButton("Nova Partida");
        JButton btnSair = new JButton("Sair");
        
        btnNovaPartida.addActionListener(e -> {
            dispose();
            janelaPai.dispose();

            new JanelaPersonagens(controlador)
                .setVisible(true);
        });
        
        btnSair.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        JPanel painelBotao = new JPanel();
        painelBotao.add(btnSair);
        painelBotao.add(btnNovaPartida);

        add(titulo, BorderLayout.NORTH);
        add(resultado, BorderLayout.CENTER);
        add(painelBotao, BorderLayout.SOUTH);
    }
}