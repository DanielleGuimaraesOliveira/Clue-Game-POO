package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.GerenciadorDePartida;

public class PainelDeFundo extends JPanel {
    
	private Image imagem;
	private JanelaJogo janelaPai;
	
	public PainelDeFundo(Image imagem) {
		this.imagem = imagem;
	}
	
	// Sobrecarga de constructor (JanelaJogo)
    public PainelDeFundo(Image imagem, JanelaJogo janelaPai) {
        this.imagem = imagem;
        this.janelaPai = janelaPai;
        
        // tratamento de eventos (sem lambda)
        this.addMouseListener(new MouseAdapter() {
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		
        		// pegando a largura e altura exata de cada quadradinho
        		int larguraCasa = getWidth()/ 24;
        		int alturaCasa = getHeight()/ 25;
        		
        		// converte o pixel do click para x e y da matriz
        		int xLogico = e.getX() / larguraCasa;
        		int yLogico = e.getY() / alturaCasa;
        		
        		// pega o valor dos dados do ComboBox da janelaJogo
        		int valorDados = janelaPai.getValorSimuladoDados();
        		
        		// Singleton processa jogada
        		GerenciadorDePartida.getInstance().processaClickTela(xLogico, yLogico, valorDados);
        		
        		// atualiza a tela para desenhar a peça no novo lugar
        		repaint();
        	}
        });
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // 1° camada - tabuleiro (fundo)
        if (imagem != null) {           
            g2d.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
        }
        
        // 2° camada - peças
        int larguraCasa = getWidth()/ 24;
		int alturaCasa = getHeight()/ 25;
		
		// busca todos os jogadores
		if (GerenciadorDePartida.getInstance().getJogadores() != null) {
		            
            for (model.Jogador j : GerenciadorDePartida.getInstance().getJogadores()) {
                
                // Pega onde ele está no mapa lógico
                model.Casa posicao = j.getPersonagem().getPosicaoAtual();
                
                if (posicao != null) {
                    // 3. Multiplica X e Y para achar os pixels exatos na tela
                    int xPixel = posicao.getX() * larguraCasa;
                    int yPixel = posicao.getY() * alturaCasa;
                    
                    // DESENHANDO A PEÇA
                    // (Troque isso pelo g2d.drawImage(imagemPiao, ...) no futuro!)
                    g2d.setColor(java.awt.Color.RED); 
                    
                    // Desenha um círculo um pouquinho menor que o quadrado para caber direitinho
                    g2d.fillOval(xPixel + 2, yPixel + 2, larguraCasa - 4, alturaCasa - 4);
                }
            }
		}
    }
}