package com.test.springboot.rest.example.transaction.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.springboot.rest.example.transaction.defs.Channels;
import com.test.springboot.rest.example.transaction.defs.SortMode;
import com.test.springboot.rest.example.transaction.dto.TransactionDto;
import com.test.springboot.rest.example.transaction.dto.TransactionResponse;
import com.test.springboot.rest.example.transaction.dto.TransactionSearchDto;
import com.test.springboot.rest.example.transaction.util.ResourceLoader;
import com.test.springboot.rest.example.util.Unchecked;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

  private ObjectMapper jsonMapper;

  @Before
  public void setUp() {
    jsonMapper = loader.getObjectMapper();
  }

  @Test
  public void shouldCreateTransaction() throws Exception {
    TransactionDto transaction = loader.loadResourceJsonObject(
      "transaction/controller/create_transaction.json", TransactionDto.class);

    TransactionResponse<TransactionDto> expectedResponse = new TransactionResponse<TransactionDto>()
      .response(transaction);

    this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(content().json(jsonMapper.writeValueAsString(expectedResponse)));

  }

  @Test
  public void shouldNotCreateTransactionByDuplicate() throws Exception {
    TransactionDto transaction = loader.loadResourceJsonObject(
      "transaction/controller/create_transaction.json", TransactionDto.class);

    TransactionResponse<TransactionDto> expectedResponse = new TransactionResponse<TransactionDto>()
      .response(transaction);

    this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().is4xxClientError());

  }

  @Test
  public void shouldNotCreateTransactionByUnknownAccount() throws Exception {
    TransactionDto transaction = loader.loadResourceJsonObject(
      "transaction/controller/create_transaction.json", TransactionDto.class);
    transaction.setAccountIban("ES9820385778983000760237");

    TransactionResponse<TransactionDto> expectedResponse = new TransactionResponse<TransactionDto>()
      .response(transaction);

    this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON))
      .andDo(print())
      .andExpect(status().is4xxClientError());

  }

  @Test
  public void shouldCreateTransactionWithoutReference() throws Exception {
    TransactionDto transaction = loader.loadResourceJsonObject(
      "transaction/controller/create_transaction.json", TransactionDto.class);
    transaction.setReference(null);
    transaction.setAmount(100.0);

    this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(print())
      .andExpect(jsonPath("response.reference").exists())
      .andExpect(jsonPath("response.accountIban").value(equalTo(transaction.getAccountIban())));
  }

  @Test
  public void shouldCreateTransactionWithoutReferenceAndWithoutDate() throws Exception {
    TransactionDto transaction = loader.loadResourceJsonObject(
      "transaction/controller/create_transaction.json", TransactionDto.class);
    transaction.setReference(null);
    transaction.setDate(null);
    transaction.setAmount(50.0);

   this.mockMvc.perform(
      post("/transactions")
        .content(jsonMapper.writeValueAsString(transaction))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response.reference").exists())
      .andExpect(jsonPath("response.date").exists());
  }

  @Test
  public void shouldReturnTransactionsByIban() throws Exception {

    TransactionSearchDto searchFilter = loader.loadResourceJsonObject(
      "transaction/controller/search_transactions.json", TransactionSearchDto.class);

    this.mockMvc.perform(
      get("/transactions/search")
        .param("account_iban", searchFilter.getAccountIban())
        .param("sort", searchFilter.getSort()))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response").isArray())
      .andExpect(jsonPath("response").isNotEmpty());
  }

  @Test
  public void shouldReturnZeroTransactionsByNotExistingAccount() throws Exception {

    TransactionSearchDto searchFilter = loader.loadResourceJsonObject(
      "transaction/controller/search_transactions.json", TransactionSearchDto.class);

    this.mockMvc.perform(
      get("/transactions/search")
        .param("account_iban", "ANYIBAN")
        .param("sort", searchFilter.getSort()))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response").isArray())
      .andExpect(jsonPath("response").isEmpty());

  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldReturnTransactionsByIbanDefaultOrdering() throws Exception {

    TransactionSearchDto searchFilter = loader.loadResourceJsonObject(
      "transaction/controller/search_transactions.json", TransactionSearchDto.class);
    searchFilter.sort(null);

    MvcResult result = this.mockMvc.perform(
      get("/transactions/search")
        .param("account_iban", searchFilter.getAccountIban())
        .param("sort", searchFilter.getSort()))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response").isArray())
      .andExpect(jsonPath("response").isNotEmpty())
      .andReturn();

    Optional.of(result)
      .map(MvcResult::getResponse)
      .map(Unchecked.function(MockHttpServletResponse::getContentAsString))
      .map(Unchecked.function(this::jsonToResponse))
      .map(TransactionResponse::getResponse)
      .ifPresent(transactions -> assertThat(transactions).isSortedAccordingTo(Comparator.comparingDouble(
        TransactionDto::getAmount)));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldReturnTransactionsByIbanOrderingAscending() throws Exception {

    TransactionSearchDto searchFilter = loader.loadResourceJsonObject(
      "transaction/controller/search_transactions.json", TransactionSearchDto.class);

    MvcResult result = this.mockMvc.perform(
      get("/transactions/search")
        .param("account_iban", searchFilter.getAccountIban())
        .param("sort", searchFilter.getSort()))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response").isArray())
      .andExpect(jsonPath("response").isNotEmpty())
      .andReturn();

    Optional.of(result)
      .map(MvcResult::getResponse)
      .map(Unchecked.function(MockHttpServletResponse::getContentAsString))
      .map(Unchecked.function(this::jsonToResponse))
      .map(TransactionResponse::getResponse)
      .ifPresent(transactions -> assertThat(transactions).isSortedAccordingTo(Comparator.comparingDouble(
        TransactionDto::getAmount)));
  }

  @Test
  public void shouldReturnTransactionsByIbanOrderingDescending() throws Exception {

    TransactionSearchDto searchFilter = loader.loadResourceJsonObject(
      "transaction/controller/search_transactions.json", TransactionSearchDto.class);
    searchFilter.setSort(SortMode.DESC.getMode());

    MvcResult result = this.mockMvc.perform(
      get("/transactions/search")
        .param("account_iban", searchFilter.getAccountIban())
        .param("sort", searchFilter.getSort()))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response").isArray())
      .andExpect(jsonPath("response").isNotEmpty())
      .andReturn();


    Optional.of(result)
      .map(MvcResult::getResponse)
      .map(Unchecked.function(MockHttpServletResponse::getContentAsString))
      .map(Unchecked.function(this::jsonToResponse))
      .map(TransactionResponse::getResponse)
      .ifPresent(transactions -> assertThat(transactions).isSortedAccordingTo(Comparator.comparingDouble(
        TransactionDto::getAmount).reversed()));

  }

  @Test
  public void shouldReturnTransactionsStatus() throws Exception {
    this.mockMvc.perform(
      get("/transactions/status")
        .param("reference", "00001A")
        .param("channel", Channels.ATM.getValue()))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response.reference", equalTo("00001A")))
      .andExpect(jsonPath("response.status", equalTo("SETTLED")));
  }

  @Test
  public void shouldReturnErrorByNotExistingTransaction() throws Exception {
    this.mockMvc.perform(
      get("/transactions/status")
        .param("reference", "00020A")
        .param("channel", Channels.ATM.getValue()))
      .andExpect(status().isOk())
      .andDo(print()).andExpect(status().isOk())
      .andExpect(jsonPath("response.reference", equalTo("00020A")))
      .andExpect(jsonPath("response.status", equalTo("INVALID")));
  }

  private TransactionResponse<List<TransactionDto>> jsonToResponse(String content) throws Exception {
    return jsonMapper.readValue(content,
      new TypeReference<TransactionResponse<List<TransactionDto>>>() {
      });
  }
}
