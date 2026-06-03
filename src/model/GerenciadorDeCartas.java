package model;
import java.util.*;

import Interfaces.ICarta;

public class GerenciadorDeCartas {
    private Envelope envelope;
    private Baralho baralho;
    private Random random = new Random();
    private Map<String, String> identificadoresVisuais;

    public GerenciadorDeCartas() {
        this.identificadoresVisuais = new HashMap<>();
        inicializarIdentificadoresVisuais();
    }

    public void iniciarCartas() {
    	baralho = new Baralho();
    	baralho.embaralhar();
    	envelope = criaEnvelope();
    }

    private void inicializarIdentificadoresVisuais() {
        identificadoresVisuais.put("Sr. Verde", "sr_verde");
        identificadoresVisuais.put("Srta. Scarlet", "srta_scarlet");
        identificadoresVisuais.put("Coronel Mustard", "coronel_mustard");
        identificadoresVisuais.put("Professor Plum", "professor_plum");
        identificadoresVisuais.put("Sra. Peacock", "sra_peacock");
        identificadoresVisuais.put("Sra. White", "sra_white");

        identificadoresVisuais.put("Corda", "corda");
        identificadoresVisuais.put("Cano de Chumbo", "cano_de_chumbo");
        identificadoresVisuais.put("Faca", "faca");
        identificadoresVisuais.put("Chave Inglesa", "chave_inglesa");
        identificadoresVisuais.put("Castiçal", "castical");
        identificadoresVisuais.put("Revólver", "revolver");

        identificadoresVisuais.put("Cozinha", "cozinha");
        identificadoresVisuais.put("Salão de Baile", "salao_baile");
        identificadoresVisuais.put("Sala de Jantar", "sala_jantar");
        identificadoresVisuais.put("Escritório", "escritorio");
        identificadoresVisuais.put("Biblioteca", "biblioteca");
        identificadoresVisuais.put("Sala de Estar", "sala_estar");
        identificadoresVisuais.put("Jardim de Inverno", "jardim_inverno");
        identificadoresVisuais.put("Hall", "hall");
        identificadoresVisuais.put("Sala de Música", "sala_musica");
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



    public ICarta responderPalpite(Jogador jogadorAtual, List<Jogador> jogadores, ICarta suspeito, ICarta arma, ICarta comodo) {
        if (jogadorAtual == null || jogadores == null || jogadores.isEmpty()) {
            return null;
        }

        int indiceAtual = jogadores.indexOf(jogadorAtual);
        if (indiceAtual < 0) {
            return null;
        }

        List<ICarta> palpite = Arrays.asList(suspeito, arma, comodo);
        for (int offset = 1; offset < jogadores.size(); offset++) {
            Jogador candidato = jogadores.get((indiceAtual + offset) % jogadores.size());
            for (ICarta carta : palpite) {
                Carta revelada = candidato.mostrarCarta(carta);
                if (revelada != null) {
                    return revelada;
                }
            }
        }

        return null;
    }

    public String responderPalpite(Jogador jogadorAtual, List<Jogador> jogadores, String suspeito, String arma, String comodo) {
        ICarta cartaSuspeito = new Carta(suspeito, TipoCarta.SUSPEITO);
        ICarta cartaArma = new Carta(arma, TipoCarta.ARMA);
        ICarta cartaComodo = new Carta(comodo, TipoCarta.COMODO);

        ICarta revelada = responderPalpite(jogadorAtual, jogadores, cartaSuspeito, cartaArma, cartaComodo);
        return revelada != null ? revelada.getNome() : null;
    }

    public boolean realizarAcusacao(ICarta suspeito, ICarta arma, ICarta comodo) {
        // envelope usa Carta internamente, valida por nome
        return envelope.verificarAcusacao(new Carta(suspeito.getNome(), TipoCarta.SUSPEITO), new Carta(arma.getNome(), TipoCarta.ARMA), new Carta(comodo.getNome(), TipoCarta.COMODO));
    }

	public void definirEnvelope(String assassino, String arma, String local) {
		Carta cartaAssassino = new Carta(assassino, TipoCarta.SUSPEITO);
		Carta cartaArma = new Carta(arma, TipoCarta.ARMA);
		Carta cartaLocal = new Carta(local, TipoCarta.COMODO);
		this.envelope = new Envelope(cartaAssassino, cartaArma, cartaLocal);
	}

    public String getIdentificadorVisual(ICarta carta) {
        if (carta == null) {
            return null;
        }
        return identificadoresVisuais.get(carta.getNome());
    }

    public String[] exportarEnvelope() {
        if (envelope == null) {
            return new String[] { null, null, null };
        }
        return new String[] {
            envelope.getAssassino().getNome(),
            envelope.getArma().getNome(),
            envelope.getLocal().getNome()
        };
    }
}