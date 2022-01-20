package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import br.com.thiago.cm.exceptions.ExplosionException;
import br.com.thiago.cm.model.MineField;

public class MineFieldTest {

  private MineField minefield = new MineField(3, 3);

  @Test
  // Teste padrão de vizinhos na horizontal/vertical com 1 de distancia
  public void neighborTestDistance1() {
    var neighbor = new MineField(3, 2);
    boolean result = minefield.addNeighbor(neighbor);
    assertTrue(result);
  }

  @Test
  // Teste de vizinhos na diagonal
  public void neighborTestDiagonal() {
    var neighbor = new MineField(4, 2);
    boolean result = minefield.addNeighbor(neighbor);
    assertTrue(result);
  }

  @Test
  // Teste induzindo falha para ver se há a possibilidade de ter vizinho com 2
  // casas de distancia na horizontal/vertical -> deve falhar pois só pode haver 2
  // campos de distancia na diagonal
  public void neighborTestFail() {
    var neighbor = new MineField(1, 1);
    boolean result = minefield.addNeighbor(neighbor);
    assertFalse(result);
  }

  @Test
  // Teste para ver se o campo inicializa marcado ou não -> precisa inicializar
  // não marcado
  public void checkedDefaultTest() {
    assertFalse(minefield.isChecked());
  }

  @Test
  // Teste para checar se é possivel marcar o campo
  public void checkedToggleTest() {
    minefield.checkToggle();
    assertTrue(minefield.isChecked());
  }

  @Test
  // Teste para ver se é possivel marcar mais de uma vez o campo
  public void checkedMultipleToggleTest() {
    minefield.checkToggle();
    minefield.checkToggle();
    assertFalse(minefield.isChecked());
  }

  @Test
  // Teste para abrir um campo padrão(sem minas/marcações)
  public void openMineFieldDefaultTest() {
    assertTrue(minefield.openMineField());
  }

  @Test
  // Teste para abrir o campo somente marcado -> não deve abrir pois esta marcado
  public void openMineFieldCheckedTest() {
    minefield.checkToggle();
    assertFalse(minefield.openMineField());
  }

  @Test
  // Teste para abrir o campo marcado e minado -> não deve abrir pois esta marcado
  public void openMineFieldMinedAndCheckedTest() {
    minefield.checkToggle();
    minefield.putMine();
    assertFalse(minefield.openMineField());
  }

  @Test
  // Teste para abrir o campo que tem uma mina -> irá garantir o tipo da exceção
  public void openMineFieldMinedTest() {
    minefield.putMine();
    assertThrows(ExplosionException.class, () -> {
      minefield.openMineField();
    });
  }

  @Test
  // Teste para ver até aonde o campo vai ser aberto -> tendo em vista que o
  // vizinho 11 e o vizinho 22 não possui minas e não estão marcados eles devem
  // abrir
  public void mineFieldOpenSpreadTest() {
    MineField neighbor11 = new MineField(1, 1);
    MineField neighbor22 = new MineField(2, 2);
    neighbor22.addNeighbor(neighbor11);
    minefield.addNeighbor(neighbor22);
    minefield.openMineField();
    assertTrue(neighbor22.isOpen() && neighbor11.isOpen());
  }

  @Test
  // Teste para ver o spread do campo se algum dos vizinhos estiverem minados
  public void mineFieldOpenSpreadTest2() {
    MineField neighbor11 = new MineField(1, 1);
    MineField neighbor12 = new MineField(1, 2);
    MineField neighbor22 = new MineField(2, 2);
    neighbor12.putMine();
    neighbor22.addNeighbor(neighbor12);
    neighbor22.addNeighbor(neighbor11);
    minefield.addNeighbor(neighbor22);
    minefield.openMineField();
    assertTrue(neighbor22.isOpen() && !neighbor11.isOpen());
  }
}
