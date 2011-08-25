BASEDIR=`dirname $0`/..
CLASSPATH=
for l in `find -name \*.jar -print`; do
   CLASSPATH=$CLASSPATH:$l
done

export CLASSPATH

exec java -Xmx256m \
     -server \
     -Dtvrecorder.config=conf/myconfig.xml \
     de.inselhome.tvrecorder.server.App \
     2>&1 > /dev/null
