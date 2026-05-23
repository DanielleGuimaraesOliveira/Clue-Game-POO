package view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.GerenciadorDePartida;

/*
	JDialog é uma "janela-filha" que pode travar a tela de trás (comportamento Modal) 
	para o jogador não conseguir clicar em mais nada no tabuleiro até terminar o palpite.
 */

public class JanelaPalpite extends JDialog {
	
	public JanelaPalpite (JFrame janelaPai) {
		// O "true" no final significa que ela é MODAL (bloqueia o clique no tabuleiro)
        super(janelaPai, "Fazer Palpite", true);
        
        setSize(400, 200);
        setLocationRelativeTo(janelaPai); // Nasce bem no meio do tabuleiro
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
        
        JLabel lblMensagem = new JLabel("Você entrou no cômodo! Prepare seu palpite.");
        JButton btnSair = new JButton("Sair (Passar Turno)");
        
        // Ação do botão (usando Classe Anônima, sem Lambda!)
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // futuramente aqui pegará a Arma e Suspeito escolhidos...
                
                // passa a vez para o próximo jogador
                GerenciadorDePartida.getInstance().proximoTurno();
                System.out.println("Agora é a vez de: " + GerenciadorDePartida.getInstance().getJogadorAtual().getPersonagem().getNome());
                
                // destrói essa janelinha e volta pro tabuleiro
                dispose(); 
            }
        });
        
        add(lblMensagem);
        add(btnSair);
        
	}
}
