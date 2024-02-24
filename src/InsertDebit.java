import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class InsertDebit extends VoltProcedure {

	private final SQLStmt UPDATE = new SQLStmt("update debits set debits = debits + ? where account = ?");

	public VoltTable[] run(int from_account, int amount) {
		voltQueueSQL(UPDATE, amount, from_account);
		return voltExecuteSQL();
	}
}
