package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Interfaces.ICarta;
import controller.ControladorPartida;

public class JanelaResultadoPalpite extends JDialog {

    public JanelaResultadoPalpite(JFrame janelaPai, ControladorPartida controlador, ICarta cartaRevelada, String nomeDono) {
        super(janelaPai, "Resultado do Palpite", true);

        setSize(400, 450);
        setLocationRelativeTo(janelaPai);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel painelCentral = new JPanel();
        painelCentral.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painelCentral.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblMensagem = new JLabel();
        lblMensagem.setFont(new Font("Arial", Font.BOLD, 16));
        lblMensagem.setHorizontalAlignment(SwingConstants.CENTER);

        // Se ninguém refutou o palpite
        if (cartaRevelada == null) {
            lblMensagem.setText("<html><center>Nenhum jogador pôde<br>refutar o seu palpite!</center></html>");
            painelCentral.add(lblMensagem);
        } 
        // Se alguém mostrou a carta
        else {
            lblMensagem.setText("<html><center>O jogador <b>" + nomeDono + "</b><br>mostrou a seguinte carta:</center></html>");
            painelCentral.add(lblMensagem);

            // Tenta desenhar a imagem da carta
            try {
                String caminhoImagem = controlador.getIdentificadorVisual(cartaRevelada);
                if (caminhoImagem != null) {
                    Image imagem = ImageIO.read(getClass().getResource(caminhoImagem));
                    Image imgRedimensionada = imagem.getScaledInstance(160, 250, Image.SCALE_SMOOTH);
                    JLabel lblImagem = new JLabel(new ImageIcon(imgRedimensionada));
                    painelCentral.add(lblImagem);
                }
            } catch (Exception e) {
                JLabel lblErro = new JLabel("[" + cartaRevelada.getNome() + "]");
                painelCentral.add(lblErro);
            }
        }

        JButton btnOk = new JButton("Entendido");
        btnOk.setPreferredSize(new Dimension(150, 40));
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });

        JPanel painelSul = new JPanel();
        painelSul.add(btnOk);

        add(painelCentral, BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);
    }
}