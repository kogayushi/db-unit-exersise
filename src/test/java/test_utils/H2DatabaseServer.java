package test_utils;

import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Properties;

import org.h2.tools.Server;
import org.h2.util.JdbcUtils;
import org.junit.rules.ExternalResource;

public class H2DatabaseServer extends ExternalResource {

    private static final String baseDir = "./h2";
    private static final String dbName = "db";
    private static final String username = "sa";
    private static final String password = "as";
    private static final int DEFAULT_PORT = 8082;
    private static final int privatePortFirst = 49152;
    private static final int privatePortLast = 65535;

    private final int port ;
    private final DbMode dbMode;
    private final Class executingClass;
    
    private Server server = null;

    public H2DatabaseServer() {
        this(null, DEFAULT_PORT);
    }

    public H2DatabaseServer(Class executingClass) {
        this(executingClass, DEFAULT_PORT);
    }

    public H2DatabaseServer(Class executingClass, int port) {
        this(executingClass, port, null);
    }

    public H2DatabaseServer(Class executingClass, int port, String dbMode) {
        this.executingClass = executingClass;
        this.port = port;
        this.dbMode = DbMode.modeOf(dbMode);
    }

    /**
     * generate random port between 49152 and 65535.
     * @return
     */
    public static int getRandomPort() {
        return new Random().nextInt(privatePortLast - privatePortFirst) + privatePortFirst;
    }

    @Override
    protected void before() throws Throwable {
        // DBサーバの起動
        server = Server.createTcpServer("-baseDir", baseDir + (this.executingClass != null ? "/" + this.executingClass.getCanonicalName().replaceAll("\\.", "/") : ""), "-tcpPort", String.valueOf(port));
        server.start();
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        String url = "jdbc:h2:" + server.getURL() + "/" + dbName + (DbMode.NONE != dbMode ? "MODE=" + dbMode.getMode() + ";" : "");
        Connection conn = org.h2.Driver.load().connect(url, props);
        File file = new File(this.getClass().getResource("/db/migration/initialize.sql").toURI());
        
        try(BufferedReader br = new BufferedReader(new FileReader(file));) {
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
