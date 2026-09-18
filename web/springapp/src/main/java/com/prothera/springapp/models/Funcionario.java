package com.prothera.springapp.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.prothera.springapp.utils.Formatters;

public class Funcionario extends Pessoa {
  BigDecimal salario;
  String funcao;

  public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
    super(nome, dataNascimento);
    this.salario = salario;
    this.funcao = funcao;
  }

  public BigDecimal getSalario() {
    return salario;
  }

  public void setSalario(BigDecimal salario) {
    this.salario = salario;
  }

  public String getFuncao() {
    return funcao;
  }

  public void setFuncao(String funcao) {
    this.funcao = funcao;
  }

  @Override
  public String toString() {
    return "Nome: ".concat(this.getNome()) + " " +
        "Data Nascimento: ".concat(this.getDataNascimento().format(Formatters.dateFormatter)) + " " +
        "Salário: ".concat(Formatters.numberFormatter.format(this.getSalario())) + " " +
        "Função: ".concat(this.getFuncao());
  }

}
