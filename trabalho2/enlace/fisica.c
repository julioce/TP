
#include <stdio.h> /* para printf() */
#include <stdlib.h>
#include <string.h> /* para memset() */
#include <sys/socket.h> /* para socket() */
#include <netinet/in.h> /* para htonl(), htons() */
#include <arpa/inet.h> /* para htonl(), htons(), inet_addr() */
#include <sys/types.h>
#include <unistd.h> /* close() */
#include <fcntl.h> /* fcntl() */
#include <sys/file.h> /* O_NONBLOCK, O_ASYNC */
#include <signal.h> /* para sigaction() */
#include <errno.h> /* errno */

/* funcao para tratamento do sinal SIGIO */
void SIGIOHandler(int signalType);

/*efetua as inicializacoes necessarias da camada fisica, recebe a especificacao da porta que ser a usada para a comunicacao e do endereco da maquina remota, retorna 1 em caso de sucesso e 0 em caso de falha*/
int P_Activate_Request(int port, char *ip);

/*solicita a transmissao de 1 byte e recebe o byte a ser transmitido*/
void P_Data_Request(char data);

/*testa se ha um byte recebido na camada fisica, retorna 1 caso exista um byte recebido na camada fisica*/
int P_Data_Indication(void);

/*busca na camada fisica o ultimo byte recebido e retorna o byte recebido*/
char P_Data_Receive(void);

/*encerra o canal de comunicacao estabelecido*/
void P_Deactivate_Request(void);


int sock;
struct sockaddr_in addr;
struct sockaddr_in destAddr;
char buffer;
ssize_t recvSize;

struct sigaction handler; /* definicao da acao para tratamento de sinal */

/*  efetua as inicializacoes necessarias da camada fisica; recebe a porta a ser usada para a comunicacao e o endereco da maquina remota;
    retorna 1 em caso de sucesso e 0 em caso de falha
*/
int P_Activate_Request(int port, char *ip) {
	/* cria o descritor de socket para o servico entrega nao-confiavel */
	if ((sock = socket(PF_INET, SOCK_DGRAM, IPPROTO_UDP)) < 0) {
		printf("Erro na criacao do socket\n");
		return 0;
	}
	
	/* configura a estrutura de enderecamento com o endereco local */
	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_addr.s_addr = htonl(INADDR_ANY);
	addr.sin_port = htons(port);
	
	/* associa o descritor de socket com o endereco local */
	if (bind(sock,(struct sockaddr *)&addr, sizeof(struct sockaddr_in)) < 0) {
		printf("Erro no bind \n");
		return 0;
	} 
	
	/* configura a estrutura de enderecamento com o endereco de destino */
	memset(&destAddr, 0, sizeof(destAddr));
	destAddr.sin_family = AF_INET;
	destAddr.sin_addr.s_addr = inet_addr(ip);
	destAddr.sin_port = htons(port);
	
	/* estabelece a funcao de tratamento de sinal */
	handler.sa_handler = SIGIOHandler;
	/* Mascara todos os sinais */
	if (sigfillset(&handler.sa_mask) < 0) {
		printf("sigfillset() falhou\n");
		return 0;
	}
	/* sem flags */
	handler.sa_flags = 0;
	
	/* atribui o sinal SIGIO ao handler */
	if (sigaction(SIGIO, &handler, NULL) < 0) {
		printf("sigaction() falhou para SIGIO\n") ;
		return 0;
	}
	
	/* o socket precisa pertencer ao processo para receber o sinal SIGIO */
	if (fcntl(sock, F_SETOWN, getpid()) < 0) {
		printf("Nao foi possivel fazer o socket pertncer ao processo\n");
		return 0;
	}
	/* prepara para E/S nao bloqueante e para recebimento do sinal SIGIO */
	if (fcntl(sock, F_SETFL, O_NONBLOCK | O_ASYNC) < 0) {
		printf("Nao foi possivel colocar o socket em modo nao bloqueante e assincrono\n");
		return 0;
	}
	
	/* inicializa com recSize -1 para nao falsamente tornar P_Data_indication() == 1 */
	recvSize = -1;
	
	return 1;
}


/*recebe 1 byte e solicita sua transmissao*/
void P_Data_Request(char data) {
	/* envia 1 byte para o endereco em data para o destino */
	ssize_t bytes_sent = sendto(sock, (void *)&data, 1, 0, (struct sockaddr*)&destAddr, sizeof (struct sockaddr_in));
	/* erro no recebimento */
	if (bytes_sent < 0) {
		printf("Erro ao enviar\n");
		return;
	}
}

/*testa se ha um byte recebido na camada fisica; retorna 1 caso exista um byte recebido na camada fisica*/
int P_Data_Indication(void) {
	if (recvSize != -1) return 1;
	else return 0;
}

/*busca na camada fisica o ultimo byte recebido e o retorna*/
char P_Data_Receive(void) {
	/* coloca o valor de recvSize em -1 para P_Data_indication() passar a retornar 0*/
	recvSize = -1;
	
	return buffer;
}

/* funcao para tratamento do sinal SIGIO */
void SIGIOHandler(int signalType)
{
	struct sockaddr_in fromAddr;
	unsigned int fromLen;
	fromLen = sizeof(fromAddr);
	if ((recvSize = recvfrom(sock, (void *)&buffer, 1, 0, (struct sockaddr *)&fromAddr, &fromLen)) < 0)
	{
		/* unico erro aceitavel: recvfrom() teria bloqueado;
		   este erro ocorre quando SIGIOHandler() for chamada para tratar entrada assincrona no socket
		   e recvfrom() nao estiver pronto */
		if (errno != EWOULDBLOCK){
			printf("Erro no recebimento");
			return;
                }
	}
}

/*encerra o canal de comunicacao estabelecido*/
void P_Deactivate_Request(void) {
	close(sock);
}

