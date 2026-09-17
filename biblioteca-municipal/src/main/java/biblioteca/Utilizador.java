package biblioteca;

/**
 * Representa um utilizador (leitor) registado na Biblioteca Municipal.
 */
public class Utilizador {

    private final int id;
    private String nome;
    private String email;

    public Utilizador(int id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("[%03d] %s <%s>", id, nome, email);
    }
}
