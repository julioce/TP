#include <cstdio> /* para printf() */
#include <cstdlib> 
#include <cstring> /* para memset() */
#include <sys/socket.h> /* para socket() */
#include <netinet/in.h> /* para htonl(), htons() */
#include <arpa/inet.h> /* para htonl(), htons(), inet_addr() */
#include <sys/types.h>
#include <unistd.h> /* close() */
#include <fcntl.h> /* fcntl() */
#include <sys/file.h> /* O_NONBLOCK, O_ASYNC */
#include <signal.h> /* para sigaction() */
#include <errno.h> /* errno */
#include <map>
#include <string>


#define MAX_OPS 100
#define TAM_BUFFER_MAX 10 //numero de quadros no buffer
#define TAM_FRAME_MAX 1000 //tamanho maximo de um frame

using namespace std;

int sock, flag, port;
char buffer;
char buffer_in[TAM_FRAME_MAX]; //guarda um frame recebido
map <unsigned char, string> tabela_broadcast;
map <unsigned char, string> tabela_comutador;
string buffer_in_temp; //guarda um frame recebido
ssize_t recvSize;
fd_set readfds;
struct timeval timeout;
 
struct sigaction handler; /* definicao da acao para tratamento de sinal */
struct sockaddr_in fromAddr;
struct sockaddr_in destAddr;

int Activate_Request(int);
void SIGIOHandler(int signalType); /* funcao para tratamento do sinal SIGIO */
void Deactivate_Request(void);

void Data_Request(char *, char *, int);
void Data_Request_Broadcast(char *data, unsigned char MAC_src);
void Transmite_Byte(char c, char *addr, int porta);

int Data_Receive (unsigned char *addr, unsigned char *dest_addr, char *data, int size);
void Recebe_Byte(void);
char P_Data_Receive(void);

int Data_Indication (void);
int P_Data_Indication(void);


int main(int argc, char *argv[]) {
   char data[TAM_FRAME_MAX], IP[20];
   unsigned char MAC_addr_src, MAC_addr_dest;
   int size;
   tabela_broadcast.clear();

   if(argc == 2) {
      port = atoi(argv[1]);
      if(Activate_Request(port) == 1){
	printf("Execução do Comutador iniciada. Aguardando hosts...\n", buffer_in);
         while(1){
            //recebo um byte da camada fisica 
            if(P_Data_Indication() == 1){
               Recebe_Byte();
               if(Data_Indication()){
                  //recebendo o frame
                  Data_Receive(&MAC_addr_src, &MAC_addr_dest, data, size);
                  //buscando o endereco IP do emissor
                  strcpy(IP, inet_ntoa(fromAddr.sin_addr));
                  //um host se conectando
                  if(data[0] == 'h' && data[1] == 'i'){
                     //adiciono na tabela de broadcast
                     tabela_broadcast[MAC_addr_src] = IP;
                     printf("Host %d conectado\n", MAC_addr_src);
				  }
                  else{
                     //adiciono o endereco na tabela
                     tabela_comutador[MAC_addr_src] = IP;
                     
                     if(MAC_addr_dest == 255){
                        printf("Enviando \"%s\" por broadcast. MAC Address 255\n", buffer_in);
                        Data_Request_Broadcast(buffer_in, MAC_addr_src);
                        
                     }
                     //se o endereco MAC estiver na tabela
                     else if(tabela_broadcast.find(MAC_addr_dest) != tabela_broadcast.end()){
                        strcpy(IP, tabela_broadcast[MAC_addr_dest].c_str());
                        printf("Enviando o \"%s\" para %d\n", buffer_in, MAC_addr_dest);
                        Data_Request(buffer_in, IP, port);
                     }
                     else{
			printf("Enviando \"%s\" por Broadcast devido a MAC desconhecido pelo comutador.\n", buffer_in);
                        Data_Request_Broadcast(buffer_in, MAC_addr_src);
                     }
                  }
               }
            }
         }
      }
      else{
         printf("Erro na funcao Activate_Request\n");
      }
   }
   else{
      printf("Erro na inicializacao: %s <remote server Port> \n", argv[0]);
   }
   return 0;
}


