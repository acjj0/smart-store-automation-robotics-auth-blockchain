package com.cscie97.ledger;
import java.util.*;

/**
 * The Ledger manages the Blocks of the blockchain. It also provides the API used by clients of
 * the Ledger. The Ledger processes transaction processing requests, and also queries about the
 * state of the Ledger, including Account balances, Transaction details, and Block details.
 */
public class Ledger
{
    /**
     * Name of the ledger.
     */
    private String name;

    /**
     * Ledger description.
     */
    private String description;

    /**
     * The Seed that is used as input to the hashing algorithm.
     */
    private String seed;

    /**
     * Association - The initial Block of the blockchain.
     */
    private Block genesisBlock;

    /**
     * Association - A map of block numbers and the associated Blocks.
     */
    private TreeMap<Integer, Block> blockMap;

    /**
     * Track the current block to make rest of the code efficient
     */
    private Block currentBlock;

    /**
     * Maintain a map of accounts to quickly get to one based on Ids
     */
    private Map<String, Account> accountMap;


    /**
     * Constructor for Ledger that initializes name, description and seed based on parameters passed in.
     * Initialize genesisBlock and blockMap. Create masterAccount with total amount of currency available in Ledger.
     * Track accounts in a map. Track the current block, initialized with the genesisBlock.
     * @param name  Name of the ledger
     * @param description   Description of the ledger
     * @param seed  Seed for the ledger
     */
    public Ledger (String name, String description, String seed)
    {
        this.name = name;
        this.description = description;
        this.seed = seed;

        this.genesisBlock = new Block();

        this.blockMap = new TreeMap<>();

        Account masterAccount = new Account("master");
        masterAccount.setBalance(Integer.MAX_VALUE);

        this.accountMap = new HashMap<>();
        this.accountMap.put(masterAccount.getAddress(), masterAccount);

        this.currentBlock = this.genesisBlock;
    }

    /**
     * Getter for name of this ledger
     * @return String   Name of the ledger
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Getter for description of the ledger
     * @return String   Readable description of this ledger object
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Getter of the seed of this ledger object
     * @return String   Seed of this ledger object
     */
    public String getSeed()
    {
        return this.seed;
    }

    /**
     * Create a new account, assign a unique identifier, and set the balance to 0. Return the account identifier.
     * @param accountId String based unique identifier for a new Account. No other account must already have this ID.
     * @return String The accountId now associated with the new account
     * @throws LedgerException if an account with the same accountId already exists
     */
    public String createAccount (String accountId) throws LedgerException
    {
        if (this.accountMap.get(accountId) != null)
        {
            throw new LedgerException("createAccount", "Account Id already exists");
        }

        Account newAccount = new Account(accountId);
        newAccount.setBalance(0);

        this.accountMap.put(newAccount.getAddress(), newAccount);

        return newAccount.getAddress();
    }

    /**
     * Process a transaction. Validate the Transaction and if valid, add the Transaction to the current Block
     * and update the associated Account balances for the current Block. Return the assigned transaction id.
     * If the transaction is not valid, throw a LedgerException.
     * @param transaction   A complete Transaction object
     * @return String   The Id of the newly added transaction
     * @throws LedgerException When fee is less than 10
     * @throws LedgerException When transaction exceeds currency
     * @throws LedgerException When payer does not have enough balance
     */
    private String processTransaction(Transaction transaction) throws LedgerException
    {
        Account payer = transaction.getPayer();
        Account receiver = transaction.getReceiver();

        Account master = this.accountMap.get("master");

        // Insufficient fee
        if (transaction.getFee() < 10)
        {
            throw new LedgerException("processTransaction", "Transaction fee less than minimum.");
        }

        // Exceeds currency.
        if (transaction.getAmount() > Integer.MAX_VALUE)
        {
            throw new LedgerException("processTransaction", "Transaction amount higher than currency");
        }

        // Insufficient funds with the payer
        if ((transaction.getAmount() + transaction.getFee()) > payer.getBalance())
        {
            throw new LedgerException("processTransaction", "Payer has insufficient balance for amount + fee");
        }

        this.currentBlock.addTransaction(transaction);

        // This set of transactions should really be ACID - atomicity, consistency, isolation, durability
        payer.setBalance(payer.getBalance() - (transaction.getAmount() + transaction.getFee()));
        receiver.setBalance(receiver.getBalance() + transaction.getAmount());
        master.setBalance(master.getBalance() + transaction.getFee());

        // When we hit 10 transactions, submit the block
        // NOTE: CHANGED THIS NUMBER TO 1 TO SIMULATE AND VALIDATE CORRECTNESS OF StoreControllerService functionality
        // In a real life blockchain which will have lots of transactions happening, the size of 10 transactions would
        // happen rapidly, but in this implementation waiting for 10 transactions affects ACID rules for transactions
        // and customer account balances show up with the wrong information.
        if (this.currentBlock.getTransactionList().size() == 1)
        {
            this.accountMap.forEach((k, v) -> this.currentBlock.addAccountBalance(v));
            this.currentBlock.getBlockHash(this.seed);
            this.blockMap.put(this.currentBlock.getBlockNumber(), currentBlock);
            this.currentBlock = new Block(this.currentBlock);
        }

        return transaction.getTransactionId();
    }

