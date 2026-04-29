# TCCBank – Arquitetura de Pagamento (RecebedorPSP, PagadorPSP e BancoCentral)

Este repositório contém uma simulação de pagamento de cobranças via REST com 3 serviços Spring Boot:

- **recebedorPSP (porta 8080)**: cria e mantém cobranças, disponibiliza página HTML de pagamento e recebe webhook de confirmação.
- **pagadorPSP (porta 8090)**: inicia o pagamento a partir de um `txid`.
- **BancoCentral (porta 8095)**: processa o pagamento, valida o `txid` e notifica o recebedorPSP via webhook.

## Metodologia de pagamento (fluxo)

A metodologia implementada segue este encadeamento:

1. **Criação da cobrança (recebedorPSP)**
   - O recebedor cria uma cobrança e armazena localmente com **status inicial `PENDENTE`**.
   - Um `txid` único é gerado (quando não fornecido).
   - A cobrança retorna um link de pagamento `payLink` que aponta para uma página HTML.

2. **Exposição da página de pagamento (HTML + JS)**
   - A página de pagamento é servida via `GET /pay/{txid}` e renderiza:
     - `txid`
     - `status` atual
     - dados da cobrança (nome do recebedor, banco destino, valor)
     - botão **Pagar**
   - Após o clique no botão, uma requisição HTTP `POST` é realizada para iniciar o pagamento.
   - A página faz **polling** a cada ~2 segundos consultando o status até confirmar `PAGO`.

3. **Início do pagamento (pagadorPSP → BancoCentral)**
   - O pagadorPSP recebe `POST /pagar` com `{ "txid": "..." }`.
   - Ele encaminha para o BancoCentral em `POST /processar-pagamento`.
   - Quando o BancoCentral está exposto via **ngrok**, o pagadorPSP pode usar o `central.baseUrl` apontando para a URL pública.

4. **Processamento do pagamento (BancoCentral)**
   - O BancoCentral valida o `txid` consultando o recebedorPSP.
   - Marca o pagamento como **`PAGO`** internamente.
   - Retorna sucesso para o pagador.

5. **Webhook (BancoCentral → recebedorPSP)**
   - Após confirmar o pagamento, o BancoCentral envia um **HTTP POST real** para o recebedorPSP:
     - `POST /webhook/pagamento`
     - Body: `{ "txid": "string", "status": "PAGO" }`
   - O recebedorPSP atualiza a cobrança de `PENDENTE` para `PAGO`.

6. **Atualização automática da tela**
   - A página HTML (no browser) detecta a mudança via polling.
   - Quando `status = PAGO`, mostra **“Pagamento confirmado”**.

## Endpoints principais

### recebedorPSP (8080)

- `POST /cobrancas`  
  Cria cobrança em memória com `txid` único e status `PENDENTE`.

  Exemplo:
  ```json
  {
    "nomeRecebedor": "Loja Exemplo",
    "bancoDestino": "TCCBank",
    "valor": 49.90,
    "descricao": "Pedido 123"
  }
  ```
  Observação: se `nomeRecebedor` ou `bancoDestino` vierem vazios/ausentes, o serviço aplica valores padrão.

- `GET /cobrancas/{txid}`  
  Retorna os dados e o status atual.

- `GET /pay/{txid}`  
  Página HTML simples (para uso em navegador).

- `POST /webhook/pagamento`  
  Webhook consumido pelo BancoCentral para atualizar o status.

#### Cobrança persistida (JPA): CobrancaPix

O recebedorPSP também possui a entidade **CobrancaPix** (JPA) com endpoints existentes no projeto:

- `POST /cobrancapix`
- `GET /cobrancapix/{txid}` (gera QR Code em PNG do payload)

### pagadorPSP (8090)

- `POST /pagar`  
  Body:
  ```json
  { "txid": "..." }
  ```
  Encaminha o pagamento para `POST /processar-pagamento` no BancoCentral.

### BancoCentral (8095)

- `POST /processar-pagamento`  
  Processa pagamento e dispara webhook para o recebedorPSP.

#### UI no BancoCentral (para uso via ngrok)

Para facilitar a renderização da página ao escanear um QR Code em ambiente com URL pública:

- `GET /qrcode/pay/{txid}` → retorna QR Code (PNG) apontando para `/pay/{txid}` no domínio público configurado.
- `GET /pay/{txid}` → página HTML hospedada pelo BancoCentral.
- `GET /ui/cobrancas/{txid}` → proxy do BancoCentral para consultar o recebedorPSP.

## Execução com Docker Compose

Subir tudo:

```bash
docker compose up -d --build
```

Portas:

- recebedorPSP: `http://localhost:8080`
- pagadorPSP: `http://localhost:8090`
- BancoCentral: `http://localhost:8095`

Parar:

```bash
docker compose down
```

## Execução com ngrok (BancoCentral público)

Quando você expõe o BancoCentral via ngrok, a URL pública pode ser usada para:

- abrir `https://<seu-ngrok>/pay/{txid}` no celular
- gerar o QR Code via `https://<seu-ngrok>/qrcode/pay/{txid}`

Configurações relevantes (via properties/env):

- **BancoCentral**
  - `public.baseUrl` (URL pública do ngrok, usada para montar o link dentro do QR Code)
  - `recebedor.baseUrl` (URL do recebedorPSP que receberá webhook e consultará status)
- **pagadorPSP**
  - `central.baseUrl` (URL do BancoCentral, pode ser o domínio do ngrok)

## Teste rápido (passo a passo)

1. Crie a cobrança no recebedor:
   - `POST http://localhost:8080/cobrancas`
2. Pegue o `txid` retornado.
3. Abra a página:
   - Local: `http://localhost:8080/pay/{txid}`
   - Via ngrok (BancoCentral): `https://<seu-ngrok>/pay/{txid}`
4. Clique em **Pagar**.
5. Aguarde o status atualizar para `PAGO` e a mensagem **“Pagamento confirmado”** aparecer.

