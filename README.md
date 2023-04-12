# Five-Stage-Pipelined-Processing-Simulator
This is a simulator for five-stage pipelined processing implemented in Java. 
The simulator supports the following instructions: ADD, SUB, MUL, DIV, LOAD, and STORE with their standard meanings.
ADD, SUB, LOAD, and STORE require one cycle to execute (EX stage), while MUL and DIV require two cycles to execute (EX stage). 
LOAD and STORE require three cycles to access memory. The IF, ID, and WB stages always take one cycle to complete. 
Each instruction has a maximum of three register operands (a destination register and one or two source registers).
As input, a sequence of instructions is provided. The output is presented in a table that shows, for each cycle (column), the stages (or stalls) in which the input instructions (rows) are located. 
The simulator allows for specifying whether data forwarding between pipeline stages will be performed or not. 
It also allows for printing cases where data forwarding occurs, with all relevant information (instruction serial numbers and their execution stages during forwarding). 
Additionally, the simulator implements detection of hazards (RAW dependencies) between instructions in the input sequence.

## Example Output


| Cycle | IF | ID | EX | MEM | WB |
|-------------|----|----|----|-----|----|
| 1 | ADD1 |   |   |   |   |
| 2 | SUB2 | ADD1 |   |   |   |
| 3 | MUL3 | SUB2 | ADD1  |   |   |
| 4 |    | MUL3 | SUB2 | ADD1 |   |
| 5 |   |    | MUL3 | SUB2 |   |
| 6 |   |   |   | MUL3 | SUB2 |
| 7 |   |   |   |   | MUL3 |


This output shows the instructions ADD1, SUB2, and MUL3 being executed in the pipeline over 7 cycles.

