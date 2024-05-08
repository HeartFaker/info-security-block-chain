package com.blockchain.backend.Controller;

import com.blockchain.backend.Model.Block;
import com.blockchain.backend.Model.BlockChain;
import com.blockchain.backend.Model.Result;
import com.blockchain.backend.Service.BlockService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/blockChain")
public class BlockController {
    @Autowired
    BlockService blockService;

    Boolean isGenesis = false;

    @PostMapping("/genesis")
    public Result createGenesis(){
        Block genesis = blockService.createGenesis();
        return Result.success(genesis);
    }

    @GetMapping("")
    public Result getBlockChain(){
        return Result.success(blockService.getBlockChain());
    }

    @PostMapping("/block")
    public Result addNewBlock(@RequestParam String info, @RequestParam String fromName, @RequestParam String toName, @RequestParam float value){
        if(!isGenesis){
            Block genesis = blockService.createGenesis();
            isGenesis = true;
        }
        Block newBlock = blockService.addBlock(info, fromName, toName, value);
        return Result.success(newBlock);
    }

    @GetMapping("/valid")
    public Result checkChainValid(){
        return Result.success(blockService.checkChainValid());
    }
    @GetMapping("/balance")
    public Result queryBalance(@RequestParam String name){
        float balance = blockService.queryBalance(name);
        return Result.success(balance);
    }

    @GetMapping("/participants")
    public Result queryParticipants(){
        return Result.success(blockService.queryParticipant());
    }

    @GetMapping("/transactions")
    public Result queryTransactions(){
        return Result.success(blockService.queryTransactions());
    }

    @PostMapping("/products")
    public Result addProducts(@RequestParam String name){
        return Result.success(blockService.addProducts(name));
    }
}
