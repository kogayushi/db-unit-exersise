package testing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static test_utils.ITableMatcher.tableOf;

import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import test_utils.DbUnitTestHelper;
import test_utils.H2DatabaseServer;


@RunWith(Enclosed.class)
public class DbUnitSample {

    public static class Nested {

        @ClassRule
        public static H2DatabaseServer server = new H2DatabaseServer();

        @Rule
        public DbUnitTestHelper helper = new DbUnitTestHelper(this.getClass());

        @Test
        public void test() throws Exception {

            QueryDataSet actual = new QueryDataSet(helper.getConnection());
            actual.addTable("db_unit_sample");

            ITable expectedTable = helper.getExpectedTable("db_unit_sample");
            ITable actualTable = DefaultColumnFilter.includedColumnsTable(actual.getTable("db_unit_sample"),expectedTable.getTableMetaData().getColumns());
            
            assertThat(actualTable, is(tableOf(expectedTable)));
        }
    }

}
