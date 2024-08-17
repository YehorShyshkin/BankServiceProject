package com.bankapp.app.service.impl;


import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.exception.AccountNotFoundException;
import com.bankapp.app.exception.ClientNotFoundException;
import com.bankapp.app.mapper.AccountMapper;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Client;
import com.bankapp.app.repository.AccountRepository;
import com.bankapp.app.repository.AgreementRepository;
import com.bankapp.app.repository.ClientRepository;
import com.bankapp.app.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ClientRepository clientRepository;
    private final AgreementRepository agreementRepository;

    @Override
    @Transactional
    public AccountDTO findAccountById(UUID accountsId) {
        return accountMapper.toDto(findById(accountsId));
    }

    @Override
    @Transactional
    public Account findById(UUID accountsId){
        return accountRepository.findById(accountsId)
                .orElseThrow(()-> new AccountNotFoundException(
                        String.format("Account with id %s not found", accountsId)));
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account newAccount = accountMapper.toEntity(accountDTO);
        Client client = clientRepository.findById(UUID.fromString(accountDTO.getClientId()))
                .orElseThrow(()-> new ClientNotFoundException("Client with id " + accountDTO.getClientId() + " not found"));

        newAccount.setClient(client);
        log.info("Created new account {}", newAccount);
        Account savedAccount = accountRepository.save(newAccount);
        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDTO updateAccount(UUID accountsId, AccountDTO accountDTO) {
        Account account = findById(accountsId);
        accountMapper.updateAccountFromDTO(accountDTO, account);
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toDto(updatedAccount);
    }




//    @Override
//    @Transactional
//    public Account getAccountById(String id) {
//        return accountRepository.findById(UUID.fromString(id)).orElseThrow();
//    }
//
//    @Override
//    @Transactional
//    public AccountDTO getAccountDTO(String id) {
//        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(id));
//        Account account = accountOptional.orElseThrow(() -> new NoSuchElementException("Account not found!"));
//        return accountMapper.toDTO(account);
//    }
//
//    @Override
//    @Transactional
//    public List<AccountDTO> findAll() {
//        return accountMapper.toDTO(accountRepository.findAll());
//    }
//
//    @Override
//    @Transactional
//    public Account getById(String id) {
//        return accountRepository.getReferenceById(UUID.fromString(id));
//    }
//
//
//    @Override
//    public Account save(Account account) {
//        return accountRepository.save(account);
//    }
//
//    @Override
//    public Account findAccountById(UUID accountId) {
//        return accountRepository.findById(accountId)
//                .orElseThrow(() -> new EntityNotFoundException("Account not found!"));
//    }
//
//    @Override
//    public Account updateAccount(UUID id, AccountDTO accountDTO) {
//        Account currentAccount = findAccountById(id);
//        Account updateAccount = accountMapper.updateAccountFromDTO(accountDTO, currentAccount);
//        return accountRepository.save(updateAccount);
//    }
//
//    @Override
//    public boolean deleteAccount(UUID id) {
//        if (accountRepository.existsById(id)) {
//            accountRepository.deleteById(id);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    @Transactional
//    public void updateBalance(Transaction transaction) {
//        Account firstAccount = findAccountById(transaction.getDebitAccount().getId());
//        Account secondAccount = findAccountById(transaction.getCreditAccount().getId());
//
//        BigDecimal firstAccountBalance = firstAccount.getBalance();
//        BigDecimal secondAccountBalance = secondAccount.getBalance();
//        BigDecimal transactionAmount = transaction.getAmount();
//
//        BigDecimal newFirstAccountBalance = firstAccountBalance.subtract(transactionAmount);
//        BigDecimal newSecondAccountBalance = secondAccountBalance.add(transactionAmount);
//        firstAccount.setBalance(newFirstAccountBalance);
//        secondAccount.setBalance(newSecondAccountBalance);
//
//        accountRepository.save(firstAccount);
//        accountRepository.save(secondAccount);
//    }
}
