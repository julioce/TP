#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <queue>
#include <string>
#include <ctime>

#include "fisica.c"
#include "enlace.h"

#define TAM_BUFFER_MAX 10 // numero de quadros no buffer
#define TAM_FRAME_MAX 2000 // tamanho maximo de um frame

using namespace std;

unsigned char local_addr; // endereco MAC local
int flag; // indica que ha de um quadro disponivel para recebimento
float loss_prob; // probabilidade de erro
char buffer_in[TAM_FRAME_MAX]; //guarda um frame recebido da camada fisica 
string buffer_in_temp; //guarda um frame recebido da camada fisica
string buffer_out_temp; //guarda os bytes do quadro a serem transmitidos pela camada fisica 
queue<string> buffer_out; //guarda os frames a serem transmitidos pela camada fisica

int Calcula_Codigo_Erro(const char *frame);

int L_Activate_Request (unsigned char _local_addr, int switch_port, char *switch_ip){
   if (P_Activate_Request(switch_port, switch_ip)){
      //setando o endereco MAC
      local_addr = _local_addr;

      //inicializando as variaveis
      flag = 0;
      memset(buffer_in, 0, sizeof(buffer_in));
      return 1;
   } 
   else 
      return 0;
}

void L_Data_Request (unsigned char dest_addr, char *data, int size){
   char frame[TAM_FRAME_MAX], temp[TAM_FRAME_MAX];  
   int error_code;
   //calculando o codigo de erro
   sprintf(temp, "%03u%03u%03d%s000", dest_addr, local_addr, size, data);
   error_code = Calcula_Codigo_Erro(temp);
   
   //faco o enquadramento
   sprintf(frame, "%03u%03u%03d%s%03d", dest_addr, local_addr, size, data, error_code);
   
   //limito o numero de quadros no buffer para TAM_BUFFER_MAX
   if (buffer_out.size() < TAM_BUFFER_MAX){
      buffer_out.push(frame);
   }
}

int L_Data_Indication (void){
   if (flag == 1) 
      return 1;
   else
      return 0;
}

int L_Data_Receive (unsigned char *src_addr, char *data, int size){
   int tam, addr;
   char temp[10];

   //recuperando o src_addr
   strncpy(temp, &buffer_in[3], 3);
   temp[3] = '\0';
   sscanf(temp, "%d", &addr);
   *src_addr = addr;

   //recuperando o tamanho do campo de dados
   strncpy(temp, &buffer_in[6], 3);
   temp[3] = '\0';
   sscanf(temp, "%d", &tam);

   //recuperando os dados
   strncpy(data, &buffer_in[9], tam);
   data[tam] = '\0';
   
   //limpando as variaveis necessarias
   flag = 0;
   memset(buffer_in, 0, sizeof(buffer_in));
   
   if (tam <= size)
      return tam;
   else
      return -1;
}

void L_MainLoop (void){
   //recebo um byte da camada fisica 
   if(P_Data_Indication() == 1){
      l_Recebe_Byte();
   }
   
   //preencho o buffer buffer_out_temp se necessario
   if(!buffer_out.empty() && buffer_out_temp.empty()){
	  usleep(1000000);
      buffer_out_temp = buffer_out.front();
      buffer_out.pop();
   }
   
   //transmito um byte se o buffer nao estiver vazio
   if(!buffer_out_temp.empty()){
	   l_Transmite_Byte();
   }
}


void L_Set_Loss_Probability (float rate){
   loss_prob = rate;
}


void L_Deactivate_Request(void){
   P_Deactivate_Request();
}


void l_Recebe_Byte(void){
   int tam_dados, tam_frame = -1;
   char temp[10];
   char c;
   c = P_Data_Receive();
   //printf("%c\n", c);
   //adiciono o byte recebido ao buffer
   buffer_in_temp += c;
   
   //trecho para recuperar o tamanho do quadro sendo recebido
   if(buffer_in_temp.size() >= 9){
      //recuperando o tamanho do campo de dados
      strncpy(temp, &(buffer_in_temp.c_str()[6]), 3);
      temp[3] = '\0';
      sscanf(temp, "%d", &tam_dados);
      
      //12 = 3 bytes de end. dest. + 3 de bytes end. emissor + 3 bytes de tamanho + 3 bytes de erro 
      tam_frame = tam_dados + 12;
   }
   
   //se o quadro for completamente recebido faco a validacao
   if(buffer_in_temp.size() == tam_frame){
      if(l_Valida_Quadro(buffer_in_temp.c_str())){     
         //copio para o buffer_in
         strcpy(buffer_in, buffer_in_temp.c_str());
         
         //limpo o buffer_in_temp
         buffer_in_temp.clear();
         
         //aviso que um quadro foi recebido
         flag = 1;
      }
      //descarto o quadro se nao passar no teste de validacao
      else
         buffer_in_temp.clear();
   }
}


void l_Transmite_Byte(void){
  
   //transmitindo um byte e o apagando do buffer
   //printf("(%c)\n", *buffer_out_temp.begin());
   P_Data_Request(*buffer_out_temp.begin());
   buffer_out_temp.erase(buffer_out_temp.begin());
   usleep(10000); // um tempo de sleep para a realizacao de E/S
}

bool l_Valida_Quadro(const char *frame){
   int fim = strlen(frame), addr, codigo_erro;
   char temp[10];
   double prob;
  
   //gera um numero entre 0 e 1
   srand ( time(NULL) );  
   prob = ((double)rand())/RAND_MAX;
   
   //faco a verificacao de erro que ocorreu de fato
   sscanf(&frame[fim-3], "%d", &codigo_erro);
   if(Calcula_Codigo_Erro(frame) != codigo_erro){
      printf("Quadro invalido recebido: codigo de erro\n");
      return false;
   }
   
   //usando a probabilidade randomica para forcar a ocorrencia de erros
   else if(prob < loss_prob){
      printf("Quadro invalido recebido: probabilidade forcada de falha na conexao\n");
      return false;
   }
   
   else{
      //recuperando o endereco do destinatario
      strncpy(temp, frame, 3);
      temp[3] = '\0';
      sscanf(temp, "%d", &addr);
      
      //se o quadro for para mim
      if(local_addr == addr || addr == 255){
         return true;
      }
      else{
         printf("Quadro invalido recebido: endereco de destinatario diferente\n");
         return false;
      }
   }
}

//calculando o codigo de erro
int Calcula_Codigo_Erro(const char *frame){
   int tam, i, codigo = 0;
   tam = strlen(frame) - 3;
   for(i = 0; i < tam; i++){
      codigo = codigo + frame[i];
   }
   codigo = codigo % 1000;

   return codigo;
}
