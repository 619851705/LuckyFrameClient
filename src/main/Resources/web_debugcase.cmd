set classpath=%CLASSPATH%;.\luckyclient;
@echo Web������������ӿ�
@echo ����˵�� ����Ϊ��������� ִ����
java -Djava.ext.dirs=./lib luckyclient.caserun.WebDebugExecute %1 %2
exit
