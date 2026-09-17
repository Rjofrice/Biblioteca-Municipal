package biblioteca;

import java.time.LocalDate;

/**
 * Representa um registo histórico de empréstimo (linha do histórico),
 * associando um utilizador a um livro, com a data de empréstimo e,
 * eventualmente, a data de devolução.
 */
public class Emprestimo {

    private final int id;
    private final int idLivro;
    private final int idUtilizador;
    private final LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private boolean devolvido;

    public Emprestimo(int id, int idLivro, int idUtilizador, LocalDate dataEmprestimo) {
        this.id = id;
        this.idLivro = idLivro;
        this.idUtilizador = idUtilizador;
        this.dataEmprestimo = dataEmprestimo;
        this.devolvido = false;
    }

    public int getId() {
        return id;
    }

    public int getIdLivro() {
        return idLivro;
    }

    public int getIdUtilizador() {
        return idUtilizador;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void marcarComoDevolvido(LocalDate dataDevolucao) {
        this.devolvido = true;
        this.dataDevolucao = dataDevolucao;
    }

    @Override
    public String toString() {
        String estado = devolvido ? ("devolvido em " + dataDevolucao) : "em curso";
        return String.format(
                "Empréstimo #%03d | Livro #%03d | Utilizador #%03d | Emprestado em %s | Estado: %s",
                id, idLivro, idUtilizador, dataEmprestimo, estado);
    }
}
