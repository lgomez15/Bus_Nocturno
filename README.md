# Bus_Nocturno 
Instrucciones para el correcto funcionamiento:
1. Crear fichero "jade" en ruta, tal que C:\jade tenga el fichero jade.rar (Recomendable utilizar la ultima versión).
2. Añadir a las variables de entorno: %CLASSPATH%;.;C:\jade\jade.jar
3. asegurarse de tener los ficheros linea1Data.txt y linea2Data.txt en el escritorio.

Instrucciones de ejecución:
1. cd .\Desktop\Bus_Nocturno\src\ (cd hasta el fichero src del proyecto donde se encuentre).
2. Compilar con: javac agents/*.java
3. En la primera terminal ejecutar Start-Process Powershell;  Start-Process Powershell; Start-Process Powershell; Start-Process Powershell;
4. En esa misma terminal ejecutar: java jade.Boot -gui
5. java jade.Boot -container Client:agents.Client ,en la segunda terminal para lanzar al cliente.
6. En la tercera terminal ejecutar: java jade.Boot -container "AgenteLinea1:agents.AgenteLinea(1)" ,para desplegar al agente linea uno.
7. En la cuarta: java jade.Boot -container "AgenteLinea2:agents.AgenteLinea(2)" , para el dos.
8. En la quinta terminal, lanzar el servicio con: java jade.Boot -container Servicio:agents.Service.

En caso de querer probar multiples clientes (comprobar el correcto funcionamiento del paralelismo y la concurrencia), lanzar varios clientes con diferentes nombres, por ejemplo:
1. java jade.Boot -container Client1:agents.Client
2. java jade.Boot -container Client2:agents.Client
3. java jade.Boot -container Client3:agents.Client
