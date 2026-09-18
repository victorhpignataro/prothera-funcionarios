package com.prothera.springapp.dto;

import java.util.ArrayList;

import com.prothera.springapp.models.Funcionario;

public record InicializarRecord(
    Boolean inicializado,
    ArrayList<Funcionario> listaFuncionarios) {
  public InicializarRecord() {
    this(false, new ArrayList<>());
  }
}
