import java.util.InputMismatchException;
import java.util.Scanner;

import br.com.thiago.cm.model.Board;
import br.com.thiago.cm.view.UserConsole;

public class App {

    public static void main(String[] args) throws Exception {

        Scanner input = new Scanner(System.in);
        boolean menu = true;
        System.out.println("Olá seja bem vindo ao campo minado");
        System.out.print(
                "Selecione a dificuldade 1.Facil (3x3, 6 minas), 2.Médio(6x6, 11 minas)  3.Dificil(9x9, 30 minas) 4.Quero criar meu campo: ");
        while (menu) {
            try {
                int option = input.nextInt();
                if (option == 1) {
                    input.close();
                    Board board = new Board(3, 3, 6);
                    new UserConsole(board);
                } else if (option == 2) {
                    input.close();
                    Board board = new Board(6, 6, 11);
                    new UserConsole(board);
                } else if (option == 3) {
                    input.close();
                    Board board = new Board(9, 9, 30);
                    new UserConsole(board);
                } else if (option == 4) {
                    System.out.print("Digite quantas linhas: ");
                    int lines = input.nextInt();
                    System.out.print("Digite quantas colunas: ");
                    int columns = input.nextInt();
                    System.out.print("Digite quantas minas: ");
                    int mines = input.nextInt();
                    input.close();

                    Board board = new Board(lines, columns, mines);
                    new UserConsole(board);
                } else {
                    System.out.println("Por favor digite uma das opções acima: ");
                }

            } catch (InputMismatchException e) {
                System.out.println("Por favor digite apenas números");
                menu = false;
                System.out.println("Aplicação desligando...");
            }
        }
    }
}
