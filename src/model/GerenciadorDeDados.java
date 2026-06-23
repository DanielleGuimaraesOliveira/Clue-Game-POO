package model;

import java.util.ArrayList;
import java.util.List;

class GerenciadorDeDados {

    private List<Dado> dados;
    private int ultimoDado1;
    private int ultimoDado2;
    private int valorDados;

    public GerenciadorDeDados() {
        dados = new ArrayList<>();
        dados.add(new Dado());
        dados.add(new Dado());
    }

    public int[] lancarDados() {

        ultimoDado1 = dados.get(0).rolar();
        ultimoDado2 = dados.get(1).rolar();

        valorDados = ultimoDado1 + ultimoDado2;

        return new int[] { ultimoDado1, ultimoDado2 };
    }

    public void definirResultadoDados(int dado1, int dado2) {
        this.ultimoDado1 = dado1;
        this.ultimoDado2 = dado2;
        this.valorDados = dado1 + dado2;
    }

    public void zerarDados() {
        this.ultimoDado1 = 0;
        this.ultimoDado2 = 0;
        this.valorDados = 0;
    }

    public int getUltimoDado1() {
        return ultimoDado1;
    }

    public int getUltimoDado2() {
        return ultimoDado2;
    }

    public int getValorDados() {
        return valorDados;
    }
}