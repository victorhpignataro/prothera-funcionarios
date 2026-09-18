package com.prothera.springapp.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.prothera.springapp.models.Funcionario;

public interface FuncionarioService {
  public void inicializar();

  public ArrayList<Funcionario> findAllFuncionarios();

  public void deleteFuncionarioByName(String nome);

  public void patchFuncionariosSalario(BigDecimal porcentagem);

  public HashMap<String, ArrayList<Funcionario>> groupByFuncao();

  public BigDecimal sumSalarios();

}
