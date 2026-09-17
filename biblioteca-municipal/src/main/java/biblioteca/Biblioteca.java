package biblioteca;

import java.time.LocalDate;

/**
 * Núcleo do sistema: mantém a "base de dados" simulada em memória através
 * de vetores (arrays) redimensionáveis manualmente e de uma matriz de
 * inteiros que cruza utilizadores com livros (linhas x colunas), guardando
 * o número de empréstimos ativos de cada par utilizador/livro.
 *
 * Não são utilizadas coleções da Java Collections Framework (ArrayList,
 * HashMap, etc.) propositadamente, para cumprir o requisito do enunciado
 * de manipulação da informação através de vetores/arrays e matrizes.
 */
public class Biblioteca {

    private static final int CAPACIDADE_INICIAL = 10;

    // ---- Vetores (arrays) que simulam as tabelas da base de dados ----
    private Livro[] livros;
    private int totalLivros;

    private Utilizador[] utilizadores;
    private int totalUtilizadores;

    private Emprestimo[] emprestimos;
    private int totalEmprestimos;

    // ---- Matriz utilizador x livro: empréstimos ativos por par ----
    private int[][] matrizEmprestimosAtivos;

    private int proximoIdLivro = 1;
    private int proximoIdUtilizador = 1;
    private int proximoIdEmprestimo = 1;

    public Biblioteca() {
        livros = new Livro[CAPACIDADE_INICIAL];
        utilizadores = new Utilizador[CAPACIDADE_INICIAL];
        emprestimos = new Emprestimo[CAPACIDADE_INICIAL];
        matrizEmprestimosAtivos = new int[CAPACIDADE_INICIAL][CAPACIDADE_INICIAL];
    }

    // =====================================================================
    // REGISTO DE LIVROS
    // =====================================================================

    public Livro registarLivro(String titulo, String autor, int anoPublicacao, int quantidade) {
        if (totalLivros == livros.length) {
            livros = crescer(livros);
            matrizEmprestimosAtivos = crescerColunas(matrizEmprestimosAtivos, livros.length);
        }
        Livro novo = new Livro(proximoIdLivro++, titulo, autor, anoPublicacao, quantidade);
        livros[totalLivros++] = novo;
        return novo;
    }

    public Livro[] getLivros() {
        Livro[] copia = new Livro[totalLivros];
        System.arraycopy(livros, 0, copia, 0, totalLivros);
        return copia;
    }

