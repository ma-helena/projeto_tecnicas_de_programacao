package dominio;

import java.util.Date;

public record Time(String nome){
    @Override
    public String toString() {
        return  nome;
    }

}