all: link comutador1 rm-obj
fisica: fisica.c
	g++ -c fisica.c
enlace: enlace.cpp
	g++ -c enlace.cpp
enlace-host: enlace-host.cpp
	g++ -c enlace-host.cpp
link: fisica enlace enlace-host 
	g++ -o host fisica.o enlace.o enlace-host.o -Xlinker -zmuldefs
comutador1: comutador.cpp
	g++ -o comutador comutador.cpp
link-comutador: comutador1 fisica enlace 
	g++ -o comutador comutador.o fisica.o enlace.o -Xlinker -zmuldefs
rm-obj: 
	rm fisica.o enlace.o enlace-host.o