    public Livro procurarLivroPorId(int id) {
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].getId() == id) {
                return livros[i];
            }
        }
        return null;
    }

    /** Devolve o índice (posição no array) de um livro pelo id, ou -1. */
    private int indiceLivro(int id) {
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    // =====================================================================
    // CONSULTA DE CATÁLOGO
    // =====================================================================

    public Livro[] pesquisarPorTitulo(String termo) {
        Livro[] resultadosTemp = new Livro[totalLivros];
        int count = 0;
        String termoLower = termo.toLowerCase();
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].getTitulo().toLowerCase().contains(termoLower)) {
                resultadosTemp[count++] = livros[i];
            }
        }
        Livro[] resultados = new Livro[count];
        System.arraycopy(resultadosTemp, 0, resultados, 0, count);
        return resultados;
    }

    public Livro[] pesquisarPorAutor(String termo) {
        Livro[] resultadosTemp = new Livro[totalLivros];
        int count = 0;
        String termoLower = termo.toLowerCase();
        for (int i = 0; i < totalLivros; i++) {
            if (livros[i].getAutor().toLowerCase().contains(termoLower)) {
                resultadosTemp[count++] = livros[i];
            }
        }
        Livro[] resultados = new Livro[count];
        System.arraycopy(resultadosTemp, 0, resultados, 0, count);
        return resultados;
    }

    // =====================================================================
    // REGISTO DE UTILIZADORES
    // =====================================================================

    public Utilizador registarUtilizador(String nome, String email) {
        if (totalUtilizadores == utilizadores.length) {
            utilizadores = crescer(utilizadores);
            matrizEmprestimosAtivos = crescerLinhas(matrizEmprestimosAtivos, utilizadores.length);
        }
        Utilizador novo = new Utilizador(proximoIdUtilizador++, nome, email);
        utilizadores[totalUtilizadores++] = novo;
        return novo;
    }

    public Utilizador[] getUtilizadores() {
        Utilizador[] copia = new Utilizador[totalUtilizadores];
        System.arraycopy(utilizadores, 0, copia, 0, totalUtilizadores);
        return copia;
    }

    public Utilizador procurarUtilizadorPorId(int id) {
        for (int i = 0; i < totalUtilizadores; i++) {
            if (utilizadores[i].getId() == id) {
                return utilizadores[i];
            }
        }
        return null;
    }

    private int indiceUtilizador(int id) {
        for (int i = 0; i < totalUtilizadores; i++) {
            if (utilizadores[i].getId() == id) {
                return i;
            }
        }
        return -1;
    }

    // =====================================================================
    // GESTÃO DE EMPRÉSTIMOS E DEVOLUÇÕES
    // =====================================================================

    /**
     * Resultado possível de uma tentativa de empréstimo/devolução, usado
     * para comunicar ao menu (Main) o que aconteceu sem recorrer a exceções.
     */
    public enum Resultado {
        SUCESSO, LIVRO_INEXISTENTE, UTILIZADOR_INEXISTENTE, SEM_EXEMPLARES, SEM_EMPRESTIMO_ATIVO
    }

    public Resultado efetuarEmprestimo(int idUtilizador, int idLivro) {
        int iu = indiceUtilizador(idUtilizador);
        int il = indiceLivro(idLivro);
        if (iu == -1) return Resultado.UTILIZADOR_INEXISTENTE;
        if (il == -1) return Resultado.LIVRO_INEXISTENTE;

        Livro livro = livros[il];
        if (!livro.temExemplarDisponivel()) {
            return Resultado.SEM_EXEMPLARES;
        }

        livro.registarEmprestimo();
        matrizEmprestimosAtivos[iu][il]++;

        if (totalEmprestimos == emprestimos.length) {
            emprestimos = crescer(emprestimos);
        }
        Emprestimo registo = new Emprestimo(proximoIdEmprestimo++, idLivro, idUtilizador, LocalDate.now());
        emprestimos[totalEmprestimos++] = registo;

        return Resultado.SUCESSO;
    }

    public Resultado efetuarDevolucao(int idUtilizador, int idLivro) {
        int iu = indiceUtilizador(idUtilizador);
        int il = indiceLivro(idLivro);
        if (iu == -1) return Resultado.UTILIZADOR_INEXISTENTE;
        if (il == -1) return Resultado.LIVRO_INEXISTENTE;

        if (matrizEmprestimosAtivos[iu][il] <= 0) {
            return Resultado.SEM_EMPRESTIMO_ATIVO;
        }

        // Localiza o empréstimo mais antigo ainda em curso para este par.
        Emprestimo alvo = null;
        for (int i = 0; i < totalEmprestimos; i++) {
            Emprestimo e = emprestimos[i];
            if (!e.isDevolvido() && e.getIdLivro() == idLivro && e.getIdUtilizador() == idUtilizador) {
                alvo = e;
                break;
            }
        }
        if (alvo == null) {
            return Resultado.SEM_EMPRESTIMO_ATIVO;
        }

        alvo.marcarComoDevolvido(LocalDate.now());
        livros[il].registarDevolucao();
        matrizEmprestimosAtivos[iu][il]--;

        return Resultado.SUCESSO;
    }

    public Emprestimo[] getHistoricoEmprestimos() {
        Emprestimo[] copia = new Emprestimo[totalEmprestimos];
        System.arraycopy(emprestimos, 0, copia, 0, totalEmprestimos);
        return copia;
    }

    /** Devolve a matriz utilizador x livro com o número de empréstimos ativos. */
    public int[][] getMatrizEmprestimosAtivos() {
        return matrizEmprestimosAtivos;
    }

    // =====================================================================
    // ESTATÍSTICAS
    // =====================================================================

    public Livro livroMaisEmprestado() {
        if (totalLivros == 0) return null;
        Livro maisEmprestado = livros[0];
        for (int i = 1; i < totalLivros; i++) {
            if (livros[i].getVezesEmprestado() > maisEmprestado.getVezesEmprestado()) {
                maisEmprestado = livros[i];
            }
        }
        return maisEmprestado;
    }

    public int totalLivrosRequisitados() {
        return totalEmprestimos;
    }

    public int totalEmprestimosEmCurso() {
        int count = 0;
        for (int i = 0; i < totalEmprestimos; i++) {
            if (!emprestimos[i].isDevolvido()) {
                count++;
            }
        }
        return count;
    }

    // =====================================================================
    // UTILITÁRIOS DE REDIMENSIONAMENTO DE VETORES/MATRIZ
    // =====================================================================

    private Livro[] crescer(Livro[] original) {
        Livro[] novo = new Livro[original.length * 2];
        System.arraycopy(original, 0, novo, 0, original.length);
        return novo;
    }

    private Utilizador[] crescer(Utilizador[] original) {
        Utilizador[] novo = new Utilizador[original.length * 2];
        System.arraycopy(original, 0, novo, 0, original.length);
        return novo;
    }

    private Emprestimo[] crescer(Emprestimo[] original) {
        Emprestimo[] novo = new Emprestimo[original.length * 2];
        System.arraycopy(original, 0, novo, 0, original.length);
        return novo;
    }

    private int[][] crescerLinhas(int[][] original, int novasLinhas) {
        int colunas = original.length > 0 ? original[0].length : CAPACIDADE_INICIAL;
        int[][] novo = new int[novasLinhas * 2][colunas];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, novo[i], 0, colunas);
        }
        return novo;
    }

    private int[][] crescerColunas(int[][] original, int novasColunas) {
        int[][] novo = new int[original.length][novasColunas * 2];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, novo[i], 0, original[i].length);
        }
        return novo;
    }
}
