<script setup lang="ts">
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  LucideArrowDownAZ,
  LucideBanknoteArrowUp,
  LucideCheck,
  LucideEraser,
  LucideFilter,
  LucideListChevronsDownUp,
  LucideTrash,
  LucideX,
} from "@lucide/vue";
import { format, parse } from "date-fns";
import { ref } from "vue";
import Button from "./components/ui/button/Button.vue";
import CardDescription from "./components/ui/card/CardDescription.vue";
import Input from "./components/ui/input/Input.vue";
import Popover from "./components/ui/popover/Popover.vue";
import PopoverContent from "./components/ui/popover/PopoverContent.vue";
import PopoverTrigger from "./components/ui/popover/PopoverTrigger.vue";
import Toggle from "./components/ui/toggle/Toggle.vue";
import { MESES, SALARIO_MINIMO } from "./utils.ts";

const funcionarios = ref<Funcionario[]>([]);
const showAumentoInput = ref<Boolean>(false);

const funcionariosPorFuncao = ref<Funcionario[]>([]);
const showPorFuncao = ref<Boolean>(false);

const porcentagemAumento = ref<Number>(0);

const sumSalarios = ref<Number>(0);

const SELECTED_MESES_DEFAULT = {};

Object.keys(MESES).forEach((mes) => {
  SELECTED_MESES_DEFAULT[MESES[mes].id] = false;
});

const selectedMeses = ref<any>(
  JSON.parse(JSON.stringify(SELECTED_MESES_DEFAULT)),
);

const toggleSortByName = ref(false);

const fetchData = () => {
  const queryParams = new URLSearchParams({
    arrMeses: Object.keys(selectedMeses.value).filter(
      (key) => !!selectedMeses.value[key],
    ),
    sortNames: toggleSortByName.value,
  });

  fetch(`/api/funcionario?${queryParams.toString()}`)
    .then((res) => {
      return res.json();
    })
    .then(async (data) => {
      funcionarios.value = data;
      sumSalarios.value = await fetch("api/funcionario/sumSalarios").then(
        (res) => res.json(),
      );
    });
};
fetchData();

const renderDate = (dataNascimento: string) => {
  return format(
    parse(dataNascimento, "yyyy-MM-dd", dataNascimento, new Date()),
    "dd/MM/yyyy",
  );
};

const renderCurrency = (moeda: string) => {
  const formatted = Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
  }).format(moeda);
  return formatted;
};

const renderSalariosMinimos = (salario) => {
  const divisao = Math.trunc((salario / SALARIO_MINIMO) * 100) / 100;
  return divisao;
};

const removeUserByName = (name: String) => {
  fetch(`api/funcionario/${name}`, {
    method: "DELETE",
  }).then(() => {
    fetchData();
  });
};

const onAumento = () => {
  if (typeof porcentagemAumento.value !== "number") {
    return;
  }
  const porcentagem = porcentagemAumento.value / 100;

  fetch(`api/funcionario/aumento`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      porcentagem,
    }),
  }).then((res) => {
    fetchData();
    showAumentoInput.value = false;
  });
};

const onAgruparPorFuncao = () => {
  showPorFuncao.value = !showPorFuncao.value;
  if (showPorFuncao.value) {
    fetch("api/funcionario/groupByFuncao")
      .then((res) => res.json())
      .then((res) => {
        funcionariosPorFuncao.value = res;
      });
  }
};

const clearMonthFilter = () => {
  selectedMeses.value = JSON.parse(JSON.stringify(SELECTED_MESES_DEFAULT));
  fetchData();
};
</script>

