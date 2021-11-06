/*
 * %W% %E% Zain-Ul-Abedin
 *
 * Copyright (c) 2017-2018 Miranz Technology. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Miranz
 * technology. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Miranz.
 *
 */

package com.android.twallet.secure.web3J;

import android.os.FileUtils;

import com.android.twallet.secure.exception.InvalidPasswordException;
import com.android.twallet.secure.exception.WalletDeleteException;
import com.android.twallet.secure.utils.TWalletUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import java8.util.concurrent.CompletableFuture;


/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @author Zain-Ul-Abedin
 * @version 1.10 24 Aug 2017
 */

public class Web3jHandler {

    private static final String ETH_NETWORK = "https://rinkeby.infura.io/v3/16625fd0e5ca41c481255fe77e02eee0";
    private static final String ETHER_SCAN_NETWORK = "https://api-rinkeby.etherscan.io/api?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=desc&apikey=IPKTZE9U5NE95EHX5VD1C74H5AIWB5C5G8";
    private static final String CRYPTO_COMPARE_RATE = "https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=USD";
    private static final String LOG_FILENAME = "/wallet-log.txt";
    private static final int TX_MAX = 10;

    /* Web3j variable 'web3' is used to implement all the functions, exist in Web3j Library */
    private static Web3j web3;

    /* TwalletUtils used to access all functions inside the framework TWallet */
    private static TWalletUtils tWalletUtils;

    /* Credentials variable 'credentials' is used to implement all the functions, exist in Credentials Library */
    private static Credentials credentials;

    /* accountInfo variable which contains the info stored in secure memory, related to the current logged in account */
    private static AccountInfo accountInfo;

    /* historyChanged variable used to state if changed occurred between the accountInfo and the local history */
    private static boolean historyChanged;

    /* Transaction History Variable, used to present */
    private static List<Map<String, String>> txHistory;

    /**
     * web3Connection function is used to create the connection with the end-client node.
     *
     * @return condition
     */
    public static boolean web3Connection() {
        tWalletUtils = new TWalletUtils();
        web3 = Web3j.build(new HttpService(ETH_NETWORK));
        return web3 != null;
    }

    /**
     * setTxHistory function is used to initialize the list of transaction history
     */
    public static void setAccountInfo() {
        accountInfo = tWalletUtils.readData(credentials.getAddress());
        txHistory = new ArrayList<>();
        historyChanged = accountInfo.isEmpty();
    }

    /**
     * createWallet function is used to create a new UTC-JSON file and load into memory.
     *
     * @param password is used to access your UTC-JSON file.
     * @param path     is the location of the UTC-JSON file.
     * @throws IOException
     * @throws CipherException
     */
    public static void createWallet(String password, String path) throws IOException, CipherException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, InvalidPasswordException {

        if (password.isEmpty())
            throw new InvalidPasswordException();

        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.addProvider(new BouncyCastleProvider());

        String filename = WalletUtils.generateFullNewWalletFile(password, new File(path));
        credentials = WalletUtils.loadCredentials(password, path + "/" + filename);
        tWalletUtils.storeCredentials(credentials.getAddress(), password);
    }

    /**
     * exportWallet function is used to create a new UTC-JSON file and load into memory.
     *
     * @param password is used to access your UTC-JSON file.
     * @param opath    is the location of the UTC-JSON file.
     * @param dpath    location of the exported UTC-JSON file.
     * @throws WalletDeleteException
     * @throws IOException
     * @throws InvalidPasswordException if password is invalid
     */
    public static void exportWallet(String password, String opath, String dpath) throws WalletDeleteException, IOException, InvalidPasswordException {

        if (password.isEmpty() || !password.equalsIgnoreCase(password))
            throw new InvalidPasswordException();

        File directory = new File(opath);
        if (Objects.requireNonNull(directory.listFiles()).length > 1)
            throw new WalletDeleteException();
        if (!Objects.requireNonNull(directory.listFiles())[0].canRead())
            throw new WalletDeleteException();

        File destiny = new File(dpath + Objects.requireNonNull(directory.listFiles())[0].getName());
        FileUtils.copy(new FileInputStream(Objects.requireNonNull(directory.listFiles())[0]), new FileOutputStream(destiny));
    }

    /**
     * deleteWallet function is used to create a new UTC-JSON file and load into memory.
     *
     * @param password is used to access your UTC-JSON file.
     * @param path     is the location of the UTC-JSON file.
     * @throws WalletDeleteException    if delete operation fails
     * @throws InvalidPasswordException if password is invalid
     */
    public static void deleteWallet(String password, String path) throws WalletDeleteException, InvalidPasswordException {

        if (password.isEmpty() || !password.equalsIgnoreCase(password))
            throw new InvalidPasswordException();

        File directory = new File(path);
        File wallet = null;
        for (File file: Objects.requireNonNull(directory.listFiles()))
            if (file.getName().contains(credentials.getAddress().substring(2))) {
                wallet = file;
                break;
            }

        assert wallet != null;
        if (!wallet.delete())
            throw new WalletDeleteException();
        tWalletUtils.deleteCredentials();
        tWalletUtils.deleteData(credentials.getAddress());
    }

