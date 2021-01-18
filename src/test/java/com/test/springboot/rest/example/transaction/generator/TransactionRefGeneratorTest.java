package com.test.springboot.rest.example.transaction.generator;

import com.test.springboot.rest.example.transaction.defs.Error;
import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.persistent.Reference;
import com.test.springboot.rest.example.transaction.persistent.Transaction;
import com.test.springboot.rest.example.transaction.repository.ReferenceRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class TransactionRefGeneratorTest {

  @Parameters(name = "Generate {0} from id {1}")
  public static Collection<Object[]> ids() {
    return  Arrays.asList(new Object[][]{
      {"first reference", 1L, "00001A", null, null},
      {"last reference before letter change", 99999L, "99999A", null, null},
      {"first reference after letter change", 100000L, "00000B", null, null},
      {"last valid reference", 2599999L, "99999Z", null, null},
      {"invalid reference", 2600000L, "ERROR", TransactionException.class, Error.GENERATE_TRANSACTION_REFERENCES_FULL.getDescription()}
    });
  };

  private TransactionRefGenerator transactionRefGenerator;

  private ReferenceRepository referenceRepository;

  @Parameter(0)
  public String testName;
  @Parameter(1)
  public Long id;
  @Parameter(2)
  public String expectedReference;
  @Parameter(3)
  public Class<Exception> expectedException;
  @Parameter(4)
  public String expectedExceptionMessage;


  @Before
  public void setUp() {
    referenceRepository = Mockito.mock(ReferenceRepository.class);
    transactionRefGenerator = new TransactionRefGenerator(referenceRepository);
  }

  @Test
  public void shouldGenerateSequential() {
    Reference reference = new Reference().id(this.id);

    when(referenceRepository.save(any(Reference.class)))
      .thenReturn(reference);

    if (this.expectedException != null) {
      assertThrows(this.expectedException, () -> transactionRefGenerator.generateReference(), this.expectedExceptionMessage);
    } else {
      String referenceValue = transactionRefGenerator.generateReference();

      assertThat(referenceValue).hasSize(TransactionRefGenerator.REFERENCE_SIZE);
      assertThat(referenceValue).isEqualTo(this.expectedReference);
    }
  }
}