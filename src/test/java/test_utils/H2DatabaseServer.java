package test_utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Properties;

import org.h2.tools.Server;
import org.h2.util.JdbcUtils;
import org.junit.rules.ExternalResource;

public class H2DatabaseServer extends ExternalResource {

    private final String baseDir;
    private final String dbName;
    private final String schemaName;
    private final String username;
    private final String password;
    private Server server = null;

    public H2DatabaseServer() {
        this.baseDir = "./h2";
        this.dbName = "db";
        this.schemaName = "ut";
        this.username = "sa";
        this.password = "as";
    }

    @Override
    protected void before() throws Throwable {
        // DBサーバの起動
        server = Server.createTcpServer("-baseDir", baseDir);
        server.start();
        // スキーマの設定
        Properties props = new Properties();
        props.setProperty("user", this.username);
        props.setProperty("password", this.password);
        String url = "jdbc:h2:" + server.getURL() + "/" + dbName;
//        String url = "jdbc:h2:/" + baseDir + "/"+ dbName;
        Connection conn = org.h2.Driver.load().connect(url, props);
        File file = new File(this.getClass().getResource("/db/migration/initialize.sql").toURI());
        
        try(BufferedReader br = new BufferedReader(new FileReader(file));) {
            conn.createStatement().execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
            String line;
            StringBuilder statement = new StringBuilder();
            while ((line = br.readLine()) != null) {
                statement.append(line + " ");
                if (line.endsWith(";")) {
                    conn.createStatement().execute(statement.toString());
                    statement.delete(0, statement.length());
                }
            }
        } finally {
            JdbcUtils.closeSilently(conn);
        }
    }

    @Override
    protected void after() {
        // DBサーバの停止
        server.shutdown();
    }
}
