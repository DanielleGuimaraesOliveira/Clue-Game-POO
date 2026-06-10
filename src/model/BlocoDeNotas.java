package model;

import java.util.*;

// Package-private notebook model storing boolean marks per card name
class BlocoDeNotas {
    private Map<String, Boolean> marcado = new HashMap<>();

    public void marcar(String nomeCarta, boolean valor) {
        marcado.put(nomeCarta, valor);
    }

    public boolean estaMarcado(String nomeCarta) {
        Boolean v = marcado.get(nomeCarta);
        return v != null && v.booleanValue();
    }

    public Map<String, Boolean> getTodasMarcas() {
        return new HashMap<>(marcado);
    }

    public void setTodasMarcas(Map<String, Boolean> mapa) {
        this.marcado.clear();
        if (mapa != null) this.marcado.putAll(mapa);
    }
}
