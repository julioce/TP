UFRJ - Universidade Federal do Rio de Janeiro
=============
Série de trabalhos para a disciplina de Teleprocessamento e Redes - DCC UFRJ
=============

Trabalho 1 - Chat Cliente-Servidor
-----------

  Parâmetros de chamada: N/A
	Arquivos auxiliares utilizados: N/A
	Programa auxiliar: N/A
	Forma de uso:
		1. Execução do programa Servidor.jar em uma máquina
		2. Execução do programa Cliente.jar em uma máquina diferente da sendo usada no Servidor.jar
		3. Em Cliente, inserir os valores de conexão com o Servidor.
	
Trabalho 2 - Emulador da camada de Enlace
-----------
  Compilação
    $ cd enlace
    $ make

  Comutador
	Parâmetros de chamada: ./comutador <Porta do comutador>
  Arquivos auxiliares utilizados: N/A
	Programa auxiliar: N/A
	Forma de uso:
		1. Execução do programa com os parâmetros corretos
		2. Exibição das ações do comutador e dos hosts

  Host
  Parâmetros de chamada: ./host <IP da maquina do comutador> <Porta da maquina do comutador> <MAC
address ficticio>
  Arquivos auxiliares utilizados: N/A
	Programa auxiliar: ./comutador
	Forma de uso:
		1. Garantir as permissões para a execuução do arquivo host com $ chmod 777 ./host
    2. Execução do programa com os parâmetros corretos
		3. Definir a taxa de erro da camada com valor [0-1]
    4. Escrever a mensagem a ser enviada.
    5. Definir o endereço MAC ficticio de destino da mensagem. 225 define envio por broadcast.


Este README tem apenas como função analisar brevemente o uso/utilidade de cada programa assim como o seu funcionamento básico. Mais detalhes sobre o uso de cada um deve ser feito através da depuração do código fonte ou pela leitura dos relatórios de cada trabalho.
	
Todos os programas foram escritos a fim de demonstrar as caracterísitcas individuais dos trabalhos propostos. Dessa forma, cada programa tem como finalidade educacional e se comporta de forma independente. O seu uso em aplicações de alto risco e missão crítica não é recomendado.

Não há intenções por parte do desenvolvedor na manutenção deste repositório nem atualizações referentes a implementações de novos métodos/funcionalidades.
No entanto, você é livre para utilizá-lo e prosseguir em seu desenvolvimento de acordo com as normas BY-NC-SA - Creative Commons License. 