    /**
     * Return the account balance for the Account with a given address based on the most recent completed block.
     * If the Account does not exist, throw a LedgerException.
     * @param accountId String  accountId of the account for which we are getting balance
     * @return int  Balance available for accountId
     * @throws LedgerException  When no previous blocks and so there are no accounts
     * @throws LedgerException  When account id does not exist as a key in previous blocks accountBalanceMap
     */
    public int getAccountBalance(String accountId) throws LedgerException
    {
        Block previousBlock = this.blockMap.get(this.currentBlock.getBlockNumber() - 1);

        if (previousBlock == null)
        {
            throw new LedgerException("getAccountBalance", "Block not found");
        }

        if (previousBlock.getAccountBalanceMap().containsKey(accountId) == false)
        {
            throw new LedgerException("getAccountBalance", "Account not found");
        }

        return previousBlock.getAccountBalanceMap().get(accountId);
    }

    /**
     * Return the account balance map for the most recently completed block.
     * @return Map  A map of accounts and balances in the blockchain
     * @throws LedgerException  No committed block and genesis block is still the only block
     */
    public Map<String, Integer> getAccountBalances() throws LedgerException
    {
        int currentBlockNumber = this.currentBlock.getBlockNumber();

        if (currentBlockNumber == 1)
        {
            throw new LedgerException("getAccountBalances", "No accounts found");
        }

        return this.blockMap.get(currentBlockNumber - 1).getAccountBalanceMap();
    }

    /**
     * Return the Block for the given block number.
     * @param blockNumber int   Block number for the block requested
     * @return Block    The Block requested
     * @throws LedgerException  Exception object thrown
     */
    public Block getBlock(int blockNumber) throws LedgerException
    {
        if (blockNumber >= this.currentBlock.getBlockNumber())
        {
            throw new LedgerException("getBlock", "Block requested does not exist");
        }

        return this.blockMap.get(blockNumber);
    }

    /**
     * Return the Transaction for the given transaction id.
     * @param transactionId String  Transaction Id for the transaction requested
     * @return Transaction  The transaction requested
     * @throws LedgerException  Exception object
     */
    public Transaction getTransaction(String transactionId) throws LedgerException
    {
        final Transaction[] transaction = new Transaction[1];

        this.blockMap.forEach((k, v) ->
        {
            Transaction transactionListGetResult = v.getTransactionList().get(transactionId);

            if (v.getTransactionList().get(transactionId) != null)
            {
                transaction[0] = transactionListGetResult;
            }
        });

        if (transaction[0] == null)
        {
            throw new LedgerException("getTransaction", "Transaction does not exist.");
        }

        return transaction[0];
    }

    /**
     * Validate the current state of the blockchain. For each block, check the hash of the previous hash, make sure
     * that the account balances total to the max value. Verify that each completed block has exactly 10 transactions.
     * Start with the last block and count downward. For each block
     * @throws  LedgerException when mismatch of previousHash and previousBlock's hash
     * @throws  LedgerException when block contains more than 10 transactions
     * @throws  LedgerException when total of all blocks in accountBalance map does not equal Integer.MAX_VALUE
     */
    public void validate() throws LedgerException
    {
        int blockCounter = this.blockMap.size();

        while (blockCounter > 0)
        {
            Block block = this.blockMap.get(blockCounter);

            if (block.getBlockNumber() > 1)
            {
                Block previousBlock = this.blockMap.get(block.getBlockNumber() - 1);

                if (block.getPreviousHash() != previousBlock.getHash())
                {
                    throw new LedgerException("validate", "Mismatch of previousHash and previousBlock's hash");
                }
            }

            if (block.getTransactionList().size() > 10)
            {
                throw new LedgerException("validate", "Block contains more than 10 transactions");
            }

            int totalAccountBalance = 0;
            Iterator accountBalanceMapIterator = block.getAccountBalanceMap().entrySet().iterator();

            while (accountBalanceMapIterator.hasNext())
            {
                Map.Entry accountBalance = (Map.Entry) accountBalanceMapIterator.next();
                totalAccountBalance = totalAccountBalance + Integer.parseInt(accountBalance.getValue().toString());
            }

            if (totalAccountBalance != Integer.MAX_VALUE)
            {
                throw new LedgerException("validate", "Total balance of all accounts exceeds currency");
            }

            blockCounter--;
        }
    }

    /**
     * Method to be called from CommandProcessor to create a transaction when only a Ledger object is available
     * @param transactionId String     Transaction ID sent from command
     * @param amount int    Amount to transfer from payer to receiver
     * @param fee int   Fee to be charged to payer
     * @param note String   Notes about the transactions
     * @param payer String  Account ID for payer
     * @param receiver String   Account ID for receiver
     * @return  String  Transaction The transaction object
     * @throws  LedgerException when payer or receiver accounts don't exist
     */
    public String createTransaction (String transactionId, int amount, int fee, String note,
                                     String payer, String receiver) throws LedgerException
    {
        Account payerAccount = this.accountMap.get(payer);
        Account receiverAccount = this.accountMap.get(receiver);

        if (payerAccount == null)
        {
            throw new LedgerException("Create Transaction", "Payer account does not exist");
        }
        if (receiverAccount == null)
        {
            throw new LedgerException("Create Transaction", "Receiver account does not exist");
        }

        Transaction transaction = new Transaction(transactionId, amount, fee, note, payerAccount, receiverAccount);

        System.out.println("Transaction Object Created " + transaction);

        return this.processTransaction(transaction);
    }
}