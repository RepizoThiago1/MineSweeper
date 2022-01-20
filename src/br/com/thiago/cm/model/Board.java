package br.com.thiago.cm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import br.com.thiago.cm.exceptions.ExplosionException;

public class Board {

  private int lines;
  private int columns;
  private int mines;

  private final List<MineField> MINEFIELDS = new ArrayList<>();

  public Board(int lines, int columns, int mines) {
    this.lines = lines;
    this.columns = columns;
    this.mines = mines;

    createFields();
    createBoard();
    drawMines();
  }

  // Método que popula o array com campos do tipo MineField -> for dentro de outro
  // for que instancia o array com suas coordenadas.
  private void createFields() {
    for (int line = 0; line < lines; line++) {
      for (int column = 0; column < columns; column++) {
        MINEFIELDS.add(new MineField(line, column));
      }
    }
  }

  // Método que junta todos os campos populados dentro do array -> um for dentro
  // de outro for. No qual o for de dentro adiciona outro vizinho
  private void createBoard() {
    for (MineField mineField1 : MINEFIELDS) {
      for (MineField mineField2 : MINEFIELDS) {
        mineField1.addNeighbor(mineField2);
      }
    }
  }

  // Método que implementa as minas no jogo -> A implementação ocorre através de
  // coordenadas geradas aleatoriamente na linha 49 e consecutivamente armazenada
  // na variavel minesSet. Ou seja, essa variavel tanto salvará as coordenadas
  // quanto irá contar quantas vezes esse processo ocorre assim satisfazendo a
  // condição do while aonde minesSet(coordenadas e vezes que foi feito o
  // processo) < mines (quantidade de minas serão implementadas)
  private void drawMines() {
    long minesSet = 0;
    Predicate<MineField> mined = m -> m.isMined();

    do {
      int random = (int) (Math.random() * MINEFIELDS.size());
      MINEFIELDS.get(random).putMine();
      minesSet = MINEFIELDS.stream()
          .filter(mined)
          .count();
    } while (minesSet < mines);
  }

  // Método que contempla se o jogo foi terminado ou não -> faz a interação dentro
  // do array do tipo MineField e se todos os campos do tipo mineField estiverem
  // completos ele retorna true. Caso isso acontaça, na classe UserConsole o jogo
  // acaba
  public boolean boardObjective() {
    return MINEFIELDS.stream().allMatch(m -> m.fieldObjective());
  }

  // Método para reiniciar a board. A função irá interagir com o array do tipo
  // MineField e que cada campo dentro do array possui um método de resetar a si
  // mesmo. E depois é chamado o método de colocar as minas.
  public void resetBoard() {
    MINEFIELDS.stream().forEach(m -> m.resetField());
    drawMines();
  }

  // Método que abre o campo dentro do array MINEFIELD (classe Board) do tipo
  // MineField -> através da lambda expressions no qual a função filtra o array
  // através dos parametros da função (linha e coluna) e checa se é equivalente as
  // coordenadas do elemento MineField, a função acha a primeira coordenada que
  // der match (pois só haverá uma) e se estiver presente ativa a função da classe
  // MineField para este MineField em questão. Caso a função abra um campo que
  // tenha a exceção de explosão (Abrir um campo minado) o array todo é aberto e
  // mostrado no console na classe UserConsole e o erro consecutivamente é lançado

  public void openMineField(int line, int column) {
    try {
      MINEFIELDS.stream().filter(m -> m.getLINE() == line && m.getCOLUMN() == column)
          .findFirst()
          .ifPresent(m -> m.openMineField());
    } catch (ExplosionException e) {
      MINEFIELDS.forEach(m -> m.setOpen(true));
      throw e;
    }
  }

  // Método que marca/desmarca o campo dentro do array MINEFIELD (classe Board) do
  // tipo MineField -> através da lambda expressions no qual ele filtra o array
  // através dos parametros da função (linha e coluna) e checa se é equivalente as
  // coordenadas do elemento MineField, a função acha a primeira coordenada que
  // der match (pois só haverá uma) e se estiver presente ativa a função da classe
  // MineField para este
  // MineField em questão.
  public void checkToggle(int line, int column) {
    MINEFIELDS.stream().filter(m -> m.getLINE() == line && m.getCOLUMN() == column)
        .findFirst()
        .ifPresent(m -> m.checkToggle());
  }

  // Campo é Renderizado através do toString no App.java -> para renderizar o
  // campo é nescessário fazer um for dentro de for para gerar as linhas e
  // colunas, e os campos(do tipo MineFields), são gerado através do
  // StringBuilder.append(MINEFIELDS.get(i)) no qual os campos do Tipo MineFields
  // são gerados através do array DA CLASSE BOARD chamado MINEFIELDS através do
  // método .get()
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    sb.append("      ");
    for (int column = 0; column < columns; column++) {
      sb.append(column + "Y");
      sb.append(" ");
    }
    sb.append("\n");
    for (int line = 0; line < lines; line++) {
      sb.append(line + " X: ");
      for (int column = 0; column < columns; column++) {
        sb.append("|");
        sb.append(MINEFIELDS.get(i));
        sb.append("|");
        i++;
      }
      sb.append("\n");
    }

    return sb.toString();
  }
}
