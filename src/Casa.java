
public class Casa {
	
	private int x;
	private int y;
	
	private boolean ehPorta;
	private Peca ocupante;
	
	public Casa( int x, int y, boolean ehPorta) {
		this.x = x;
		this.y = y;
		this.ehPorta = ehPorta;
	}
	
	// sobre carga de constructor
	public Casa(int x, int y) {
		this.x = x;
		this.y = y;
		this.ehPorta = false;
	}
	
	public boolean estaOcupada() {
		return ocupante != null;
	}
	
	// get e set
	public Peca getOcupante() {
		return ocupante;
	}
	
	public void setOcupante(Peca peca) {
		this.ocupante = peca;
	}
}
