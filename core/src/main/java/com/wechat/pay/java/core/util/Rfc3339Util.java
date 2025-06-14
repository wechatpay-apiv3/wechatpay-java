package com.wechat.pay.java.core.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

/** RFC3339标准解析工具 */
public class Rfc3339Util {
  /** 时区偏移量 */
  private static ZoneOffset zoneOffset = OffsetTime.now().getOffset();

  /** 时间格式 */
  private static final DateTimeFormatter FORMATTER =
      new DateTimeFormatterBuilder()
          .parseCaseInsensitive()
          .append(DateTimeFormatter.ISO_LOCAL_DATE)
          .appendLiteral('T')
          .appendValue(ChronoField.HOUR_OF_DAY, 2)
          .appendLiteral(':')
          .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
          .appendLiteral(':')
          .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
          .appendOffsetId()
          .toFormatter();

  /**
   * 设置时区偏移量
   *
   * @param zoneId 时区ID
   */
  public static void setZoneOffset(ZoneId zoneId) {
    Rfc3339Util.zoneOffset = zoneId.getRules().getOffset(Instant.now());
  }

  /**
   * 设置时区偏移量
   *
   * @param zoneOffset 时区偏移量
   */
  public static void setZoneOffset(ZoneOffset zoneOffset) {
    Rfc3339Util.zoneOffset = zoneOffset;
  }

  /**
   * OffsetDateTime转RFC3339格式时间字符串
   *
   * @param offsetDateTime OffsetDateTime
   * @return RFC3339格式时间字符串
   */
  public static String fromOffsetDateTime(OffsetDateTime offsetDateTime) {
    return offsetDateTime.format(FORMATTER);
  }

  /**
   * Instant转RFC3339格式时间字符串
   *
   * @param instant Instant
   * @return RFC3339格式时间字符串
   */
  public static String fromInstant(Instant instant) {
    return fromInstant(instant, zoneOffset);
  }

  /**
   * Instant转RFC3339格式时间字符串
   *
   * @param instant Instant
   * @param zoneOffset 时区偏移量
   * @return RFC3339格式时间字符串
   */
  public static String fromInstant(Instant instant, ZoneOffset zoneOffset) {
    return fromOffsetDateTime(instant.atOffset(zoneOffset));
  }

  /**
   * Date转RFC3339格式时间字符串
   *
   * @param date Date
   * @return RFC3339格式时间字符串
   */
  public static String fromDate(Date date) {
    return fromInstant(date.toInstant());
  }

  /**
   * LocalDateTime转RFC3339格式时间字符串
   *
   * @param localDateTime LocalDateTime
   * @return RFC3339格式时间字符串
   */
  public static String fromLocalDateTime(LocalDateTime localDateTime) {
    return fromLocalDateTime(localDateTime, zoneOffset);
  }

  /**
   * LocalDateTime转RFC3339格式时间字符串
   *
   * @param localDateTime LocalDateTime
   * @param zoneOffset 时区偏移量
   * @return RFC3339格式时间字符串
   */
  public static String fromLocalDateTime(LocalDateTime localDateTime, ZoneOffset zoneOffset) {
    return fromOffsetDateTime(localDateTime.atOffset(zoneOffset));
  }

  /**
   * RFC3339格式时间字符串转OffsetDateTime
   *
   * @param rfc3339 RFC3339格式时间字符串
   * @return OffsetDateTime
   */
  public static OffsetDateTime toOffsetDateTime(String rfc3339) {
    return OffsetDateTime.parse(rfc3339, FORMATTER);
  }

  /**
   * RFC3339格式时间字符串转Instant
   *
   * @param rfc3339 RFC3339格式时间字符串
   * @return Instant
   */
  public static Instant toInstant(String rfc3339) {
    return toOffsetDateTime(rfc3339).toInstant();
  }

  /**
   * RFC3339格式时间字符串转Date
   *
   * @param rfc3339 RFC3339格式时间字符串
   * @return Date
   */
  public static Date toDate(String rfc3339) {
    return Date.from(toInstant(rfc3339));
  }

  /**
   * RFC3339格式时间字符串转LocalDateTime
   *
   * @param rfc3339 RFC3339格式时间字符串
   * @return LocalDateTime
   */
  public static LocalDateTime toLocalDateTime(String rfc3339) {
    return toOffsetDateTime(rfc3339).toLocalDateTime();
  }
}
