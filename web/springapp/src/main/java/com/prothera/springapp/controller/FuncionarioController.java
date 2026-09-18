package com.prothera.springapp.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prothera.springapp.Service.FuncionarioService;
import com.prothera.springapp.models.Funcionario;

@RestController
@RequestMapping("/api/funcionario")
public class FuncionarioController {

  private final FuncionarioService funcionarioService;

  public FuncionarioController(FuncionarioService funcionarioService) {
    funcionarioService.inicializar();
    this.funcionarioService = funcionarioService;
  }

  @GetMapping
  public ResponseEntity<?> getAllFuncionarios() {

    ArrayList<Funcionario> funcionarios = funcionarioService.findAllFuncionarios();

    return ResponseEntity.ok(funcionarios);
  }

  @DeleteMapping("{name}")
  public void deleteFuncionarioPorNome(@PathVariable String name) {
    funcionarioService.deleteFuncionarioByName(name);
  }

  public record AumentoRequest(BigDecimal porcentagem) {
  }

  @PatchMapping("/aumento")
  public void aplicarAumento(@RequestBody AumentoRequest body) {
    funcionarioService.patchFuncionariosSalario(body.porcentagem);
  }

  @GetMapping("/groupByFuncao")
  public ResponseEntity<?> groupByFuncao() {
    HashMap<String, ArrayList<Funcionario>> funcionariosAgrupadosPorFuncao = funcionarioService.groupByFuncao();
    return ResponseEntity.ok(funcionariosAgrupadosPorFuncao);
  }

  @GetMapping("/sumSalarios")
  public ResponseEntity<BigDecimal> sumSalarios() {
    BigDecimal sum = funcionarioService.sumSalarios();
    return ResponseEntity.ok(sum);
  }

}
