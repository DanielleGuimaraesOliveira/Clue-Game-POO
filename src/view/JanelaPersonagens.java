package view;

import javax.swing.JFrame;

import controller.ControladorPartida;


public class JanelaPersonagens extends JFrame {
 private ControladorPartida controlador;

 public JanelaPersonagens(ControladorPartida controlador) {
     this.controlador = controlador;

     setSize(1500, 800);
 }
}