<template>
  <main class="p-2 h-[100vh] overflow-hidden flex flex-col">
    <Card class="flex flex-col min-h-0 overflow-hidden">
      <CardHeader class="flex min-h-0 justify-between">
        <section>
          Controle de Funcionários
          <CardDescription>
            {{ funcionarios.length }} funcionários
          </CardDescription>
        </section>
        <section>
          <div v-if="!showAumentoInput" class="flex gap-2">
            <Button variant="outline" v-on:click="onAgruparPorFuncao()">
              <LucideListChevronsDownUp></LucideListChevronsDownUp> Agrupar
              Funções
            </Button>
            <Button variant="outline" v-on:click="showAumentoInput = true">
              <LucideBanknoteArrowUp></LucideBanknoteArrowUp> Aumento
            </Button>
          </div>
          <div v-if="showAumentoInput" class="flex gap-2">
            <Button variant="outline" v-on:click="showAumentoInput = false">
              <LucideX></LucideX>
            </Button>
            <Button variant="outline" v-on:click="onAumento()">
              <LucideCheck></LucideCheck>
            </Button>
            <Input
              placeholder="Aumento em %"
              type="number"
              v-model="porcentagemAumento"
            />
          </div>
        </section>
      </CardHeader>
      <CardContent class="flex flex-col overflow-hidden min-h-0">
        <div class="flex w-full justify-end mt-4">
          <div>Total Salários: {{ renderCurrency(sumSalarios) }}</div>
        </div>
        <Table class="flex-1 min-h-0 overflow-auto" v-if="!showPorFuncao">
          <TableHeader class="bg-card sticky top-0">
            <TableRow>
              <TableHead>
                <div class="flex items-center gap-2">
                  Nome
                  {{ toggleSortByName }}
                  <Toggle
                    variant="ghost"
                    v-model="toggleSortByName"
                    @update:model-value="fetchData()"
                  >
                    <LucideArrowDownAZ size="12"></LucideArrowDownAZ>
                  </Toggle>
                </div>
              </TableHead>
              <TableHead>
                <div class="flex items-center gap-2">
                  Data de Nascimento
                  <Popover>
                    <PopoverTrigger>
                      <Button size="xs" variant="ghost">
                        <LucideFilter size="12"></LucideFilter>
                      </Button>
                    </PopoverTrigger>
                    <PopoverContent>
                      <Toggle
                        :aria-label="mes.id"
                        v-for="mes in MESES"
                        :key="mes.id"
                        v-model="selectedMeses[mes.id]"
                      >
                        {{ mes.label }}
                      </Toggle>
                      <div class="flex justify-end gap-2">
                        <Button variant="outline" @click="clearMonthFilter()">
                          <LucideEraser></LucideEraser>
                          Limpar
                        </Button>
                        <Button @click="fetchData()">
                          <LucideFilter></LucideFilter> Filtrar
                        </Button>
                      </div>
                    </PopoverContent>
                  </Popover>
                </div>
              </TableHead>
              <TableHead> Salário </TableHead>
              <TableHead> Salários Mínimos </TableHead>
              <TableHead> Função </TableHead>
              <TableHead> </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow v-for="(funcionario, i) in funcionarios" :key="i">
              <TableCell> {{ funcionario.nome }} </TableCell>
              <TableCell>
                {{ renderDate(funcionario.dataNascimento) }}
              </TableCell>
              <TableCell>
                {{ renderCurrency(funcionario.salario) }}
              </TableCell>
              <TableCell>
                {{ renderSalariosMinimos(funcionario.salario) }}
              </TableCell>
              <TableCell> {{ funcionario.funcao }} </TableCell>
              <TableCell>
                <Button
                  v-on:click="removeUserByName(funcionario.nome)"
                  class="cursor-pointer"
                  variant="destructive"
                  size="sm"
                >
                  <LucideTrash></LucideTrash>
                </Button>
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>

        <Table class="flex-1 min-h-0 overflow-auto" v-if="showPorFuncao">
          <TableHeader class="bg-card sticky top-0 z-10">
            <TableRow>
              <TableHead> Função </TableHead>
              <TableHead> Funcionários </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            <TableRow
              v-for="(funcao, i) in Object.keys(funcionariosPorFuncao)"
              :key="i"
            >
              <TableCell width="200px"> {{ funcao }} </TableCell>
              <TableCell>
                <Table class="table-fixed">
                  <TableBody>
                    <TableRow
                      v-for="funcionario in funcionariosPorFuncao[funcao]"
                    >
                      <TableCell> {{ funcionario.nome }} </TableCell>
                      <TableCell>
                        {{ renderDate(funcionario.dataNascimento) }}
                      </TableCell>
                      <TableCell>
                        {{ renderCurrency(funcionario.salario) }}
                      </TableCell>
                      <TableCell> {{ funcionario.funcao }} </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  </main>
</template>

<style scoped></style>
