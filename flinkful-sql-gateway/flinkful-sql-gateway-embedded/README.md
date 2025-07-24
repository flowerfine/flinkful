# Sql Gateway



## TODO

- [ ] 封装 session。现在是直接暴露出去了，后续需封装起来，前端应该是无感的。后端把 session 和用户的 cookie/session 关联起来，这样每个用户就在自己的环境中进行操作，互不干扰。如果要达成互不干扰，相关场景也应该是 ad-hoc 查询，如果是编辑相关的 sql 任务，还是要隔离的，业务需求不同
- [ ] 封装 sql gateway。支持多种 flink sql gateway 和 kyuubi

## 参考链接

* [Sql Gateway](https://nightlies.apache.org/flink/flink-docs-release-2.0/docs/dev/table/sql-gateway/overview/)
* [kyuubi-flink-sql-engine](https://github.com/apache/kyuubi/blob/master/externals/kyuubi-flink-sql-engine/pom.xml)。除了 flink 官方的 flink sql gateway 实现，还有 kyuubi 实现的 flink sql gateway。
* amoro
  * [TerminalManager](https://github.com/apache/amoro/blob/master/amoro-ams/src/main/java/org/apache/amoro/server/terminal/TerminalManager.java)
  * [TerminalController](https://github.com/apache/amoro/blob/master/amoro-ams/src/main/java/org/apache/amoro/server/dashboard/controller/TerminalController.java)
  * [DashboardServer](https://github.com/apache/amoro/blob/master/amoro-ams/src/main/java/org/apache/amoro/server/dashboard/DashboardServer.java)
* [T3 出行 Apache Kyuubi Flink SQL Engine 设计和相关实践](https://www.cnblogs.com/163yun/articles/16082256.html)