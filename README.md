# Sistema Financeiro

[![CI](https://github.com/FabricioBarross/SistemaFinanceiro/actions/workflows/ci.yml/badge.svg)](https://github.com/FabricioBarross/SistemaFinanceiro/actions/workflows/ci.yml)

Aplicativo de console em Java para controlar receitas e despesas, com persistência em SQLite.

<!-- CAPTURA DE TELA DO TERMINAL AQUI -->

## Funcionalidades

- Registrar receitas e despesas com descrição, valor, categoria e data
- Consultar receitas, despesas e saldo
- Filtrar movimentações por período (inclusivo)
- Ver totais por categoria
- Remover movimentações, com confirmação
- Dados persistidos entre execuções

## Requisitos

- JDK 25
- Maven 3.9+

## Como rodar

Pela IDE: execute a classe `com.sistemafinanceiro.Main`.

Pelo terminal (Linux/macOS):

```bash
mvn -q compile dependency:copy-dependencies -DincludeScope=runtime
java --enable-native-access=ALL-UNNAMED -cp "target/classes:target/dependency/*" com.sistemafinanceiro.Main
```

No Windows, troque `:` por `;` no classpath. O parâmetro `--enable-native-access` evita um aviso do JDK sobre o carregamento da biblioteca nativa do SQLite; o programa funciona sem ele. O banco `financeiro.db` é criado na pasta de onde o programa é executado.

## Como rodar os testes

```bash
mvn clean test
```

## Estrutura

```
src/main/java/com/sistemafinanceiro/
├── Main.java                  # menu e entrada/saída
├── model/                     # Movimentacao, Categoria, TipoMovimentacao
├── repository/                # interface + implementações em memória e SQLite
└── service/                   # ServicoFinanceiro (regras e cálculos)
src/test/java/...              # testes JUnit 5 espelhando a estrutura
```

## Decisões de projeto

1. **`BigDecimal` para dinheiro.** `double` não representa 0,1 exatamente; somas acumulam erro. `BigDecimal` garante cálculo exato.
2. **Valor em centavos (INTEGER) no SQLite.** O SQLite não tem tipo decimal exato; guardar `REAL` reintroduziria o erro de ponto flutuante. Inteiros em centavos são exatos e convertidos de volta com escala 2.
3. **Rejeitar em vez de arredondar.** Um valor com mais de 2 casas decimais é entrada inválida. Arredondar em silêncio alteraria o dado do usuário sem aviso. A regra vive em um único lugar (`Movimentacao.normalizarValor`) e é reutilizada pela interface.
4. **Interface `RepositorioMovimentacao`.** O serviço depende da abstração, não do SQLite. Isso permite testar as regras com a implementação em memória e trocar o armazenamento sem alterar a lógica.
5. **Enums gravados por `name()`.** O `ordinal()` quebra os dados salvos se alguém reordenar ou inserir um valor no enum. O nome é estável.

## Limitações conhecidas

- Aplicativo de console, sem interface gráfica
- Não há edição de movimentação (apenas registrar e remover)
- O banco é criado na pasta de execução, então rodar de outro diretório abre outro banco
- A classe `Main` não tem testes automatizados