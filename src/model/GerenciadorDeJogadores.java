package model;
import java.util.*;

import Interfaces.ICarta;

class GerenciadorDeJogadores {
    private List<Jogador> jogadores;
    private Jogador jogadorAtual;

    public GerenciadorDeJogadores() {
    	this.jogadores = new ArrayList<>();
    }

    public void distribuirBlocoDeNotas() {
    	for (Jogador j : jogadores) {
    		j.receberBlocoDeNotas();
    	}
    }

    public void definirPrimeiroJogador() {
        if (!jogadores.isEmpty()) {
            jogadorAtual = jogadores.get(0);
        }
    }
    
    public void adicionarNPC(PecaSuspeito peca) {
        Jogador npc = new Jogador("NPC", peca);
        npc.setEliminado(true);

        jogadores.add(npc);
    }
    
    public void adicionarNPCsAusentes(GerenciadorDeTabuleiro gerTabuleiro) {

        List<String> todosPersonagens = new ArrayList<>(Arrays.asList(
            "Srta. Scarlet",
            "Coronel Mustard",
            "Sra. White",
            "Rev. Green",
            "Sra. Peacock",
            "Prof. Plum"
        ));

        for (Jogador jogador : jogadores) {
            todosPersonagens.remove(
                jogador.getPersonagem().getNome()
            );
        }

        for (String nomePersonagem : todosPersonagens) {

            PecaSuspeito suspeito =
                gerTabuleiro.buscarSuspeito(nomePersonagem);

            adicionarNPC(suspeito);
        }
    }

    public void adicionarJogador(
    	    String nome,
    	    PecaSuspeito peca
    	) {
    	    Jogador novoJogador =
    	        new Jogador(nome, peca);

    	    jogadores.add(novoJogador);
    	}

    public void proximoTurno() {
    	int contagem = 0;
    	
    	do {
	    	int index = jogadores.indexOf(jogadorAtual);
	    	jogadorAtual = jogadores.get((index + 1) % jogadores.size());
	    	contagem++;
    	} 
    	while (jogadorAtual.isEliminado() && contagem < jogadores.size());

    }

	public void definirJogadorAtual(Jogador jogador) {
		if (jogadores.contains(jogador)) {
			this.jogadorAtual = jogador;
		}
	}

	public Jogador buscarJogadorPorNome(String nome) {
		for (Jogador jogador : jogadores) {
			if (jogador.getPersonagem().getNome().equals(nome)) {
				return jogador;
			}
		}
		return null;
	}
	
	public List<ICarta> getCartasJogadorAtual() {

	    if (jogadorAtual == null) {
	        return new ArrayList<>();
	    }

	    return jogadorAtual.getMao();
	}

    public Jogador getJogadorAtual() {
    	return jogadorAtual;
    }

    public List<Jogador> getJogadores(){
    	return jogadores;
    }
    
    public Map<String, Boolean> obterBlocoJogadorAtual() {

        if (jogadorAtual == null) {
            return new HashMap<>();
        }

        return jogadorAtual
            .getBlocoDeNotasInterno()
            .getTodasMarcas();
    }
    
    public void marcarCartaNoBlocoJogadorAtual(
            String nomeCarta,
            boolean valor) {

        if (jogadorAtual == null) {
            return;
        }

        jogadorAtual
            .getBlocoDeNotasInterno()
            .marcar(nomeCarta, valor);
    }
    
    public void garantirBlocoParaJogadorAtual() {

        if (jogadorAtual == null) {
            return;
        }

        jogadorAtual.receberBlocoDeNotas();
    }
    
}