package model;
import java.util.*;

public class GerenciadorDeCartas {
    private Envelope envelope;
    private Baralho baralho;
    private Random random = new Random();

    public void iniciarCartas() {
    	baralho = new Baralho();
    	baralho.embaralhar();
    	envelope = criaEnvelope();
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

    // O Acoplamento baixo acontece aqui: ele recebe os jogadores por parâmetro
    public void distribuiCartas(List<Jogador> jogadores) {
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

    private Carta sorteaPorTipo(TipoCarta tipo) {
    	List<Carta> lista = baralho.filtrarPorTipo(tipo);
    	return lista.get(random.nextInt(lista.size()));
    }

    public void realizarPalpite(Carta suspeito, Carta arma, Carta comodo) {
    	// logica do palpite
    }

    public boolean realizarAcusacao(Carta suspeito, Carta arma, Carta comodo) {
        return envelope.verificarAcusacao(suspeito, arma, comodo);
    }
}