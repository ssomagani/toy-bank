import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class WriteTotal extends VoltProcedure {

	private static final SQLStmt GET_TOTAL = new SQLStmt("select sum(balance) from (select credits.account as account, credits-debits as balance from credits, debits where credits.account = debits.account) a");
	private static final SQLStmt PUSH_TOTAL_STREAM = new SQLStmt("insert into totals values (?)"); 
	
	public VoltTable[] run() {
		voltQueueSQL(GET_TOTAL);
		VoltTable result = voltExecuteSQL()[0];
		if(result.advanceRow()) {
			long total = result.getLong(0);
			voltQueueSQL(PUSH_TOTAL_STREAM, total);
			return voltExecuteSQL(true);
		}
		return null;
	}
}
