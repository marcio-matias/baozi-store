# Verificação do projeto

Executado em 2 de novembro de 2026 com Java 17 e Maven 3.10.0.

Resultado: BUILD SUCCESS. Um teste de integração executado, sem falhas ou erros.

O teste inicia a aplicação HTTP em uma porta livre com H2 em memória e verifica:

- Cadastro, listagem, consulta por ID e exclusão das três entidades.
- Nome obrigatório, JSON inválido, preço negativo ou com precisão inválida.
- Quantidade positiva e referência a cliente existente.
- Rejeição de pedido para produto indisponível.
- Bloqueio da exclusão de cliente ou produto com pedido vinculado.
- Resposta 404 após exclusão e ao consultar registro ausente.

Esses testes automatizados não substituem as capturas reais do Postman exigidas na atividade.
