package br.com.thiago.cm.model;

import java.util.ArrayList;
import java.util.List;
import br.com.thiago.cm.exceptions.ExplosionException;

public class MineField {
  private final int LINE;
  private final int COLUMN;

  private boolean open;
  private boolean mined;
  private boolean checked;

  private List<MineField> neighbors = new ArrayList<MineField>();

  public MineField(int LINE, int COLUMN) {
    this.LINE = LINE;
    this.COLUMN = COLUMN;
  }

  // Método que resolve o objetivo do campo -> ele é resolvido se não estiver
  // minado e aberto || ou alertado se estiver minado e checado(bandeira)
  boolean fieldObjective() {
    boolean solved = !mined && open;
    boolean warned = mined && checked;
    return solved || warned;
  }

  // Método que faz um retorno visual de quantos campos ao redor do mesmo podem
  // estar minado
  long neighborMineAlert() {
    return neighbors.stream().filter(n -> n.mined).count();
  }

  // Método que reseta a instancia Minefield
  void resetField() {
    open = false;
    mined = false;
    checked = false;
  }

  // Método de adicionar uma outra instancia MineField -> esse metodo retorna true
  // ou false se através das seguintes filtragens:
  // 1. Se a nova instancia não está na mesma coordenada que a instancia atual.
  // 2. Se a nova instancia estiver na horizontal ou vertical ela deve ter 1 casa
  // de distancia da Instancia atual.
  // 3. Se a nova instancia estiver duas casas de distancia da instancia atual. A
  // nova instancia deve estar na diagonal. Caso contrario não poderá ser feito a
  // nova instancia
  public boolean addNeighbor(MineField neighbor) {
    boolean differentLine = this.LINE != neighbor.LINE;
    boolean differentColumn = this.COLUMN != neighbor.COLUMN;
    boolean diagonal = differentLine && differentColumn;

    int deltaLine = Math.abs(this.LINE - neighbor.LINE);
    int deltaColumn = Math.abs(this.COLUMN - neighbor.COLUMN);
    int generalDelta = deltaLine + deltaColumn;

    if (generalDelta == 1 && !diagonal) {
      neighbors.add(neighbor);
      return true;
    } else if (generalDelta == 2 && diagonal) {
      neighbors.add(neighbor);
      return true;
    } else {
      return false;
    }
  }

  // Método que marca a instancia(campo) com a bandeira. atributos são por padrão
  // iniciados como false.
  public void checkToggle() {
    if (!open) {
      checked = !checked;
    }
  }

  // Método de abertura da instancia -> é feito a verificação se a mesma está
  // marcada ou não, caso ela passe nesse teste é ainda necessário checar se a
  // instancia está minada, caso esteja é lançado um erro de explosão. Caso não
  // esteja minado é feito um teste nos vizinhos até aonde os campos podem ser
  // abertos
  public boolean openMineField() {
    if (!open && !checked) {
      open = true;
      if (mined) {
        throw new ExplosionException();
      }
      if (neighborsMineCheck()) {
        neighbors.forEach(n -> n.openMineField());
      }
      return true;
    } else {
      return false;
    }
  }

  // Método que faz a checagem até aonde pode ser aberto.
  public boolean neighborsMineCheck() {
    return neighbors.stream().noneMatch(n -> n.mined);
  }

  // Método que coloca as minas
  public void putMine() {
    mined = true;
  }

  // Método que retorna se esta checado ou não (bandeira no jogo)
  public boolean isChecked() {
    return checked;
  }

  // Método que retorna se esta aberto ou não
  public boolean isOpen() {
    return open;
  }

  // Método que realmente muda a instancia para abrir
  void setOpen(boolean open) {
    this.open = open;
  }

  // Método que retorna se esta minado ou não
  public boolean isMined() {
    return mined;
  }

  // Método que retorna a linha da instancia
  public int getLINE() {
    return LINE;
  }

  // Método que retorna a coluna da instancia
  public int getCOLUMN() {
    return COLUMN;
  }

  // Método que faz a renderização de uma instancia no campo ->
  // 1.Retorna X caso a instancia esteja marcado/checado;
  // 2.Retorna * caso a instancia esteja aberta e minada;
  // 3.Retorna visualmente a qtd de minas em volta do campo;
  // 4.Retorna " " caso a instancia esteja aberta;
  // 5.Seria a condição inicial da instancia.
  public String toString() {
    if (checked) {
      return "x";
    } else if (open && mined) {
      return "*";
    } else if (open && neighborMineAlert() > 0) {
      return Long.toString(neighborMineAlert());
    } else if (open) {
      return " ";
    } else {
      return "?";
    }
  }
}
