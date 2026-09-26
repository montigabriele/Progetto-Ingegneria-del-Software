package it.foodmood.infrastructure.io.console;

import it.foodmood.infrastructure.io.OutputWriter;

public final class ConsoleOutputWriter implements OutputWriter {
    @Override
    public void print(String s){
        System.out.print(s);
    }

    @Override
    public void println(String s){
        System.out.println(s);
    }

    @Override
    public void displayTitle(String title) {
        final int WIDTH = 80;

        String text = title.toUpperCase();
        int padding = (WIDTH - text.length()) / 2;

        String space = " ".repeat(Math.max(padding, 0));
        String titleRow = String.format("║%s%s%s║", space,text, " ".repeat(WIDTH - space.length() - text.length()));

        String topBorder = "╔" + "═".repeat(WIDTH) + "╗";
        String bottomBorder = "╚" + "═".repeat(WIDTH) + "╝";

        System.out.println(topBorder);
        System.out.println(titleRow);
        System.out.println(bottomBorder);
    }

}
