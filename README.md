# README

日志输出路path
```text
        
<classroom-service>
		<File name="FileInfo" fileName="e:/dd-yinyue-log/info/classroom.log" append="false"/>
		<File name="FileDebug" fileName="e:/dd-yinyue-log/debug/classroom.log" append="false"/>
		<RollingFile name="RollingFile" fileName="e:/dd-yinyue-log/logs/classroom.log"/>
<classroom-service/>
        
<exercise-service>暂无log4j.xml</exercise-service>
<resource-server>暂无log4j.xml</resource-server>
<test-service>暂无log4j.xml</test-service>

```

调整为:

\data0\app\logs\dd-yinyue\${project-name}