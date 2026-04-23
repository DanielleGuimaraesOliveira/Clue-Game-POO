import java.util.*;

public class Comodo {

	private String nome;
	private List<Casa> portas;
	private Comodo passagemSecreta; // se não tiver, é null
	private List<Peca> pecasAcomodadas;
	
	public Comodo(String nome) {
		this.nome = nome;
		this.portas = new ArrayList<>();
		this.pecasAcomodadas = new ArrayList<>();
	}
	
	public void adicionarPeca(Peca peca) {
		pecasAcomodadas.add(peca);
	}
	
	public void removerPeca(Peca peca) {
		pecasAcomodadas.remove(peca);
	}
}
