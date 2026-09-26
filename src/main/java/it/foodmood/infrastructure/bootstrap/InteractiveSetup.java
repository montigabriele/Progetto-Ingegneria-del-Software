package it.foodmood.infrastructure.bootstrap;

import it.foodmood.config.PersistenceMode;
import it.foodmood.config.StartupEnvironment;
import it.foodmood.infrastructure.io.InputReader;
import it.foodmood.infrastructure.io.OutputWriter;
import it.foodmood.view.ui.theme.UiTheme;

public final class InteractiveSetup {

    private static final String CLEAR_CONSOLE = "\033[H\033[J";

    private final InputReader in;
    private final OutputWriter out;
    private final UiTheme theme;

    public InteractiveSetup(InputReader in, OutputWriter out, UiTheme theme) {
        this.in = in;
        this.out = out;
        this.theme = theme;
    }

    public StartupEnvironment askUser() {
        clearScreen();
        out.displayTitle("FoodMood");
        out.println("\nBenvenuto in FoodMood, procedi con la configurazione del software\n");

        UiMode uiMode = askUiMode();
        PersistenceMode pm = askPersistenceMode();

        return new StartupEnvironment(uiMode, pm);
    }

    private UiMode askUiMode() {
        while (true) {
            out.displayTitle("Seleziona Interfaccia");
            out.println("");
            out.println(
                    theme.success("1. CLI ")
                            + theme.info("-> Interfaccia a riga di comando")
            );
            out.println(
                    theme.success("2. GUI ")
                            + theme.info("-> Interfaccia grafica")
            );
            out.print("\nScelta: ");

            String input = readInput();

            if ("1".equals(input)) {
                return UiMode.CLI;
            }

            if ("2".equals(input)) {
                return UiMode.GUI;
            }

            clearScreen();
            out.println(theme.warning("Scelta non valida.\n"));
        }
    }

    private PersistenceMode askPersistenceMode() {
        while (true) {
            out.displayTitle("Seleziona Modalità di Persistenza");
            out.println("");
            out.println("\nLa modalità determina dove vengono salvati i dati\n");

            out.println(
                    theme.success("1. DEMO        ")
                            + theme.info(
                                    "-> Nessun salvataggio, dati in memoria volatile"
                            )
            );

            out.println(
                    theme.success("2. FILESYSTEM  ")
                            + theme.info(
                                    "-> Salvataggio in file CSV locali (cartella /data/csv/ )"
                            )
            );

            out.println(
                    theme.success("3. FULL        ")
                            + theme.info(
                                    "-> Salvataggio completo tramite database MySQL"
                            )
            );

            out.print("\nScelta: ");

            String input = readInput();

            if ("1".equals(input)) {
                return PersistenceMode.DEMO;
            }

            if ("2".equals(input)) {
                return PersistenceMode.FILESYSTEM;
            }

            if ("3".equals(input)) {
                return PersistenceMode.FULL;
            }

            clearScreen();
            out.println(theme.warning("Scelta non valida.\n"));
        }
    }

    private String readInput() {
        String input = in.readLine();

        if (input == null) {
            throw new IllegalStateException("Input non disponibile");
        }

        return input.trim();
    }

    public void clearScreen() {
        out.print(CLEAR_CONSOLE);
    }
}