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

        // SUSPEITOS
        identificadoresVisuais.put("Sr. Verde", "/assets/img/Suspeitos/Green.jpg");
        identificadoresVisuais.put("Srta. Scarlet", "/assets/img/Suspeitos/Scarlet.jpg");
        identificadoresVisuais.put("Coronel Mustard", "/assets/img/Suspeitos/Mustard.jpg");
        identificadoresVisuais.put("Professor Plum", "/assets/img/Suspeitos/Plum.jpg");
        identificadoresVisuais.put("Sra. Peacock", "/assets/img/Suspeitos/Peacock.jpg");
        identificadoresVisuais.put("Sra. White", "/assets/img/Suspeitos/White.jpg");

        // ARMAS
        identificadoresVisuais.put("Corda", "/assets/img/Armas/Corda.jpg");
        identificadoresVisuais.put("Cano de Chumbo", "/assets/img/Armas/Cano.jpg");
        identificadoresVisuais.put("Faca", "/assets/img/Armas/Faca.jpg");
        identificadoresVisuais.put("Chave Inglesa", "/assets/img/Armas/ChaveInglesa.jpg");
        identificadoresVisuais.put("Castiçal", "/assets/img/Armas/Castical.jpg");
        identificadoresVisuais.put("Revólver", "/assets/img/Armas/Revolver.jpg");

        // CÔMODOS
        identificadoresVisuais.put("Biblioteca", "/assets/img/Comodos/Biblioteca.jpg");
        identificadoresVisuais.put("Cozinha", "/assets/img/Comodos/Cozinha.jpg");
        identificadoresVisuais.put("Entrada", "/assets/img/Comodos/Entrada.jpg");
        identificadoresVisuais.put("Escritório", "/assets/img/Comodos/Escritorio.jpg");
        identificadoresVisuais.put("Jardim de Inverno", "/assets/img/Comodos/JardimInverno.jpg");
        identificadoresVisuais.put("Sala de Estar", "/assets/img/Comodos/SalaDeEstar.jpg");
        identificadoresVisuais.put("Sala de Jantar", "/assets/img/Comodos/SalaDeJantar.jpg");
        identificadoresVisuais.put("Salão de Música", "/assets/img/Comodos/SalaDeMusica.jpg");
        identificadoresVisuais.put("Salão de Jogos", "/assets/img/Comodos/SalaoDeJogos.jpg");
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

    // Nota: a lógica de resposta ao palpite está em `responderPalpite(...)`.
    // O método `realizarPalpite(Carta...)` anterior foi removido por ser redundante.

    public ICarta responderPalpite(Jogador jogadorAtual, List<Jogador> jogadores, ICarta suspeito, ICarta arma, ICarta comodo) {
        if (jogadorAtual == null || jogadores == null || jogadores.isEmpty()) {
            return null;
        }

        int indiceAtual = jogadores.indexOf(jogadorAtual);
        if (indiceAtual < 0) {
            return null;
        }

        List<ICarta> palpite = Arrays.asList(suspeito, arma, comodo);
        
        // roda a mesa para a esquerda
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

    public ICarta responderPalpite(Jogador jogadorAtual, List<Jogador> jogadores, String suspeito, String arma, String comodo) {
        ICarta cartaSuspeito = new Carta(suspeito, TipoCarta.SUSPEITO);
        ICarta cartaArma = new Carta(arma, TipoCarta.ARMA);
        ICarta cartaComodo = new Carta(comodo, TipoCarta.COMODO);

        return responderPalpite(jogadorAtual, jogadores, cartaSuspeito, cartaArma, cartaComodo);
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
    
    
    // retorna todos os nomes de cartas do jogo (suspeitos, armas, cômodos)
    public java.util.List<String> getTodosNomesCartas() {
        java.util.List<String> nomes = new java.util.ArrayList<>();
        nomes.addAll(Arrays.asList("Sr. Verde", "Srta. Scarlet", "Coronel Mustard", "Professor Plum", "Sra. Peacock", "Sra. White"));
        nomes.addAll(Arrays.asList("Corda", "Cano de Chumbo", "Faca", "Chave Inglesa", "Castiçal", "Revólver"));
        nomes.addAll(Arrays.asList("Cozinha", "Salão de Jogos", "Sala de Jantar", "Escritório", "Biblioteca", "Sala de Estar", "Jardim de Inverno", "Hall", "Sala de Música"));
        return nomes;
    }
}