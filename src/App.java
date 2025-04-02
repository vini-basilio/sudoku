import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.ArrayList;

import br.com.dio.util.BoardTemplate;
import br.com.dio.util.model.Board;
import br.com.dio.util.model.Space;

public class App {

    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) throws Exception {
        final var positions = Stream.of(args)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]));

        var option = -1;
        while (true) {
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }
        }
    }

    private static void startGame(final Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var positionsConfig = positions.get("%s,%s".formatted(i, j));
                var expected = Integer.parseInt(positionsConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionsConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);

                spaces.get(i).add(currentSpace);
            }
        }
    }

    private static void inputNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Informe a coluna em que deseja inserir o número");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha em que deseja inserir o número");
        var row = runUntilGetValidNumber(0, 8);
        System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);
        var value = runUntilGetValidNumber(1, 9);
        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s,%s] tem valor fixo \n");
        }
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }
        System.out.println("Informe a coluna em que deseja limpar o número");
        var col = runUntilGetValidNumber(0, 8);
        System.out.println("Informe a linha em que deseja limpar o número");
        var row = runUntilGetValidNumber(0, 8);
        if (!board.clearSpace(col, row)) {
            System.out.printf("A posição [%s,%s] tem valor fixo \n");
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
        }
        var args = new Object[81];
        var argPos = 0;

        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (var col : board.getSpaces()) {
                args[argPos++] = " " + (isNull(col.get(i).getActual()) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Seu jogo se encontra da seguinte forma");
        System.err.printf(BoardTemplate.BOARD_TEMPLATE + "\n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
        }
        System.out.printf("O jogo se encontra com o seguinte estado %s",
                board.getStatus().getLabel());
        if (board.hasErros()) {
            System.out.println("O jogo contém erros");

        } else {
            System.out.println("O jogo não contém erros");
        }
    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
        }
        System.out.println("Tem certeza que deseja limpar todo seu jogo e perder seu progresso? [Sim/não]");

        var confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("sim") || !confirm.equalsIgnoreCase("não")) {
            System.out.println("Informe sim ou não");
            confirm = scanner.next();
        }
        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
        }
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
        }
        if (board.gameIsFinished()) {
            System.out.println("Parabéns você concluiu o jogo");
            showCurrentGame();
            board = null;
        } else if (board.hasErros()) {
            System.out.println("Seu jogo contém erros");
        } else {
            System.out.println("Você ainda precisa preencher algum espaço");
        }
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        var current = scanner.nextInt();

        while (current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }

        return current;
    }

}
