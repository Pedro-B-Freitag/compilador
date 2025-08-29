mvn -q clean package
mvn -q exec:java
java -jar target/CompiladorInterface-1.0-SNAPSHOT.jar
