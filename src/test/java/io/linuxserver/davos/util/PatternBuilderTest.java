package io.linuxserver.davos.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void builderShouldTurnQuestionMarksIntoSingleCharacterRegexMatcher() {

        String filter = "This?is?a filter";
        String expected = "This.{1}is.{1}a filter";

        assertThat(PatternBuilder.buildFromFilterString(filter)).isEqualTo(expected);
    }

    @Test
    public void builderShouldTurnAsterixesIntoManyCharacterRegexMatcher() {

        String filter = "This*is*a filter";
        String expected = "This.+is.+a filter";

        assertThat(PatternBuilder.buildFromFilterString(filter)).isEqualTo(expected);
    }

    @Test
    public void regexStringReturnedShouldBeAbleToActuallyMatchUsingRegexOperation() {

        String normalValue = "Clean Code.pdf";
        String filteredValue = "Clean?Code*";

        assertThat(normalValue.matches(PatternBuilder.buildFromFilterString(filteredValue))).isTrue();
    }

    @Test
    public void stringWithBothAsterixAndQuestionMarkShouldMatchProperly() {

        String anotherValue = "File Name with a Prefix12Then some text";
        String slightlyMoreComplicated = "File?Name*Prefix??Then some text";

        assertThat(anotherValue.matches(PatternBuilder.buildFromFilterString(slightlyMoreComplicated))).isTrue();
    }
}
