package com.cscie97.ledger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CommandProcessor is a utility class for feeding the Ledger a set of operations, using command syntax.
 */
public final class CommandProcessor
{
    /**
     * Blockchain ledger object to use
     */
    private Ledger ledger;

    /**
     * Process a set of commands provided within the given commandFile. Throw a CommandProcessorException on error.
     * @param fileName String   Path of the file with the commands
     * @throws CommandProcessorException When no file found
     */

    public void processCommandFile(String fileName) throws CommandProcessorException
    {
        // Inspiration from https://www.javatpoint.com/post/java-scanner-hasnextline-method
        File file = new File(fileName);
        Scanner script;

        int lineNumber = 0;

        try
        {
            script = new Scanner(file);
        }
        catch (FileNotFoundException e)
        {
            throw new CommandProcessorException("processCommandFile", "File not found", null);
        }

        while (script.hasNextLine()) {
            String line = script.nextLine();
            lineNumber++;

            // Skip empty lines and lines that are comments
            if (line.length() != 0 && !(line.startsWith("#"))) {
                try {
                    processCommand(line);
                } catch (CommandProcessorException e) {
                    System.out.println(String.format("SCRIPT ERROR at line number: (%s)\n" +
                                    "COMMAND: %s \n" +
                                    "REASON: %s\n",
                            lineNumber, e.getCommand(), e.getReason()));
                }
            }
        }

        script.close();
    }


    /**
     * Responds to the command to create a ledger
     * @param args  List of strings for each argument passed with the command
     */
    private void createLedger(List<String> args)
    {
        this.ledger = new Ledger(args.get(1), args.get(3), args.get(5));
        System.out.println(String.format("CREATED NEW LEDGER \n" +
                        "Name: %s \n" +
                        "Description: %s \n" +
                        "Seed: %s\n",
                this.ledger.getName(),
                this.ledger.getDescription(),
                this.ledger.getSeed()));
    }

    /**
     * Responds to the command to create an account
     * @param args  List of strings for each argument passed with the command
     * @throws LedgerException  The exception object
     */
    private void createAccount(List<String> args) throws LedgerException
    {
        try
        {
            System.out.println(String.format("CREATED NEW ACCOUNT \n" +
                            "AccountId: %s\n",
                    this.ledger.createAccount(args.get(1))));
        }
        catch (LedgerException e)
        {
            throw e;
        }
    }

    /**
     * Responds to the command to process a transaction
     * @param args  List of strings for each argument passed with the command
     * @throws LedgerException  Exception object thrown
     */
    private void processTransaction(List<String> args) throws LedgerException
    {
        String transactionId;

        try
        {
            transactionId = this.ledger.createTransaction(args.get(1),
                    Integer.parseInt(args.get(3)),
                    Integer.parseInt(args.get(5)),
                    args.get(7),
                    args.get(9),
                    args.get(11));
            System.out.println(String.format("TRANSACTION: %s \n" +
                            "TRANSACTION PROCESSED Transaction ID: %s \n",
                    args,
                    transactionId));
        }
        catch (LedgerException e)
        {
            throw e;
        }
    }

    /**
     * Responds to the command to get an account balance
     * @param args  List of strings for each argument passed with the command
     * @throws LedgerException  Exception object thrown
     */
    private void getAccountBalance(List<String> args) throws LedgerException
    {
        int accountBalance;

        try
        {
            accountBalance = ledger.getAccountBalance(args.get(1));
        }
        catch (LedgerException e)
        {
            throw e;
        }

        System.out.println(String.format("GET ACCOUNT BALANCE \n" +
                "%s: %s\n",
                args.get(1),
                accountBalance));
    }

    /**
     * Responds to the command to get account balances of all accounts
     * @param   arg  List of strings for each argument passed with the command
     * @throws LedgerException  Exception object thrown
     */
    private void getAccountBalances(List<String> arg) throws LedgerException
    {
        try {
            System.out.println(String.format("GET ACCOUNT BALANCES \n" +
                            "%s\n",
                    ledger.getAccountBalances()));
        }
        catch(LedgerException e)
        {
            throw e;
        }
    }

    /**
     * Responds to the command to get information about a block
     * @param args  List of strings for each argument passed with the command
     * @throws LedgerException  Exception object thrown
     */
    private void getBlock(List<String> args) throws LedgerException
    {
        Block block = null;

        try
        {
            block = this.ledger.getBlock(Integer.parseInt(args.get(1)));
        }
        catch(LedgerException e)
        {
            throw e;
        }

        System.out.println(String.format("GET BLOCK \n" +
                "Block Number: %s" +
                "Account Balance Map: %s" +
                "Transactions: %s\n",
                Integer.parseInt(args.get(1)),
                block.getAccountBalanceMap(),
                block.getTransactionList()));
    }

    /**
     * Responds to the command to get information about one transaction
     * @param args  List of strings for each argument passed with the command
     * @throws LedgerException  Exception object thrown
     */
    private void getTransaction(List<String> args) throws LedgerException
    {
        try {
            System.out.println(String.format("GET TRANSACTION\n" +
                            "%s\n",
                    this.ledger.getTransaction(args.get(1)).toString()));
        }
        catch(LedgerException e)
        {
            throw e;
        }
    }

    /**
     * *
     * Process a single command. The output of the command is formatted and displayed to stdout. Throw a
     * CommandProcessorException on error.
     * @param line String    One command containing arguments separated by spaces
     * @throws CommandProcessorException if the command (arg[0]) is not recognized in the switch statement
     * @throws CommandProcessorException if a command files to be successfully executed by the ledger service
     */
    public void processCommand (String line) throws CommandProcessorException
    {
        List<String> args = new ArrayList<>();

        // Inspiration from https://howtodoinjava.com/java-regular-expression-tutorials/
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");

        Matcher matcher = regex.matcher(line);

        while (matcher.find())
        {
            args.add(matcher.group().replace("\"", ""));
        }

        String command = args.get(0);

        try
        {
            switch (command)
            {
                case "create-ledger":
                    this.createLedger(args);
                    break;
                case "create-account":
                    this.createAccount(args);
                    break;
                case "process-transaction":
                    this.processTransaction(args);
                    break;
                case "get-account-balance":
                    this.getAccountBalance(args);
                    break;
                case "get-account-balances":
                    this.getAccountBalances(args);
                    break;
                case "get-block":
                    this.getBlock(args);
                    break;
                case "get-transaction":
                    this.getTransaction(args);
                    break;
                case "validate":
                    this.ledger.validate();
                    System.out.println("BLOCKCHAIN VALID\n");
                    break;
                default:
                    throw new CommandProcessorException(command, "Unknown command", null);
            }
        }
        catch (LedgerException e)
        {
            throw new CommandProcessorException(command, e.getReason(), null);
        }
    }
}