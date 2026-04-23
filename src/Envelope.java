public class Envelope {
	
	private Carta assassino;
	private Carta arma;
	private Carta local;
	
	public Envelope(Carta assassino, Carta arma, Carta local) {
		this.assassino = assassino;
		this.arma = arma;
		this.local = local;
	}
	
	public boolean verificarAcusacao(Carta suspeito, Carta arma, Carta local) {
		return 
				this.assassino.getNome().equals(suspeito.getNome()) &&
				this.arma.getNome().equals(arma.getNome()) &&
				this.local.getNome().equals(local.getNome());
	}
}
