package conecta4;

import java.util.Random;
import java.util.Scanner;

public class conecta4 {

    public static final String GROC = "\u001B[33m";
    public static final String LILA = "\u001B[35m";
    public static final String RESET = "\u001B[0m";
    public static final String VERMELL = "\u001B[31m";
    public static final String VERD = "\u001B[32m";
    private static final Scanner teclat = new Scanner(System.in);
    private static final Random random = new Random();
    private static int jugador = 1;
    private static int[] partidesGuanyades = new int[2];

    public static void main(String[] args) {
        int opcio;
        boolean jugar = true;
        int partides = 0;

        System.out.println(VERMELL + "   __________  _   _______________________       __ __");
        System.out.println(VERMELL + "  / ____/ __ \\/ | / / ____/ ____/_  __/   |     / // /");
        System.out.println(VERMELL + " / /   / / / /  |/ / __/ / /     / / / /| |    / // /_");
        System.out.println(VERMELL + "/ /___/ /_/ / /|  / /___/ /___  / / / ___ |   /__  __/");
        System.out.println(VERMELL + "\\____/\\____/_/ |_/_____/\\____/ /_/ /_/  |_|     /_/\n" + RESET);
        while (jugar) {
            partides++;
            System.out.printf("Partida número %d \n", partides);
            int files = obtenirDimensio("files");
            int columnes = obtenirDimensio("columnes");
            String[][] taulell = crearTaulell(files, columnes);
            boolean empat = false, guanyador = false;
            jugador = random.nextInt(2) + 1;
            System.out.printf("Comença el jugador número %d\n==================\n", jugador);

            while (!guanyador && !empat) {
                mostrarTaulell(taulell);
                int posicio = obtenirPosicio(columnes);
                colocarFitxa(taulell, posicio);
                guanyador = verificarGuanyador(taulell, files, columnes);
                empat = verificarEmpat(taulell);
                if (guanyador) {
                    mostrarTaulell(taulell);
                    gestionarGuanyador();
                } else if (empat) {
                    mostrarTaulell(taulell);
                    System.out.println("\nHEU EMPATAT! PARTIDA ANULADA!");
                }
                if (!guanyador && !empat) {
                    jugador = (jugador == 1) ? 2 : 1;
                }
            }
            opcio = mostrarMenu();
            jugar = (opcio == 2);
        }
    }

    /**
     * Obté les dimensions del taulell introduïda pel jugador
     *
     * @param tipus tipus de dimensió a obtenir (files o columnes)
     * @return la dimensió validada introduïda pel jugador
     */
    private static int obtenirDimensio(String tipus) {
        System.out.printf("Introdueix el nombre de %s pel taulell (mínim 4): ", tipus);
        while (!teclat.hasNextInt()) {
            System.out.print("Error! Entrada no vàlida. Introdueix un número: ");
            teclat.next();
        }
        int dimensio = teclat.nextInt();
        while (dimensio < 4) {
            System.out.print("El valor ha de ser mínim 4. Introdueix un altre valor: ");
            while (!teclat.hasNextInt()) {
                System.out.print("Error! Entrada no vàlida. Introdueix un número: ");
                teclat.next();
            }
            dimensio = teclat.nextInt();
        }
        return dimensio;
    }

