import java.util.*;

public class Jogador {
	
	private String nome;
	private PecaSuspeito personagem;
	private List<Carta> mao;
	private boolean eliminado;
	
	public Jogador(String nome, PecaSuspeito personagem) {
		this.nome = nome;
		this.personagem = personagem;
		this.mao = new ArrayList<>();
		this.eliminado = false;
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
}
