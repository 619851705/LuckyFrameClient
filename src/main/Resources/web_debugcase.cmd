set classpath=%CLASSPATH%;.\luckyclient;
@echo Web������������ӿ�
@echo ����˵�� ����Ϊ��������� ִ����
java -Djava.ext.dirs=./lib;.%3 luckyclient.caserun.WebDebugExecute %1 %2
exit
