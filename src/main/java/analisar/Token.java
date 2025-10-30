package analisar;

public class Token {
    public String texto;
    public TipoToken kind;

    public Token(String texto, TipoToken kind) {
        this.texto = texto;
        this.kind = kind;
    }

    public static TipoToken verificarSePalavraChave(String texto) {
        for (TipoToken tipo : TipoToken.values()) {
            if (tipo.name().equals(texto) && tipo.getValue() > 100 && tipo.getValue() < 200) {
                return tipo;
            }
        }
        return null;
    }
}