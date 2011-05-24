BASEDIR=`dirname $0`/..
CLASSPATH=
for l in `find -name \*.jar -print`; do
   CLASSPATH=$CLASSPATH:$l
done

export CLASSPATH

exec java -Xmx256m \
     -server \
     de.inselhome.tvrecorder.server.tvguide.rss.RssTvShowManager \
     2>&1 > /dev/null
