package view;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import model.GerenciadorDePartida;


public class JanelaJogo extends JFrame {
 
	private GerenciadorDePartida gerenciador;
	private PainelDeFundo painelTabuleiro;

	 public JanelaJogo(GerenciadorDePartida gerenciador) {
	     this.gerenciador = gerenciador;
	     
	     // teste - forçando jogadores 
	     if (this.gerenciador.getJogadores().isEmpty()) {
	         this.gerenciador.adicionarJogador("Dani", "Miss Scarlet");
	         this.gerenciador.adicionarJogador("Judy", "Coronel Mustard");
	         
	         // ATENÇÃO: Isso é vital! Cria o tabuleiro e põe as peças na casa Inicial
	         this.gerenciador.iniciarPartida(); 
	     }
	     
	     // configurações 
	     setTitle("Clue Tabuleiro | Dani e Judy");
	     setSize(800, 800); 
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
	     // usando o BorderLayout para organizar os elementos 
	     setLayout(new BorderLayout());
	     
	     // carregar imagem do mapa
	     Image imagemMapa = null;
	     try {
	    	 imagemMapa = ImageIO.read(getClass().getResource("/assets/img/Tabuleiros/Tabuleiro-Clue-C.jpg"));
	    	 System.out.println("Imagem do tabuleiro carregada com sucesso");
	     }
	     catch (Exception e){
	    	 System.out.println("Erro ao carregar imagem do tabuleiro");
	    	 e.printStackTrace();
	     }
	     
	     // instanciado o PainelDeFundo
	     painelTabuleiro = new PainelDeFundo(imagemMapa, this);
	     
	     // colocando o painel de fundo no centro da janela
	     add(painelTabuleiro, BorderLayout.CENTER);
	     
	     // garante a centralização da janela 
	     setLocationRelativeTo(null);     
	 }
	 
	 // método para pegar a soma dos dados (PainelDeFundo)
	 public int getValorSimuladoDados() {
		 /*
		 if ( dado1 != null && dado2 != null) {
			 int valor1 = (integer) dado1.getSelectedItem();
			 int valor2 = (Integer) dado2.getSelectedItem();
			 return valor1 + valor2;
		 }
		 */
		 
		 return 12;
	 }
}