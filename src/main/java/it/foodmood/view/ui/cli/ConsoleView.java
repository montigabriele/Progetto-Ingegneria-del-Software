package it.foodmood.view.ui.cli;

import java.math.BigDecimal;
import java.util.List;

import it.foodmood.view.ui.cli.cliinterface.CliUserInterface;
import it.foodmood.exception.BackRequestedException;
import it.foodmood.infrastructure.io.InputReader;
import it.foodmood.infrastructure.io.OutputWriter;
import it.foodmood.infrastructure.io.console.ConsoleInputReader;
import it.foodmood.infrastructure.io.console.ConsoleOutputWriter;
import it.foodmood.view.ui.theme.AnsiUiTheme;
import it.foodmood.view.ui.theme.UiTheme;

public abstract class ConsoleView implements CliUserInterface {

    protected final InputReader in;
    protected final OutputWriter out;
    protected final UiTheme theme;

    private static final String CLEAR_CONSOLE = "\033[H\033[J";
    private static final String BACK_COMMAND = "0";
    private static final String INVALID_VALUE = "Valore non valido";
    private static final String INVALID_NUMBER = "Inserisci un numero valido.";

    protected ConsoleView() {
        this.in = ConsoleInputReader.getInstance();
        this.out = new ConsoleOutputWriter();
        this.theme = new AnsiUiTheme();
    }
        
    @Override
    public void showError(String error) {
        out.println(theme.error(error));
    }

    @Override
    public void showSuccess(String success) {
        out.println(theme.success(success));
    }

    @Override
    public void showWarning(String warning) {
        out.println(theme.warning(warning));
    }

    @Override
    public void showInfo(String info) {
        out.println(theme.info(info));
    }

    @Override
    public void showBold(String bold) {
        out.println(theme.bold(bold));
    }

    @Override
    public void showSeparator(String separator) {
        out.println(separator);
    }
    
    @Override
    public void showTitle(String title) {
        out.displayTitle(title);
    }

    @Override
    public void waitForEnter(String prompt) {
        String message = (prompt == null || prompt.isBlank()) ? "Premi INVIO per continuare" : prompt;

        out.print(theme.info(message));
        in.readLine();
    }

    @Override
    public void clearScreen() {
        out.print(CLEAR_CONSOLE);
    }

    public void showExceptionMessage(String error) {
        out.println(theme.error(error));
        waitForEnter(null);
    }

    public void displayTable(List<String> headers, List<List<String>> data, List<Integer> columnWidths) {
        if(headers.size() != columnWidths.size()){
            showError("Il numero di intestazioni e la larghezza delle colonne non coincidono");
            return;
        }

        StringBuilder formatBuilder = new StringBuilder();
        for(int widht : columnWidths){
            formatBuilder.append("%-").append(widht).append("s | ");
        }
        String format = formatBuilder.toString().trim();

        int totalWidth = columnWidths.stream().mapToInt(Integer::intValue).sum()+ (columnWidths.size() * 3) - 1;        
        String separator = "═".repeat(totalWidth);
        
        showInfo(separator);
        out.println(String.format(format, headers.toArray()));
        showInfo(separator);

        for(List<String> row : data) {
            if(row.size() != headers.size()){
                showError("Una riga ha numero di colonne diverso dalle intestazioni!");
                continue;
            }
            out.println(String.format(format, row.toArray()));
            showInfo("");
        }
        showInfo(separator);
    }

    protected String askInput(String prompt){
        while(true){
            out.print(prompt);
            String input = in.readLine();

            if(input == null || input.isBlank()){
                showError("Il campo non può essere vuoto");
                continue;
            }

            return input.trim();
        }
    }

    protected String askInputOrNull(String prompt){
        out.print(prompt);
        String input = in.readLine();
        return input == null ? null : input.trim();
    }

    protected String askInputOrBack(String prompt){
        while(true){
            out.print(prompt + " [0 = indietro]: ");
            String input = in.readLine();

            if(input == null || input.isBlank()){
                showError("Il campo non può essere vuoto");
                continue;
            }
            input = input.trim();
            if(BACK_COMMAND.equalsIgnoreCase(input)){
                throw new BackRequestedException();
            }
            return input;
        }
    }

    protected boolean askConfirmation(String prompt){
        while(true){
            out.print(prompt + " (s/n): ");
            String input = in.readLine();

            if(input == null || input.isBlank()){
                showError("Risposta non valida");
                continue;
            }

            input = input.trim().toLowerCase();

            if(input.equals("s") || input.equals("si") || input.equals("y")){
                return true;
            }

            if(input.equals("no") || input.equals("n")){
                return false;
            }

            showWarning("Inserisci una risposta valida (s/n).");
        }
    }

    protected BigDecimal askBigDecimal(String prompt){
        while(true){
            out.print(prompt);
            String input = in.readLine();

            if(input == null){
                showError(INVALID_VALUE);
                continue;
            }

            input = input.replace(",", ".");

            try {
                return new BigDecimal(input);
            } catch (NumberFormatException _) {
                showError(INVALID_NUMBER + " Usa punto o virgola per i decimali.\n");
            }           
        }
    }

    protected Integer askPositiveInt(String prompt){
        while(true){
            out.print(prompt);
            String input = in.readLine();

            if(input == null){
                showError(INVALID_VALUE);
                continue;
            }

            input = input.trim();

            try {
                int positiveInt = Integer.parseInt(input);
                if(positiveInt > 0){
                    return positiveInt;
                } else {
                    showError("Inserisci un numero maggiore di zero.");
                }
            } catch (NumberFormatException _) {
                showError("Inserisci un numero intero positivo valido.\n");
            }           
        }
    }

    protected Double askDouble(String prompt){
        while(true){
            out.print(prompt);
            String input = in.readLine();

            if(input == null){
                showError(INVALID_VALUE);
                continue;
            }

            input = input.replace(",", ".");

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException _) {
                showError(INVALID_NUMBER + " Usa punto o virgola per i decimali.\n");
            }           
        }
    }

    protected Integer parseInteger(String input, int maxSize){
        int index;
        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException _) {
            showError(INVALID_NUMBER);
            waitForEnter(input);
            return null;
        }

        if(index < 0 || index > maxSize){
            showError("Indice non valido.");
            waitForEnter(null);
            return null;
        }
        return index;
    }
}
