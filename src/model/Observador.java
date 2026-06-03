package model;

public interface Observador {
    // Notification without exposing the model object; controller forwards state
    void atualizar();
}