import java.util.*;

public class GerenciadorDePartida {
	private List<Jogador> jogadores;
    private Jogador jogadorAtual;
    private Tabuleiro tabuleiro;
    private Envelope envelope;
    private Baralho baralho;
    private List<Dado> dados;
    
    public GerenciadorDePartida() {
    	this.jogadores = new ArrayList<>();
    	this.dados = new ArrayList<>();
    }
    
    public void iniciarPartida() {
        // 1. Criar Baralho
        // 2. Sortear cartas do Envelope (Crime)
        // 3. Distribuir restante para jogadores
        // 4. Posicionar peças no tabuleiro
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
