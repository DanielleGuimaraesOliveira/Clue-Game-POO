package model;

import java.util.ArrayList;
import java.util.List;

class GerenciadorDeObservadores {

    private List<Observador> observadores;

    public GerenciadorDeObservadores() {
        observadores = new ArrayList<>();
    }

    public void registrar(Observador observador) {

        if (observador == null) {
            return;
        }

        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void remover(Observador observador) {
        observadores.remove(observador);
    }

    public void notificar() {
        List<Observador> copia =
            new ArrayList<>(observadores);

        for (Observador observador : copia) {
            observador.atualizar();
        }
    }
}