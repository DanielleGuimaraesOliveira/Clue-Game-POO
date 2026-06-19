package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class JanelaVitoria extends JDialog {

    public JanelaVitoria(
        JFrame janelaPai,
        String vencedor,
        String assassino,
        String arma,
        String local
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

        JButton btnFechar = new JButton("Encerrar Jogo");

        btnFechar.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        JPanel painelBotao = new JPanel();
        painelBotao.add(btnFechar);

        add(titulo, BorderLayout.NORTH);
        add(resultado, BorderLayout.CENTER);
        add(painelBotao, BorderLayout.SOUTH);
    }
}