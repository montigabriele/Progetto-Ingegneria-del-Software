package it.foodmood.infrastructure.io.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.foodmood.exception.ConsoleReadException;
import it.foodmood.infrastructure.io.InputReader;

public final class ConsoleInputReader implements InputReader {

    private final BufferedReader bufferedReader;
    private boolean closed;

    private ConsoleInputReader(){
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    private static class Holder{
        private static final ConsoleInputReader instance = new ConsoleInputReader();
    }

    public static ConsoleInputReader getInstance(){
        return Holder.instance;
    }

    @Override
    public String readLine(){
        if(closed){
            throw new IllegalStateException("Input da tastiera chiuso.");
        }
        try{
            return bufferedReader.readLine();
        } catch (IOException e){
            throw new ConsoleReadException("Errore nella lettura da console: ", e);
        }
    }

    @Override
    public void close(){
        closed = true;
    }

}
