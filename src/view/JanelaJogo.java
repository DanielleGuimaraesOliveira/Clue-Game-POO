package view;

import javax.swing.JFrame;

import model.GerenciadorDePartida;


public class JanelaJogo extends JFrame {
 private GerenciadorDePartida gerenciador;

 public JanelaJogo(GerenciadorDePartida gerenciador) {
     this.gerenciador = gerenciador;
     

     setSize(800, 800); 
     
     
 }
}