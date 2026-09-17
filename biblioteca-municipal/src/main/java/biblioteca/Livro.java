package biblioteca;

/**
 * Representa um livro do catálogo da Biblioteca Municipal.
 *
 * Cada livro possui um identificador único, título, autor, ano de
 * publicação, quantidade total de exemplares, quantidade atualmente
 * disponível para empréstimo e um contador de quantas vezes já foi
 * emprestado (utilizado para as estatísticas).
 */
public class Livro {

    private final int id;
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private int quantidadeTotal;
    private int quantidadeDisponivel;
    private int vezesEmprestado;

    public Livro(int id, String titulo, String autor, int anoPublicacao, int quantidadeTotal) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;
        this.quantidadeTotal = quantidadeTotal;
        this.quantidadeDisponivel = quantidadeTotal;
        this.vezesEmprestado = 0;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public int getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public int getVezesEmprestado() {
        return vezesEmprestado;
    }

    public boolean temExemplarDisponivel() {
        return quantidadeDisponivel > 0;
    }

    /** Diminui a quantidade disponível e incrementa o contador de empréstimos. */
    public void registarEmprestimo() {
        quantidadeDisponivel--;
        vezesEmprestado++;
    }

    /** Repõe um exemplar no acervo depois de uma devolução. */
    public void registarDevolucao() {
        if (quantidadeDisponivel < quantidadeTotal) {
            quantidadeDisponivel++;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "[%03d] \"%s\" - %s (%d) | Disponíveis: %d/%d | Vezes emprestado: %d",
                id, titulo, autor, anoPublicacao, quantidadeDisponivel, quantidadeTotal, vezesEmprestado);
    }
}
