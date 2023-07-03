--------------- Trabalho de Redes - Implementação do BitTorrent ------------------------

Executando o Tracker:

1. Baixar o .Zip
2. Descompactar na pasta desejada
3. Verificar se existe JAVA/JDK no computador em que foi baixado, caso contrário, baixar a JDK e ajustar a JAVA_HOME nas Variáveis do Sistema (adicionando o caminho da JDK baixada)
4. Recomendado usar Visual Code Studio, com Extension Pack for Java instalado.
5. Abrir o cmd, usar o comando "ipconfig" para descobrir o ip do Tracker.
6. Abrir a Classe Tracker e executar, clicando em Run. Deve abrir o terminal e aguardar os clientes se conectarem.


-----------------------------------------------------------------------------------------
Executando o Clinte:

1. Baixar o .Zip
2. Descompactar na pasta desejada
3. Verificar se existe JAVA/JDK no computador em que foi baixado, caso contrário, baixar a JDK e ajustar a JAVA_HOME nas Variáveis do Sistema (adicionando o caminho da JDK baixada)
4. Recomendado usar Visual Code Studio, com Extension Pack for Java instalado.
5. Abrir o cmd, usar o comando "ipconfig" para descobrir o ip do Cliente.
6. Adicionar o ip e porta do Tracker na função sendFileListToTrackerPeriodically na Classe Peer.
7. Adicionar os arquivos que cada Cliente tem com this.peer.addFile("Caminho"); na Classe Client. No caminho, selecione um dos arquivos da pasta peer_to_peer_Files e clique com o botão direito, copie o caminho e cole entre Áspas na função mostrada.
8. Adicionar o ip e porta do Cliente na main da Classe Client.
9. Abrir a Classe Cliente e executar, clicando em Run. Deve abrir o terminal.