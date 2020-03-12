#!/bin/bash -x 

echo "----------danner001 process------------"
ssh danner001 "$JAVA_HOME/bin/jps"
echo "                                                "


echo "----------danner002 process------------"
ssh danner002 "$JAVA_HOME/bin/jps"
echo "                                                "


echo "----------danner003 process------------"
ssh danner003 "$JAVA_HOME/bin/jps"
echo "                                                "

