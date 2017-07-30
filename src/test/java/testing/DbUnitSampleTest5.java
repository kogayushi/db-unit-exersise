package testing;

import static org.hamcrest.MatcherAssert.assertThat;
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
public class DbUnitSampleTest5 {

    public static class Nested {
        public static final int port = H2DatabaseServer.getRandomPort();
//      public static final int port = 8081; 

        @ClassRule
        public static H2DatabaseServer server = new H2DatabaseServer(DbUnitSampleTest5.class, port);

        @Rule
        public DbUnitTestHelper helper = new DbUnitTestHelper(this.getClass(), port);

        @Test
        public void test() throws Exception {

            // 実際のデータ変更などは、自作DAOなどでやっている想定
            QueryDataSet actual = new QueryDataSet(helper.getConnection());
            actual.addTable("db_unit_sample");

            // 期待値データをDbUnitで取得する
            ITable expectedTable = helper.getExpectedTable("test01", "db_unit_sample");

            // 期待値データと同じカラムのみ比較対象データとする
            ITable actualTable = DefaultColumnFilter.includedColumnsTable(actual.getTable("db_unit_sample"), expectedTable.getTableMetaData().getColumns());

            // 比較する
            assertThat(actualTable, tableOf(expectedTable));
        }
    }

}
