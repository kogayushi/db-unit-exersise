/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_utils;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.AbstractDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DbUnitTestHelper extends AbstractDatabaseTester implements TestRule {

    private static final int DEFAULT_PORT = 8082;
    private final Class executingClass;
    private final String connectionUrl;
    private final String username;
    private final String password;
    private final int port;

    public DbUnitTestHelper(Class executingClass) {
        this(executingClass, DEFAULT_PORT);
    }

    public DbUnitTestHelper(Class executingClass, int port) {
        this(executingClass, port, null);
    }

    public DbUnitTestHelper(Class executingClass, int port, String dbMode) {
        this.executingClass = executingClass;
        this.port = port;
        DbMode mode = DbMode.modeOf(dbMode);
        if(mode == DbMode.NONE) {
            this.connectionUrl = "jdbc:h2:tcp://localhost:" + this.port + "/db;DB_CLOSE_DELAY=5;DB_CLOSE_ON_EXIT=true;";
        } else {
            this.connectionUrl = "jdbc:h2:tcp://localhost:" + this.port + "/db;DB_CLOSE_DELAY=5;DB_CLOSE_ON_EXIT=true;MODE=" + mode.getMode();
        }
        this.username = "sa";
        this.password = "as";
    }
    
    public void executeQuery(String sql) throws Exception {
        Connection conn = getConnection().getConnection();
        conn.createStatement().execute(sql);
        conn.commit();
        conn.close();
    }
    
    @Override
    public IDatabaseConnection getConnection() throws Exception {
        Connection conn = null;
        if (username == null && password == null) {
            conn = DriverManager.getConnection(connectionUrl);
        } else {
            conn = DriverManager.getConnection(connectionUrl, username, password);
        }
        DatabaseConnection dbConnection = new DatabaseConnection(conn);
        DatabaseConfig config = dbConnection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new MySqlDataTypeFactory());

        return dbConnection;
    }

    protected void before() throws Exception {
        IDataSet preparedDataSet = createDataSet();
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), preparedDataSet);
    }

    protected IDataSet createDataSet() throws Exception {
        URL url = this.getClass().getResource("/" + this.executingClass.getCanonicalName().replaceAll("\\.", "/") + "/given");
        File file = new File(url.toURI());
        CsvDataSet preparedDataSet = new CsvDataSet(file);
        return preparedDataSet;

    }

    public ITable getExpectedTable(String table) throws Exception {
        URL url = this.executingClass.getResource("/" + this.executingClass.getCanonicalName().replaceAll("\\.", "/") + "/expected");
        File file = new File(url.toURI());
        CsvDataSet expected = new CsvDataSet(file);
        return expected.getTable(table);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                before();
                setDataSet(createDataSet());
                onSetup();
                try {
                    base.evaluate();
                } finally {
                    onTearDown();
                }
            }
        };
    }
}