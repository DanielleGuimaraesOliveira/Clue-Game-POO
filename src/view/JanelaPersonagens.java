package view;

import javax.swing.JFrame;

import model.GerenciadorDePartida;


public class JanelaPersonagens extends JFrame {
 private GerenciadorDePartida gerenciador;

 public JanelaPersonagens(GerenciadorDePartida gerenciador) {
     this.gerenciador = gerenciador;
     

     setSize(1500, 800); 
     
     
 }
}