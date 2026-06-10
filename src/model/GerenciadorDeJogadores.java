package model;
import java.util.*;

import Interfaces.ICarta;

public class GerenciadorDeJogadores {
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

    public void adicionarJogador(String nome, String nomePersonagem) {
    	PecaSuspeito peca = new PecaSuspeito(nomePersonagem);
    	Jogador novoJogador = new Jogador(nome, peca);
    	
    	jogadores.add(novoJogador);
    }

    public void proximoTurno() {
    	int index = jogadores.indexOf(jogadorAtual);
    	jogadorAtual = jogadores.get((index + 1) % jogadores.size());
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

    // get e set
    public Jogador getJogadorAtual() {
    	return jogadorAtual;
    }

    public List<Jogador> getJogadores(){
    	return jogadores;
    }
}