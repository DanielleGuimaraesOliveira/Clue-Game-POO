package model;

import java.util.*;

class Baralho {
	private List<Carta> cartas;
	
	public Baralho() {
		cartas = new ArrayList<>();
		inicializaCartas();
	}
	
	private void inicializaCartas() {
		adicionaCartas(new String[]{"Sr. Verde", "Srta. Scarlet", "Coronel Mustard",
	            "Professor Plum", "Sra. Peacock", "Sra. White"}, TipoCarta.SUSPEITO);
	
		adicionaCartas(new String[]{"Corda", "Cano de Chumbo", "Faca",
	            "Chave Inglesa", "Castiçal", "Revólver"}, TipoCarta.ARMA);
		
		adicionaCartas(new String[]{"Cozinha", "Salão de Baile", "Sala de Jantar",
	            "Escritório", "Biblioteca", "Sala de Estar",
	            "Jardim de Inverno", "Hall", "Sala de Música"}, TipoCarta.COMODO);
	}
	
	private void adicionaCartas(String[] nomes, TipoCarta tipo) {
		for(String nome: nomes) {
			cartas.add(new Carta(nome,tipo));
		}
	}
	
	public Carta compraCarta() {
		if(cartas.isEmpty()) return null;
		return cartas.remove(0);
	}
	
	public List<Carta> filtrarPorTipo(TipoCarta tipo) {
        List<Carta> resultado = new ArrayList<>();

        for (Carta carta : cartas) {
            if (carta.getTipo() == tipo) {
                resultado.add(carta);
            }
        }

        return resultado;
    }
	
	 public void removeCarta(Carta carta) {
	        cartas.remove(carta);
	    }
	
	public void embaralhar() {
		Collections.shuffle(cartas);
	}
	
	public List<Carta> getCartas(){
		return cartas;
	}
	

}
