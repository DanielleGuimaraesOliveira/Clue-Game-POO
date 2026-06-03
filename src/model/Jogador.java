package model;
import java.util.*;

import Interfaces.ICarta;
import Interfaces.ICasa;
import Interfaces.IJogador;

// Classe de domínio agora é package-private e implementa a interface pública IJogador
class Jogador implements IJogador {
	
	private String nome;
	private PecaSuspeito personagem;
	private List<Carta> mao;
	private boolean eliminado;
	private boolean possuiBlocoDeNotas; 
	private BlocoDeNotas blocoDeNotas;
	
	public Jogador(String nome, PecaSuspeito personagem) {
		this.nome = nome;
		this.personagem = personagem;
		this.mao = new ArrayList<>();
		this.eliminado = false;
		this.possuiBlocoDeNotas = false;
	}
	
	public void recebeCartas(Carta carta) {
		 mao.add(carta);
	}

	public void adicionarCarta(Carta carta) {
		mao.add(carta);
	}

	public void limparMao() {
		mao.clear();
	}
	
	public Carta mostrarCarta(ICarta palpite) {
		// se o jogador tiver a carta, ele mostra
		for (Carta c : mao) {
			if (c.getNome().equals(palpite.getNome())) {
				return c;
			}
		}

		return null;
	}
	
	public PecaSuspeito getPersonagem() {
		return personagem;
	}

	@Override
	public ICasa getPosicaoAtual() {
		return personagem.getPosicaoAtual();
	}

	public String getNome() {
		return nome;
	}
	
	public java.util.List<ICarta> getMao() {
		// retorna uma cópia segura como lista de ICarta para não expor a implementação
		return new ArrayList<ICarta>(mao);
	}
	
	public boolean isEliminado() {
	    return eliminado;
	}
	
	public void receberBlocoDeNotas() {
		this.possuiBlocoDeNotas = true;
		if (this.blocoDeNotas == null) this.blocoDeNotas = new BlocoDeNotas();
	}

	public void setPossuiBlocoDeNotas(boolean possuiBlocoDeNotas) {
		this.possuiBlocoDeNotas = possuiBlocoDeNotas;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
	
	public boolean isPossuiBlocoDeNotas() {
		return possuiBlocoDeNotas;
	}

	// package-private access for GerenciadorDePartida to manage bloco
	BlocoDeNotas getBlocoDeNotasInterno() {
		if (blocoDeNotas == null) blocoDeNotas = new BlocoDeNotas();
		return blocoDeNotas;
	}
}
