all: FibonacciMaxHeap.class Node.class Traverse.class hashtagcounter.class
FibonacciMaxHeap.class: FibonacciMaxHeap.java
	javac -d . -classpath . FibonacciMaxHeap.java
Node.class: Node.java
	javac -d . -classpath . Node.java
Traverse.class: Traverse.java
	javac -d . -classpath . Traverse.java
hashtagcounter.class: hashtagcounter.java
	javac -d . -classpath . hashtagcounter.java
clean:
	rm -f *.class
