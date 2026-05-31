package model;
import java.util.*;

public class GerenciadorDePartida {
    private static GerenciadorDePartida instancia;
    
    // Sub-gerenciadores completamente desacoplados entre si
    private GerenciadorDeJogadores gerJogadores;
    private GerenciadorDeCartas gerCartas;
    private GerenciadorDeTabuleiro gerTabuleiro;
    
    private List<Dado> dados;
    private List<Observador> observadores;
    private int ultimoDado1;
    private int ultimoDado2;
    private int valorDados;
    
    private GerenciadorDePartida() {
        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        
    	this.dados = new ArrayList<>();
	    this.observadores = new ArrayList<>();
    }

    void reiniciarPartida() {
        this.gerJogadores = new GerenciadorDeJogadores();
        this.gerCartas = new GerenciadorDeCartas();
        this.gerTabuleiro = new GerenciadorDeTabuleiro();
        this.dados = new ArrayList<>();
        this.ultimoDado1 = 0;
        this.ultimoDado2 = 0;
        this.valorDados = 0;
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
    	
        // evita duplicar dados se iniciarPartida() for chamado várias vezes
        if (dados.isEmpty()) {
            dados.add(new Dado());
            dados.add(new Dado());
        }
        
        gerTabuleiro.posicionarPecas(gerJogadores.getJogadores());
        
        gerJogadores.distribuirBlocoDeNotas();
        gerJogadores.definirPrimeiroJogador();

        notificarObservadores();
    }
    
    public void adicionarJogador(String nome, String nomePersonagem) {
    	gerJogadores.adicionarJogador(nome, nomePersonagem);
    }
    
    public int[] lancarDados() {
        ultimoDado1 = dados.get(0).rolar();
        ultimoDado2 = dados.get(1).rolar();

	    valorDados = ultimoDado1 + ultimoDado2;

	    notificarObservadores();
    	
        // devolve os dois valores separados para a View poder desenhar as imagens dos dados
        return new int[]{ultimoDado1, ultimoDado2}; 
    }

    public void definirResultadoDados(int dado1, int dado2) {
        this.ultimoDado1 = dado1;
        this.ultimoDado2 = dado2;
        this.valorDados = dado1 + dado2;
        notificarObservadores();
    }

    public void zerarDadosRolados() {
        this.ultimoDado1 = 0;
        this.ultimoDado2 = 0;
        this.valorDados = 0;
        notificarObservadores();
    }
     
    public void proximoTurno() {
    	gerJogadores.proximoTurno();
	    notificarObservadores();
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
	        notificarObservadores();
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
	    boolean sucesso = gerTabuleiro.usaPassagemSecreta(gerJogadores.getJogadorAtual());
	    if (sucesso) {
	        notificarObservadores();
	    }
	    return sucesso;
    }

    public void registrarObservador(Observador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(Observador observador) {
        observadores.remove(observador);
    }

    private void notificarObservadores() {
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.atualizar(this);
        }
    }
    
    // get e set
    public Jogador getJogadorAtual() {
    	return gerJogadores.getJogadorAtual();
    }
    
    public List<Jogador> getJogadores(){
    	return gerJogadores.getJogadores();
    }

    public int getValorDados() {
        return valorDados;
    }

    public int getUltimoDado1() {
        return ultimoDado1;
    }

    public int getUltimoDado2() {
        return ultimoDado2;
    }
}

