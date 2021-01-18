package com.test.springboot.rest.example.transaction.generator;

import com.test.springboot.rest.example.transaction.exception.TransactionException;
import com.test.springboot.rest.example.transaction.persistent.Reference;
import com.test.springboot.rest.example.transaction.repository.ReferenceRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class TransactionRefGeneratorTest {

  @Parameters(name = "Generate reference from id {0}")
  public static Collection<Object[]> ids() {
    return  Arrays.asList(new Object[][]{
      {1L, "00001A"},
      {99999L, "99999A"},
      {100000L, "00000B"},
      {2599999L, "99999Z"},
      {2600000L, "ERROR"},
    });
  };

  private TransactionRefGenerator transactionRefGenerator;

  private ReferenceRepository referenceRepository;

  @Parameter(0)
  public Long id;
  @Parameter(1)
  public String expectedReference;

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

    String referenceValue = transactionRefGenerator.generateReference();

    assertThat(referenceValue).hasSize(TransactionRefGenerator.REFERENCE_SIZE);
    assertThat(referenceValue).isEqualTo(this.expectedReference);
  }
}