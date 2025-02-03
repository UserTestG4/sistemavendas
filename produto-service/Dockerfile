# Use uma imagem base do Node.js
FROM node:16

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o package.json e instala as dependências
COPY package.json package-lock.json* ./
RUN npm install

# Copia o restante do código para dentro do container
COPY . .

# Expõe a porta 3000
EXPOSE 3000

# Comando para iniciar a aplicação
CMD ["node", "index.js"]
