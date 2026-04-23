public class Carta {
	
	private String nome;
	private TipoCarta tipo;
	
	public Carta(String nome, TipoCarta tipo) {
		this.nome = nome;
		this.tipo = tipo;
	}
	
	// get 
	public String getNome() {
		return nome;
	}
	
	public TipoCarta getTipo() {
		return tipo;
	}
}
