set classpath=%CLASSPATH%;.\luckyclient;
@echo ָ������ִ��
@echo ��Ŀ���� ����ID
java -Djava.ext.dirs=./lib;.%2 luckyclient.caserun.RunAutomationTest %1
exit