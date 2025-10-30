// Salve como: analisar/AnalisadorLexico.java
package analisar;

public class AnalisadorLexico {

    private String source;
    private char curChar;
    private int curPos;

    public AnalisadorLexico(String source) {
        this.source = source + "\n";
        this.curPos = -1;
        this.proximoCaractere();
    }

    public void proximoCaractere() {
        this.curPos++;
        if (this.curPos >= this.source.length()) {
            this.curChar = '\0';
        } else {
            this.curChar = this.source.charAt(this.curPos);
        }
    }

    public char pesquisar() {
        if (this.curPos + 1 >= this.source.length()) {
            return '\0';
        }
        return this.source.charAt(this.curPos + 1);
    }

    public void aborta(String message) {
        throw new RuntimeException("Erro do Analisador Léxico: " + message);
    }

    public void pularEspacoBranco() {
        while (this.curChar == ' ' || this.curChar == '\t' || this.curChar == '\r') {
            this.proximoCaractere();
        }
    }

    public void pularComentario() {
        if (this.curChar == '#') {
            while (this.curChar != '\n') {
                this.proximoCaractere();
            }
        }
    }

    public Token obterToken() {
        this.pularEspacoBranco();
        this.pularComentario();

        Token token;

        if (this.curChar == '+') {
            token = new Token(String.valueOf(this.curChar), TipoToken.PLUS);
        } else if (this.curChar == '-') {
            token = new Token(String.valueOf(this.curChar), TipoToken.MINUS);
        } else if (this.curChar == '*') {
            token = new Token(String.valueOf(this.curChar), TipoToken.ASTERISK);
        } else if (this.curChar == '/') {
            token = new Token(String.valueOf(this.curChar), TipoToken.SLASH);
        } else if (this.curChar == '=') {
            if (this.pesquisar() == '=') {
                char lastChar = this.curChar;
                this.proximoCaractere();
                token = new Token(String.valueOf(lastChar) + this.curChar, TipoToken.EQEQ);
            } else {
                token = new Token(String.valueOf(this.curChar), TipoToken.EQ);
            }
        } else if (this.curChar == '>') {
            if (this.pesquisar() == '=') {
                char lastChar = this.curChar;
                this.proximoCaractere();
                token = new Token(String.valueOf(lastChar) + this.curChar, TipoToken.GTEQ);
            } else {
                token = new Token(String.valueOf(this.curChar), TipoToken.GT);
            }
        } else if (this.curChar == '<') {
            if (this.pesquisar() == '=') {
                char lastChar = this.curChar;
                this.proximoCaractere();
                token = new Token(String.valueOf(lastChar) + this.curChar, TipoToken.LTEQ);
            } else {
                token = new Token(String.valueOf(this.curChar), TipoToken.LT);
            }
        } else if (this.curChar == '!') {
            if (this.pesquisar() == '=') {
                char lastChar = this.curChar;
                this.proximoCaractere();
                token = new Token(String.valueOf(lastChar) + this.curChar, TipoToken.NOTEQ);
            } else {
                this.aborta("Caractere esperado =, obtido !" + this.pesquisar());
                return null;
            }

        } else if (this.curChar == '"') {
            this.proximoCaractere();
            int startPos = this.curPos;

            while (this.curChar != '"') {
                if (this.curChar == '\r' || this.curChar == '\n' || this.curChar == '\t' || this.curChar == '\\' || this.curChar == '%') {
                    this.aborta("Caractere ilegal na String/Texto.");
                }
                this.proximoCaractere();
            }

            String tokText = this.source.substring(startPos, this.curPos);
            token = new Token(tokText, TipoToken.STRING);

        } else if (Character.isDigit(this.curChar)) {
            int startPos = this.curPos;
            while (Character.isDigit(this.pesquisar())) {
                this.proximoCaractere();
            }

            if (this.pesquisar() == '.') {
                this.proximoCaractere();

                if (!Character.isDigit(this.pesquisar())) {
                    this.aborta("Caractere ilegal no número.");
                }
                while (Character.isDigit(this.pesquisar())) {
                    this.proximoCaractere();
                }
            }

            String tokText = this.source.substring(startPos, this.curPos + 1);
            token = new Token(tokText, TipoToken.NUMBER);

        } else if (Character.isLetter(this.curChar)) {
            int startPos = this.curPos;

            while (Character.isLetterOrDigit(this.pesquisar())) {
                this.proximoCaractere();
            }

            String tokText = this.source.substring(startPos, this.curPos + 1);
            TipoToken keyword = Token.verificarSePalavraChave(tokText);

            if (keyword == null) {
                token = new Token(tokText, TipoToken.IDENT);
            } else {
                token = new Token(tokText, keyword);
            }

        } else if (this.curChar == '\n') {
            token = new Token(String.valueOf(this.curChar), TipoToken.NEWLINE);

        } else if (this.curChar == '\0') {
            token = new Token("", TipoToken.EOF);

        } else {
            this.aborta("Token desconhecido: " + this.curChar);
            return null;
        }

        this.proximoCaractere();
        return token;
    }
}