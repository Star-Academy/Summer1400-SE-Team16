package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchQueryTest {
    @Test
    void SearchQueryConstructorTest() {
        String firstInput = "+fastest +holding -easily -stresses need information";
        String[] firstRequiredWordsExpected = {"need", "inform"};
        String[] firstOptionalWordsExpected = {"hold", "fastest"};
        String[] firstBannedWordsExpected = {"stress", "easili"};
        SearchQuery firstSearchQuery = new SearchQuery(firstInput);
        Assertions.assertAll(() -> {
            Assertions.assertArrayEquals(firstRequiredWordsExpected, firstSearchQuery.getRequiredWords());
            Assertions.assertArrayEquals(firstOptionalWordsExpected, firstSearchQuery.getOptionalWords());
            Assertions.assertArrayEquals(firstBannedWordsExpected, firstSearchQuery.getBannedWords());
        });
    }
}
