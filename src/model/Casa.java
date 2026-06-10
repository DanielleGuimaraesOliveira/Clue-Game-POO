package model;

import Interfaces.ICasa;

class Casa implements ICasa {
	
	private int x;
	private int y;
	
	private String tipo; // rcebe o caracter do arquivo txt
	private Peca ocupante;
	
	public Casa( int x, int y,String tipo) {
		this.x = x;
		this.y = y;
		this.tipo = tipo;
	}
	
	// Get e Set
	public String getTipo() {
		return this.tipo;
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
	
	// Regra de negócio
	public boolean isCaminhavel() {
		// a peca só pode andar nos corredores ("1") e portas ("p")
		return tipo.equals("1") || tipo.equals("p");
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
