import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import models.Funcionario;
import utils.FuncionarioFactory;

public class Principal {
  private static final BigDecimal SALARIO_MINIMO = BigDecimal.valueOf(1212.00);
  private static ArrayList<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();

  public static void inserirFuncionario(Funcionario funcionario) {
    listaFuncionarios.add(funcionario);
  }

  public static void imprimirFuncionarios() {
    System.out.println("Lista de Funcionários\n");
    listaFuncionarios.forEach((Funcionario funcionario) -> {
      System.out.println(funcionario.toString());
    });
    System.out.println("");
  }

  public static void removerFuncionarioPorNome(String nome, Optional<Boolean> all) {
    for (int i = 0; i < listaFuncionarios.size(); i++) {
      if (listaFuncionarios.get(i).getNome().equals(nome)) {
        listaFuncionarios.remove(i);
        if (all.isPresent() && Boolean.FALSE.equals(all.get())) {
          break;
        }
      }
    }
  }

  public static void removerPrimeiroFuncionarioPorNome(String nome) {
    removerFuncionarioPorNome(nome, Optional.of(false));
  }

  public static void removerTodoFuncionarioPorNome(String nome) {
    removerFuncionarioPorNome(nome, Optional.of(true));
  }

  public static void aplicarAumento(BigDecimal porcentagem) {
    listaFuncionarios.replaceAll(funcionario -> {
      BigDecimal salarioAtual = funcionario.getSalario();
      BigDecimal novoSalario = salarioAtual.add(salarioAtual.multiply(porcentagem));
      funcionario.setSalario(novoSalario);
      return funcionario;
    });
  }

  public static void agruparFuncionariosPorFuncao() {
    HashMap<String, ArrayList<Funcionario>> funcionariosPorFuncao = new HashMap<String, ArrayList<Funcionario>>();
    listaFuncionarios.forEach((Funcionario funcionarioI) -> {
      funcionariosPorFuncao.merge(funcionarioI.getFuncao(), new ArrayList<Funcionario>(Arrays.asList(funcionarioI)),
          (ArrayList<Funcionario> fA, ArrayList<Funcionario> fB) -> {
            ArrayList<Funcionario> result = new ArrayList<Funcionario>(fA);
            result.add(fB.get(0));
            return result;
          });
    });
    System.out.println("Funcionarios Agrupados por função");
    funcionariosPorFuncao.forEach((String funcao, ArrayList<Funcionario> listaFuncionarios) -> {
      System.out.println(funcao + " (" + listaFuncionarios.size() + ")");
      listaFuncionarios.forEach((Funcionario funcionario) -> {
        System.out.println(funcionario.getNome());

      });
      System.out.println("");
    });
  }

  public static void imprimirFuncionariosAniversarioEm(List<Month> meses) {
    System.out.println("Funcionários com aniversários em " + meses.stream().map(mes -> {
      return mes.getValue();
    }).collect(Collectors.toList()));

    listaFuncionarios.forEach((Funcionario funcionario) -> {
      meses.forEach(
          (Month mes) -> {
            if (funcionario.getDataNascimento().getMonth().equals(mes)) {
              System.out.println(funcionario.getNome());
            }
            ;
          });
    });
  }

  public static void imprimirFuncionarioComMaiorIdade() {
    LocalDate maxDate = LocalDate.MAX;
    Funcionario maxFuncionario = null;

    for (int i = 0; i < listaFuncionarios.size(); i++) {
      Funcionario funcionario = listaFuncionarios.get(i);
      LocalDate dataNascimento = funcionario.getDataNascimento();
      if (dataNascimento.isBefore(maxDate)) {
        maxDate = dataNascimento;
        maxFuncionario = funcionario;
      }
      ;
    }

    System.out.println("O funcionário mais velho é: " + maxFuncionario.getNome() + " com "
        + maxDate.until(LocalDate.now(), ChronoUnit.YEARS) + " anos");
  }

  public static void imprimirPorOrdemAlfabetica() {
    System.out.println("Impressão por ordem alfabética");
    ArrayList<Funcionario> listaOrdenada = new ArrayList<Funcionario>(listaFuncionarios);

    listaOrdenada.sort((Funcionario fA, Funcionario fB) -> {
      return fA.getNome().compareToIgnoreCase(fB.getNome());
    });
    listaOrdenada.forEach(funcionario -> {
      System.out.println(funcionario.toString());
    });
  }

  public static void imprimirTotalSalarios() {
    System.out.println("Total dos salários dos funcionários");
    Object somaTotal = listaFuncionarios.stream().map(Funcionario::getSalario).reduce(BigDecimal.ZERO, BigDecimal::add);
    System.out.println(somaTotal);
  }

  public static void imprimirSalariosMinimosPorFuncionario() {
    System.out.println("Quantidade de salários mínimos por funcionario: ");
    listaFuncionarios.forEach(funcionario -> {
      BigDecimal quantidadeSalariosMinimos = funcionario.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
      System.out.println(funcionario.getNome() + " -> " + quantidadeSalariosMinimos);
    });

  }

  public static void main(String[] args) throws Exception {

    inserirFuncionario(FuncionarioFactory.criarFuncionario("Maria", "18/10/2000",
        2009.44,
        "Operador"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("João", "12/05/1990",
        2284.38,
        "Operador"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Caio", "02/05/1961",
        9836.14,
        "Coordenador"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Miguel", "14/10/1988",
        19119.88,
        "Diretor"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Alice", "05/01/1995",
        2234.68,
        "Recepcionista"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Heitor", "19/11/1999",
        1582.72,
        "Operador"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Arthur", "31/03/1993",
        4071.84,
        "Contador"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Laura", "08/07/1994",
        3017.45,
        "Gerente"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Heloísa", "24/05/2003",
        1606.85,
        "Eletricista"));
    inserirFuncionario(FuncionarioFactory.criarFuncionario("Helena", "02/09/1996",
        2799.93,
        "Gerente"));

    imprimirFuncionarios();

    imprimirTotalSalarios();

    removerPrimeiroFuncionarioPorNome("João");

    imprimirFuncionarios();

    aplicarAumento(BigDecimal.valueOf(0.10));

    imprimirFuncionarios();

    agruparFuncionariosPorFuncao();

    imprimirFuncionariosAniversarioEm(Arrays.asList(Month.OCTOBER, Month.DECEMBER));

    imprimirFuncionarioComMaiorIdade();

    imprimirPorOrdemAlfabetica();

    imprimirTotalSalarios();

    imprimirSalariosMinimosPorFuncionario();

  }

}
