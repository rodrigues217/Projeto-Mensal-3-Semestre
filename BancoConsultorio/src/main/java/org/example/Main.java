package org.example;

import jakarta.persistence.EntityManager;
import org.example.Service.CurvaABC;
import org.example.Util.Factory;
import org.example.entities.Produtos;
import org.example.entities.Usuario;
import org.example.repository.ProdutosRepository;
import org.example.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        EntityManager em = Factory.getEntityManager();
        ProdutosRepository produtosRepository = new ProdutosRepository(em);
        UsuarioRepository usuarioRepository = new UsuarioRepository(em);
        Scanner scanner = new Scanner(System.in);

        // Inicia login de administrador
        Usuario adminLogado = fazerLoginAdmin(scanner, usuarioRepository);

        // Se o login for bem-sucedido, carrega o menu principal
        if (adminLogado != null) {
            menuPrincipal(scanner, produtosRepository, usuarioRepository, em, adminLogado);
        } else {
            System.out.println("Login falhou! Finalizando o sistema.");
        }
    }

    private static Usuario fazerLoginAdmin(Scanner scanner, UsuarioRepository usuarioRepository) {
        Usuario adminLogado = null;

        while (adminLogado == null) {
            System.out.println("*** LOGIN ADMINISTRADOR ***");
            System.out.print("Login: ");
            String login = scanner.next();
            System.out.print("Senha: ");
            String senha = scanner.next();

            adminLogado = usuarioRepository.autenticar(login, senha);

            if (adminLogado == null || !adminLogado.getLogin().equals("admin")) {
                System.out.println("Credenciais inválidas ou não é administrador.");
            } else {
                System.out.println("Último login registrado: " +
                        (adminLogado.getUltimoLogin() != null ? adminLogado.getUltimoLogin() : "Nunca"));

                // Atualiza o último login
                adminLogado.setUltimoLogin(LocalDateTime.now());
                usuarioRepository.salvar(adminLogado);

                System.out.println("Login bem-sucedido!\n");
            }
        }
        return adminLogado;
    }

    private static void menuPrincipal(Scanner scanner, ProdutosRepository produtosRepository,
                                      UsuarioRepository usuarioRepository, EntityManager em, Usuario adminLogado) {
        while (true) {
            System.out.println("\n*** MENU INTERATIVO ***");
            System.out.println("1 - Cadastrar Produto");
            System.out.println("2 - Registrar Venda");
            System.out.println("3 - Listar Produtos");
            System.out.println("4 - Adicionar Estoque");
            System.out.println("5 - Mostrar Lucro do Dia");
            System.out.println("6 - Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    cadastrarProduto(scanner, produtosRepository);
                    break;
                case 2:
                    registrarVenda(scanner, produtosRepository, adminLogado);
                    break;
                case 3:
                    listarProdutos(produtosRepository);
                    break;
                case 4:
                    adicionarEstoque(scanner, produtosRepository);
                    break;
                case 5:
                    System.out.println("m Lucro total do dia: R$ " + org.example.Service.LucroService.getLucroTotalDoDia());
                    break;
                case 6:
                    System.out.println("Saindo...");
                    em.close();
                    return;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        }
    }

    private static void adicionarEstoque(Scanner scanner, ProdutosRepository produtosRepository) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade a ser adicionada ao estoque: ");
        int quantidade = scanner.nextInt();
        produtosRepository.adicionarEstoque(idProduto, quantidade);
    }

    private static void cadastrarProduto(Scanner scanner, ProdutosRepository produtosRepository) {
        scanner.nextLine(); // Limpar buffer
        System.out.print("Digite o nome do produto: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o valor do produto: ");
        double valor = scanner.nextDouble();
        System.out.print("Digite a quantidade em estoque: ");
        int estoque = scanner.nextInt();

        Produtos produto = new Produtos();
        produto.setNome(nome);
        produto.setValor(valor);
        produto.setEstoque(estoque);
        produto.setQuantidade_vendida(0);

        produtosRepository.salvar(produto);
        System.out.println("Produto cadastrado com sucesso!");
        System.out.println("ID do produto cadastrado: " + produto.getId());
    }

    private static void registrarVenda(Scanner scanner, ProdutosRepository produtosRepository, Usuario vendedor) {
        System.out.print("Informe o ID do produto: ");
        long idProduto = scanner.nextLong();
        System.out.print("Informe a quantidade de venda: ");
        int quantidade = scanner.nextInt();

        produtosRepository.registrarVenda(idProduto, quantidade, vendedor);

        List<Produtos> produtos = produtosRepository.buscarTodos();
        List<Produtos> produtosClassificados = CurvaABC.classificar(produtos);
        produtosRepository.atualizarProdutos(produtosClassificados);

        System.out.println("Venda registrada com sucesso!");
    }

    private static void listarProdutos(ProdutosRepository produtosRepository) {
        List<Produtos> produtos = produtosRepository.buscarTodos();
        List<Produtos> produtosClassificados = CurvaABC.classificar(produtos);

        System.out.println("\n*** LISTA DE PRODUTOS ***");
        for (Produtos produto : produtosClassificados) {
            System.out.println("ID: " + produto.getId() +
                    ", Nome: " + produto.getNome() +
                    ", Estoque: " + produto.getEstoque() +
                    ", Quantidade Vendida: " + produto.getQuantidade_vendida() +
                    ", Categoria: " + produto.getCategoria() +
                    ", Valor Consumo: " + produto.getValorConsumo());
        }
    }

    private static void cadastrarUsuario(Scanner scanner, UsuarioRepository usuarioRepository) {
        scanner.nextLine(); // Limpar buffer

        System.out.print("Digite o login do administrador: ");
        String login = scanner.nextLine();
        System.out.print("Digite a senha do administrador: ");
        String senha = scanner.nextLine();

        Usuario admin = usuarioRepository.autenticar(login, senha);
        if (admin == null || !admin.getLogin().equals("admin")) {
            System.out.println("Acesso negado. Somente administradores podem cadastrar novos usuários.");
            return;
        }

        System.out.print("Digite o login do novo usuário: ");
        String novoLogin = scanner.nextLine();
        System.out.print("Digite a senha do novo usuário: ");
        String novaSenha = scanner.nextLine();
        System.out.print("Digite a descrição do novo usuário: ");
        String descricao = scanner.nextLine();

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(novoLogin);
        novoUsuario.setSenha(novaSenha);
        novoUsuario.setDescricao(descricao);

        usuarioRepository.salvar(novoUsuario);
        System.out.println("Usuário cadastrado com sucesso!");
    }
}
