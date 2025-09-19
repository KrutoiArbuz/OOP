javac src/main/java/ru/nsu/masolygin/*.java -d ./build
javadoc -d build/docs/javadoc -sourcepath src/main/java -subpackages ru.nsu.masolygin
java -cp ./build ru.nsu.masolygin.Blackjack
