import java.util.*;

public abstract class Peca {
	private String nome;
	private Casa posicaoAtual;
	
	public Peca(String nome) {
		this.nome = nome;
	}
	
	// Get e Set
	public String getNome() {
		return nome;
	}
	
	public Casa getPosicaoAtual() {
		return posicaoAtual;
	}
	
	public void setPosicaoAtual(Casa posicao) {
		this.posicaoAtual = posicao;
	}
}
