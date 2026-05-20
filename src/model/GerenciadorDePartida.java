package model;
import java.util.*;

public class GerenciadorDePartida {
    private static GerenciadorDePartida instancia;
	private List<Jogador> jogadores;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;
    private Envelope envelope;
    private Baralho baralho;
    private List<Dado> dados;
    
    private Random random = new Random();
    
    private GerenciadorDePartida() {
    	this.jogadores = new ArrayList<>();
    	this.dados = new ArrayList<>();
    }
    
    public static synchronized GerenciadorDePartida getInstance() {
         if (instancia == null) instancia = new GerenciadorDePartida();
        return instancia; 
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
    	tabuleiro = new Tabuleiro();
    	
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
         if (jogadores.isEmpty()) {
        throw new IllegalStateException("Nenhum jogador para distribuir cartas.");
    }
    	int i = 0;
    	
    	while(!baralho.getCartas().isEmpty()) {
    		Jogador jogador = jogadores.get(i % jogadores.size());
    		jogador.recebeCartas(baralho.compraCarta());
    		i++;
    	}
    }
    
    private void posicionarPecas() {
    	
    	for (Jogador j : jogadores) {
    		boolean posicionado = false;
    		
    		// O algoritmo varre o tabuleiro procurando um lugar seguro para nascer
            for (int y = 0; y < 25 && !posicionado; y++) {
                for (int x = 0; x < 24 && !posicionado; x++) {
                    
                    Casa casaTeste = tabuleiro.getCasa(x, y);
                    
                    // Se a casa existe, é um corredor ("1") e ninguém pisou nela ainda:
                    if (casaTeste != null && casaTeste.getTipo().equals("1") && !casaTeste.estaOcupada()) {
                        
                        // Posiciona a peça!
                        tabuleiro.moverPeca(j.getPersonagem(), casaTeste);
                        posicionado = true; // Para o loop e vai para o próximo jogador
                        
                    }
                }
            }
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
    

    public void deslocarPiao(String destino) {
        Casa casaDestino = transformaStringParaCasa(destino);
        tabuleiro.moverPeca(jogadorAtual.getPersonagem(), casaDestino);
    }
    

    public String getPosicaoAtualFormatada() {
        // acessa o jogador e a peça
        Casa atual = jogadorAtual.getPersonagem().getPosicaoAtual();
        
        // retorna apenas a String formatada para main
        return "(" + atual.getX() + "," + atual.getY() + ")";
    }
    
    
    private Casa transformaStringParaCasa(String casa){
        casa = casa.replace("(", "").replace(")", "");

        String[] partes = casa.split(",");

        int x = Integer.parseInt(partes[0]);
        int y = Integer.parseInt(partes[1]);

        Casa casaFormatada = tabuleiro.getCasa(x, y);
        
        return casaFormatada;
    }

    public boolean realizarAcusacao(Carta suspeito, Carta arma, Carta comodo) {
        return envelope.verificarAcusacao(suspeito, arma, comodo);
    }
    
    // método chamado pela interface gráfica
    public void processaClickTela(int xLogico, int yLogico, int valorDados) {
    	
    	if (tabuleiro == null || jogadorAtual == null) {
    		return;
    	}
    	
    	// pega a casa do click 
    	Casa destino = tabuleiro.getCasa(xLogico, yLogico);
    	if (destino == null) {
    		return; // clicou fora do tabuleiro
    	}   	
    	    	
    	// chama o DFS para saber as casas que pode ir
    	List<Casa> casasPossiveis = mapearCasas(valorDados);
    	
    	// valida se o destino está na lista das casas permitidas
    	if (casasPossiveis.contains(destino)) {
    		
    		// verifica se é corredor ou porta
    		if (destino.isCaminhavel()) {
    			
    			// move a peça
    			tabuleiro.moverPeca(jogadorAtual.getPersonagem(), destino);
    			System.out.println("Movimento para: " + xLogico + ", " + yLogico);
    		}
    	}
    }
    
    // get e set
    
    public Jogador getJogadorAtual() {
    	return jogadorAtual;
    }
    
    public List<Jogador> getJogadores(){
    	return jogadores;
    }
    
    
}
