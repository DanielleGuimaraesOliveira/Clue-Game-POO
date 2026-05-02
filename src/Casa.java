
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
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public Peca getOcupante() {
		return ocupante;
	}
	
	public void setOcupante(Peca peca) {
		this.ocupante = peca;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (!(o instanceof Casa)) return false;
	    Casa c = (Casa) o;
	    return x == c.x && y == c.y;
	}

	@Override
	public int hashCode() {
	    return java.util.Objects.hash(x, y);
	}
	
}
