package org.apache.cayenne.crypto.transformer.value;

import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class LocalDateTimeConverterTest {
    private LocalDateTime localDate(String dateString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateString, formatter);
    }

    @Test
    public void testFromBytes() throws ParseException {
        assertEquals(localDate("2015-01-07 11:00:02"), LocalDateTimeConverter.INSTANCE.fromBytes(new byte[]{64, 58, 0, 0, 36, 4, -113, 36, 116, 0}));
    }

    @Test
    public void testToBytes() throws ParseException {
        assertArrayEquals(new byte[]{64, 58, 0, 0, 36, 4, -113, 36, 116, 0},
                LocalDateTimeConverter.INSTANCE.toBytes(localDate("2015-01-07 11:00:02")));
    }
}