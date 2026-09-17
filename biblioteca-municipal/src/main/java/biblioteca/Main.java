package biblioteca;

import java.util.Scanner;

/**
 * Ponto de entrada do sistema. Apresenta um menu interativo em consola
 * que permite ao bibliotecário registar livros e utilizadores, consultar
 * o catálogo, gerir empréstimos/devoluções e ver estatísticas.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Biblioteca biblioteca = new Biblioteca();

    public static void main(String[] args) {
        semearDadosDemonstracao();

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcao = lerInteiro("Escolha uma opção: ");
            switch (opcao) {
                case 1 -> registarLivro();
                case 2 -> registarUtilizador();
                case 3 -> listarCatalogo();
                case 4 -> pesquisarCatalogo();
                case 5 -> efetuarEmprestimo();
                case 6 -> efetuarDevolucao();
                case 7 -> mostrarEstatisticas();
                case 8 -> listarUtilizadores();
                case 0 -> {
                    continuar = false;
                    System.out.println("\nA encerrar o sistema. Até breve!");
                }
                default -> System.out.println("\nOpção inválida. Tente novamente.");
            }
        }
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n=================================================");
        System.out.println("   BIBLIOTECA MUNICIPAL - SISTEMA DE GESTÃO");
        System.out.println("=================================================");
        System.out.println(" 1. Registar novo livro");
        System.out.println(" 2. Registar novo utilizador");
        System.out.println(" 3. Listar catálogo completo");
        System.out.println(" 4. Pesquisar livro (título/autor)");
        System.out.println(" 5. Efetuar empréstimo");
        System.out.println(" 6. Efetuar devolução");
        System.out.println(" 7. Ver estatísticas");
        System.out.println(" 8. Listar utilizadores registados");
        System.out.println(" 0. Sair");
        System.out.println("=================================================");
    }

    // ---------------------------------------------------------------
    // Opção 1
    // ---------------------------------------------------------------
    private static void registarLivro() {
        System.out.println("\n--- Registo de Novo Livro ---");
        String titulo = lerTexto("Título: ");
        String autor = lerTexto("Autor: ");
        int ano = lerInteiro("Ano de publicação: ");
        int quantidade = lerInteiro("Quantidade de exemplares: ");

        Livro livro = biblioteca.registarLivro(titulo, autor, ano, quantidade);
        System.out.println("Livro registado com sucesso -> " + livro);
    }

    // ---------------------------------------------------------------
    // Opção 2
    // ---------------------------------------------------------------
    private static void registarUtilizador() {
        System.out.println("\n--- Registo de Novo Utilizador ---");
        String nome = lerTexto("Nome completo: ");
        String email = lerTexto("Email: ");

        Utilizador utilizador = biblioteca.registarUtilizador(nome, email);
        System.out.println("Utilizador registado com sucesso -> " + utilizador);
    }

    // ---------------------------------------------------------------
    // Opção 3
    // ---------------------------------------------------------------
    private static void listarCatalogo() {
        System.out.println("\n--- Catálogo Completo ---");
        Livro[] livros = biblioteca.getLivros();
        if (livros.length == 0) {
            System.out.println("O catálogo encontra-se vazio.");
            return;
        }
        for (Livro l : livros) {
            System.out.println(l);
        }
        System.out.println("Total de títulos distintos: " + livros.length);
    }

    // ---------------------------------------------------------------
    // Opção 4
    // ---------------------------------------------------------------
    private static void pesquisarCatalogo() {
        System.out.println("\n--- Pesquisa no Catálogo ---");
        System.out.println("1. Pesquisar por título");
        System.out.println("2. Pesquisar por autor");
        int opcao = lerInteiro("Escolha o critério: ");

        String termo = lerTexto("Termo de pesquisa: ");
        Livro[] resultados = (opcao == 2)
                ? biblioteca.pesquisarPorAutor(termo)
                : biblioteca.pesquisarPorTitulo(termo);

        if (resultados.length == 0) {
            System.out.println("Nenhum livro encontrado para \"" + termo + "\".");
            return;
        }
        System.out.println("Resultados encontrados (" + resultados.length + "):");
        for (Livro l : resultados) {
            System.out.println(l);
        }
    }

    // ---------------------------------------------------------------
    // Opção 5
    // ---------------------------------------------------------------
    private static void efetuarEmprestimo() {
        System.out.println("\n--- Empréstimo de Livro ---");
        int idUtilizador = lerInteiro("ID do utilizador: ");
        int idLivro = lerInteiro("ID do livro: ");

        Biblioteca.Resultado resultado = biblioteca.efetuarEmprestimo(idUtilizador, idLivro);
        switch (resultado) {
            case SUCESSO -> System.out.println("Empréstimo registado com sucesso!");
            case UTILIZADOR_INEXISTENTE -> System.out.println("Erro: utilizador não encontrado.");
            case LIVRO_INEXISTENTE -> System.out.println("Erro: livro não encontrado.");
            case SEM_EXEMPLARES -> System.out.println("Erro: não há exemplares disponíveis para este livro.");
            default -> System.out.println("Não foi possível concluir o empréstimo.");
        }
    }

    // ---------------------------------------------------------------
    // Opção 6
    // ---------------------------------------------------------------
    private static void efetuarDevolucao() {
        System.out.println("\n--- Devolução de Livro ---");
        int idUtilizador = lerInteiro("ID do utilizador: ");
        int idLivro = lerInteiro("ID do livro: ");

        Biblioteca.Resultado resultado = biblioteca.efetuarDevolucao(idUtilizador, idLivro);
        switch (resultado) {
            case SUCESSO -> System.out.println("Devolução registada com sucesso!");
            case UTILIZADOR_INEXISTENTE -> System.out.println("Erro: utilizador não encontrado.");
            case LIVRO_INEXISTENTE -> System.out.println("Erro: livro não encontrado.");
            case SEM_EMPRESTIMO_ATIVO -> System.out.println("Erro: não existe empréstimo ativo para este par utilizador/livro.");
            default -> System.out.println("Não foi possível concluir a devolução.");
        }
    }

    // ---------------------------------------------------------------
    // Opção 7
    // ---------------------------------------------------------------
    private static void mostrarEstatisticas() {
        System.out.println("\n--- Estatísticas da Biblioteca ---");
        Livro maisEmprestado = biblioteca.livroMaisEmprestado();
        if (maisEmprestado == null) {
            System.out.println("Ainda não existem livros registados.");
        } else {
            System.out.println("Livro mais emprestado: " + maisEmprestado);
        }
        System.out.println("Total de livros requisitados (histórico): " + biblioteca.totalLivrosRequisitados());
        System.out.println("Empréstimos atualmente em curso: " + biblioteca.totalEmprestimosEmCurso());
    }

    // ---------------------------------------------------------------
    // Opção 8
    // ---------------------------------------------------------------
    private static void listarUtilizadores() {
        System.out.println("\n--- Utilizadores Registados ---");
        Utilizador[] utilizadores = biblioteca.getUtilizadores();
        if (utilizadores.length == 0) {
            System.out.println("Ainda não existem utilizadores registados.");
            return;
        }
        for (Utilizador u : utilizadores) {
            System.out.println(u);
        }
    }

    // ---------------------------------------------------------------
    // Auxiliares de leitura
    // ---------------------------------------------------------------
    private static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return sc.nextLine().trim();
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = sc.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Por favor introduza um número válido.");
            }
        }
    }

    /** Popula o sistema com alguns dados iniciais para facilitar a demonstração. */
    private static void semearDadosDemonstracao() {
        biblioteca.registarLivro("Dom Casmurro", "Machado de Assis", 1899, 3);
        biblioteca.registarLivro("O Alquimista", "Paulo Coelho", 1988, 2);
        biblioteca.registarLivro("1984", "George Orwell", 1949, 4);
        biblioteca.registarUtilizador("Ana Fernandes", "ana.fernandes@email.com");
        biblioteca.registarUtilizador("Carlos Mahumana", "carlos.mahumana@email.com");
    }
}
