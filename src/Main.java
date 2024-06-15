import java.io.*;
import java.util.*;

class Conta {
    private int id;
    private String nome;
    private double saldo;

    public Conta(int id, String nome, double saldo) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return id + "," + nome + "," + saldo;
    }
}

class Banco {
    private List<Conta> contas;
    private String arquivo;

    public Banco(String arquivo) {
        this.arquivo = arquivo;
        this.contas = new ArrayList<>();
        lerContas();
    }

    private void lerContas() {
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                int id = Integer.parseInt(dados[0]);
                String nome = dados[1];
                double saldo = Double.parseDouble(dados[2]);
                contas.add(new Conta(id, nome, saldo));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public void salvarContas() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (Conta conta : contas) {
                bw.write(conta.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    public boolean incluirConta(int id, String nome, double saldo) {
        if (id <= 0 || saldo < 0) {
            System.out.println("ID deve ser maior que zero e saldo inicial deve ser maior ou igual a zero.");
            return false;
        }
        for (Conta conta : contas) {
            if (conta.getId() == id) {
                System.out.println("Número da conta já existe.");
                return false;
            }
        }
        contas.add(new Conta(id, nome, saldo));
        return true;
    }

    public boolean alterarSaldo(int id, double valor) {
        if (valor <= 0) {
            System.out.println("O valor deve ser maior que zero.");
            return false;
        }
        for (Conta conta : contas) {
            if (conta.getId() == id) {
                conta.setSaldo(conta.getSaldo() + valor);
                return true;
            }
        }
        System.out.println("Conta não encontrada.");
        return false;
    }

    public boolean excluirConta(int id) {
        Iterator<Conta> iterator = contas.iterator();
        while (iterator.hasNext()) {
            Conta conta = iterator.next();
            if (conta.getId() == id) {
                if (conta.getSaldo() == 0) {
                    iterator.remove();
                    return true;
                } else {
                    System.out.println("A conta não pode ser excluída pois o saldo não é zero.");
                    return false;
                }
            }
        }
        System.out.println("Conta não encontrada.");
        return false;
    }

    public void consultarContas() {
        for (Conta conta : contas) {
            System.out.println(conta);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Banco banco = new Banco("contas.csv");
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("Menu:");
            System.out.println("1. Incluir conta");
            System.out.println("2. Alterar saldo");
            System.out.println("3. Excluir conta");
            System.out.println("4. Consultar contas");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("ID da conta: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consumir a nova linha
                    System.out.print("Nome do correntista: ");
                    String nome = scanner.nextLine();
                    System.out.print("Saldo inicial: ");
                    double saldo = scanner.nextDouble();
                    if (banco.incluirConta(id, nome, saldo)) {
                        System.out.println("Conta incluída com sucesso.");
                    }
                    break;

                case 2:
                    System.out.print("ID da conta: ");
                    int idAlterar = scanner.nextInt();
                    System.out.print("Valor para crédito/débito: ");
                    double valor = scanner.nextDouble();
                    if (banco.alterarSaldo(idAlterar, valor)) {
                        System.out.println("Saldo alterado com sucesso.");
                    }
                    break;

                case 3:
                    System.out.print("ID da conta: ");
                    int idExcluir = scanner.nextInt();
                    if (banco.excluirConta(idExcluir)) {
                        System.out.println("Conta excluída com sucesso.");
                    }
                    break;

                case 4:
                    banco.consultarContas();
                    break;

                case 5:
                    banco.salvarContas();
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
            System.out.println();
        } while (opcao != 5);

        scanner.close();
    }
}
