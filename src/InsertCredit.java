import org.voltdb.SQLStmt;
import org.voltdb.VoltProcedure;
import org.voltdb.VoltTable;

public class InsertCredit extends VoltProcedure {

	private final SQLStmt UPDATE = new SQLStmt("update credits set credits = credits + ? where account = ?");

	public VoltTable[] run(int to_account, int amount) {
		voltQueueSQL(UPDATE, amount, to_account);
		return voltExecuteSQL();
	}
}