    /**
     * monitorWallet function is used to unlock/lock the monitoring of the secure components operations.
     *
     * @param password is used to access your UTC-JSON file.
     * @throws InvalidPasswordException if password is invalid
     */
    public static void monitorWallet(String password) throws InvalidPasswordException {

        if (password.isEmpty() || !password.equalsIgnoreCase(password))
            throw new InvalidPasswordException();

        tWalletUtils.seTrigger(true);
    }

    /**
     * loadCredentials function is used to load the UTC-JSON file from a particular path.
     *
     * @param path is the location of the UTC-JSON file.
     * @throws IOException
     * @throws CipherException
     */
    public static void loadCredentials(String path) throws IOException, CipherException {
        String[] user = tWalletUtils.loadCredentials();
        File directory = new File(path);
        String filename = "";

        for (File file: Objects.requireNonNull(directory.listFiles()))
            if (file.getName().contains(user[0].substring(2))) {
                filename = file.getName();
                break;
            }

        credentials = WalletUtils.loadCredentials(user[1], path + "/" + filename);
    }

    /**
     * transaction function is used to send funds from your address to another Ethereum address.
     *
     * @param address    is a TO address or a address where you want to transfer funds.
     * @param ethBalance is a amount you want to send.
     * @return
     * @throws Exception
     */
    public static TransactionReceipt transaction(String address, double ethBalance) throws Exception {
        TransactionReceipt receipt = Transfer.sendFunds(web3, credentials, address, BigDecimal.valueOf(ethBalance), Convert.Unit.ETHER).send();

        addTransaction(receipt.getFrom(), receipt.getTo(), Double.toString(ethBalance), false, new Timestamp(System.currentTimeMillis()).getTime());
        historyChanged = true;
        return receipt;
    }


    /**
     * getBalance function is used to get Balance and it returns the BigInteger value.
     *
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static BigInteger getBalance() {
        CompletableFuture<EthGetBalance> ethGetBalanceFuture = web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync();
        try {
            return ethGetBalanceFuture.get().getBalance();
        } catch (Exception e) {
            return BigInteger.ONE;
        }
    }

    /**
     * getBalanceUSD function is used to get Balance and it returns the BigInteger value.
     *
     * @param balance
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static double getBalanceUSD(BigDecimal balance) throws IOException {

        double rate;
        Document doc = Jsoup.connect(CRYPTO_COMPARE_RATE).ignoreContentType(true).get();
        Elements links = doc.select("body");
        Gson gson = new Gson();

        JsonObject json = JsonParser.parseString(links.first().text()).getAsJsonObject();
        rate = gson.fromJson(json.get("USD"), Double.class);
        return rate * balance.doubleValue();
    }

    /**
     * This function is returning wallet address from credentials
     *
     * @return address in String
     */
    public static String getWalletAddress() {
        return credentials.getAddress();
    }

    /**
     * This function is returning transactions done by the current logged in user, in the last block
     *
     * @return address in String
     */
    public static List<Map<String, String>> getPastTransactions() {

        List<Transaction> list = null;
        try {
            if (historyChanged && !(list = getTransactions()).isEmpty()) {
                accountInfo.transactions = list;
                tWalletUtils.writeData(credentials.getAddress(), accountInfo);

                historyChanged = false;
                txHistory.clear();
            }

            for (Transaction tx : accountInfo.transactions)
                if (!tx.getValue().equalsIgnoreCase("0"))
                    addTransaction(tx.getFrom(), tx.getTo(), tx.getValue(), true, Long.parseLong(tx.getTimeStamp()));

        } catch (IOException ignored) {
        }
        historyChanged = false;

        return txHistory;
    }

    private static void addTransaction(String from, String to, String value, boolean completed, long timeStamp) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        Map<String, String> item = new HashMap<>();

        if (completed) {
            item.put("status", "status: Completed");
        } else {
            item.put("status", "status: Pending");
        }

        Date date = new Date(timeStamp * 1000);
        item.put("date", dateFormat.format(date));

        if (from.contains(credentials.getAddress()))
            item.put("address", "to: " + to);
        else if (to.contains(credentials.getAddress()))
            item.put("address", "from: " + from);

        item.put("value", "value: " +
                Convert.toWei(value, Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000")).toString());
        txHistory.add(item);
    }

    private static List<Transaction> getTransactions() throws IOException {

        List<Transaction> list = new ArrayList<>();
        Document doc = Jsoup.connect(String.format(ETHER_SCAN_NETWORK, credentials.getAddress())).ignoreContentType(true).get();
        Elements links = doc.select("body");
        Gson gson = new Gson();

        for (Element link : links) {
            JsonObject json = JsonParser.parseString(link.text()).getAsJsonObject();
            JsonArray array = (JsonArray) json.get("result");
            if (array.size() >= txHistory.size() && historyChanged)
                for (int i = 0; i < TX_MAX && i < array.size(); i++)
                    list.add(gson.fromJson(array.get(i), Transaction.class));
        }

        return list;
    }

    /**
     * retrieveLog function is used to retrieve the log of operations performed by the secure components.
     *
     * @param dpath location of the exported log file.
     */
    public static void retrieveLog(String dpath) {

        String logData = tWalletUtils.getLoggingData();
        File destiny = new File(dpath + LOG_FILENAME);

        try (FileOutputStream fos = new FileOutputStream(destiny);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            byte[] bytes = logData.getBytes();

            bos.write(bytes);
            bos.close();
            fos.close();
            System.out.print("Data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
