package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.util.Random; 

import model.GerenciadorDePartida;

public class JanelaJogo extends JFrame {
 
	private GerenciadorDePartida gerenciador;
	private PainelDeFundo painelTabuleiro;
	
	// Elementos da barra lateral
    private JLabel lblTurno;
    private JLabel lblImagemDado1;
    private JLabel lblImagemDado2;
    private JButton btnRolarDados;
    private JLabel lblTextoPassos;
    private JComboBox<Integer> comboDado1;
    private JComboBox<Integer> comboDado2;
    
    private int valorDados = 0; // Armazena a soma total para o PainelDeFundo usar

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
	     setSize(1200, 800); 
	     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     
	     // usando o BorderLayout para organizar os elementos 
	     setLayout(new BorderLayout());
	     
	     // carregar imagem do mapa (no centro)
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
	     
	     // carrega o menu lateral (na direita)
	     JPanel painelMenu = criaMenuLateral();
	     add(painelMenu, BorderLayout.EAST);
	     
	     // garante a centralização da janela 
	     setLocationRelativeTo(null);     
	 }
	 
	 // método privado para organizar a criação da barra lateral 
	 private JPanel criaMenuLateral() {
		 JPanel painel = new JPanel();
		 painel.setPreferredSize(new Dimension(250, 0)); // trava a largura do menu lateral
		 painel.setBackground(new Color(240, 240, 240));
		 painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // margem interna
		 
		 // GridLayout para empilhar os itens verticalmente
		 painel.setLayout(new GridLayout(7, 1, 10, 10)); // 8 linhas, 1 coluna, espaçamento de 10px
		 
		 // Turno
		 lblTurno = new JLabel("Turno de: " + gerenciador.getJogadorAtual().getPersonagem().getNome(), SwingConstants.CENTER);
		 lblTurno.setFont(new Font("Arial", Font.BOLD, 16));
		 painel.add(lblTurno);
		 
		// Dados (iteração 2)
        Integer[] facesDados = {1, 2, 3, 4, 5, 6};
        comboDado1 = new JComboBox<>(facesDados);
        comboDado2 = new JComboBox<>(facesDados);
        
        lblImagemDado1 = new JLabel(carregarImagemDado(1), SwingConstants.CENTER);
        lblImagemDado2 = new JLabel(carregarImagemDado(1), SwingConstants.CENTER);
        
        // colocando os dois dados lado a lado dentro de um sub-painel 
        JPanel painelDadosLadoALado = new JPanel();
        painelDadosLadoALado.setBackground(new Color(240, 240, 240));
        painelDadosLadoALado.add(lblImagemDado1);
        painelDadosLadoALado.add(lblImagemDado2);
        painel.add(painelDadosLadoALado);
        
        btnRolarDados = new JButton("🎲 Rolar Dados");
        
        // texto informa a qtd de passos que o jogador tirou
        lblTextoPassos = new JLabel("Aguardando rolagem...", SwingConstants.CENTER);
        lblTextoPassos.setFont(new Font("Arial", Font.ITALIC, 13));
        
        // ação rolar dadoos (chama o método do Model)
        btnRolarDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] resultadoDados = gerenciador.lancarDados();
                
                // pega o valor de cada dado
                int d1 = resultadoDados[0];
                int d2 = resultadoDados[1];
                
                // salva na variável usada no DFS
                valorDados = d1 + d2;
                
                // troca a imagem na tela em tempo real
                lblImagemDado1.setIcon(carregarImagemDado(d1));
                lblImagemDado2.setIcon(carregarImagemDado(d2));
                
                // atualiza o rótulo de texto na barra lateral
                lblTextoPassos.setText("Você tirou " + valorDados + " passos!");
                
                System.out.println("🎲 O Model sorteou: " + d1 + " e " + d2 + " (Total: " + valorDados + ")");
            }
        });
        
        painel.add(btnRolarDados);
        painel.add(lblTextoPassos);
        
        /*
        painel.add(new JLabel("Resultado do Dado 1:"));
        painel.add(comboDado1);
        
        painel.add(new JLabel("Resultado do Dado 2:"));
        painel.add(comboDado2);
        */
        
        // Botões
        JButton btnAcusacao = new JButton("Fazer Acusação Final");
        JButton btnSalvar = new JButton("Salvar Partida");
        
        
	     // Exemplo de ação do botão de salvar (Sempre sem lambda!)
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botão salvar clicado!");
                // aqui será chamodo o gerenciador.salvarJogo()
            }
        });
        
        //painel.add(new JLabel("")); // um espaço vazio para afastar os botões
        painel.add(btnAcusacao);
        painel.add(btnSalvar);
        
        return painel;
	 }
	 
	// método auxiliar para desenhar as imagens dos dados
	private ImageIcon carregarImagemDado(int valorFace) {
        try {
            // Busca a imagem baseada no número (dado1.png, dado2.png, etc)
            Image img = ImageIO.read(getClass().getResource("/assets/img/Dados/dado" + valorFace + ".jpg"));
            
            // Redimensiona a imagem para ficar quadradinha com 60x60 pixels
            Image imgRedimensionada = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            return new ImageIcon(imgRedimensionada);
            
        } catch (Exception e) {
            // Se a imagem não for encontrada, ele avisa no console
            System.out.println("Erro ao carregar a imagem: /assets/img/Dados/dado" + valorFace + ".jpg");
            return null;
        }
	}
	    
	// método que o PainelDeFund chama ao click
    public int getValorSimuladoDados() {
        return valorDados; // Retorna o valor exato que saiu no último clique do botão!
    }
	

	 
	
}