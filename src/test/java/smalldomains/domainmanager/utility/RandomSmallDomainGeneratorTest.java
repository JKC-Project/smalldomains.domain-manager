package smalldomains.domainmanager.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RandomSmallDomainGeneratorTest {
    // all CsvSources were generated by a python script instead of typed out manually by hand

    @Test
    void isRandomSmallDomainCorrectLength() {
        final String randomSmallDomain = new RandomSmallDomainGenerator().generateRandomSmallDomain();
        assertEquals(7, randomSmallDomain.length());
    }

    @ParameterizedTest
    @MethodSource("provideAllValidCodepoints")
    void doesEncodingExistForEveryValidCodepoint(final int validCodepoint) {
        assertDoesNotThrow(() -> RandomSmallDomainGenerator.encodeInteger(validCodepoint));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,62})
    void isExceptionThrownForInvalidCodepoints(final int invalidCodepoint) {
        assertThrows(IllegalArgumentException.class, () -> RandomSmallDomainGenerator.encodeInteger(invalidCodepoint));
    }

    @ParameterizedTest
    @CsvSource({
            "0,a",
            "1,b",
            "2,c",
            "3,d",
            "4,e",
            "5,f",
            "6,g",
            "7,h",
            "8,i",
            "9,j",
            "10,k",
            "11,l",
            "12,m",
            "13,n",
            "14,o",
            "15,p",
            "16,q",
            "17,r",
            "18,s",
            "19,t",
            "20,u",
            "21,v",
            "22,w",
            "23,x",
            "24,y",
            "25,z"
    })
    void isEncodingToLowercaseAlphaCorrect(final int codepoint, final char expectedEncoding) {
        final char actualEncoding = RandomSmallDomainGenerator.encodeInteger(codepoint);
        assertEquals(expectedEncoding, actualEncoding);
    }

    @ParameterizedTest
    @CsvSource({
            "26,A",
            "27,B",
            "28,C",
            "29,D",
            "30,E",
            "31,F",
            "32,G",
            "33,H",
            "34,I",
            "35,J",
            "36,K",
            "37,L",
            "38,M",
            "39,N",
            "40,O",
            "41,P",
            "42,Q",
            "43,R",
            "44,S",
            "45,T",
            "46,U",
            "47,V",
            "48,W",
            "49,X",
            "50,Y",
            "51,Z"
    })
    void isEncodingToUppercaseAlphaCorrect(final int codepoint, final char expectedEncoding) {
        final char actualEncoding = RandomSmallDomainGenerator.encodeInteger(codepoint);
        assertEquals(expectedEncoding, actualEncoding);
    }

    @ParameterizedTest
    @CsvSource({
            "52,0",
            "53,1",
            "54,2",
            "55,3",
            "56,4",
            "57,5",
            "58,6",
            "59,7",
            "60,8",
            "61,9"
    })
    void isEncodingToNumericDigitCorrect(final int codepoint, final char expectedEncoding) {
        final char actualEncoding = RandomSmallDomainGenerator.encodeInteger(codepoint);
        assertEquals(expectedEncoding, actualEncoding);
    }

    private static IntStream provideAllValidCodepoints() {
        return IntStream.range(0, 62);
    }
}