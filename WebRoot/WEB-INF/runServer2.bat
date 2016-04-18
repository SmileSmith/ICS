cd data
@java -classpath ../lib/hsqldb.jar org.hsqldb.Server  -port 9001 -database.0 file:../data/mydb -dbname.0 mydb
