# Volt implementation of Jamie Brandon's Toy Bank example 
(https://www.scattered-thoughts.net/writing/internal-consistency-in-streaming-systems/) 
This example simply processes a stream of transactions at a bank where money is transferred from one acccount id to another. Everyone starts with $0 and accounts can go negative so the sum total value of money in the bank will always be 0.
The stream processing system is expected to split up the transaction events into credits and debits events that are then processed by the respective "operators". 
A stream of values is emitted by the stream processing system that simply states the sum total value in the bank as it sees. Ideally this value should always be equal to 0.

## Version 1 - Vanilla Volt v13.0
This version uses Compound Procedures to create intermediate streams of credits and debits.
You should be able to observe that Volt will emit wrong answers (value=1) sometimes because there is no mechanism for maintaining consistency over chained procedure invocations. 
The values will quickly converge to a 0 (the expected value) 
The wrong values are limited to just {1} because there is a single queue for Compound Procedures and the most that the total can be offset by is 1 since that's the amount transferred in each transaction. (although i'm not sure why i never saw -1)

### Running Instructions
Execute the App.java client and tail the TOTALS_FILE export file to observe the emitted results.
