package ai.youki.wattifybackend.controller;

import ai.youki.wattifybackend.exception.ApiRequestException;
import ai.youki.wattifybackend.model.TransactionInfo;
import ai.youki.wattifybackend.model.Wallet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/wallet")
public class TransferController {
    final Web3j web3 = Web3j.build(new HttpService("https://rinkeby.infura.io/v3/2b16d663032f426da2acb00a8395dd04"));  // defaults to http://localhost:8545/
    final Credentials credentials = Credentials.create("e2efc0a05ec8c63945e2b7bc353e984eb9aaedd0969b5b0d099e698520e2a2c4");
    final String smartContractAddress = "0x781065fa2Fa3A7DD5a2EB3e73bBb4a1dADeCe3ec";
    String transactionHash;

    @PostMapping()
    public ResponseEntity<TransactionInfo> transferEther(@RequestBody Wallet wallet) {
        boolean isTransferSuccessful;
        BigDecimal youkiBalance = checkYoukiWalletBalance();
        System.out.println("Youki balance : " + youkiBalance);
        if (!(youkiBalance.doubleValue() < 5.0)) {
            try {
                TransactionReceipt transactionReceipt = Transfer.sendFunds(
                        web3, credentials, wallet.walletAddress,
                        //                    "0x52b94e6C41B85878Ee617Aa806E53fC3c95cDE08",
                        BigDecimal.valueOf(0.1), Convert.Unit.ETHER).send();
                isTransferSuccessful = true;
                transactionHash = transactionReceipt.getTransactionHash();
                System.out.println("TransactionReceipt :" + transactionHash);
            } catch (Exception e) {
                isTransferSuccessful = false;
                e.printStackTrace();
            }
        } else {
            throw new ApiRequestException("Insufficient funds for ether transfer, please fund manually");
        }
        return ResponseEntity.ok(new TransactionInfo(transactionHash, isTransferSuccessful));
    }

    public BigDecimal checkYoukiWalletBalance() {
        EthGetBalance ethGetBalance;
        BigDecimal balance = new BigDecimal("0.0");
        try {
            ethGetBalance = web3.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();
            balance = Convert.fromWei(ethGetBalance.getBalance().toString(), Convert.Unit.ETHER);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return balance;
    }

}
