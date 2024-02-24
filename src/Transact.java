import org.voltdb.VoltCompoundProcedure;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.types.TimestampType;

public class Transact extends VoltCompoundProcedure {
	
	private int id, from_account, to_account, amount;
	private TimestampType ts;
	
	public VoltTable[] run(int id, int from_account, int to_account, int amount, TimestampType ts) {
		this.id = id;
		this.from_account = from_account;
		this.to_account = to_account;
		this.amount = amount;
		this.ts = ts;
		
		this.newStageList(this::insertCredit)
		.then(this::insertDebit)
		.then(this::finish)
		.build();
		
		return null;
	}
	
	private void insertCredit(ClientResponse[] resp) {
		queueProcedureCall("InsertCredit", to_account, amount);
	}
	
	private void insertDebit(ClientResponse[] resp) {
		queueProcedureCall("InsertDebit", from_account, amount);
		
	}
	
	private void finish(ClientResponse[] resp) {
		this.completeProcedure(0);
	}
}
