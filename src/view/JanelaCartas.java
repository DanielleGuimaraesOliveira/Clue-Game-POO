package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Interfaces.ICarta;
import controller.ControladorPartida;

public class JanelaCartas extends JDialog {

    private ControladorPartida controlador;

    public JanelaCartas(JFrame parent, ControladorPartida controlador) {
        super(parent, "Cartas do Jogador", true);
        this.controlador = controlador;

        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        painelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        carregarCartas(painelPrincipal);
        JScrollPane scroll = new JScrollPane(painelPrincipal);
        add(scroll, BorderLayout.CENTER);
    }

    private void carregarCartas(JPanel painel) {
        List<ICarta> cartas = controlador.getCartasJogadorAtual();

        for (ICarta carta : cartas) {
            JPanel painelCarta = criarPainelCarta(carta);
            painel.add(painelCarta);
        }
    }

    private JPanel criarPainelCarta(ICarta carta) {
        JPanel painelCarta = new JPanel();
        painelCarta.setPreferredSize(new Dimension(160, 260));
        painelCarta.setLayout(new BorderLayout());

        try {
        	String caminhoImagem = controlador.getIdentificadorVisual(carta);
        	Image imagem = ImageIO.read(getClass().getResource(caminhoImagem));
        		
            Image imagemRedimensionada = imagem.getScaledInstance(140, 220,Image.SCALE_SMOOTH);

            JLabel lblImagem = new JLabel(new ImageIcon(imagemRedimensionada));
            lblImagem.setHorizontalAlignment(JLabel.CENTER);

            JLabel lblNome =new JLabel(carta.getNome());
            lblNome.setHorizontalAlignment(JLabel.CENTER);

            painelCarta.add(lblImagem, BorderLayout.CENTER);
            painelCarta.add(lblNome, BorderLayout.SOUTH);

        }
        catch (Exception e) {
            JLabel erro = new JLabel("Imagem não encontrada");
            erro.setHorizontalAlignment(JLabel.CENTER);
            painelCarta.add(erro, BorderLayout.CENTER);
        }

        return painelCarta;
    }
}