package model;
import java.util.*;

class Jogador {
	
	private String nome;
	private PecaSuspeito personagem;
	private List<Carta> mao;
	private boolean eliminado;
	private boolean possuiBlocoDeNotas; 
	
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
	
	public Carta mostrarCarta(Carta palpite) {
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
	
	public List<Carta> getMao() {
		return mao;
	}
	
	public boolean isEliminado() {
	    return eliminado;
	}
	
	public void receberBlocoDeNotas() {
		this.possuiBlocoDeNotas = true;
	}
	
	public boolean isPossuiBlocoDeNotas() {
		return possuiBlocoDeNotas;
	}
}
