set classpath=%CLASSPATH%;.\luckyclient;
@echo ����ִ������
@echo ����˵�� ����Ϊ����Ŀ���� tastId ������ 
java -Djava.ext.dirs=./lib luckyclient.caserun.BatchCaseExecute %1 %2
exit
