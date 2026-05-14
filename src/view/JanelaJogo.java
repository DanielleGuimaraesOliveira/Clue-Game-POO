package view;

import javax.swing.JFrame;

import model.GerenciadorDePartida;

//No pacote view
public class JanelaJogo extends JFrame {
 private GerenciadorDePartida gerenciador;

 public JanelaJogo(GerenciadorDePartida gerenciador) {
     this.gerenciador = gerenciador;
     

     setSize(1200, 800); 
     
     
 }
}