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
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DbUnitTestHelper extends AbstractDatabaseTester implements TestRule {

    private final String connectionUrl;
    private final String username;
    private final String password;
    private final String executingClass;

    public DbUnitTestHelper(String connectionUrl, DbMode dbMode) {
        if(connectionUrl == null) throw new IllegalArgumentException("connectionUrl is required");
        if(dbMode == null) throw new IllegalArgumentException("dbMode is required");
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        this.executingClass = ste.getClassName().replaceAll("[$.]", "/");
        this.connectionUrl = connectionUrl + (dbMode == DbMode.NONE ? "" : ";MODE=" + dbMode.getMode());
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
        try {
            // JDBCドライバのロード
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
        if (username == null && password == null) {
            conn = DriverManager.getConnection(connectionUrl);
        } else {
            conn = DriverManager.getConnection(connectionUrl, username, password);
        }
        DatabaseConnection dbConnection = new DatabaseConnection(conn);
        DatabaseConfig config = dbConnection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());

        return dbConnection;
    }

    protected void before() throws Exception {
        IDataSet preparedDataSet = createDataSet();
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), preparedDataSet);
    }

    protected IDataSet createDataSet() throws Exception {
        URL url = this.getClass().getResource("/" + this.executingClass + "/given");
        File file = new File(url.toURI());
        CsvDataSet preparedDataSet = new CsvDataSet(file);
        return preparedDataSet;

    }
    
    public ITable getExpectedTable(String table) throws Exception {
        return getExpectedTable(null, table);
    }

    public ITable getExpectedTable(String scenario, String table) throws Exception {
        URL url = this.getClass().getResource("/" + this.executingClass + "/expected" + (scenario != null && scenario.length() > 0 ? "/" + scenario : "" ));
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