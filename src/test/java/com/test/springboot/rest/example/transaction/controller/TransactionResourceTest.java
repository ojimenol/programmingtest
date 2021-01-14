package com.test.springboot.rest.example.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.springboot.rest.example.transaction.dto.TransactionResponse;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.util.ResourceLoader;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionResourceTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ResourceLoader loader;

  @Autowired
  private ObjectMapper jsonMapper;

  @Test
  public void shouldCreateTransaction() throws Exception {
    Transaction transaction = loader.loadResourceJsonObject("create_transaction.json", Transaction.class);

    TransactionResponse<Transaction> expectedResponse = new TransactionResponse<Transaction>()
      .response(transaction);

    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));

  }

  @Test
  public void shouldNotCreateTransactionByDuplicate() throws Exception {
    Transaction transaction = loader.loadResourceJsonObject("create_transaction.json", Transaction.class);

    TransactionResponse<Transaction> expectedResponse = new TransactionResponse<Transaction>()
      .response(transaction);

    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andDo(print())
      .andExpect(status().is4xxClientError());

  }

  @Test
  public void shouldNotCreateTransactionByUnknownAccount() throws Exception {
    Transaction transaction = loader.loadResourceJsonObject("create_transaction.json", Transaction.class);
    transaction.setAccountIban("ES9820385778983000760237");

    TransactionResponse<Transaction> expectedResponse = new TransactionResponse<Transaction>()
      .response(transaction);

    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andDo(print())
      .andExpect(status().is4xxClientError());

  }

  @Test
  public void shouldCreateTransactionWithoutReference() throws Exception {
    Transaction transaction = loader.loadResourceJsonObject("create_transaction.json", Transaction.class);
    transaction.setReference(null);

    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andExpect(status().isOk())
      .andDo(print())
      .andExpect(jsonPath("response.reference").exists())
      .andExpect(jsonPath("response.accountIban").value(equalTo(transaction.getAccountIban())));
  }

  @Test
  public void shouldCreateTransactionWithoutReferenceAndWithoutDate() throws Exception {
    Transaction transaction = loader.loadResourceJsonObject("create_transaction.json", Transaction.class);
    transaction.setReference(null);
    transaction.setDate(null);

    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response.reference").exists())
      .andExpect(jsonPath("response.date").exists());
  }

  @Test
  public void shouldReturnTransactionsByIban() throws Exception {
    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions/search")
        .content(loader.loadResourceContent("search_transactions.json"))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response").isArray())
      .andExpect(jsonPath("response").isNotEmpty());
  }

  @Test
  public void shouldReturnZeroTransactionsByNotExistingAccount() throws Exception {
    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions/search")
        .content(loader.loadResourceContent("search_transactions_account_unknown.json"))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    MvcResult response = mockMvc.perform(asyncDispatch(asyncRequest))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response").isArray())
      .andExpect(jsonPath("response").isEmpty())
      .andReturn();

  }

  @Test
  public void shouldReturnTransactionsStatus() throws Exception {
    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions/status")
        .content(loader.loadResourceContent("search_transaction_status.json"))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response.reference", equalTo("00001A")))
      .andExpect(jsonPath("response.status", equalTo("SETTLED")));
  }

  @Test
  public void shouldReturnErrorByNotExistingTransaction() throws Exception {
    MvcResult asyncRequest = this.mockMvc.perform(
      post("/transactions/status")
        .content(loader.loadResourceContent("search_transaction_status_unknown_reference.json"))
        .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    mockMvc.perform(asyncDispatch(asyncRequest))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response.reference", equalTo("00002A")))
      .andExpect(jsonPath("response.status", equalTo("INVALID")));
  }
}
