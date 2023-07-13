#!/bin/sh

mvn clean; ./build.sh;
cd target;
java -cp .:../jars/*: benchmark.BCBenchmark 2 10 78 -q ./contract/Write.class ./counter/Counter.class ../

java -cp .:../jars/*: benchmark.BCBenchmark 2 10 78 -s ./contract/Write.class ./counter/Counter.class ../
java -cp .:../jars/*: benchmark.BCBenchmark 2 10 78 -q ./contract/Write.class ./counter/Counter.class ../

cd ../; mvn clean; ./build.sh; cd target
java -cp .:../jars/*: benchmark.BCBenchmark 2 10 78 -b ./contract/Write.class ./counter/Counter.class ../
java -cp .:../jars/*: benchmark.BCBenchmark 2 10 78 -q ./contract/Write.class ./counter/Counter.class ../
