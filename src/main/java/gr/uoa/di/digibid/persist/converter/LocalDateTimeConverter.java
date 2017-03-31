package gr.uoa.di.digibid.persist.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Created by amehrabyan, gpozidis on 27/08/16.
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    private static final DateTimeFormatter FULL_DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withZone(ZoneId.systemDefault());
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return null == localDateTime ? null : Timestamp.valueOf(localDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return null == timestamp ? null : timestamp.toLocalDateTime();
    }

    public static String toFormattedString(LocalDateTime time) {
        return time.format(FULL_DATE_TIME_FORMATTER);
    }

    public static String toSimpleFormattedString(LocalDateTime time) {
        return time.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parse(String time) {
        return LocalDateTime.parse(time, FULL_DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseSimple(String time) {
        return LocalDateTime.parse(time, DATE_TIME_FORMATTER);
    }
}