int Activate_Request(int port) {
   struct sockaddr_in addr;
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
      printf("Nao foi possivel fazer o socket pertencer ao processo\n");
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


//transmite os dados para o endereco e porta especificado
void Data_Request(char *data, char *addr, int porta){
   int tam, i;
   //transmite byte a byte
   tam = strlen(data);
   
   for (i = 0; i < tam; i++)
      Transmite_Byte(data[i], addr, porta);
}


void Transmite_Byte(char data, char *addr, int porta){
   // configura a estrutura de enderecamento com o endereco de destino 
   memset(&destAddr, 0, sizeof(destAddr));
   destAddr.sin_family = AF_INET;
   destAddr.sin_addr.s_addr = inet_addr(addr);
   destAddr.sin_port = htons(porta);

   // envia 1 byte para o endereco em data para o destino
   ssize_t bytes_sent = sendto(sock, (void *)&data, 1, 0, (struct sockaddr*)&destAddr, sizeof (struct sockaddr_in));
   usleep(5000); // um tempo de sleep para a realizacao de E/S
   
   // erro no recebimento
   if (bytes_sent < 0) {
      printf("Erro ao enviar\n");
      return;
   }
}


int Data_Receive (unsigned char *src_addr, unsigned char *dest_addr, char *data, int size){
   int tam, addr;
   char temp[10];
   
   //recuperando o src_addr
   strncpy(temp, &buffer_in[3], 3);
   temp[3] = '\0';
   sscanf(temp, "%d", &addr);
   *src_addr = addr;

   //recuperando o dest_addr
   strncpy(temp, buffer_in, 3);
   temp[3] = '\0';
   sscanf(temp, "%d", &addr);
   *dest_addr = addr;
   
   //recuperando o tamanho do campo de dados
   strncpy(temp, &buffer_in[6], 3);
   temp[3] = '\0';
   sscanf(temp, "%d", &tam);

   //recuperando os dados
   strncpy(data, &buffer_in[9], tam);
   data[tam] = '\0';

   //limpando as variaveis necessarias
   flag = 0;
   
   if (tam <= size)
      return tam;
   else
      return -1;
}


void Recebe_Byte(void){
   int tam_dados, tam_frame = -1;
   char temp[10], c;
   //adiciono o byte recebido ao buffer
	
	c = P_Data_Receive();
	//printf("%c\n", c);
   buffer_in_temp += c;
   
   //trecho para recuperar o tamanho do quadro que estao sendo recebido
   if(buffer_in_temp.size() >= 9){
      //recuperando o tamanho do campo de dados
      strncpy(temp, &(buffer_in_temp.c_str()[6]), 3);
      temp[3] = '\0';
      sscanf(temp, "%d", &tam_dados);
      
      //12 = 3 bytes de end. dest. + 3 de bytes end. emissor + 3 bytes de tamanho + 3 bytes de erro 
      tam_frame = tam_dados + 12;
   }
   
   //se o quadro for completamente
   if(buffer_in_temp.size() == tam_frame){
      //copio para o buffer_in
      strcpy(buffer_in, buffer_in_temp.c_str());
      
      //limpo o buffer_in_temp
      buffer_in_temp.clear();
      
      //aviso que um quadro foi recebido
      flag = 1;
   }
}


/*busca na camada fisica o ultimo byte recebido e o retorna*/
char P_Data_Receive(void) {
   /* coloca o valor de recvSize em -1 para P_Data_indication() passar a retornar 0*/
   recvSize = -1;

   return buffer;
}


//avisa se um quadro chegou
int Data_Indication (void){
   if (flag == 1) 
      return 1;
   else
      return 0;
}


/*testa se ha um byte recebido; retorna 1 caso exista um byte recebido*/
int P_Data_Indication(void) {
   if (recvSize != -1) return 1;
   else return 0;
}


/*envia os dados por broadcast menos ao emissor*/
void Data_Request_Broadcast(char *data, unsigned char MAC_src) {
   map <unsigned char, string> ::iterator it;
   char IP[20];
   
   //envio os dados para cada entrada da tabela diferente do MAC_scr
   for(it = tabela_broadcast.begin(); it != tabela_broadcast.end(); it++){
      if((*it).first != MAC_src){
         strcpy(IP, ((*it).second).c_str());
         Data_Request(data, IP, port);
      }
   }
}


/* funcao para tratamento do sinal SIGIO */
void SIGIOHandler(int signalType){
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
void Deactivate_Request(void){
   close(sock);
}
