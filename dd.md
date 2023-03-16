

\------------------------

LATEST DETECTED DEADLOCK

\------------------------

2023-03-14 15:46:59 281472817692608

*** (1) TRANSACTION:

TRANSACTION 662406, ACTIVE 0 sec starting index read

mysql tables in use 1, locked 1

LOCK WAIT 7 lock struct(s), heap size 1128, 3 row lock(s), undo log entries 1

MySQL thread id 386, OS thread handle 281472227262400, query id 24012 172.17.0.1 root updating

update book_groups set version=1 where id=1 and version=0



*** (1) HOLDS THE LOCK(S):

RECORD LOCKS space id 30548 page no 4 n bits 72 index PRIMARY of table `dadok_test`.`book_groups` trx id 662406 lock mode S locks rec but not gap

Record lock, heap no 2 PHYSICAL RECORD: n_fields 17; compact format; info bits 0

.....



*** (1) WAITING FOR THIS LOCK TO BE GRANTED:

RECORD LOCKS space id 30548 page no 4 n bits 72 index PRIMARY of table `dadok_test`.`book_groups` trx id 662406 lock_mode X locks rec but not gap waiting

Record lock, heap no 2 PHYSICAL RECORD: n_fields 17; compact format; info bits 0

.....



*** (2) TRANSACTION:

TRANSACTION 662414, ACTIVE 0 sec starting index read

mysql tables in use 1, locked 1

LOCK WAIT 7 lock struct(s), heap size 1128, 3 row lock(s), undo log entries 1

MySQL thread id 390, OS thread handle 281472764166080, query id 24017 172.17.0.1 root updating

update book_groups set version=1 where id=1 and version=0



*** (2) HOLDS THE LOCK(S):

RECORD LOCKS space id 30548 page no 4 n bits 72 index PRIMARY of table `dadok_test`.`book_groups` trx id 662414 lock mode S locks rec but not gap

Record lock, heap no 2 PHYSICAL RECORD: n_fields 17; compact format; info bits 0

....



*** (2) WAITING FOR THIS LOCK TO BE GRANTED:

RECORD LOCKS space id 30548 page no 4 n bits 72 index PRIMARY of table `dadok_test`.`book_groups` trx id 662414 lock_mode X locks rec but not gap waiting

Record lock, heap no 2 PHYSICAL RECORD: n_fields 17; compact format; info bits 0

.....



*** WE ROLL BACK TRANSACTION (2)

\------------

TRANSACTIONS

\------------

Trx id counter 662670

Purge done for trx's n:o < 662669 undo n:o < 0 state: running but idle

History list length 80

LIST OF TRANSACTIONS FOR EACH SESSION:

---TRANSACTION 562948277657600, not started

0 lock struct(s), heap size 1128, 0 row lock(s)

---TRANSACTION 562948277659216, not started

0 lock struct(s), heap size 1128, 0 row lock(s)

---TRANSACTION 562948277658408, not started

0 lock struct(s), heap size 1128, 0 row lock(s)

---TRANSACTION 562948277656792, not started

0 lock struct(s), heap size 1128, 0 row lock(s)

---TRANSACTION 562948277655984, not started

0 lock struct(s), heap size 1128, 0 row lock(s)

---TRANSACTION 562948277655176, not started

0 lock struct(s), heap size 1128, 0 row lock(s)

\--------

FILE I/O

\--------

I/O thread 0 state: waiting for completed aio requests (insert buffer thread)

I/O thread 1 state: waiting for completed aio requests (log thread)

I/O thread 2 state: waiting for completed aio requests (read thread)

I/O thread 3 state: waiting for completed aio requests (read thread)

I/O thread 4 state: waiting for completed aio requests (read thread)

I/O thread 5 state: waiting for completed aio requests (read thread)

I/O thread 6 state: waiting for completed aio requests (write thread)

I/O thread 7 state: waiting for completed aio requests (write thread)

I/O thread 8 state: waiting for completed aio requests (write thread)

I/O thread 9 state: waiting for completed aio requests (write thread)

Pending normal aio reads: [0, 0, 0, 0] , aio writes: [0, 0, 0, 0] ,

 ibuf aio reads:, log i/o's:

Pending flushes (fsync) log: 0; buffer pool: 0

1820 OS file reads, 193061 OS file writes, 109987 OS fsyncs

0.00 reads/s, 0 avg bytes/read, 0.00 writes/s, 0.00 fsyncs/s

\-------------------------------------

INSERT BUFFER AND ADAPTIVE HASH INDEX

\-------------------------------------

Ibuf: size 1, free list len 0, seg size 2, 0 merges

merged operations:

 insert 0, delete mark 0, delete 0

discarded operations:

 insert 0, delete mark 0, delete 0

Hash table size 34679, node heap has 2 buffer(s)

Hash table size 34679, node heap has 1 buffer(s)

Hash table size 34679, node heap has 3 buffer(s)

Hash table size 34679, node heap has 0 buffer(s)

Hash table size 34679, node heap has 4 buffer(s)

Hash table size 34679, node heap has 1 buffer(s)

Hash table size 34679, node heap has 1 buffer(s)

Hash table size 34679, node heap has 4 buffer(s)

0.00 hash searches/s, 0.00 non-hash searches/s

\---

LOG

\---

Log sequence number     1797521042

Log buffer assigned up to  1797521042

Log buffer completed up to  1797521042

Log written up to      1797521042

Log flushed up to      1797521042

Added dirty pages up to   1797521042

Pages flushed up to     1797521042

Last checkpoint at      1797521042

Log minimum file id is    529

Log maximum file id is    548

122502 log i/o's done, 0.00 log i/o's/second

\----------------------

BUFFER POOL AND MEMORY

\----------------------

Total large memory allocated 0

Dictionary memory allocated 723196

Buffer pool size  8192

Free buffers    2999

Database pages   5177

Old database pages 1917

Modified db pages 0

Pending reads   0

Pending writes: LRU 0, flush list 0, single page 0

Pages made young 13494, not young 64565

0.00 youngs/s, 0.00 non-youngs/s

Pages read 1204, created 17458, written 46503

0.00 reads/s, 0.00 creates/s, 0.00 writes/s

No buffer pool page gets since the last printout

Pages read ahead 0.00/s, evicted without access 0.00/s, Random read ahead 0.00/s

LRU len: 5177, unzip_LRU len: 0

I/O sum[0]:cur[0], unzip sum[0]:cur[0]

\--------------

ROW OPERATIONS

\--------------

0 queries inside InnoDB, 0 queries in queue

0 read views open inside InnoDB

Process ID=1, Main thread ID=281472792514496 , state=sleeping

Number of rows inserted 43225, updated 82, deleted 0, read 42268

0.00 inserts/s, 0.00 updates/s, 0.00 deletes/s, 0.00 reads/s

Number of system rows inserted 94857, updated 224099, deleted 89708, read 1185742

0.00 inserts/s, 0.00 updates/s, 0.00 deletes/s, 0.00 reads/s

\----------------------------

END OF INNODB MONITOR OUTPUT

============================