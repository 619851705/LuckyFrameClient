set classpath=%CLASSPATH%;.\luckyclient;
@echo ����ִ������
@echo ����˵�� ����Ϊ����Ŀ���� tastId ������ 
java -Djava.ext.dirs=./lib;.%3 luckyclient.caserun.BatchCaseExecute %1 %2
exit
