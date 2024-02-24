import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.voltdb.types.TimestampType;

public class App {

	public static void main (String[] args) throws NoConnectionsException, IOException, ProcCallException {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int max_id = 1000000;
		ArrayList<Row> transactions = new ArrayList<>();
		
		IntStream.range(0, max_id).forEach(
				(id) -> {
					int second = ((60*id)/max_id);
					int delay = random.nextInt(10);
					Row row = new Row(second+delay, id, random.nextInt(10), 
							random.nextInt(10), 1, new TimestampType("2021-01-01 00:00:"+second));
					transactions.add(row);
				}
				);
		Row[] transArray = transactions.toArray(new Row[0]);
		Arrays.sort(transArray);
		
		Client client = ClientFactory.createClient();
		client.createConnection("localhost");

		for(Row row : transArray) {
			client.callProcedure("Transact", row.id, row.from_account, row.to_account, row.amount, row.ts);
		}
	}
}

class Row implements Comparable<Row> {
	public int ingestTime, id, from_account, to_account, amount;
	public TimestampType ts;
	
	public Row(int ingestTime, int id, int from_account, int to_account, int amount, TimestampType ts) {
		this.ingestTime = ingestTime;
		this.id = id;
		this.from_account = from_account;
		this.to_account = to_account;
		this.amount = amount;
		this.ts = ts;
	}
	
	public int compareTo(Row o) {
		return this.ingestTime - o.ingestTime;
	}
	
	public String toString() {
		return this.from_account + " " + this.to_account + " " + this.amount;
	}
}
