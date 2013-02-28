#ifndef ENLACE_H
#define ENLACE_H

#define FRAME_SIZE 110

int L_Activate_Request (unsigned char, int, char *);

void L_Data_Request (unsigned char, char *, int);

int L_Data_Indication (void);

int L_Data_Receive (unsigned char *, char *, int);

void L_MainLoop (void);

void L_Set_Loss_Probability (float);

void L_Deactivate_Request(void);

void l_Recebe_Byte(void);

bool l_Valida_Quadro(const char *);

void l_Transmite_Byte();

#endif
