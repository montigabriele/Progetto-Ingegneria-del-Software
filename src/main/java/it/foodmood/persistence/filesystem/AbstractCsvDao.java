package it.foodmood.persistence.filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.foodmood.exception.PersistenceException;

public abstract class AbstractCsvDao {
    
    protected final File file;

    protected AbstractCsvDao(String filePath){
        this.file = new File(filePath);
        createFileIfNotExists();
    }

    protected void createFileIfNotExists(){
        try {
            File parent = file.getParentFile();
            if(parent != null && !parent.exists() && !parent.mkdirs()){
                throw new PersistenceException("Impossibile creare la directory: " + parent);
            }
            if(!file.exists()){
                boolean created = file.createNewFile();
                if(!created){
                    throw new PersistenceException("Impossibile creare il file: " + file);
                }
            }
        } catch (IOException e) {
            throw new PersistenceException("Errore durante la creazione del file: " + file, e);
        }
    }

    protected List<String> readAllLines(){
        List<String> out = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = reader.readLine()) != null){
                if(!line.isBlank()){
                    out.add(line);
                }
            }
        } catch (IOException _) {
            throw new PersistenceException("Errore leggendo il file: " + file);
        }
        return out;
    }

    protected void appendLine(String line){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            writer.write(line);
            writer.newLine();
        } catch (IOException _){
            throw new PersistenceException("Errore scrivendo il file: " + file);
        }
    }

    protected void overwriteAllLines(List<String> lines){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))){
            for(String line : lines){
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException _){
            throw new PersistenceException("Errore riscrivendo il file: " + file);
        }
    }
}
