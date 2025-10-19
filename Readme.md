## Gerar o `.jar` automaticamente

O projeto possui um script chamado **`build-jar.bat`** na raiz que automatiza a criação do arquivo `.jar`.

### Como usar

1. Dê **duplo clique** no arquivo `build-jar.bat`  
   ou execute no terminal:

   ```bash
   build-jar.bat

O script irá:

- Executar `mvn clean package`

---

### Resultado

Após a execução, o arquivo **`CompiladorInterface-1.0-SNAPSHOT.jar`** será criado na **raiz do projeto**.


Você pode executá-lo com:

```bash
java -jar CompiladorInterface-1.0-SNAPSHOT.jar
