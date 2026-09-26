package it.foodmood.domain.value;

import java.net.URI;
import java.util.Objects;

/*
 * Rappresenta il riferimento a un'immagine associata al piatto o al prodotto
 * Contiene solamente il percorso dell'immagine (URI)
*/

public final class Image{
    private URI uri;

    public Image(URI uri){
        this.uri = Objects.requireNonNull(uri, "Percorso nullo!");
    }

    public URI getUri() { return uri; }

    public void setUri(URI uri){
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Image image)) return false;
        return Objects.equals(this.uri, image.uri);
    }

    @Override
    public int hashCode(){
        return Objects.hash(uri);
    }
}