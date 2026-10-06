# CLAUDE.md — pet-api 项目约定

Spring Boot 示例服务，监听 **8080**。

## ⚠️ 端口清理：禁止 `lsof -ti:8080 | xargs kill -9`

`lsof -i:8080` 匹配的是**本地端口或远端端口为 8080 的所有 socket**，不只是"正在监听 8080 的进程"。
本机上微信 / 企业微信会长期持有 8080 相关连接，结果被这条命令连续误杀 6 次
（2026-10-05 20:38、22:45，2026-10-06 15:11、15:42、16:23、17:09；系统日志证据：
`exited due to SIGKILL | sent by kill[...]`，与命令执行时间毫秒级对齐）。

### ✅ 正确写法（只杀监听 8080 的进程）

```bash
lsof -ti tcp:8080 -sTCP:LISTEN | xargs kill -9
```

### ✅ 更保险（只杀监听 8080 的 java 进程）

```bash
lsof -ti tcp:8080 -sTCP:LISTEN -a -c java | xargs kill -9
```

### ❌ 禁止写法

```bash
lsof -ti:8080 | xargs kill -9        # 会误杀其它持有 8080 socket 的 App
kill -9 $(lsof -ti:8080)             # 同上
```

### 只想看端口被谁占用（只读，不杀）

```bash
lsof -nP -iTCP:8080 -sTCP:LISTEN
```

## 本地运行

- 启动：`mvn spring-boot:run`（监听 8080）
- 重启流程：先用上面的**正确写法**释放端口，再启动
- 自测：`curl http://localhost:8080/pets`

## 通用原则

任何"清理端口 / 杀占用进程"的命令，都必须先确认**匹配范围**（是否限定 `-sTCP:LISTEN`、
是否限定进程名），不要在整机上执行无差别 `kill -9`。
