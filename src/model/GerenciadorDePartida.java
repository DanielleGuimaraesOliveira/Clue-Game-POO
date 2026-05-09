package model;
import java.util.*;

public class GerenciadorDePartida {
	private List<Jogador> jogadores;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;
    private Envelope envelope;
    private Baralho baralho;
    private List<Dado> dados;
    
    private Random random = new Random();
    
    public GerenciadorDePartida() {
    	this.jogadores = new ArrayList<>();
    	this.dados = new ArrayList<>();
    }
    
    public void iniciarPartida() {
        // 1. Criar Baralho
    	baralho = new Baralho();
    	baralho.embaralhar();
    	
        // 2. Sortear cartas do Envelope (Crime)
    	envelope = criaEnvelope();
    	
        // 3. Distribuir restante para jogadores
    	distribuiCartas();
    	
        // 4. Posicionar peças no tabuleiro
    	tabuleiro = new Tabuleiro(5, 5);
    	
        dados.add(new Dado());
        dados.add(new Dado());
        
        posicionarPecas();
        
        
        distribuirBlocoDeNotas();
        definirPrimeiroJogador();
    }
    
    private void distribuirBlocoDeNotas() {
    	for (Jogador j : jogadores) {
    		j.receberBlocoDeNotas();
    	}
    }
    
    private void definirPrimeiroJogador() {
    	jogadorAtual = null;
    	
    	for (Jogador j : jogadores) {
    		if (j.getPersonagem().getNome().equals("Srta. Scarlet")) {
    			jogadorAtual = j;
    			break;
    		}
    	}
    	
    	if (jogadorAtual == null && !jogadores.isEmpty()) {
    		jogadorAtual = jogadores.get(0);
    	}
    }
    
    private Envelope criaEnvelope() {
    	
    	Carta assassino = sorteaPorTipo(TipoCarta.SUSPEITO);
    	Carta arma = sorteaPorTipo(TipoCarta.ARMA);
    	Carta local = sorteaPorTipo(TipoCarta.COMODO);
    	
    	baralho.removeCarta(assassino);
    	baralho.removeCarta(arma);
    	baralho.removeCarta(local);
    	
    	return new Envelope(assassino, arma, local);
    }
    
    private void distribuiCartas() {
    	int i = 0;
    	
    	while(!baralho.getCartas().isEmpty()) {
    		Jogador jogador = jogadores.get(i % jogadores.size());
    		jogador.recebeCartas(baralho.compraCarta());
    		i++;
    	}
    }
    
    private void posicionarPecas() {
    	
    	int i = 0;
    	
    	for (Jogador j : jogadores) {
    		
    		Casa casaInicial = tabuleiro.getCasa(i, 0);
    		
    		tabuleiro.moverPeca(j.getPersonagem(), casaInicial);
    		
    		i++;
    	}
    }
    
    
    private Carta sorteaPorTipo(TipoCarta tipo) {
    	List<Carta> lista = baralho.filtrarPorTipo(tipo);
    	return lista.get(random.nextInt(lista.size()));
    }
    
    public void adicionarJogador(String nome, String nomePersonagem) {
    	PecaSuspeito peca = new PecaSuspeito(nomePersonagem);
    	Jogador novoJogador = new Jogador(nome, peca);
    	
    	jogadores.add(novoJogador);
    }
    
    public int lancarDados() {
        return dados.get(0).rolar() + dados.get(1).rolar();
    }
    
    
    
    public void proximoTurno() {
    	
    	    int index = jogadores.indexOf(jogadorAtual);
    	    jogadorAtual = jogadores.get((index + 1) % jogadores.size());

    }

    public void realizarPalpite(Carta suspeito, Carta arma, Carta comodo) {
    	// logica do palpite
    }
    
    public List<Casa> mapearCasas(int passos) {
        Casa origem = jogadorAtual.getPersonagem().getPosicaoAtual();
        return tabuleiro.calculaCaminhosValidos(origem, passos);
    }
    
    public List<String> mapearCasaFormatadas(int passos){
    	List<Casa> casasValidas = mapearCasas(passos);
    	
    	// converte para string no formato (x, y)
    	List<String> casasFormatadas = new ArrayList<>();
    	for (Casa c : casasValidas) {
    		casasFormatadas.add("(" + c.getX() + "," + c.getY() + ")");
    	}
    	
    	return casasFormatadas;
    }
    
    public String getPosicaoAtualFormatada() {
        // acessa o jogador e a peça
        Casa atual = jogadorAtual.getPersonagem().getPosicaoAtual();
        
        // retorna apenas a String formatada para main
        return "(" + atual.getX() + "," + atual.getY() + ")";
    }
    
    public void deslocarPiao(Casa destino) {
        tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino);
    }
    
    public boolean realizarAcusacao(Carta suspeito, Carta arma, Carta comodo) {
        return envelope.verificarAcusacao(suspeito, arma, comodo);
    }
    
    public Jogador getJogadorAtual() {
    	return jogadorAtual;
    }
    
    public List<Jogador> getJogadores(){
    	return jogadores;
    }
    
    
}