    /**
     * Crea el taulell de joc buit amb les dimensions especificades
     *
     * @param files    nombre de files del taulell
     * @param columnes nombre de columnes del taulell
     * @return array bidimensional que representa el taulell de joc
     */
    private static String[][] crearTaulell(int files, int columnes) {
        String[][] taulell = new String[files][columnes];
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                taulell[i][j] = " ";
            }
        }
        return taulell;
    }

    /**
     * Mostra el taulell de joc a la consola, amb les fitxes dels jugadors
     *
     * @param taulell taulell a mostrar
     */
    private static void mostrarTaulell(String[][] taulell) {
        System.out.print("  ");
        for (int i = 1; i <= taulell[0].length; i++)
            System.out.print(" " + i);
        System.out.println();
        for (int i = 0; i < taulell.length; i++) {
            System.out.print((taulell.length - i) + " ");
            for (String cell : taulell[i]) {
                String cellDisplay = cell.equals("X") ? LILA + cell + RESET
                        : cell.equals("O") ? GROC + cell + RESET : " ";
                System.out.print("|" + cellDisplay);
            }
            System.out.println("|");
        }
    }

    /**
     * Obté la columna en la qual el jugador vol col·locar la seva fitxa
     *
     * @param columnes nombre total de columnes del taulell
     * @return la columna triada pel jugador on col·locar la fitxa del jugador
     */
    private static int obtenirPosicio(int columnes) {
        System.out.printf("Jugador %d, introdueix la columna per posar la fitxa: ", jugador);
        while (!teclat.hasNextInt()) {
            System.out.print("Error! Entrada no vàlida. Introdueix un número: ");
            teclat.next();
        }
        int col = teclat.nextInt() - 1;
        while (col < 0 || col >= columnes) {
            System.out.print("Columna no vàlida. Torna a introduir una columna: ");
            while (!teclat.hasNextInt()) {
                System.out.print("Error! Entrada no vàlida. Introdueix un número: ");
                teclat.next();
            }
            col = teclat.nextInt() - 1;
        }
        return col;
    }

    /**
     * Col·loca la fitxa del jugador en la columna indicada del taulell
     *
     * @param taulell taulell on es col·loca la fitxa
     * @param columna columna on el jugador vol col·locar la fitxa
     */
    private static void colocarFitxa(String[][] taulell, int columna) {
        for (int fila = taulell.length - 1; fila >= 0; fila--) {
            if (taulell[fila][columna].equals(" ")) {
                taulell[fila][columna] = (jugador == 1) ? "X" : "O";
                return;
            }
        }
        System.out.println("Columna plena! Escull una altra columna.");
    }

    /**
     * Comprova si hi ha un guanyador
     *
     * @param taulell  taulell a comprovar
     * @param files    nombre de files del taulell
     * @param columnes nombre de columnes del taulell
     * @return {@code true} si hi ha un guanyador; {@code false} si no hi ha
     *         guanyador
     */
    private static boolean verificarGuanyador(String[][] taulell, int files, int columnes) {
        String simbol = (jugador == 1) ? "X" : "O";

        for (int fila = 0; fila < files; fila++) {
            for (int col = 0; col <= columnes - 4; col++) {
                if (taulell[fila][col].equals(simbol) && taulell[fila][col + 1].equals(simbol) &&
                        taulell[fila][col + 2].equals(simbol) && taulell[fila][col + 3].equals(simbol)) {
                    return true;
                }
            }
        }

        for (int col = 0; col < columnes; col++) {
            for (int fila = 0; fila <= files - 4; fila++) {
                if (taulell[fila][col].equals(simbol) && taulell[fila + 1][col].equals(simbol) &&
                        taulell[fila + 2][col].equals(simbol) && taulell[fila + 3][col].equals(simbol)) {
                    return true;
                }
            }
        }

        for (int fila = 0; fila <= files - 4; fila++) {
            for (int col = 0; col <= columnes - 4; col++) {
                if (taulell[fila][col].equals(simbol) && taulell[fila + 1][col + 1].equals(simbol) &&
                        taulell[fila + 2][col + 2].equals(simbol) && taulell[fila + 3][col + 3].equals(simbol)) {
                    return true;
                }
            }
        }
        for (int fila = 3; fila < files; fila++) {
            for (int col = 0; col <= columnes - 4; col++) {
                if (taulell[fila][col].equals(simbol) && taulell[fila - 1][col + 1].equals(simbol) &&
                        taulell[fila - 2][col + 2].equals(simbol) && taulell[fila - 3][col + 3].equals(simbol)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Comprova si totes les cel·les estan ocupades per determinar si hi ha empat
     *
     * @param taulell Taulell de la partida actual
     * @return {@code true} si hi ha empat; {@code false} en cas contrari
     */
    private static boolean verificarEmpat(String[][] taulell) {
        for (String[] fila : taulell) {
            for (String cell : fila) {
                if (cell.equals(" "))
                    return false;
            }
        }
        return true;
    }

    /**
     * Anuncia el guanyador i incrementa el seu comptador de partides guanyades
     */
    private static void gestionarGuanyador() {
        System.out.printf("\nPARTIDA ACABADA\n" + VERD + "El guanyador és el jugador %d\n" + RESET, jugador);
        partidesGuanyades[jugador - 1]++;
    }

    /**
     * Mostra el menú principal i retorna l’opció triada
     *
     * @return int que representa l'opció seleccionada pel jugador
     */
    private static int mostrarMenu() {
        System.out.println("\nMENÚ\n========\n1. Sortir\n2. Tornar a jugar\n3. Estadístiques\n\nOpció: ");
        while (!teclat.hasNextInt()) {
            System.out.print("Error! Entrada no vàlida. Introdueix un número: ");
            teclat.next();
        }
        int opcio = teclat.nextInt();
        if (opcio == 3)
            mostrarEstadistiques();
        return opcio;
    }

    /**
     * Mostra les victòries acumulades per cada jugador
     */
    private static void mostrarEstadistiques() {
        System.out.println("\nESTADÍSTIQUES\n==========");
        for (int i = 0; i < partidesGuanyades.length; i++) {
            System.out.printf("El jugador %d ha guanyat %d partides.\n", i + 1, partidesGuanyades[i]);
        }
        mostrarMenu();
    }
}
