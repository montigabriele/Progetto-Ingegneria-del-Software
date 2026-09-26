package it.foodmood.infrastructure.io;

public interface InputReader extends AutoCloseable {
    String readLine();

    @Override
    void close();
}
