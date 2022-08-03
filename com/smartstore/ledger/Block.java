package com.cscie97.ledger;

import java.util.*;

/**
 * The Block aggregates groups of 10 transactions. Transactions should be added to blocks in the
 * order that they are received. Prior to adding a transaction to a block, the transaction must be
 * validated. Transactions that are invalid should be discarded. The block also contains an account
 * balance map that reflects the balance of all accounts after all the transactions within the block
 * have been applied. The account balance map should be copied from the previous block and
 * updated to reflect the transactions in the current block. The block contains the hash of the
 * previous block. It also contains the hash of itself.
 */
public class Block {

    /**
     * A sequentially incrementing block number assigned to the block. The first block or
     * genesis block is assigned a block number of 1.
     */
    private int blockNumber;

    /**
     * The hash of the previous block in the blockchain. For the genesis block, this is  empty. Use the Sha-256
     * algorithm and Merkle tree to compute the hash per the requirements.
     */
    private String previousHash;

    /**
     * The hash of the current block is computed based on all properties and associations of the current Block
     * except for this attribute. Use the Sha-256 algorithm and Merkle tree to compute the hash per the requirements.
     */
    private String hash;

    /**
     * Association - An ordered list of Transactions that are included in the current block. There should
     * be exactly 10 transactions per block.
     */
    private Map<String, Transaction> transactionList;

    /**
     * Association - The full set of accounts managed by the Ledger. The account balances should reflect the
     * account state after all transactions of the current block have been applied. Note that each Block has
     * its own immutable copy of the accountBalanceMap.
     */
    private Map<String, Integer> accountBalanceMap;

    /**
     * The previousBlock association references the preceding Block in the blockchain.
     */
    private Block previousBlock;

    /**
     * Constructor of a genesis Block object with blockNumber 1.
     * For previousHash is empty. Initialize accountBalanceMap and transactionList to generalize code.
     */
    public Block()
    {
        this.blockNumber = 1;
        this.accountBalanceMap = new HashMap<>();
        this.transactionList = new LinkedHashMap<>();
    }

    /**
     * Constructor of non-genesis blocks. Using the reference to the previous block passed into this constructor,
     * initialize the blockNumber, previousHash, transactionList, accountBalanceMap, previousBlock.
     * @param previousBlock     Block   The block just before this block
     */
    public Block(Block previousBlock)
    {
        this.previousBlock = previousBlock;
        this.blockNumber = this.previousBlock.getBlockNumber() + 1;
        this.previousHash = this.previousBlock.getHash();
        this.transactionList = new LinkedHashMap<>();
        this.accountBalanceMap = new HashMap<>();
    }

    /**
     * Getter for blockNumber
     * @return int  Return the blockNumber of this block
     */
    public int getBlockNumber()
    {
        return this.blockNumber;
    }

    /**
     * Getter for hash of this block
     * @return String   Hash value of this block generated from from merkle tree and block info
     */
    public String getHash()
    {
        return this.hash;
    }

    /**
     * Getter for hash of previous block
     * @return String   Hash value of the previous block
     */
    public String getPreviousHash()
    {
        return this.previousHash;
    }

    /**
     * @return Map of Transaction ID to Transaction a map of references for all the Transactions included in this block
     */
    public Map<String, Transaction> getTransactionList()
    {
        return this.transactionList;
    }

    /**
     * Getter for full set of accounts managed by the  Ledger
     * @return Map of Account ID to Balance    Map containing account identifiers and balances when this block was constructed
     */
    public Map<String, Integer> getAccountBalanceMap()
    {
        return this.accountBalanceMap;
    }

    /**
     * Add pair of account identifier and balance to the accountBalanceMap
     * @param account Account   Account identier from which identifier and address must be added to accountBalanceMap
     */
    public void addAccountBalance(Account account)
    {
        this.accountBalanceMap.put(account.getAddress(), account.getBalance());
    }

    /**
     * Add pair of transaction identifier and transaction.
     * @param transaction   Transaction object, one of 10 in this block.
     * @throws LedgerException  Exception object thrown
     */
    public void addTransaction(Transaction transaction) throws LedgerException
    {
        if (this.transactionList.containsKey(transaction.getTransactionId()))
        {
            throw new LedgerException("addTransaction", "Transaction ID already exists");
        }
        this.transactionList.put(transaction.getTransactionId(), transaction);
    }

    /**
     * Inspiration from https://github.com/Mignet/blockchain/blob/master/src/main/java/com/v5ent/nmcoin/CommonUtils.java
     * Method to generate Merkle Tree root from list of transactions. This is called from the getBlockHash method
     * @param transactions  All transactions in this block passed from getBlockHash method
     * @return String   Representing the root hash.
     */
    public static String getMerkleRoot(ArrayList<Transaction> transactions)
    {
        int count = transactions.size();

        List<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transactions)
        {
            previousTreeLayer.add(transaction.getTransactionId());
        }
        List<String> treeLayer = previousTreeLayer;

        while(count > 1) {
            treeLayer = new ArrayList<String>();
            for(int i=1; i < previousTreeLayer.size(); i+=2) {
                treeLayer.add(Util.sha256(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }

        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }

    /**
     * Method that generates the hash for the block including the Merkle Tree root and other block info
     * @param seed  Used for seed to generate hash
     * @return String   Returns hash of this block.
     */
    public String getBlockHash(String seed) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        this.transactionList.forEach((id, transaction) -> {
            transactions.add(transaction);
        });

        // Create string to generate hash of for this block.
        String stringToHash = String.format("%s-%s-%s-%s-%s-%s",
                this.blockNumber,
                this.getMerkleRoot(transactions),
                this.accountBalanceMap,
                this.transactionList,
                this.previousHash, seed);

        this.hash = Util.sha256(stringToHash);

        return this.hash;
    }
}