## Type
O _type_ pode ser um desses tipos:

| Prefixo | Descrição           | Significado                                    |
|---------|---------------------|------------------------------------------------|
| feat    | Features            | Uma nova funcionalidade                        |
| fix     | Correções de Erros  | Uma correção de bug                            |
| docs    | Documentação        | Apenas mudanças na documentação               |
| style   | Estilos             | Mudanças em relação a estilização              |
| refactor| Refatoração de Código | Uma alteração de código que não corrige um bug nem adiciona uma funcionalidade |
| perf    | Melhorias de Performance | Uma alteração de código que melhora o desempenho |
| test    | Testes              | Adição de testes em falta ou correção de testes existentes |
| build   | Builds              | Mudanças que afetam o sistema de build ou dependências externas (exemplos de escopos: gulp, broccoli, npm) |
| ci      | Integrações Contínuas | Alterações em nossos arquivos e scripts de configuração de CI (exemplos de escopos: Travis, Circle, BrowserStack, SauceLabs) |
| chore   | Tarefas             | Outras mudanças que não modificam arquivos de código-fonte ou de teste |
| revert  | Reverter            | Reverte um commit anterior                    |


## Categoria de códigos
Os _códigos HTTP_ (ou HTTPS) possuem três dígitos, sendo que o primeiro dígito significa a classificação dentro das possíveis cinco categorias.

| Código | Descrição   | Significado |
|--------|-------------|-------------|
| 1XX    | Informativo | A solicitação foi aceita ou o processo continua em andamento; |
| 2XX    | Confirmação | A ação foi concluída ou entendida; |
| 3XX    | Redirecionamento | Indica que algo mais precisa ser feito ou precisou ser feito para completar a solicitação; |
| 4XX    | Erro do cliente | Indica que a solicitação não pode ser concluída ou contém a sintaxe incorreta; |
| 5XX    | Erro no servidor | O servidor falhou ao concluir a solicitação. |
