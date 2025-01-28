FROM php:7.4-apache

# Instalar a extensão MySQLi
RUN docker-php-ext-install mysqli

# Copiar os arquivos PHP para o servidor Apache
COPY . /var/www/html/

# Expor a porta 80
EXPOSE 80
