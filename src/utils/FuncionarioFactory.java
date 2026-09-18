package utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import models.Funcionario;

public class FuncionarioFactory {
  public static DateTimeFormatter dateFormatter = Formatters.dateFormatter;

  public static Funcionario criarFuncionario(String nome, String dataNascimento, double salario, String funcao) {

    return new Funcionario(nome, LocalDate.parse(dataNascimento, dateFormatter), BigDecimal.valueOf(salario), funcao);
  }
}
