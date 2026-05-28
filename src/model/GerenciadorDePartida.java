package model;
import java.util.*;

public class GerenciadorDePartida {
    private static GerenciadorDePartida instancia;
    
    // Sub-gerenciadores completamente desacoplados entre si
    private GerenciadorDeJogadores gerJogadores;
    private GerenciadorDeCartas gerCartas;
    private GerenciadorDeTabuleiro gerTabuleiro;
    
    private List<Dado> dados;
    
    private GerenciadorDePartida() {
        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        
    	this.dados = new ArrayList<>();
    }
    
    public static synchronized GerenciadorDePartida getInstance() {
         if (instancia == null) instancia = new GerenciadorDePartida();
        return instancia; 
    }

    public void iniciarPartida() {
        // 1. Criar Baralho
        // 2. Sortear cartas do Envelope (Crime)
    	gerCartas.iniciarCartas();
    	
        // 3. Distribuir restante para jogadores
    	gerCartas.distribuiCartas(gerJogadores.getJogadores());
    	
        // 4. Posicionar peças no tabuleiro
    	gerTabuleiro.iniciarTabuleiro();
    	
        dados.add(new Dado());
        dados.add(new Dado());
        
        gerTabuleiro.posicionarPecas(gerJogadores.getJogadores());
        
        gerJogadores.distribuirBlocoDeNotas();
        gerJogadores.definirPrimeiroJogador();
    }
    
    public void adicionarJogador(String nome, String nomePersonagem) {
    	gerJogadores.adicionarJogador(nome, nomePersonagem);
    }
    
    public int[] lancarDados() {
        int vlrDado1 = dados.get(0).rolar();
        int vlrDado2 = dados.get(1).rolar();
    	
        // devolve os dois valores separados para a View poder desenhar as imagens dos dados
        return new int[]{vlrDado1, vlrDado2}; 
    }
     
    public void proximoTurno() {
    	gerJogadores.proximoTurno();
    }

    public void realizarPalpite(Carta suspeito, Carta arma, Carta comodo) {
    	gerCartas.realizarPalpite(suspeito, arma, comodo);
    }
    
    public List<Casa> mapearCasas(int passos) {
        return gerTabuleiro.mapearCasas(gerJogadores.getJogadorAtual(), passos);
    }
    
    public List<String> mapearCasaFormatadas(int passos){
    	return gerTabuleiro.mapearCasaFormatadas(gerJogadores.getJogadorAtual(), passos);
    }
    
    public void deslocarPiao(String destino) {
        gerTabuleiro.deslocarPiao(gerJogadores.getJogadorAtual(), destino);
    }
    
    public String getPosicaoAtualFormatada() {
        return gerTabuleiro.getPosicaoAtualFormatada(gerJogadores.getJogadorAtual());
    }

    public boolean realizarAcusacao(Carta suspeito, Carta arma, Carta comodo) {
        return gerCartas.realizarAcusacao(suspeito, arma, comodo);
    }
    
    /*______________________________________________________*/
    
    // método chamado pela interface gráfica
    public boolean processaClickTela(int xLogico, int yLogico, int valorDados) {
        
        int status = gerTabuleiro.processaClickTelaInterno(gerJogadores.getJogadorAtual(), xLogico, yLogico, valorDados);
        
        if (status == 1) {
            return true; // Entrou no cômodo
        } else if (status == 2) {
            proximoTurno(); // Moveu no corredor, gerencia o turno aqui!
            System.out.println("turno muda para: " + gerJogadores.getJogadorAtual().getPersonagem().getNome() + " Debug: gerenciadorPartida l:233");
            return false;
        }
        
    	return false; // click inválido
    }
    
    // Passagem secreta
    public boolean usaPassagemSecreta() {
    	return gerTabuleiro.usaPassagemSecreta(gerJogadores.getJogadorAtual());
    }
    
    // get e set
    public Jogador getJogadorAtual() {
    	return gerJogadores.getJogadorAtual();
    }
    
    public List<Jogador> getJogadores(){
    	return gerJogadores.getJogadores();
    }
}

