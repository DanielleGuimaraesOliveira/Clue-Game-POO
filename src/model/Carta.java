package model;

import Interfaces.ICarta;

class Carta implements ICarta {
	
	private String nome;
	private TipoCarta tipo;
	
	public Carta(String nome, TipoCarta tipo) {
		this.nome = nome;
		this.tipo = tipo;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carta outra = (Carta) obj;
        return this.getNome().equals(outra.getNome());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getNome());
    }
    

	public String getNome() {
		return nome;
	}
	
	public TipoCarta getTipo() {
		return tipo;
	}
}
