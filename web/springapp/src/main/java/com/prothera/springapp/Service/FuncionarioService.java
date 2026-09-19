package com.prothera.springapp.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.prothera.springapp.models.Funcionario;

public interface FuncionarioService {
  public void inicializar();

  public List<Funcionario> findAllFuncionarios();

  public List<Funcionario> findFiltrarESortear(List<Integer> meses, Boolean sortNames);

  public void deleteFuncionarioByName(String nome);

  public void patchFuncionariosSalario(BigDecimal porcentagem);

  public HashMap<String, List<Funcionario>> groupByFuncao();

  public BigDecimal sumSalarios();

}
