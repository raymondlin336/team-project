package use_case.quotes;

import entity.Quote;
import org.junit.Before;
import org.junit.Test;
import use_case.quote.data_access_interface.QuoteDataAccessInterface;
import use_case.quote.data_access_interface.QuoteRetrievalException;
import use_case.quote.input.GetDailyQuoteInputData;
import use_case.quote.interactor.GetDailyQuoteInteractor;
import use_case.quote.output.GetDailyQuoteOutputBoundary;
import use_case.quote.output.GetDailyQuoteOutputData;

import static org.junit.Assert.*;

public class GetDailyQuoteInteractorTests {

    private FakeQuoteDataAccess fakeDataAccess;
    private MockGetDailyQuotePresenter mockPresenter;
    private GetDailyQuoteInteractor interactor;

    @Before
    public void setUp() {
        fakeDataAccess = new FakeQuoteDataAccess();
        mockPresenter = new MockGetDailyQuotePresenter();
        interactor = new GetDailyQuoteInteractor(fakeDataAccess, mockPresenter);
    }

    @Test
    public void testExecute_withForceUpdateTrue_callsDaoWithTrue() {
        // Arrange
        fakeDataAccess.setQuoteToReturn(new Quote("Live long", "Spock"));
        GetDailyQuoteInputData inputData = new GetDailyQuoteInputData(true);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue("DAO should have received forceUpdate=true", fakeDataAccess.lastForceUpdateValue);
        assertTrue("Success view should be prepared", mockPresenter.wasSuccessCalled);
        assertEquals("Live long", mockPresenter.lastSuccessData.getText());
    }

    @Test
    public void testExecute_withDefaultInput_callsDaoWithFalse() {
        // Arrange
        fakeDataAccess.setQuoteToReturn(new Quote("Live long", "Spock"));
        GetDailyQuoteInputData inputData = new GetDailyQuoteInputData(); // Default constructor

        // Act
        interactor.execute(inputData);

        // Assert
        assertFalse("DAO should have received default forceUpdate=false", fakeDataAccess.lastForceUpdateValue);
        assertTrue(mockPresenter.wasSuccessCalled);
    }

    @Test
    public void testExecute_failure_callsPresenterFail() {
        // Arrange
        fakeDataAccess.setExceptionToThrow(new QuoteRetrievalException("Error", new Exception()));
        GetDailyQuoteInputData inputData = new GetDailyQuoteInputData();

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue("Fail view should be prepared", mockPresenter.wasFailCalled);
        assertNotNull(mockPresenter.lastFailMessage);
    }

    // --- Coverage for Input Data ---
    @Test
    public void testInputDataCoverage() {
        GetDailyQuoteInputData input = new GetDailyQuoteInputData(true);
        assertTrue(input.isForceUpdate());

        GetDailyQuoteInputData inputDefault = new GetDailyQuoteInputData();
        assertFalse(inputDefault.isForceUpdate());
    }

    // ========== Test Doubles ==========

    private static class FakeQuoteDataAccess implements QuoteDataAccessInterface {
        private Quote quoteToReturn;
        private QuoteRetrievalException exceptionToThrow;
        public boolean lastForceUpdateValue; // To verify what the Interactor passed

        public void setQuoteToReturn(Quote quote) {
            this.quoteToReturn = quote;
            this.exceptionToThrow = null;
        }

        public void setExceptionToThrow(QuoteRetrievalException exception) {
            this.exceptionToThrow = exception;
        }

        @Override
        public Quote fetchQuote(boolean forceUpdate) throws QuoteRetrievalException {
            this.lastForceUpdateValue = forceUpdate;
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            return quoteToReturn;
        }
    }

    private static class MockGetDailyQuotePresenter implements GetDailyQuoteOutputBoundary {
        boolean wasSuccessCalled = false;
        boolean wasFailCalled = false;
        GetDailyQuoteOutputData lastSuccessData;
        String lastFailMessage;

        @Override
        public void presentSuccess(GetDailyQuoteOutputData outputData) {
            wasSuccessCalled = true;
            lastSuccessData = outputData;
        }

        @Override
        public void presentFailure(String errorMessage) {
            wasFailCalled = true;
            lastFailMessage = errorMessage;
        }
    }
}