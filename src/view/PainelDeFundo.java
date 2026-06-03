package view;

import javax.swing.JPanel;

import Interfaces.ICasa;
import Interfaces.IJogador;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.*;

import controller.ControladorPartida;
import model.Observador;


public class PainelDeFundo extends JPanel implements Observador {
    
	private Image imagem;
	private JanelaJogo janelaPai;
	private ControladorPartida controlador;
	
	public PainelDeFundo(Image imagem) {
		this.imagem = imagem;
	}
	
	// Sobrecarga de constructor (JanelaJogo)
	public PainelDeFundo(Image imagem, ControladorPartida controlador, JanelaJogo janelaPai) {
		this.imagem = imagem;
		this.controlador = controlador;
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
        		
				// pega o valor dos dados do controlador
				int valorDados = controlador.getValorDados();
        		
        		// se for 0, o jogador está tentando andar sem rolar os dados!
        		if (valorDados == 0) {
        			System.out.println("Movimento bloqueado! Você precisa rolar os dados antes.");
        			return; // aborta o clique, não faz nada
        		}
        		
				// guarda quem é o jogador antes do clique para sabermos se o turno passou
				IJogador jogadorAntesDoClique = controlador.getJogadorAtual();

				// Controller processa jogada
				// -> entrou num cômodo?
				boolean entrouComodo = controlador.processaClickTela(xLogico, yLogico, valorDados);
        		
        		// atualiza a tela para desenhar a peça no novo lugar
        		repaint();
        		
        		// se entrou no cômodo, abre a tela de palpite
        		if (entrouComodo) {
        			
        			// cria a janela de palpite passando a JanelaJogo original como "Pai
					JanelaPalpite popUp = new JanelaPalpite(janelaPai, controlador);
        			popUp.setVisible(true); // trava até o jogador "sair" da tela palpite
        			
        			janelaPai.resetaDadosEPassaTurno();
        			
        			// repinta para atualizar quem é o jogador da vez
        		    repaint();
        		}
        		
        		else {
					// verifica se o jogador atual mudou no Gerenciador (significa que o movimento foi válido)
					IJogador jogadorDepoisDoClique = controlador.getJogadorAtual();
        			
        			if (jogadorAntesDoClique != jogadorDepoisDoClique) {
        				// Se mudou o jogador, limpa os passos e libera o botão de dados para o próximo!
        				janelaPai.resetaDadosEPassaTurno();
        			} else {
        				System.out.println("você clicou em uma casa inválida. Tente novamente usando seus " + valorDados + " passos.");
        			}
        		}
        	}
        });
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // calcula as dimensões da grade
        int larguraCasa = getWidth() / 24;
        int alturaCasa = getHeight() / 25;
        
        // 1° camada - tabuleiro (fundo)
        if (imagem != null) {           
            g2d.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
        }
        
        // se janelaPai não for nula (evitar erro na tela inicial)
		if (janelaPai != null && controlador != null) {
			    // marcador de casas válidas!!!
			    int vlrDadosNaTela = janelaPai.getValorSimuladoDados();
            
			    // só desenha se o jogador rolou os dados
			    if (vlrDadosNaTela > 0 && controlador.getJogadorAtual() != null) {
             
					// pega a lista das casa possíveis
					List<ICasa> casasPossiveis = controlador.mapearCasas(vlrDadosNaTela);

					for (ICasa c : casasPossiveis) {
                    int px = c.getX() * larguraCasa;
                    int py = c.getY() * alturaCasa;
                    
                    // pinta um quadrado verde semi-transparente (RGBA)
                    g2d.setColor(new java.awt.Color(0, 255, 0, 100)); 
                    g2d.fillRect(px, py, larguraCasa, alturaCasa);
                    
                    // faz uma bordinha verde mais forte para ficar elegante
                    g2d.setColor(new java.awt.Color(0, 200, 0));
                    g2d.drawRect(px, py, larguraCasa - 1, alturaCasa - 1);
                }
            }
        }
        
        
        
        // 2° camada - peças

		// melhorando o peao na tela
		int tamanhoPiao = (int) (Math.min(larguraCasa, alturaCasa) * 0.7);
		
		// margem pra centralização
		int margemX = (larguraCasa - tamanhoPiao) / 2;
        int margemY = (alturaCasa - tamanhoPiao) / 2;
		
		// busca todos os jogadores 
		if (controlador != null && controlador.getJogadores() != null) {
                    
			for (IJogador j : controlador.getJogadores()) {
                
                // Pega onde ele está no mapa lógico
				ICasa posicao = j.getPosicaoAtual();
                
                if (posicao != null) {
                    // Multiplica X e Y para achar os pixels exatos na tela
                    int xPixelBase = posicao.getX() * larguraCasa;
                    int yPixelBase = posicao.getY() * alturaCasa;
                    
                    // Soma as margens calculadas para empurrar o desenho para o CENTRO exato
                    int xFinal = xPixelBase + margemX + 10 + 2;
                    int yFinal = yPixelBase + margemY + 10 + 2;
                    
                    // DESENHANDO A PEÇA
                    // trocar isso pelo g2d.drawImage(imagemPiao, ...))
					if (j.getPersonagem().getNome().equals("Miss Scarlet")) {
                        g2d.setColor(java.awt.Color.RED);
                    } else {
                        g2d.setColor(java.awt.Color.YELLOW); // Coronel Mustard
                    }
                    
                    // Desenha o círculo preenchido perfeitamente centralizado
                    g2d.fillOval(xFinal, yFinal, tamanhoPiao, tamanhoPiao);
                }
            }
		}
    }

	@Override
	public void atualizar() {
		repaint();
	}
}