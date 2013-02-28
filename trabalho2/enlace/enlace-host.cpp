#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>
#include "enlace.h"

#define MAX_OPS 200

int main(int argc, char *argv[]) {
   int port, mac_addr, i = MAX_OPS, destino;
   fd_set readfds;
   struct timeval timeout;
   char data[FRAME_SIZE], quadro[FRAME_SIZE];
   char *ip;
   unsigned char addr;
   float rate;
   if(argc == 4) {
      ip = argv[1];
      port = atoi(argv[2]);
      mac_addr = atoi(argv[3]);
      if(L_Activate_Request(mac_addr, port, ip) == 1) {
         //enviando uma mensagem de introducao para o comutador
         sprintf(data, "hi");
         L_Data_Request(0, data, strlen(data));
         //setando a taxa de erro
         printf("Defina a taxa de erro da conexão [0-1]:\n");
         scanf("%f", &rate);
         L_Set_Loss_Probability(rate);
         printf("Escreva a mensagem:\n");
         while (i >= 0) {
            FD_ZERO(&readfds);
            FD_SET(0, &readfds); /* Add o teclado ao file descriptor set */
            timeout.tv_sec = 0;
            timeout.tv_usec = 1000;
            //se o tiver quadro no buffer, recebo ele
            if (L_Data_Indication()) {
               L_Data_Receive (&addr, data, FRAME_SIZE);
               printf("Mensagem \"%s\" recebida do host %d\n", data, addr);
               --i;
            }
            //se tiver dados para enviar
            if (select(1, &readfds, NULL, NULL, &timeout) == 1) {
               scanf(" %[^\n]", data);
               printf("Endereco MAC de destino:\n");
               scanf(" %d", &destino);
               L_Data_Request(destino, data, strlen(data));
               --i;
               printf("Escreva a mensagem:\n");
            }
            L_MainLoop();
         }
      }
      
      else{
         printf("Erro na funcao Activate_Request\n");
      }
   }
   else{
      printf("Erro na inicializacao: %s <IP da maquina do comutador> <Porta da maquina do comutador> <MAC address ficticio>\n", argv[0]);
   }

   L_Deactivate_Request();
   
   return 0;
}
