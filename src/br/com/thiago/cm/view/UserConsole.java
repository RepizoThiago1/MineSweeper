package br.com.thiago.cm.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.thiago.cm.exceptions.ExplosionException;
import br.com.thiago.cm.exceptions.LeaveException;
import br.com.thiago.cm.model.Board;

public class UserConsole {

  private Board board;
  private Scanner input = new Scanner(System.in);

  public UserConsole(Board board) {
    this.board = board;
    initGame();
  }

  // Método responsavel pela inicialização e finalização do jogo -> enquanto a
  // variavel toBeContinued for verdadeira e o fluxo do jogo quebrar é questionado
  // ao usuário se ele quer ou não jogar outra partida.
  private void initGame() {
    try {
      boolean toBeContinued = true;

      while (toBeContinued) {
        gameCycle();
        System.out.println("Outra partida? (S/n): ");
        String option = input.nextLine();
        if ("n".equalsIgnoreCase(option)) {
          toBeContinued = false;
        } else {
          board.resetBoard();
        }
      }
    } catch (LeaveException e) {
      System.out.println("Tchau até mais !");
    } finally {
      input.close();
    }
  }

  // Método responsavél pelo ciclo do jogo -> A condição de vitoria do jogo é
  // quando o objetivo da classe Board estiver completo. Enquanto isso acontecerá
  // um loop que fará a interação do jogador com o console, o jogador pode:
  // 1. Digitar as coordenadas X, Y ou sair do jogo;
  // 2. Após digitado as coordenadas o jogador pode escolher abrir ou marcar o
  // campo.
  // O processo de armazenamento das coordenadas é através de uma função
  // (getUserInput()) e assim postas em um Iterator do tipo integer que é um
  // array, no qual há o método de separar o que foi digitado através do método
  // .split(",") e assim mapeado as 2 strings passam por um tratamento de
  // conversão de String(já que o método getUserInput() retorna uma String) para
  // int com o Integer.parseInt(e.trim()). E o parametro .trim() faz com que o
  // tratamento seja melhor pois somente capturará as coordenadas.
  private void gameCycle() {
    try {
      while (!board.boardObjective()) {
        System.out.println(board);
        String userInput = getUserInput("Digite (X, Y) ou 'sair' para encerrar o jogo: ");
        Iterator<Integer> coordenatesInput = Arrays.stream(userInput.split(","))
            .map(e -> Integer.parseInt(e.trim()))
            .iterator();

        userInput = getUserInput("1 Abrir o campo ou 2 - (Des)Marcar o campo: ");
        if ("1".equals(userInput)) {
          board.openMineField(coordenatesInput.next(), coordenatesInput.next());
        } else if ("2".equals(userInput)) {
          board.checkToggle(coordenatesInput.next(), coordenatesInput.next());
        }
      }
      System.out.println(board);
      System.out.println("Voce ganhou!!");
    } catch (ExplosionException e) {
      System.out.println(board);
      System.out.println("Voce perdeu :( ");
    }
  }

  // Método que captura o input do usuário baseado no output digitado na função.
  // caso o usuário digitar sair é lançado a exceção de saída
  private String getUserInput(String outPut) {
    System.out.print(outPut);
    String userInput = input.nextLine();
    if ("sair".equalsIgnoreCase(userInput)) {
      throw new LeaveException();
    }
    return userInput;
  }
}
