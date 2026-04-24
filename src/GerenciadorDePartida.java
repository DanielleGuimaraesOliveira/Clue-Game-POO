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
        // 4. Posicionar peças no tabuleiro
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
    
    private Carta sorteaPorTipo(TipoCarta tipo) {
        List<Carta> lista = baralho.filtrarPorTipo(tipo);
        return lista.get(random.nextInt(lista.size()));
    }
    
    public void proximoTurno() {
    	// passa a vez de jogar
    }

    public void realizarPalpite(Carta suspeito, Carta arma, Carta comodo) {
    	// logica do palpite
    }
    
    public boolean realizarAcusacao(Carta suspeito, Carta arma, Carta comodo) {
        return envelope.verificarAcusacao(suspeito, arma, comodo);
    }
    
    
}
