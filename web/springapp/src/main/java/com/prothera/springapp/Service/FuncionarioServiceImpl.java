package com.prothera.springapp.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.prothera.springapp.models.Funcionario;
import com.prothera.springapp.utils.FuncionarioFactory;

import jakarta.annotation.PostConstruct;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {
  private static Boolean inicializado = false;

  private static ArrayList<Funcionario> listaFuncionarios = new ArrayList<Funcionario>();

  public static void inserirFuncionario(Funcionario funcionario) {
    listaFuncionarios.add(funcionario);
  }

  @PostConstruct
  @Override
  public void inicializar() {
    if (inicializado)
      return;

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

    inicializado = true;

  }

  @Override
  public List<Funcionario> findAllFuncionarios() {
    return listaFuncionarios;
  }

  public List<Funcionario> findFiltrarESortear(List<Integer> meses, Boolean sortNames) {
    List<Month> mesesMap = meses.stream().map(Month::of).collect(Collectors.toList());

    Stream<Funcionario> ret = listaFuncionarios.stream();
    if (mesesMap.size() > 0) {
      ret = ret
          .filter(funcionario -> mesesMap.contains(funcionario.getDataNascimento().getMonth()));
    }

    if (sortNames) {
      ret = ret.sorted((fA, fB) -> {
        return fA.getNome().compareToIgnoreCase(fB.getNome());
      });
    }

    return ret.collect(Collectors.toList());
  }

  @Override
  public void deleteFuncionarioByName(String nome) {
    for (int i = 0; i < listaFuncionarios.size(); i++) {
      if (listaFuncionarios.get(i).getNome().equals(nome)) {
        listaFuncionarios.remove(i);
        break;
      }
    }

  }

  @Override
  public void patchFuncionariosSalario(BigDecimal porcentagem) {
    listaFuncionarios.replaceAll(funcionario -> {
      BigDecimal salarioAtual = funcionario.getSalario();
      BigDecimal novoSalario = salarioAtual.add(salarioAtual.multiply(porcentagem));
      funcionario.setSalario(novoSalario);
      return funcionario;
    });
  }

  @Override
  public HashMap<String, List<Funcionario>> groupByFuncao() {

    HashMap<String, List<Funcionario>> funcionariosPorFuncao = new HashMap<String, List<Funcionario>>();
    listaFuncionarios.forEach((Funcionario funcionarioI) -> {
      funcionariosPorFuncao.merge(funcionarioI.getFuncao(), new ArrayList<Funcionario>(Arrays.asList(funcionarioI)),
          (List<Funcionario> fA, List<Funcionario> fB) -> {
            List<Funcionario> result = new ArrayList<Funcionario>(fA);
            result.add(fB.get(0));
            return result;
          });
    });

    return funcionariosPorFuncao;
  }

  @Override
  public BigDecimal sumSalarios() {
    BigDecimal somaTotal = listaFuncionarios.stream().map(Funcionario::getSalario).reduce(BigDecimal.ZERO,
        BigDecimal::add);
    return somaTotal;
  }
}
